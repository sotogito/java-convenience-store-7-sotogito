package store.domain.reader.constants;

public enum PromotionReaderValue {
    NAME(0),
    BUY(1),
    GET(2),
    START_DATE(3),
    END_DATE(4),
    SKIP_LINE(1);

    private final int index;

    PromotionReaderValue(int value) {
        this.index = value;
    }

    public int get() {
        return index;
    }

}
