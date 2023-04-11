import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lib.crypto.Crypto;
import models.contract.Ownership;
import models.contract.PayToContract;
import models.contract.SingleUseSeal;
import models.dto.requests.Message;
import models.dto.requests.SignedMessage;
import models.dto.requests.contract.FunctionArgument;
import models.dto.requests.contract.agreement.AgreementCall;
import models.dto.requests.contract.deploy.DeployContract;
import models.dto.requests.contract.function.FunctionCall;
import models.dto.requests.ownership.GetOwnershipsByAddress;
import models.dto.responses.Response;
import models.dto.responses.ResponseData;
import models.party.Party;
import vm.types.RealType;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class BikeRentalContract {
    private static final String contractId = "51d909ae-45f8-47d2-90de-40699c8a8a3d";
    private static final String contractInstanceId = "1a7c6469-b4a3-4c67-8a43-ca60514345f6";
    private static final String assetId = "f1ed3bd760ac";
    private static final String ownershipId = "1ce080e5-8c81-48d1-b732-006fa1cc4e2e";

    public static void main(ResponseDeserializer responseDeserializer) throws Exception {
        Socket socket = null;
        DataInputStream inputServerStream;
        DataOutputStream outputClientStream;
        Gson gson = new Gson();

        try {
            socket = new Socket("127.0.0.1", 61000);
            System.out.println("Connected");
        } catch (IOException error) {
            System.out.println(error);
        }

        if (socket != null) {
            // Sends output to the socket
            outputClientStream = new DataOutputStream(socket.getOutputStream());

            File currentDirectory = new File(new File(".").getAbsolutePath());
            String path = currentDirectory + "/examples/";

            // Set up the message
            SignedMessage signedMessage = deployContract(path);
            // SignedMessage signedMessage = callAgreementFunction(path);
            // SignedMessage signedMessage = callOfferFunction(path);
            // SignedMessage signedMessage = callAcceptFunction(path);
            // SignedMessage signedMessage = callEndFunction(path);

            // SignedMessage signedMessage = callGetPropertiesByAddress(path);
            // SignedMessage signedMessage = callGetLenderProperties(path);
            // SignedMessage signedMessage = callGetBorrowerProperties(path);

            String json = gson.toJson(signedMessage);
            System.out.println(json);

            try {
                outputClientStream.writeUTF(json);
            } catch (IOException error) {
                System.out.println(error);
            }

            inputServerStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String fromServer = inputServerStream.readUTF();
            System.out.println("fromServer: " + fromServer);

            Gson responseGson = new GsonBuilder().registerTypeAdapter(Response.class, responseDeserializer).create();
            Response response = responseGson.fromJson(fromServer, Response.class);
            System.out.println("fromServer: " + responseGson.fromJson(fromServer, ResponseData.class));

            // Close the connection
            try {
                inputServerStream.close();
                outputClientStream.close();
                socket.close();
            } catch (IOException error) {
                System.out.println(error);
            }
        }
    }

    public static SignedMessage deployContract(String path) throws Exception {
        PrivateKey lenderPrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        String sourceCode = "stipula BikeRental {\n" +
                "    asset wallet:stipula_coin_asd345\n" +
                "    field cost, rentingTime, use_code\n" +
                "    init Inactive\n" +
                "\n" +
                "    agreement (Lender, Borrower)(cost, rentingTime){\n" +
                "        Lender, Borrower: cost, rentingTime\n" +
                "    } ==> @Inactive\n" +
                "\n" +
                "    @Inactive Lender : offer(z)[] {\n" +
                "        z -> use_code;\n" +
                "        _\n" +
                "    } ==> @Proposal\n" +
                "\n" +
                "    @Proposal Borrower : accept()[y]\n" +
                "        (y == cost) {\n" +
                "            y -o wallet;\n" +
                "            now + rentingTime >>\n" +
                "                @Using {\n" +
                "                    wallet -o Lender\n" +
                "                } ==> @End\n" +
                "    } ==> @Using\n" +
                "\n" +
                "    @Using Borrower : end()[] {\n" +
                "        wallet -o Lender;\n" +
                "        _\n" +
                "    } ==> @End\n" +
                "}\n";

        DeployContract deployContract = new DeployContract(sourceCode);

        String lenderSign = Crypto.sign(deployContract.toString(), lenderPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                lenderSign
        );

        return new SignedMessage(deployContract, signatures);
    }

    public static SignedMessage callAgreementFunction(String path) throws Exception {
        // Start example of signed message
        Base64.Encoder encoder = Base64.getEncoder();

        // Generate keys
        // KeyPair lenderKeys = generateKeyPair();
        // KeyPair borrowerKeys = generateKeyPair();

        PublicKey lenderPublicKey = Crypto.getPublicKeyFromFile(path + "alice-keys/publicKey");
        PrivateKey lenderPrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        PublicKey borrowerPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        // Get public key as String
        // String lenderPubKey = encoder.encodeToString(lenderKeys.getPublic().getEncoded());
        String lenderPubKey = encoder.encodeToString(lenderPublicKey.getEncoded());
        // String borrowerPubKey = encoder.encodeToString(borrowerKeys.getPublic().getEncoded());
        String borrowerPubKey = encoder.encodeToString(borrowerPublicKey.getEncoded());

        // Set up the addresses
        Party lenderAddress = new Party(lenderPubKey);      // ubL35Am7TimL5R4oMwm2OxgAYA3XT3BeeDE56oxqdLc=
        Party borrowerAddress = new Party(borrowerPubKey);  // f3hVW1Amltnqe3KvOT00eT7AU23FAUKdgmCluZB+nss=

        // Load the parties' addresses
        HashMap<String, Party> parties = new HashMap<>();
        parties.put("Lender", lenderAddress);
        parties.put("Borrower", borrowerAddress);

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        arguments.add(new FunctionArgument("real", "cost", "1200 2"));
        arguments.add(new FunctionArgument("time", "rentingTime", "100"));

        AgreementCall agreementCall = new AgreementCall(contractId, arguments, parties);

        // String lenderSign = sign(agreementCallMessage.toString(), lenderKeys.getPrivate());
        String lenderSign = Crypto.sign(agreementCall.toString(), lenderPrivateKey);
        // String borrowerSign = sign(agreementCallMessage.toString(), borrowerKeys.getPrivate());
        String borrowerSign = Crypto.sign(agreementCall.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                lenderSign
        );
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign
        );

        return new SignedMessage(agreementCall, signatures);
    }

    private static SignedMessage callOfferFunction(String path) throws Exception {
        PrivateKey lenderPrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        //arguments.add(new FunctionArgument("int", "z", "1"));
        arguments.add(new FunctionArgument("real", "z", "100 2"));

        FunctionCall functionCall = new FunctionCall(
                contractInstanceId,
                "offer",
                arguments
        );

        String lenderSign = Crypto.sign(functionCall.toString(), lenderPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                lenderSign);

        return new SignedMessage(functionCall, signatures);
    }

    private static SignedMessage callAcceptFunction(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey borrowerPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        // Get public key as String
        String borrowerPubKey = encoder.encodeToString(borrowerPublicKey.getEncoded());

        // Set up the address
        Party borrowerAddress = new Party(borrowerPubKey);

        // Set up the single-use seal
        RealType amount = new RealType(1200, 2);
        SingleUseSeal singleUseSeal = new SingleUseSeal(assetId, amount, borrowerAddress.getAddress());
        Ownership ownership = new Ownership(ownershipId, singleUseSeal);

        // Set up the unlock script
        String signature = Crypto.sign(ownershipId, borrowerPrivateKey);
        String unlockScript = "PUSH str " + signature + "\nPUSH str " + borrowerPubKey + "\n";

        PayToContract payToContract = new PayToContract(ownership.getId(), borrowerAddress.getAddress(), unlockScript);

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        FunctionArgument argument = new FunctionArgument("asset", "y", payToContract);
        arguments.add(argument);

        FunctionCall functionCall = new FunctionCall(contractInstanceId, "accept", arguments);

        String borrowerSign = Crypto.sign(functionCall.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign);

        System.out.println("callAcceptFunction: functionCall => " + functionCall);
        //System.exit(0);

        return new SignedMessage(functionCall, signatures);
    }

    private static SignedMessage callEndFunction(String path) throws Exception {
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        FunctionCall functionCall = new FunctionCall(
                contractInstanceId,
                "end",
                new ArrayList<FunctionArgument>()
        );

        String borrowerSign = Crypto.sign(functionCall.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign
        );

        return new SignedMessage(functionCall, signatures);
    }

    private static SignedMessage callGetPropertiesByAddress(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey borrowerPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        String borrowerPubKey = encoder.encodeToString(borrowerPublicKey.getEncoded());
        Party borrowerAddress = new Party(borrowerPubKey); // f3hVW1Amltnqe3KvOT00eT7AU23FAUKdgmCluZB+nss=

        GetOwnershipsByAddress getOwnershipsByAddress = new GetOwnershipsByAddress(borrowerAddress.getAddress());

        String borrowerSign = Crypto.sign(getOwnershipsByAddress.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign
        );

        return new SignedMessage(getOwnershipsByAddress, signatures);
    }

    private static SignedMessage callGetLenderProperties(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey lenderPublicKey = Crypto.getPublicKeyFromFile(path + "alice-keys/publicKey");
        PrivateKey lenderPrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        String lenderPubKey = encoder.encodeToString(lenderPublicKey.getEncoded());
        Party lenderAddress = new Party(lenderPubKey); // ubL35Am7TimL5R4oMwm2OxgAYA3XT3BeeDE56oxqdLc=

        GetOwnershipsByAddress getOwnershipsByAddress = new GetOwnershipsByAddress(lenderAddress.getAddress());

        String lenderSign = Crypto.sign(getOwnershipsByAddress.toString(), lenderPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                lenderSign
        );

        return new SignedMessage(getOwnershipsByAddress, signatures);
    }

    private static SignedMessage callGetBorrowerProperties(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey borrowerPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        String borrowerPubKey = encoder.encodeToString(borrowerPublicKey.getEncoded());
        Party borrowerAddress = new Party(borrowerPubKey); // f3hVW1Amltnqe3KvOT00eT7AU23FAUKdgmCluZB+nss=

        GetOwnershipsByAddress getOwnershipsByAddress = new GetOwnershipsByAddress(borrowerAddress.getAddress());

        String borrowerSign = Crypto.sign(getOwnershipsByAddress.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign
        );

        return new SignedMessage(getOwnershipsByAddress, signatures);
    }
}