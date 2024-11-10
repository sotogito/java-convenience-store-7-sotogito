package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.util.List;
import store.domain.ConvenienceStoreroom;
import store.domain.OrderParser;
import store.domain.Receipt;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.domain.record.OrderForm;
import store.enums.AnswerWhether;
import store.enums.messages.ServiceMessage;
import store.service.CartService;
import store.service.OrderService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStoreController {
    //private final InputView inputView;
    //private final OutputView outputView;
    private PromotionService promotionService;
    private CartService cartService;
    private OrderService orderService;
//
//    public ConvenienceStoreController() {
//        inputView = new InputView();
//        outputView = new OutputView();
//    }

    public void run() throws IOException {
        ConvenienceStoreroom storeroom = loadProductStock();
        Cart cart = new Cart();
        Receipt receipt = new Receipt(cart);
        orderService = new OrderService(storeroom, receipt, cart);
        promotionService = new PromotionService(storeroom, cart);
        cartService = new CartService(storeroom, cart);

        processBuy(storeroom, receipt);
    }

    private void processBuy(ConvenienceStoreroom storeroom, Receipt receipt) {
        while (orderService.isPurchase()) {
            OutputView.printOwnedProducts(storeroom);
            buy(receipt);
            updateConvenienceState();
            inputWhetherPurchase();
        }
    }

    private void buy(Receipt receipt) {
        tryBuy();
        processNonApplicablePromotionOrder();
        processAddablePromotionProductOrder();

        makeReceipt(receipt);
    }

    private void makeReceipt(Receipt receipt) {
        orderService.updateReceipt(inputWhetherMembershipDiscount());
        OutputView.printReceipt(receipt);
    }

    private void updateConvenienceState() {
        orderService.decreaseStockInConvenienceStore();
        orderService.clearPurchaseHistory();
    }


    private void processNonApplicablePromotionOrder() {
        for (Order order : promotionService.getNonApplicablePromotionOrders()) {
            String name = order.getProductName();
            int shortageQuantity = order.getNonApplicablePromotionProductQuantity();
            AnswerWhether answer = inputWhetherBuyNoPromotion(name, shortageQuantity);

            promotionService.handleNonApplicablePromotionOrder(answer, order, shortageQuantity);
        }
    }

    private AnswerWhether inputWhetherBuyNoPromotion(String productName, int shortageQuantity) {
        while (true) {
            try {
                return AnswerWhether.find(InputView.inputWhether(
                        String.format(ServiceMessage.NO_PROMOTION_DISCOUNT.get(), productName, shortageQuantity)));
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }


    private void processAddablePromotionProductOrder() {
        for (Order order : promotionService.getAddablePromotionProductOrders()) {
            String name = order.getProductName();
            int needAddQuantity = order.getNeedAddQuantity();
            AnswerWhether answer = inputWhetherAddPromotionProduct(name, needAddQuantity);

            promotionService.handleCanAddPromotionProductOrder(answer, order, needAddQuantity);
        }
    }

    private AnswerWhether inputWhetherAddPromotionProduct(String productName, int needAddQuantity) {
        while (true) {
            try {
                return AnswerWhether.find(InputView.inputWhether(
                        String.format(ServiceMessage.ADD_PROMOTION_PRODUCT.get(), productName, needAddQuantity)));
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }


    private AnswerWhether inputWhetherMembershipDiscount() {
        while (true) {
            try {
                String answer = InputView.inputWhether(ServiceMessage.APPLY_MEMBERSHIP_DISCOUNT.get());
                return AnswerWhether.find(answer);
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }


    //fixme 줄수
    private void inputWhetherPurchase() {
        while (true) {
            try {
                String answer = InputView.inputWhether(ServiceMessage.KEEP_PURCHASE.get());
                orderService.handleKeepPurchase(AnswerWhether.find(answer));
                return;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }


    private void tryBuy() {
        while (true) {
            try {
                cartService.buy(inputToOrderForm(), DateTimes.now());
                return;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private List<OrderForm> inputToOrderForm() {
        return OrderParser.parse(InputView.inputOrderProducts());
    }


    private ConvenienceStoreroom loadProductStock() throws IOException {
        return new ConvenienceStoreroom(new PromotionReader(), new ProductReader());
    }

}
