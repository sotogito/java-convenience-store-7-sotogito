package store.service;

import java.util.ArrayList;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.Receipt;
import store.domain.order.Cart;
import store.domain.record.OrderForm;
import store.enums.AnswerWhether;
import store.enums.messages.ErrorMessage;

public class OrderService {
    private final ConvenienceStoreroom convenienceStoreroom;
    private final Receipt receipt;
    private final Cart cart;
    private boolean isPurchase;
    private final List<OrderForm> orderForms;

    public OrderService(ConvenienceStoreroom convenienceStoreroom, Receipt receipt, Cart cart) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.receipt = receipt;
        this.cart = cart;
        isPurchase = true;
        orderForms = new ArrayList<>();
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

    public void checkOriginalOrderProductQuantity() {
        for (OrderForm orderForm : orderForms) {
            String name = orderForm.name();
            int quantity = orderForm.quantity();
            if (!convenienceStoreroom.isSufficientStockAfterGetPromotionProduct(name, quantity)) {
                throw new IllegalArgumentException(ErrorMessage.INPUT_INSUFFICIENT_STOCK_ORDER.get());
            }
        }
    }

    public void saveOrderForm(List<OrderForm> orderForms) {
        this.orderForms.addAll(orderForms);
    }

}
