package vm.types;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class RealType extends Type implements Serializable {
    private final Integer value;
    private final int decimals;

    public RealType() {
        this.value = 0;
        this.decimals = 2;
    }

    public RealType(Integer value) {
        this.value = value;
        this.decimals = 2;
    }

    public RealType(Integer value, int decimals) {
        this.value = value;
        this.decimals = decimals;
    }

    @Override
    public String getType() {
        return "real";
    }

    @Override
    public BigDecimal getValue() {
        BigDecimal d = new BigDecimal(this.value / Math.pow(10, this.decimals));
        return d.setScale(this.decimals, RoundingMode.CEILING);
    }

    /**
     * @return the real value as integer.
     */
    public Integer getInteger() {
        return value;
    }

    /**
     * @return the number of decimals for this value.
     */
    public Integer getDecimals() {
        return decimals;
    }

    @Override
    public String toString() {
        return "RealType{" +
                "value=" + value +
                ", decimals=" + decimals +
                '}';
    }
}
