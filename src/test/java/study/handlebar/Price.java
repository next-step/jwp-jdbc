package study.handlebar;

public class Price {
    private String name;
    private int value;
    private double taxed_value;
    private boolean in_ca;

    public Price(String name, int value, double taxed_value, boolean in_ca) {
        this.name = name;
        this.value = value;
        this.taxed_value = taxed_value;
        this.in_ca = in_ca;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public double getTaxed_value() {
        return taxed_value;
    }

    public boolean isIn_ca() {
        return in_ca;
    }
}
