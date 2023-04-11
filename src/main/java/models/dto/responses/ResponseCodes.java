package models.dto.responses;

import java.util.HashMap;

public class ResponseCodes {
    private static final HashMap<Integer, String> codes = new HashMap<Integer, String>() {
        {
            // Success
            this.put(200, "Success");

            // Errors
            this.put(300, "Error while getting the message");
            this.put(301, "There are no signatures in this message");
            this.put(302, "Too few signatures in this message");
            this.put(303, "The number of signatures does not match with the number of parties");
            this.put(304, "Wrong signature for this message");
            this.put(305, "Impossible to find the public key in the set of parties");
            this.put(306, "Too many signatures in this message");
            this.put(307, "Wrong signature for this message");
            this.put(308, "Impossible to find the funds");
            this.put(309, "Error while enqueuing the request");
            this.put(310, "This is not a valid message");

            // Virtual machine errors
            this.put(400, "Request not valid");
            this.put(401, "Message not valid");
            this.put(402, "This function cannot be called in the current state");
            this.put(403, "Impossible to find the party in the contract instance");
        }
    };

    public static String getMessage(int statusCode) {
        return codes.get(statusCode);
    }
}
