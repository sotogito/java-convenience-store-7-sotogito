package store.constants.printouts;

public enum ProductStockPrintout {
    HEAD("- "),

    GENERAL_PRODUCTS_IN_STOCK("%s %,d원 %,d개"),
    GENERAL_PRODUCT_OUT_OF_STOCK("%s %,d원 재고 없음");

    //PROMOTIONAL_PRODUCTS_IN_STOCK("%s %,d원 %,d개 %s")
    //PROMOTIONAL_PRODUCTS_OUT_OF_STOCK("%s %,d원 재고 없음 %s")

    private final String printout;

    ProductStockPrintout(String printout) {
        this.printout = printout;
    }

    public static String getGeneralProductPrintout(String name, int price, int quantity) {
        if (quantity <= 0) {
            return String.format(HEAD.printout + GENERAL_PRODUCT_OUT_OF_STOCK.printout, name, price);
        }
        return String.format(HEAD.printout + GENERAL_PRODUCTS_IN_STOCK.printout, name, price, quantity);
    }

}
