package com.pixelogicmedia.delivery.execution.aspera;

import com.ibm.aspera.models.Paths;
import com.ibm.aspera.models.TransferPostRequest;
import com.ibm.aspera.models.TransferResponseSessionSpec;
import com.ibm.aspera.models.TransferResponseSessions;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.entities.EmailNotification;
import com.pixelogicmedia.delivery.data.entities.Storage;
import com.pixelogicmedia.delivery.data.repositories.StorageRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import com.pixelogicmedia.delivery.execution.DeliveryJobReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.pixelogicmedia.delivery.utils.Utilities.humanReadableByteCount;

@Component
public class AsperaServerTransferJobExecutor extends AbstractTransferJobExecutor {

    private final StorageRepository storageRepository;

    private final DeliveryJobReportingService deliveryJobReportingService;

    @Autowired
    public AsperaServerTransferJobExecutor(final StorageRepository storageRepository, final DeliveryJobReportingService deliveryJobReportingService) {
        this.storageRepository = storageRepository;
        this.deliveryJobReportingService = deliveryJobReportingService;
    }

    @Override
    public void execute(final DeliveryJob job) {

        final var connectionConfig = (AsperaConnectionConfig) job.getProfile().getConnection().getConfig();

        job.getAssets().forEach(asset -> {
            final var storage = this.storageRepository.findByStorageId(asset.getStorageId()).orElseThrow(() ->
                    BusinessException.of(HttpStatus.PRECONDITION_REQUIRED, "Storage %s is not configured.".formatted(asset.getStorageId())));
            final var localPath = java.nio.file.Paths.get(storage.getAttributes().getAsperaNodeMountPrefix(), asset.getPath())
                    .toAbsolutePath()
                    .toString();
            asset.setLocalPath(localPath);

        });

        final var asperaRequest = new TransferPostRequest()
                .direction("send")
                .remoteHost(connectionConfig.getRemoteAddress())
                .remoteUser(connectionConfig.getUsername())
                .remotePassword(connectionConfig.getPassword())
                .targetRateKbps(connectionConfig.getTargetRate())
                .targetRateCapKbps(connectionConfig.getTargetRate());

        final var paths = job.getAssets().stream().map(asset -> new Paths()
                .source(asset.getLocalPath())
                .destination(this.destination(job.getProfile().getRootPath(), asset.getLocalPath()))).toList();

        asperaRequest.setPaths(paths);

        // All asset should have the same storage ID. Validation should be added upon submission.
        final var storage = this.storageRepository.findByStorageId(job.getAssets().get(0).getStorageId()).orElseThrow();

        final var asperaClient = this.getAsperaClientFromStorage(storage);

        final var response = asperaClient.transfer(asperaRequest);
        job.setTransferInfo(new AsperaNodeTransferInfo(response.getId(), response, null));
        job.setStatus(DeliveryJob.Status.IN_PROGRESS);
    }

    @Override
    public void poll(List<DeliveryJob> jobs) {
        jobs.parallelStream().forEach(job -> {
            final var storage = this.storageRepository.findByStorageId(job.getAssets().get(0).getStorageId()).orElseThrow();
            final var asperaClient = this.getAsperaClientFromStorage(storage);
            final var asperaTransferInfo = ((AsperaNodeTransferInfo) job.getTransferInfo());
            final var asperaTransferId = asperaTransferInfo.getTransferId();
            final var transferResponse = asperaClient.transfer(asperaTransferId);
            job.setTransferInfo(new AsperaNodeTransferInfo(asperaTransferId, asperaTransferInfo.getTransferResponseSpec(), transferResponse));
            switch (transferResponse.getStatus()) {
                case FAILED -> {
                    job.setStatus(DeliveryJob.Status.FAILED);
                    deliveryJobReportingService.reportFailure(job);
                }
                case COMPLETED -> {
                    job.setStatus(DeliveryJob.Status.COMPLETED);
                    job.setProgress(1.0);
                    deliveryJobReportingService.reportCompletion(job);
                }
                case RUNNING -> job.setProgress(this.computeProgress(transferResponse));
                default -> {}
            }
        });
    }

    @Override
    public Map<String, Object> getEmailContext(DeliveryJob job) {
        final var transferInfo = (AsperaNodeTransferInfo) job.getTransferInfo();
        final var transferResponse = transferInfo.getTransferResponseSessionSpec();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String deliveryDate = transferResponse.getEndTimeUsec() != null ? dateFormat.format(new Date(transferResponse.getEndTimeUsec()/1000)) : null;

        Map<String, Object> emailBodyData = new HashMap<>();
        emailBodyData.put("sourceSize", humanReadableByteCount(transferResponse.getBytesTransferred()));
        emailBodyData.put("filesCount", transferResponse.getFiles().size());
        var sessionIds = transferResponse.getSessions().stream().map(TransferResponseSessions::getId).toList();
        emailBodyData.put("sessionId", String.join(",", sessionIds));
        emailBodyData.put("deliveryDate", deliveryDate);
        List<Map<String,String>> files = new ArrayList<>();
        for (var asperaFile : transferResponse.getFiles()) {
            Map<String,String> file = new HashMap<>();
            final var path = asperaFile.getPath();
            file.put("fileName", path.substring(path.lastIndexOf("/") +1));
            file.put("fileSize", humanReadableByteCount(asperaFile.getBytesWritten()));
            files.add(file);
        }
        emailBodyData.put("files", files);
        return emailBodyData;
    }


    private double computeProgress(TransferResponseSessionSpec transferResponse) {
        Double totalTransferredBytes = 0.0;
        Double totalBytes = 0.0;
        for (var file : transferResponse.getFiles()) {
            totalTransferredBytes += file.getBytesWritten();
            totalBytes += file.getSize();
        }
        if (totalBytes != 0) {
            return totalTransferredBytes / totalBytes;
        }
        return 0;
    }

    private String destination(final String root, final String localPath) {
        return java.nio.file.Paths.get(root, java.nio.file.Paths.get(localPath).getFileName().toString()).toString();
    }

    private AsperaClient getAsperaClientFromStorage(Storage storage) {
        return AsperaClient.of(storage.getAttributes().getAsperaNodeHost(),
                storage.getAttributes().getAsperaNodeUsername(),
                storage.getAttributes().getAsperaNodePassword());
    }
}
