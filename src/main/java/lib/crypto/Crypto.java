package lib.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Crypto {

    /**
     * This method allows to generate a 1024-bit RSA key pair.
     *
     * @return object that represents the RSA keys.
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * This method allows to encrypt data, given a public key.
     *
     * @param plain:     data to be encrypted.
     * @param publicKey: key to use to encrypt data.
     * @return data encrypted.
     * @throws NoSuchPaddingException:    throws when a particular padding mechanism is requested but is not available in the environment.
     * @throws NoSuchAlgorithmException:  throws when the algorithm is not supported by the library.
     * @throws InvalidKeyException:       throws when the key provided is not valid.
     * @throws IllegalBlockSizeException: throws when the length of data provided to a block cipher is incorrect.
     * @throws BadPaddingException:       throws when the data is not padded properly.
     */
    public static String encrypt(String plain, PublicKey publicKey)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plain.getBytes()));
    }

    /**
     * This method allows to decrypt data, given a private key.
     *
     * @param encrypted:  data to be decrypted.
     * @param privateKey: key to use to decrypt data.
     * @return data decrypted.
     * @throws NoSuchPaddingException:    throws when a particular padding mechanism is requested but is not available in the environment.
     * @throws NoSuchAlgorithmException:  throws when the algorithm is not supported by the library.
     * @throws InvalidKeyException:       throws when the key provided is not valid.
     * @throws IllegalBlockSizeException: throws when the length of data provided to a block cipher is incorrect.
     * @throws BadPaddingException:       throws when the data is not padded properly.
     */
    public static String decrypt(String encrypted, PrivateKey privateKey)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(
                cipher.doFinal(
                        Base64.getDecoder().decode(encrypted)
                )
        );
    }

    /**
     * Load the public key from a file.
     *
     * @param filename: path to the file that contains the public key.
     * @return the public key contained in the file.
     * @throws FileNotFoundException:    throws when the file does not exist.
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     * @throws InvalidKeySpecException:  throws when the key specifications provided are not valid.
     */
    public static PublicKey getPublicKeyFromFile(String filename)
            throws FileNotFoundException,
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        String key = readKeyFromFile(filename);

        byte[] bytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Load the private key from a file.
     *
     * @param filename: path to the file that contains the private key.
     * @return the private key contained in the file.
     * @throws FileNotFoundException:    throws when the file does not exist.
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     * @throws InvalidKeySpecException:  throws when the key specifications provided are not valid.
     */
    public static PrivateKey getPrivateKeyFromFile(String filename)
            throws FileNotFoundException,
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        String key = readKeyFromFile(filename);

        byte[] bytes = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Read a key from a file.
     *
     * @param filename: path to the file that contains a key.
     * @return the key contained in the file.
     * @throws FileNotFoundException: throws when the file does not exist.
     */
    private static String readKeyFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner fileReader = new Scanner(file);
        String key = "";

        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine().trim();
            key += data;
        }
        fileReader.close();

        return key;
    }

    /**
     * Load the public key from a string.
     *
     * @param publicKey: public key in string format.
     * @return the public key given from the string.
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     * @throws InvalidKeySpecException:  throws when the key specifications provided are not valid.
     */
    public static PublicKey getPublicKeyFromString(String publicKey)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] bytes = Base64.getDecoder().decode(publicKey);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * This method allows to sign data.
     *
     * @param plain:      data to be signed.
     * @param privateKey: key needed to sign the data.
     * @return the data signed.
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     * @throws InvalidKeyException:      throws when the key provided is not valid.
     * @throws SignatureException:       throws when the signature object is not initialized properly or the signature algorithm is unable to process the data provided.
     */
    public static String sign(String plain, PrivateKey privateKey)
            throws NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plain.getBytes());
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * This method allows to verify a signature.
     *
     * @param plain:     data to match with the signature.
     * @param signature: data signed with a private key.
     * @param publicKey: key needed to verify the signature.
     * @return true, if the signature is valid and match it with plain data; false, otherwise;
     * @throws NoSuchAlgorithmException: throws when the algorithm is not supported by the library.
     * @throws InvalidKeyException:      throws when the key provided is not valid.
     * @throws SignatureException:       throws when the signature object is not initialized properly or the signature algorithm is unable to process the data provided.
     */
    public static boolean verify(String plain, String signature, PublicKey publicKey)
            throws NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plain.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }
}
