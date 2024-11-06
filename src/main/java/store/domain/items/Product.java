package store.domain.items;

import store.enums.ItemPrintout;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        if (promotion == null) {
            return String.format(ItemPrintout.GENERAL.getPrintout(),
                    name, price, quantity);
        }
        return String.format(ItemPrintout.PROMOTION.getPrintout(),
                name, price, quantity, promotion);
    }


}
