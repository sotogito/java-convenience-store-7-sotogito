package store.constants.messages;

public enum ErrorMessage {
    OUT_OF_STOCK("재고 부족 오류"),
    NOT_EXIST_PROMOTION("존재하지 않는 프로모션"),
    NOT_EXIST_PRODUCT("존재하지 않는 상품"),

    INPUT_INCORRECT_ORDER_FORM("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),

    INPUT_INSUFFICIENT_STOCK_ORDER("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INPUT_NOT_EXIST_ORDER_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),

    INCORRECT_INPUT("잘못된 입력입니다. 다시 입력해 주세요.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }

}
