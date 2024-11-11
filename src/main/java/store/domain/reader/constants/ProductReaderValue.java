package store.domain.reader.constants;

public enum ProductReaderValue {
    NAME(0),
    PRICE(1),
    QUANTITY(2),
    PROMOTION(3),
    SKIP_LINE(1);

    private final int index;

    ProductReaderValue(int value) {
        this.index = value;
    }

    public int get() {
        return index;
    }

}
