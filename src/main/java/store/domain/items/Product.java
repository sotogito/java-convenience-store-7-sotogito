package store.domain.items;

import store.enums.ItemPrintout;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return ItemPrintout.getGeneralProductPrintout(name, price, quantity);
    }


}
