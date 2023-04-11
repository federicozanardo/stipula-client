package models.dto.responses.success;

import models.dto.responses.Response;
import models.dto.responses.ResponseCodes;

public class SuccessResponse extends Response {
    public SuccessResponse() {
        super(200, ResponseCodes.getMessage(200));
    }

    public SuccessResponse(int statusCode) {
        super(statusCode, ResponseCodes.getMessage(statusCode));
    }
}
