import com.google.gson.Gson;
import lib.crypto.Crypto;
import models.contract.Ownership;
import models.contract.PayToContract;
import models.contract.SingleUseSeal;
import models.dto.requests.SignedMessage;
import models.dto.requests.contract.FunctionArgument;
import models.dto.requests.contract.agreement.AgreementCall;
import models.dto.requests.contract.deploy.DeployContract;
import models.dto.requests.contract.function.FunctionCall;
import models.dto.requests.ownership.GetOwnershipsByAddress;
import models.party.Party;
import vm.types.RealType;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class SwapContractWithEvent {
    private static final String contractId = "79caadf1-abbe-418a-a9a2-bd132a6f3e9e";
    private static final String contractInstanceId = "48819afd-e28f-4037-82fd-1d073ee1d318";
    private static final String aliceAssetId = "stipula_assetA_ed8i9wk";
    private static final String bobAssetId = "stipula_assetB_pl1n5cc";
    private static final String aliceOwnershipId = "2b4a4614-3bb4-4554-93fe-c034c3ba5a9c";
    private static final String bobOwnershipId = "7a19f50e-eae9-461d-bd58-9946ea39ccf0";

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
            // SignedMessage signedMessage = callDepositAssetAFunction(path);
            // SignedMessage signedMessage = callDepositAssetBFunction(path);

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

        String sourceCode = "stipula SwapAsset {\n" +
                "    asset assetA:stipula_assetA_ed8i9wk, assetB:stipula_assetB_pl1n5cc\n" +
                "    field amountAssetA, amountAssetB, waitTimeBeforeSwapping\n" +
                "    init Inactive\n" +
                "\n" +
                "    agreement (Alice, Bob)(amountAssetA, amountAssetB, waitTimeBeforeSwapping) {\n" +
                "        Alice, Bob: amountAssetA, amountAssetB, waitTimeBeforeSwapping\n" +
                "    } ==> @Inactive\n" +
                "\n" +
                "    @Inactive Alice : depositAssetA()[y]\n" +
                "        (y == amountAssetA) {\n" +
                "            y -o assetA;\n" +
                "            _\n" +
                "    } ==> @Deposit\n" +
                "\n" +
                "    @Deposit Bob : depositAssetB()[y]\n" +
                "        (y == amountAssetB) {\n" +
                "            y -o assetB;\n" +
                "            now + waitTimeBeforeSwapping >>\n" +
                "                @Swap {\n" +
                "                    assetB -o Alice\n" +
                "                    assetA -o Bob\n" +
                "                } ==> @End\n" +
                "    } ==> @Swap\n" +
                "}";

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
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey alicePublicKey = Crypto.getPublicKeyFromFile(path + "alice-keys/publicKey");
        PrivateKey alicePrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        PublicKey bobPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey bobPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        // Get public key as String
        String alicePubKey = encoder.encodeToString(alicePublicKey.getEncoded());
        String bobPubKey = encoder.encodeToString(bobPublicKey.getEncoded());

        // Set up the addresses
        Party aliceAddress = new Party(alicePubKey);    // ubL35Am7TimL5R4oMwm2OxgAYA3XT3BeeDE56oxqdLc=
        Party bobAddress = new Party(bobPubKey);        // f3hVW1Amltnqe3KvOT00eT7AU23FAUKdgmCluZB+nss=

        // Load the parties' addresses
        HashMap<String, Party> parties = new HashMap<>();
        parties.put("Alice", aliceAddress);
        parties.put("Bob", bobAddress);

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        arguments.add(new FunctionArgument("real", "amountAssetA", "1400 2"));
        arguments.add(new FunctionArgument("real", "amountAssetB", "1100 2"));
        arguments.add(new FunctionArgument("time", "waitTimeBeforeSwapping", "100"));

        AgreementCall agreementCall = new AgreementCall(contractId, arguments, parties);

        String aliceSign = Crypto.sign(agreementCall.toString(), alicePrivateKey);
        String bobSign = Crypto.sign(agreementCall.toString(), bobPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                aliceSign
        );
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                bobSign
        );

        return new SignedMessage(agreementCall, signatures);
    }

    private static SignedMessage callDepositAssetAFunction(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey alicePublicKey = Crypto.getPublicKeyFromFile(path + "alice-keys/publicKey");
        PrivateKey alicePrivateKey = Crypto.getPrivateKeyFromFile(path + "alice-keys/privateKey");

        // Get public key as String
        String alicePubKey = encoder.encodeToString(alicePublicKey.getEncoded());

        // Set up the address
        Party aliceAddress = new Party(alicePubKey);

        // Set up the single-use seal
        RealType amount = new RealType(1400, 2);
        SingleUseSeal singleUseSeal = new SingleUseSeal(aliceAssetId, amount, aliceAddress.getAddress());
        Ownership ownership = new Ownership(aliceOwnershipId, singleUseSeal);

        // Set up the unlock script
        String signature = Crypto.sign(aliceOwnershipId, alicePrivateKey);
        String unlockScript = "PUSH str " + signature + "\nPUSH str " + alicePubKey + "\n";

        PayToContract payToContract = new PayToContract(ownership.getId(), aliceAddress.getAddress(), unlockScript);

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        FunctionArgument argument = new FunctionArgument("asset", "y", payToContract);
        arguments.add(argument);

        FunctionCall functionCall = new FunctionCall(contractInstanceId, "depositAssetA", arguments);

        String aliceSign = Crypto.sign(functionCall.toString(), alicePrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo/GjVKS+3gAA55+kko41yINdOcCLQMSBQyuTTkKHE1mhu/TgOpivM0wLPsSga8hQMr3+v3aR0IF/vfCRf6SdiXmWx/jflmEXtnT6fkGcnV6dGNUpHWXSpwUIDt0N88jfnEqekx4S+KDCKg99sGEeHeT65fKS8lB0gjHMt9AOriwIDAQAB",
                aliceSign);

        System.out.println("callAcceptFunction: functionCall => " + functionCall);

        return new SignedMessage(functionCall, signatures);
    }

    private static SignedMessage callDepositAssetBFunction(String path) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();

        PublicKey borrowerPublicKey = Crypto.getPublicKeyFromFile(path + "bob-keys/publicKey");
        PrivateKey borrowerPrivateKey = Crypto.getPrivateKeyFromFile(path + "bob-keys/privateKey");

        // Get public key as String
        String borrowerPubKey = encoder.encodeToString(borrowerPublicKey.getEncoded());

        // Set up the address
        Party borrowerAddress = new Party(borrowerPubKey);

        // Set up the single-use seal
        RealType amount = new RealType(1100, 2);
        SingleUseSeal singleUseSeal = new SingleUseSeal(bobAssetId, amount, borrowerAddress.getAddress());
        Ownership ownership = new Ownership(bobOwnershipId, singleUseSeal);

        // Set up the unlock script
        String signature = Crypto.sign(bobOwnershipId, borrowerPrivateKey);
        String unlockScript = "PUSH str " + signature + "\nPUSH str " + borrowerPubKey + "\n";

        PayToContract payToContract = new PayToContract(ownership.getId(), borrowerAddress.getAddress(), unlockScript);

        ArrayList<FunctionArgument> arguments = new ArrayList<>();
        FunctionArgument argument = new FunctionArgument("asset", "y", payToContract);
        arguments.add(argument);

        FunctionCall functionCall = new FunctionCall(contractInstanceId, "depositAssetB", arguments);

        String borrowerSign = Crypto.sign(functionCall.toString(), borrowerPrivateKey);
        HashMap<String, String> signatures = new HashMap<>();
        signatures.put(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDErzzgD2ZslZxciFAiX3/ot7lrkZDw4148jFZrsDZPE6CVs9xXFSHGgy/mFvIFLXhnChO6Nyd2be3lbgeavLMCMVUiTStXr117Km17keWpb3sItkKKsLFBOcIIU8XXowI/OhzQN2XPZYESHgjdQ5vwEj2YyueiS7WKP94YWz/pswIDAQAB",
                borrowerSign);

        System.out.println("callAcceptFunction: functionCall => " + functionCall);

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
