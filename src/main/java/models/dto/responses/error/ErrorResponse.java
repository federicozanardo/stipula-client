package models.dto.responses.error;

import models.dto.responses.Response;
import models.dto.responses.ResponseCodes;

public class ErrorResponse extends Response {
    private String errorMessage = null;

    public ErrorResponse(int statusCode) {
        super(statusCode, ResponseCodes.getMessage(statusCode));
    }

    public ErrorResponse(int statusCode, String errorMessage) {
        super(statusCode, ResponseCodes.getMessage(statusCode));
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
