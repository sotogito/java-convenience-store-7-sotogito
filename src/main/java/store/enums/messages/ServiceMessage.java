package store.enums.messages;

public enum ServiceMessage {
    NO_PROMOTION_DISCOUNT("현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    ADD_PROMOTION_PRODUCT("현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    APPLY_MEMBERSHIP_DISCOUNT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    KEEP_PURCHASE("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

    private final String message;

    ServiceMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }

}
