package models.dto.requests.contract;

import exceptions.models.dto.requests.contract.function.UnsupportedTypeException;
import lib.datastructures.Triple;
import models.contract.PayToContract;

public class FunctionArgument {
    // Triple<variable_type, variable_name, variable_value> argument;
    private final Triple<String, String, Object> argument;

    public FunctionArgument(String type, String variableName, Object value) throws UnsupportedTypeException {
        if ((value instanceof String) || (value instanceof PayToContract)) {
            this.argument = new Triple<>(type, variableName, value);
        } else {
            throw new UnsupportedTypeException("The only supported types for 'value' are String or PayToContract");
        }
    }

    public Triple<String, String, Object> getArgument() {
        return argument;
    }

    public String getType() {
        return argument.getFirst();
    }

    public String getVariableName() {
        return argument.getSecond();
    }

    public Object getValue() {
        return argument.getThird();
    }

    @Override
    public String toString() {
        return "FunctionArgument{" +
                "argument=" + argument +
                '}';
    }
}
