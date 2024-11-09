package store.enums;

public enum ReceiptPrintout {
    STORE_NAME("W 편의점"),
    PURCHASE_PRODUCT_HEAD("상품명\t\t수량\t금액\n"),
    PURCHASE_PRODUCT_FORMAT("%s\t\t%,d\t%,d\n"),

    PROMOTION_PRODUCT("증\t정"),
    PROMOTION_PRODUCT_FORMAT("%s\t\t%d\n"),

    TOTAL_PURCHASE_AMOUNT_FORMAT("총구매액\t\t%,d\t%,d\n"),
    PROMOTION_DISCOUNT_AMOUNT_FORMAT("행사할인\t\t\t-%,d\n"),
    MEMBERSHIP_DISCOUNT_AMOUNT_FORMAT("멤버십할인\t\t\t-%,d\n"),
    FINAL_AMOUNT_FORMAT("내실돈\t\t\t%,d\n"),

    CATEGORY_DIVIDER("==============================\n"),
    CATEGORY_DIVIDER_FORMAT("===========%s=============\n");

    private final String printout;

    ReceiptPrintout(String printout) {
        this.printout = printout;
    }

    public String get() {
        return printout;
    }

}
