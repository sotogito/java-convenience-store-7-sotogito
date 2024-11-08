package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.OrderParser;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.domain.order.Receipt;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.domain.record.OrderForm;
import store.enums.AnswerWhether;
import store.service.CartService;
import store.service.OrderService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;
import store.view.messages.ServiceMessage;

public class ConvenienceStoreController {

    private PromotionService promotionService;
    private CartService cartService;

    private OrderService orderService;
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() throws IOException {
        ConvenienceStoreroom storeroom = new ConvenienceStoreroom(new PromotionReader(), new ProductReader());
        Cart cart = new Cart();
        Receipt receipt = new Receipt(cart);
        orderService = new OrderService(storeroom, receipt, cart);
        promotionService = new PromotionService(storeroom, cart);
        cartService = new CartService(storeroom, cart);

        processBuy(storeroom, receipt);
    }

    private void processBuy(ConvenienceStoreroom storeroom, Receipt receipt) {
        while (orderService.isPurchase()) {
            System.out.println(storeroom);
            buy(receipt);
            updateConvenienceState();
            inputWhetherPurchase();
        }
    }

    private void buy(Receipt receipt) {
        tryBuy();
        processShortageStockPromotionOrder();
        processLackQuantityPromotionOrder();

        printReceipt(receipt);
    }

    private void printReceipt(Receipt receipt) {
        orderService.updateReceipt(inputWhetherMembershipDiscount());
        System.out.println(receipt);
    }

    private void updateConvenienceState() {
        orderService.decreaseStockInConvenienceStore();
        orderService.clearPurchaseHistory();
    }


    private void processShortageStockPromotionOrder() {
        for (Order order : promotionService.getShortagePromotionalStock()) {
            String name = order.getProductName();
            int shortageQuantity = order.getNoPromotionQuantity();
            AnswerWhether answer = inputWhetherBuyNoPromotion(name, shortageQuantity);

            promotionService.processShortagePromotionalStock(answer, order, shortageQuantity);
        }
    }

    private AnswerWhether inputWhetherBuyNoPromotion(String productName, int shortageQuantity) {
        while (true) {
            try {
                return AnswerWhether.findByInputAnswer(inputView.inputWhether(
                        String.format(ServiceMessage.NO_PROMOTION_DISCOUNT.getMessage(),
                                productName, shortageQuantity)));
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private void processLackQuantityPromotionOrder() {
        for (Order order : promotionService.getLackQuantityPromotionOrders()) {
            String name = order.getProductName();
            int needAddQuantity = order.getNeedAddQuantity();
            AnswerWhether answer = inputWhetherAddPromotionProduct(name, needAddQuantity);

            promotionService.processLackQuantityPromotionOrders(answer, order, needAddQuantity);
        }
    }

    private AnswerWhether inputWhetherAddPromotionProduct(String productName, int needAddQuantity) {
        while (true) {
            try {
                return AnswerWhether.findByInputAnswer(inputView.inputWhether(
                        String.format(ServiceMessage.ADD_PROMOTION_PRODUCT.getMessage(),
                                productName, needAddQuantity)));
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private AnswerWhether inputWhetherMembershipDiscount() {
        while (true) {
            try {
                String answer = inputView.inputWhether(ServiceMessage.APPLY_MEMBERSHIP_DISCOUNT.getMessage());
                return AnswerWhether.findByInputAnswer(answer);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private void inputWhetherPurchase() {
        while (true) {
            try {
                String answer = inputView.inputWhether(ServiceMessage.KEEP_PURCHASE.getMessage());
                orderService.processKeepPurchase(AnswerWhether.findByInputAnswer(answer));
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private void tryBuy() {
        while (true) {
            try {
                cartService.buy(inputToOrderForm(), DateTimes.now());
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private List<OrderForm> inputToOrderForm() {
        return OrderParser.parse(inputView.inputOrderProducts());
    }

}
