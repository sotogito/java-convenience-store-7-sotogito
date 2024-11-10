package store.constants;

public enum OrderInputForm {
    OPENER("["),
    CLOSER("]"),
    NAME_QUANTITY_DELIMITER("-"),
    ORDER_DELIMITER(",");

    private final String value;

    OrderInputForm(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

}
