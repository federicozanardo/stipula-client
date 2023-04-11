import com.google.gson.*;
import exceptions.models.dto.requests.contract.function.UnsupportedTypeException;
import models.contract.PayToContract;
import models.dto.requests.Message;
import models.dto.requests.contract.FunctionArgument;
import models.dto.requests.contract.agreement.AgreementCall;
import models.dto.requests.contract.function.FunctionCall;
import models.dto.responses.Response;
import models.dto.responses.error.ErrorDataResponse;
import models.dto.responses.error.ErrorResponse;
import models.dto.responses.success.SuccessDataResponse;
import models.dto.responses.success.SuccessResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseDeserializer implements JsonDeserializer<Response> {
    private final HashMap<String, Class<? extends Response>> dataTypeRegistry;

    public ResponseDeserializer() {
        this.dataTypeRegistry = new HashMap<>();
    }

    public void registerDataType(String jsonElementName, Class<? extends Response> javaType) {
        dataTypeRegistry.put(jsonElementName, javaType);
    }

    @Override
    public Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String value = jsonObject.get("type").getAsString();

        for (HashMap.Entry<String, Class<? extends Response>> entry : dataTypeRegistry.entrySet()) {
            if (value.equals(entry.getKey())) {
                Class<? extends Response> dataType = dataTypeRegistry.get(value);
                Response response = context.deserialize(jsonObject, dataType);

                if (response instanceof SuccessResponse) {
                    System.out.println("deserialize: SuccessResponse");
                    return (SuccessResponse) response;
                } else if (response instanceof SuccessDataResponse) {
                    System.out.println("deserialize: SuccessDataResponse");
                    SuccessDataResponse successDataResponse = (SuccessDataResponse) response;
                    return new SuccessDataResponse(successDataResponse.getStatusCode(), successDataResponse.getData());
                } else if (response instanceof ErrorResponse) {
                    System.out.println("deserialize: ErrorResponse");
                    return (ErrorResponse) response;
                } else if (response instanceof ErrorDataResponse) {
                    System.out.println("deserialize: ErrorDataResponse");
                    return (ErrorDataResponse) response;
                }
            }
        }

        // TODO: Set up the error properly
        throw new RuntimeException("Oops");
    }
}
