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

    /**
     * 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
     * <p>
     * 나는 만약에 컵라면을 12개 사면 12개 프로모션 적용이 안된다고 뜨고 12개가 일반 상품으로 들어간다. 그게 아니라 프로모션 1로 두고 나머지 11을 일반상품으로,
     */
    public ConvenienceStoreController() {
        inputView = new InputView();
        outputView = new OutputView();
    }

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
                break;
            } catch (IllegalArgumentException e) {
                orderService.clearCart();
                outputView.printError(e.getMessage());
            }
        }

    }

    private void processBuy(ConvenienceStoreroom storeroom, Receipt receipt) {
        while (orderService.isPurchase()) {
            outputView.printOwnedProducts(storeroom);
            buy();

            makeReceipt(receipt); //todo 만약 재고 부족시 cart 초기화
            updateConvenienceState();
            inputWhetherPurchase();
        }
    }

    private void buy() {
        tryBuy();
        processNonApplicablePromotionOrder();
        processAddablePromotionProductOrder();

//        tryBuy();
//        processNonApplicablePromotionOrder();
//        processAddablePromotionProductOrder();
//        //todo 재고 수량 확인은 위에가 다 끝나고 해야됨
//        /**
//         * orderFrom저장해두고 마지막에 수량 체크하기
//         */
//        makeReceipt(receipt);
    }

    /**
     * 재고를 줄이기 전에 원래의 재고를 가지고 그것이 전체 재고와 부족한지를 판단해야됨
     *
     * @param receipt
     */
    private void makeReceipt(Receipt receipt) {
        orderService.updateReceipt(inputWhetherMembershipDiscount());

        orderService.checkOriginalOrderProductQuantity();

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
