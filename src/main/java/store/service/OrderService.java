package store.service;

import store.constants.AnswerWhether;
import store.domain.ConvenienceStoreroom;
import store.domain.Receipt;
import store.domain.order.Cart;

public class OrderService {
    private final ConvenienceStoreroom convenienceStoreroom;
    private final Receipt receipt;
    private final Cart cart;
    private boolean isPurchase;

    public OrderService(ConvenienceStoreroom convenienceStoreroom, Receipt receipt, Cart cart) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.receipt = receipt;
        this.cart = cart;
        isPurchase = true;
    }

    public boolean isPurchase() {
        return isPurchase;
    }

    public void handleKeepPurchase(AnswerWhether answer) {
        if (AnswerWhether.isYes(answer)) {
            return;
        }
        isPurchase = false;
    }

    public void updateReceipt(AnswerWhether answer) {
        receipt.process(AnswerWhether.isYes(answer));
    }

    public void decreaseStockInConvenienceStore() {
        cart.decreasePurchasedProductQuantity(convenienceStoreroom);
    }

    public void clearPurchaseHistory() {
        cart.clearCart();
        receipt.clearReceipt();
    }

}
