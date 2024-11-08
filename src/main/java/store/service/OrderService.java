package store.service;

import store.domain.ConvenienceStoreroom;
import store.domain.order.Cart;
import store.domain.order.Receipt;
import store.enums.AnswerWhether;

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

    
    public void updateReceipt(AnswerWhether answer) {
        receipt.process(answer);
    }

    public void decreaseStockInConvenienceStore() {
        cart.decreaseProductQuantity(convenienceStoreroom);
    }

    public void processKeepPurchase(AnswerWhether answer) {
        if (AnswerWhether.findMeaningByAnswer(answer)) {
            return;
        }
        isPurchase = false;
    }


    public void clearPurchaseHistory() {
        cart.clearCart();
        receipt.clearReceipt();
    }

}
