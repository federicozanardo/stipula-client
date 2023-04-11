package vm.trap;

public enum TrapErrorCodes {
    ASSET_IDS_DOES_NOT_MATCH {
        public String toString() {
            return "ASSET_IDS_DOES_NOT_MATCH";
        }
    },
    CRYPTOGRAPHIC_ALGORITHM_DOES_NOT_EXISTS {
        public String toString() {
            return "CRYPTOGRAPHIC_ALGORITHM_DOES_NOT_EXISTS";
        }
    },
    DECIMALS_DOES_NOT_MATCH {
        public String toString() {
            return "DECIMALS_DOES_NOT_MATCH";
        }
    },
    DIVISION_BY_ZERO {
        public String toString() {
            return "DIVISION_BY_ZERO";
        }
    },
    ERROR_CODE_DOES_NOT_EXISTS {
        public String toString() {
            return "ERROR_CODE_DOES_NOT_EXISTS";
        }
    },
    ELEMENTS_ARE_NOT_EQUAL {
        public String toString() {
            return "ELEMENTS_ARE_NOT_EQUAL";
        }
    },
    INCORRECT_TYPE {
        public String toString() {
            return "INCORRECT_TYPE";
        }
    },
    INCORRECT_TYPE_OR_TYPE_DOES_NOT_EXIST {
        public String toString() {
            return "INCORRECT_TYPE_OR_TYPE_DOES_NOT_EXIST";
        }
    },
    INSTRUCTION_DOES_NOT_EXISTS {
        public String toString() {
            return "INSTRUCTION_DOES_NOT_EXISTS";
        }
    },
    KEY_NOT_VALID {
        public String toString() {
            return "KEY_NOT_VALID";
        }
    },
    KEY_SPECIFICATIONS_NOT_VALID {
        public String toString() {
            return "KEY_SPECIFICATIONS_NOT_VALID";
        }
    },
    LABEL_DOES_NOT_EXISTS {
        public String toString() {
            return "LABEL_DOES_NOT_EXISTS";
        }
    },
    LESS_THAN_ZERO {
        public String toString() {
            return "LESS_THAN_ZERO";
        }
    },
    MISS_HALT_INSTRUCTION {
        public String toString() {
            return "MISS_HALT_INSTRUCTION";
        }
    },
    NEGATIVE_VALUE {
        public String toString() {
            return "NEGATIVE_VALUE";
        }
    },
    NOT_ENOUGH_ARGUMENTS {
        public String toString() {
            return "NOT_ENOUGH_ARGUMENTS";
        }
    },
    QUEUE_OVERFLOW {
        public String toString() {
            return "QUEUE_OVERFLOW";
        }
    },
    QUEUE_UNDERFLOW {
        public String toString() {
            return "QUEUE_UNDERFLOW";
        }
    },
    SIGNATURE_PROBLEMS {
        public String toString() {
            return "SIGNATURE_PROBLEMS";
        }
    },
    STACK_OVERFLOW {
        public String toString() {
            return "STACK_OVERFLOW";
        }
    },
    STACK_UNDERFLOW {
        public String toString() {
            return "STACK_UNDERFLOW";
        }
    },
    TOO_MANY_ARGUMENTS {
        public String toString() {
            return "TOO_MANY_ARGUMENTS";
        }
    },
    TYPE_DOES_NOT_EXIST {
        public String toString() {
            return "TYPE_DOES_NOT_EXIST";
        }
    },
    VARIABLE_ALREADY_EXIST {
        public String toString() {
            return "VARIABLE_ALREADY_EXIST";
        }
    },
    VARIABLE_DOES_NOT_EXIST {
        public String toString() {
            return "VARIABLE_DOES_NOT_EXIST";
        }
    },
    ZERO_VALUE {
        public String toString() {
            return "ZERO_VALUE";
        }
    }
}
