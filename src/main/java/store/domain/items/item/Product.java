package store.domain.items.item;

import store.constants.messages.ErrorMessage;
import store.constants.printouts.ProductStockPrintout;

public class Product {
    private final String name;
    private final int price;
    private int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int calculateQuantityDeduction(int promotionCorrectQuantity) {
        if (promotionCorrectQuantity >= quantity) {
            return promotionCorrectQuantity - quantity;
        }
        return -1;
    }

    public void decreaseQuantity(int purchasedQuantity) {
        if (quantity >= purchasedQuantity) {
            quantity -= purchasedQuantity;
            return;
        }
        throw new IllegalStateException(ErrorMessage.OUT_OF_STOCK.get());
    }

    public void decreaseQuantitySingly() {
        quantity--;
    }


    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    public boolean isSufficientStock(int purchasedQuantity) {
        return quantity >= purchasedQuantity;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }


    @Override
    public String toString() {
        return ProductStockPrintout.getGeneralProductPrintout(name, price, quantity);
    }

}
