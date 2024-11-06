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

    public boolean isInStock(int purchaseQuantity) {
        return purchaseQuantity <= quantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isPromotionItem() {
        return this.promotion != null;
    }

    @Override
    public String toString() {
        return ItemPrintout.getPrintout(name, price, quantity, promotion);
    }


}
