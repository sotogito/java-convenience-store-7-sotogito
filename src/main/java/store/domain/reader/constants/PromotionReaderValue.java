package store.domain.reader.constants;

public enum PromotionReaderValue {
    NAME(0),
    BUY(1),
    GET(2),
    START_DATE(3),
    END_DATE(4),
    SKIP_LINE(1);

    private final int value;

    PromotionReaderValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
