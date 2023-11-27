package com.pixelogicmedia.delivery.execution.aspera;

import com.ibm.aspera.models.Error;
import com.ibm.aspera.models.*;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import kong.unirest.jackson.JacksonObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.List;

public class AsperaClient {
    private final String url;
    private final String username;
    private final String password;

    private final UnirestInstance unirest = Unirest.spawnInstance();


    private AsperaClient(final String url, final String username, final String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        this.unirest.config()
                .setObjectMapper(new JacksonObjectMapper());
    }

    public static AsperaClient of(final String url, final String username, final String password) {
        return new AsperaClient(url, username, password);
    }

    public InfoGet200 info() {
        return this.unirest.get("%s/%s".formatted(this.url, "info"))
                .basicAuth(this.username, this.password)
                .asObject(InfoGet200.class)
                .ifFailure(this::handleFailure)
                .getBody();
    }

    public FilesBrowseResponse browse(final FilesBrowseRequest request) {
        return this.unirest.post("%s/%s".formatted(this.url, "files/browse"))
                .basicAuth(this.username, this.password)
                .body(request)
                .asObject(FilesBrowseResponse.class)
                .ifFailure(this::handleFailure)
                .getBody();
    }

    public List<TransferResponseSessionSpec> transfers() {
        return this.unirest.get("%s/%s".formatted(this.url, "ops/transfers"))
                .basicAuth(this.username, this.password)
                .asObject(new GenericType<List<TransferResponseSessionSpec>>() {
                })
                .ifFailure(this::handleFailure)
                .getBody();
    }

    public TransferResponseSessionSpec transfer(final String id) {
        return this.unirest.get("%s/%s/{id}".formatted(this.url, "ops/transfers"))
                .routeParam("id", id)
                .basicAuth(this.username, this.password)
                .asObject(TransferResponseSessionSpec.class)
                .ifFailure(this::handleFailure)
                .getBody();
    }

    public TransferResponseSpec transfer(final TransferPostRequest request) {
        return this.unirest.post("%s/%s".formatted(this.url, "ops/transfers"))
                .basicAuth(this.username, this.password)
                .contentType("application/json")
                .body(request)
                .asObject(TransferResponseSpec.class)
                .ifFailure(this::handleFailure)
                .getBody();
    }

    private <T> void handleFailure(final HttpResponse<T> response) {
        throw BusinessException.of(HttpStatus.resolve(response.getStatus()), response.mapError(Error.class).getError().getReason());
    }
}
