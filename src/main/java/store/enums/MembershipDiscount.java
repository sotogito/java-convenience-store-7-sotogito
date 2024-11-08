package store.enums;

public enum MembershipDiscount {
    DISCOUNT_PERCENT(30),
    MAX_DISCOUNT_AMOUNT(8000);

    private final int value;

    MembershipDiscount(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public double getDiscountValue() {
        return (double) DISCOUNT_PERCENT.value / 100;
    }

}
