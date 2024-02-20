package com.epam.finaltask.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoteResponse {
    private boolean succeeded;
    private String statusCode;
    private String statusMessage;
    private List results;

    public static RemoteResponse create(boolean succeeded, String statusCode, String statusMessage, List additionalElements) {
        RemoteResponse response = new RemoteResponse();
        response.setSucceeded(succeeded);
        response.setStatusCode(statusCode);
        response.setStatusMessage(statusMessage);
        response.setResults(additionalElements);

        return response;
    }

}
