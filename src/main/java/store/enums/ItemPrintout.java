package store.enums;

import store.domain.items.Promotion;

public enum ItemPrintout {
    HEAD("- "),

    GENERAL_PRODUCTS_IN_STOCK("%s %,d원 %,d개"),
    GENERAL_PRODUCT_OUT_OF_STOCK("%s %,d원 재고 없음"),

    PROMOTIONAL_PRODUCTS_IN_STOCK("%s %,d원 %,d개 %s"),
    PROMOTIONAL_PRODUCTS_OUT_OF_STOCK("%s %,d원 재고 없음 %s");

    private final String printout;

    ItemPrintout(String printout) {
        this.printout = printout;
    }


    public static String getPrintout(String name, int price, int quantity, Promotion promotion) {
        if (promotion == null) {
            if (quantity <= 0) {
                return String.format(HEAD.printout + GENERAL_PRODUCT_OUT_OF_STOCK.printout, name, price);
            }
            return String.format(HEAD.printout + GENERAL_PRODUCTS_IN_STOCK.printout, name, price, quantity);
        }
        if (quantity <= 0) {
            return String.format(HEAD.printout + PROMOTIONAL_PRODUCTS_OUT_OF_STOCK.printout, name, price, quantity);
        }
        return String.format(HEAD.printout + PROMOTIONAL_PRODUCTS_IN_STOCK.printout, name, price, quantity, promotion);
    }

    /**
     * 일반 + 재고 o
     * 일반 + 재고 x
     *
     * - 콜라 1,000원 10개 -> 재고있는 일반상품
     * - 사이다 1,000원 8개 탄산2+1 -> 재고 있는 프로모션 상품
     * - 오렌지주스 1,800원 재고 없음 -> 재고 없는 일반 상품
     * - 오렌지주스 1,800원 재고 없음 탄산2+1 -> 재고 없는 프로모션 상품
     *
     */

}
