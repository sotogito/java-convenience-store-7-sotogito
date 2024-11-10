package store.domain.policies;

public enum MembershipDiscount {
    DISCOUNT_PERCENT(30),
    MAX_DISCOUNT_AMOUNT(8000),
    NONE(0);

    private final int value;

    MembershipDiscount(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public double getDiscountValue() {
        return (double) DISCOUNT_PERCENT.value / 100;
    }

}
