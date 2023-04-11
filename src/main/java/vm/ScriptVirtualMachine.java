package vm;

import exceptions.datastructures.stack.StackOverflowException;
import exceptions.datastructures.stack.StackUnderflowException;
import lib.datastructures.Stack;
import vm.trap.Trap;
import vm.trap.TrapErrorCodes;
import vm.types.BoolType;
import vm.types.StrType;
import vm.types.Type;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import static lib.crypto.Crypto.getPublicKeyFromString;
import static lib.crypto.Crypto.verify;

public class ScriptVirtualMachine {
    private final String[] instructions;
    private final String ownershipId;
    private boolean isRunning = true;
    private int executionPointer = -1;
    private final Stack<Type> stack = new Stack<Type>(10);
    private final Trap trap;

    public ScriptVirtualMachine(String[] instructions, String ownershipId) {
        this.instructions = instructions;
        this.ownershipId = ownershipId;
        this.trap = new Trap(0);
    }

    public boolean execute() {
        while (isRunning) {
            if (!trap.isStackEmpty()) {
                haltProgramExecution();
                break;
            }

            try {
                executionPointer++;
                String singleInstruction = this.instructions[executionPointer].trim();
                String[] instruction = singleInstruction.split(" ");

                if (!(instruction.length == 1 && instruction[0].endsWith(":"))) {
                    switch (instruction[0]) {
                        case "PUSH":
                            this.pushOperation(instruction);
                            break;
                        case "DUP":
                            this.dupOperation(instruction);
                            break;
                        case "SHA256":
                            this.sha256Operation(instruction);
                            break;
                        case "EQUAL":
                            this.equalOperation(instruction);
                            break;
                        case "CHECKSIG":
                            this.checksigOperation(instruction);
                            break;
                        case "HALT":
                            this.haltOperation(instruction);
                            break;
                        default:
                            trap.raiseError(TrapErrorCodes.INSTRUCTION_DOES_NOT_EXISTS, executionPointer, Arrays.toString(instruction));
                    }
                }
            } catch (StackOverflowException exception) {
                trap.raiseError(TrapErrorCodes.STACK_OVERFLOW, executionPointer);
            } catch (StackUnderflowException exception) {
                trap.raiseError(TrapErrorCodes.STACK_UNDERFLOW, executionPointer);
            }
        }

        if (!trap.isStackEmpty()) {
            System.out.println("\nScriptVirtualMachine: execute => Errors in the stack");
            System.out.println(trap.printStack());
            return false;
        }

        if (stack.isEmpty()) {
            System.out.println("ScriptVirtualMachine: execute => The stack is empty");
            return false;
        }

        try {
            Type value = stack.pop();

            if (!stack.isEmpty()) {
                System.out.println("ScriptVirtualMachine: execute => There is more than one value in the stack");
                return false;
            }

            if (!value.getType().equals("bool")) {
                System.out.println("ScriptVirtualMachine: execute => The last value in the stack is not a boolean");
                return false;
            }

            BoolType boolVal = (BoolType) value;

            if (!boolVal.getValue()) {
                System.out.println("ScriptVirtualMachine: execute => The funds can't be used. The script can't be unlocked");
                return false;
            }

            System.out.println("ScriptVirtualMachine: execute => Final state of the execution below");

            System.out.println("\nGlobal state of the execution" +
                    "\nrunning -> " + isRunning +
                    "\nexecutionPointer -> " + executionPointer +
                    "\nlength of the program -> " + instructions.length);

            return !isRunning;
        } catch (StackUnderflowException exception) {
            System.out.println("ScriptVirtualMachine: execute => Stack underflow");
            return false;
        }
    }

    private void haltProgramExecution() {
        isRunning = false;
    }

    private boolean argumentsAreMoreThan(String[] instruction, int num) {
        if ((instruction.length - 1) > num) {
            trap.raiseError(TrapErrorCodes.TOO_MANY_ARGUMENTS, executionPointer, Arrays.toString(instruction));
            return true;
        }
        return false;
    }

    private boolean argumentsAreLessThan(String[] instruction, int num) {
        if ((instruction.length - 1) < num) {
            trap.raiseError(TrapErrorCodes.NOT_ENOUGH_ARGUMENTS, executionPointer, Arrays.toString(instruction));
            return true;
        }
        return false;
    }

