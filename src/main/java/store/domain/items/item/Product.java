package store.domain.items.item;

import store.enums.ItemPrintout;

public class Product {
    private final String name;
    private final int price;
    private int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int calculateQuantityDeduction(int promotionCorrectQuantity) {
        if (promotionCorrectQuantity >= quantity) {
            return promotionCorrectQuantity - quantity;
        }
        return 0;
    }

    public int calculateShortageQuantity(int promotionPurchaseQuantity) {
        return promotionPurchaseQuantity - quantity;
    }

    public boolean isInStock(int purchaseQuantity) {
        return purchaseQuantity <= quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public void decreaseQuantity(int purchaseQuantity) {
        this.quantity -= purchaseQuantity;
    }

    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    @Override
    public String toString() {
        return ItemPrintout.getGeneralProductPrintout(name, price, quantity);
    }


}
