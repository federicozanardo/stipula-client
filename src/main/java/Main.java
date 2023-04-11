import models.dto.responses.error.ErrorDataResponse;
import models.dto.responses.error.ErrorResponse;
import models.dto.responses.success.SuccessDataResponse;
import models.dto.responses.success.SuccessResponse;

public class Main {
    public static void main(String[] args) throws Exception {
        // Set up the deserializer of messages
        ResponseDeserializer responseDeserializer = new ResponseDeserializer();

        // Responses
        responseDeserializer.registerDataType(SuccessResponse.class.getSimpleName(), SuccessResponse.class);
        responseDeserializer.registerDataType(SuccessDataResponse.class.getSimpleName(), SuccessDataResponse.class);
        responseDeserializer.registerDataType(ErrorResponse.class.getSimpleName(), ErrorResponse.class);
        responseDeserializer.registerDataType(ErrorDataResponse.class.getSimpleName(), ErrorDataResponse.class);

        BikeRentalContract.main(responseDeserializer);
        // SwapContract.main(responseDeserializer);
        // SwapContractWithEvent.main(responseDeserializer);
    }
}
