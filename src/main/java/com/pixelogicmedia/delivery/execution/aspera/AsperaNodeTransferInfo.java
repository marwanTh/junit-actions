package com.pixelogicmedia.delivery.execution.aspera;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.aspera.models.TransferResponseSessionSpec;
import com.ibm.aspera.models.TransferResponseSpec;
import com.pixelogicmedia.delivery.execution.TransferInfo;
import org.springframework.lang.Nullable;

public class AsperaNodeTransferInfo extends TransferInfo {

    private final String transferId;
    private TransferResponseSpec transferResponseSpec;
    private TransferResponseSessionSpec transferResponseSessionSpec;

    @JsonCreator
    public AsperaNodeTransferInfo(@JsonProperty("transferId") final String transferId,
                                  @Nullable @JsonProperty("transferResponseSpec") final TransferResponseSpec transferResponseSpec,
                                  @Nullable @JsonProperty("transferResponseSessionSpec") final TransferResponseSessionSpec transferResponseSessionSpec) {
        this.transferId = transferId;
        this.transferResponseSpec = transferResponseSpec;
        this.transferResponseSessionSpec = transferResponseSessionSpec;
    }

    public String getTransferId() {
        return this.transferId;
    }

    public TransferResponseSpec getTransferResponseSpec() {
        return transferResponseSpec;
    }

    public TransferResponseSessionSpec getTransferResponseSessionSpec() {
        return transferResponseSessionSpec;
    }
}
