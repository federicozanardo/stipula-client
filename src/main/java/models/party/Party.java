package models.party;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Party implements Serializable {
    private final String address;
    private final String publicKey;

    public Party(String publicKey) throws NoSuchAlgorithmException {
        this.publicKey = publicKey;

        // Hash the public key
        Base64.Encoder encoder = Base64.getEncoder();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        this.address = encoder.encodeToString(digest.digest(publicKey.getBytes(StandardCharsets.UTF_8)));
    }

    public String getAddress() {
        return address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
