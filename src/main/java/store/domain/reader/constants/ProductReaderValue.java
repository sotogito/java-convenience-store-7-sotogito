package store.domain.reader.constants;

public enum ProductReaderValue {
    NAME(0),
    PRICE(1),
    QUANTITY(2),
    PROMOTION(3),
    SKIP_LINE(1);

    private final int value;

    ProductReaderValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
