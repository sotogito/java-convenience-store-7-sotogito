package store.controller;

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
    private final InputView inputView;
    private final OutputView outputView;
    private PromotionService promotionService;
    private CartService cartService;
    private OrderService orderService;

    public ConvenienceStoreController() {
        inputView = new InputView();
        outputView = new OutputView();
    }

    /**
     * 상품이 부족하다는 예외는 나중에 받아야됨
     *
     * @throws IOException
     */


    /**
     * 1. orderForm 리스트를 넘김. 2. 주문 받고 -> 여부 물어보고 -> 멤버십 받고 그거를 로직을 한번에 처리해야됨. 2. 여부 물어봄 - OrderList로 처리해야됨
     */
    public void run() throws IOException {
        ConvenienceStoreroom storeroom = loadProductStock();
        Cart cart = new Cart();
        Receipt receipt = new Receipt(cart);
        orderService = new OrderService(storeroom, receipt, cart);
        promotionService = new PromotionService(storeroom, cart);
        cartService = new CartService(storeroom, cart);

        while (true) {
            try {
                processBuy(storeroom, receipt);
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }

    }

    private void processBuy(ConvenienceStoreroom storeroom, Receipt receipt) {
        while (orderService.isPurchase()) {
            outputView.printOwnedProducts(storeroom);
            buy(receipt);
            updateConvenienceState();
            inputWhetherPurchase();
        }
    }

    private void buy(Receipt receipt) {
        tryBuy();
        processNonApplicablePromotionOrder();
        processAddablePromotionProductOrder();

        //todo 여기서 재고 확인해야됨
        makeReceipt(receipt);
    }

    /**
     * 재고를 줄이기 전에 원래의 재고를 가지고 그것이 전체 재고와 부족한지를 판단해야됨
     *
     * @param receipt
     */
    private void makeReceipt(Receipt receipt) {
        orderService.checkOriginalOrderProductQuantity();
        orderService.updateReceipt(inputWhetherMembershipDiscount());
        outputView.printReceipt(receipt);
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
                return AnswerWhether.find(inputView.inputWhether(
                        String.format(ServiceMessage.NO_PROMOTION_DISCOUNT.get(), productName, shortageQuantity)));
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
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
                return AnswerWhether.find(inputView.inputWhether(
                        String.format(ServiceMessage.ADD_PROMOTION_PRODUCT.get(), productName, needAddQuantity)));
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private AnswerWhether inputWhetherMembershipDiscount() {
        while (true) {
            try {
                String answer = inputView.inputWhether(ServiceMessage.APPLY_MEMBERSHIP_DISCOUNT.get());
                return AnswerWhether.find(answer);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    //fixme 줄수
    private void inputWhetherPurchase() {
        while (true) {
            try {
                String answer = inputView.inputWhether(ServiceMessage.KEEP_PURCHASE.get());
                orderService.handleKeepPurchase(AnswerWhether.find(answer));
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private void tryBuy() {
        while (true) {
            try {
                List<OrderForm> orderForms = inputToOrderForm();
                orderService.saveOrderForm(orderForms);
                cartService.buy(orderForms);
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private List<OrderForm> inputToOrderForm() {
        return OrderParser.parse(inputView.inputOrderProducts());
    }


    private ConvenienceStoreroom loadProductStock() throws IOException {
        return new ConvenienceStoreroom(new PromotionReader(), new ProductReader());
    }

}
