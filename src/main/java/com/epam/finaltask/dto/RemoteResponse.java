package com.epam.finaltask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RemoteResponse {
    private boolean succeeded;
    private String statusCode;
    private String statusMessage;
    private List<Object> results;
}