    private void haltOperation(String[] instruction) {
        if (this.argumentsAreMoreThan(instruction, 0)) {
            return;
        }

        // Terminate the execution
        this.haltProgramExecution();
    }

    private void pushOperation(String[] instruction) throws StackOverflowException {
        if (this.argumentsAreLessThan(instruction, 2)) {
            return;
        }

        if (this.argumentsAreMoreThan(instruction, 2)) {
            return;
        }

        String type = instruction[1];
        String value = instruction[2];

        if (!type.equals("str")) {
            trap.raiseError(TrapErrorCodes.INCORRECT_TYPE, executionPointer, Arrays.toString(instruction));
            return;
        }

        stack.push(new StrType(value));
    }

    private void dupOperation(String[] instruction) throws StackUnderflowException, StackOverflowException {
        if (this.argumentsAreMoreThan(instruction, 0)) {
            return;
        }

        Type value = stack.pop();
        stack.push(value);
        stack.push(value);
    }

    private void sha256Operation(String[] instruction) throws StackUnderflowException, StackOverflowException {
        if (this.argumentsAreMoreThan(instruction, 0)) {
            return;
        }

        Type first = stack.pop();

        if (!first.getType().equals("str")) {
            trap.raiseError(TrapErrorCodes.INCORRECT_TYPE, executionPointer, Arrays.toString(instruction));
            return;
        }

        StrType firstStr = (StrType) first;

        Base64.Encoder encoder = Base64.getEncoder();
        try {
            // Compute the SHA256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StrType result = new StrType(encoder.encodeToString(digest.digest(firstStr.getValue().getBytes(StandardCharsets.UTF_8))));

            stack.push(result);
        } catch (NoSuchAlgorithmException e) {
            trap.raiseError(TrapErrorCodes.CRYPTOGRAPHIC_ALGORITHM_DOES_NOT_EXISTS, executionPointer, Arrays.toString(instruction));
        }
    }

    private void equalOperation(String[] instruction) throws StackUnderflowException {
        if (this.argumentsAreMoreThan(instruction, 0)) {
            return;
        }

        Type second = stack.pop();
        Type first = stack.pop();

        if (!first.getType().equals("str") && !second.getType().equals("str")) {
            trap.raiseError(TrapErrorCodes.INCORRECT_TYPE, executionPointer, Arrays.toString(instruction));
            return;
        }

        StrType firstStr = (StrType) first;
        StrType secondStr = (StrType) second;

        if (!firstStr.getValue().equals(secondStr.getValue())) {
            trap.raiseError(TrapErrorCodes.INCORRECT_TYPE_OR_TYPE_DOES_NOT_EXIST, executionPointer, Arrays.toString(instruction));
            return;
        }
    }

    private void checksigOperation(String[] instruction) throws StackUnderflowException, StackOverflowException {
        if (this.argumentsAreMoreThan(instruction, 0)) {
            return;
        }

        Type second = stack.pop();  // Public key
        Type first = stack.pop();   // Signature

        if (!first.getType().equals("str") && !second.getType().equals("str")) {
            trap.raiseError(TrapErrorCodes.INCORRECT_TYPE, executionPointer, Arrays.toString(instruction));
            return;
        }

        StrType signature = (StrType) first;
        StrType publicKey = (StrType) second;

        try {
            // Get the public key
            PublicKey pubKey = getPublicKeyFromString(publicKey.getValue());

            // Verify the signature
            BoolType result = new BoolType(verify(ownershipId, signature.getValue(), pubKey));

            stack.push(result);
        } catch (NoSuchAlgorithmException e) {
            trap.raiseError(TrapErrorCodes.CRYPTOGRAPHIC_ALGORITHM_DOES_NOT_EXISTS, executionPointer, Arrays.toString(instruction));
        } catch (InvalidKeyException e) {
            trap.raiseError(TrapErrorCodes.KEY_NOT_VALID, executionPointer, Arrays.toString(instruction));
        } catch (InvalidKeySpecException e) {
            trap.raiseError(TrapErrorCodes.KEY_SPECIFICATIONS_NOT_VALID, executionPointer, Arrays.toString(instruction));
        } catch (SignatureException e) {
            trap.raiseError(TrapErrorCodes.SIGNATURE_PROBLEMS, executionPointer, Arrays.toString(instruction));
        }
    }
}
