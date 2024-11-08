package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.time.LocalDateTime;
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
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStoreController {
    /**
     * Conveni 출력에 필요한 객체 : Recipt
     */
    private ConvenienceStoreroom storeroom;
    private OrderService orderService;
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() throws IOException {
        storeroom = new ConvenienceStoreroom(new PromotionReader(), new ProductReader());
        Cart cart = new Cart();
        Receipt receipt = new Receipt(cart);
        orderService = new OrderService(storeroom, receipt, cart);

        processBuy(receipt);

    }

    private void processBuy(Receipt receipt) {
        while (orderService.isPurchase()) {
            System.out.println(storeroom);
            tryBuy(); //시간 여기로 넘기기

            processShortageStockPromotionOrder();
            processLackQuantityPromotionOrder();

            processReceipt();

            //orderService.printCart(); //note 확인차
            orderService.decreaseStockInConvenienceStore();
            System.out.println(receipt);
            orderService.clearOrderList();
            orderService.clearReceipt();
            //멤버심
            //계속할거냐 물어보기
            //todo 여부 확인으로 변경
            inputWhetherPurchase();
        }
    }

    private void inputWhetherPurchase() {
        while (true) {
            try {
                String answer = inputView.inputWhether("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
                orderService.processPurchase(AnswerWhether.findByInputAnswer(answer));
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }

    }

    private void processReceipt() {
        orderService.handlePurchaseProgress(inputWhetherMembershipDiscount());
    }

    private AnswerWhether inputWhetherMembershipDiscount() {
        while (true) {
            try {
                String answer = inputView.inputWhether("멤버십 할인을 받으시겠습니까? (Y/N)");
                return AnswerWhether.findByInputAnswer(answer);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    //note 먼저
    private void processShortageStockPromotionOrder() {
        for (Order order : orderService.getShortagePromotionalStock()) {
            String name = order.getProductName();
            int shortageQuantity = order.getNoPromotionQuantity();
            AnswerWhether answer = inputWhetherBuyNoPromotion(name, shortageQuantity);

            orderService.processShortagePromotionalStock(answer, order, shortageQuantity);
        }
    }

    private AnswerWhether inputWhetherBuyNoPromotion(String productName, int shortageQuantity) {
        while (true) {
            try {
                String answer = inputView.inputWhether(
                        String.format("현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
                                , productName, shortageQuantity));

                return AnswerWhether.findByInputAnswer(answer);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    //note 나중에
    private void processLackQuantityPromotionOrder() {
        for (Order order : orderService.getLackQuantityPromotionOrders()) {
            String name = order.getProductName();
            int needAddQuantity = order.getNeedAddQuantity();
            AnswerWhether answer = inputWhetherAddPromotionProduct(name, needAddQuantity);

            orderService.processLackQuantityPromotionOrders(answer, order, needAddQuantity);
        }
    }

    private AnswerWhether inputWhetherAddPromotionProduct(String productName, int needAddQuantity) {
        while (true) {
            try {
                String answer = inputView.inputWhether(
                        String.format("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
                                , productName, needAddQuantity));
                return AnswerWhether.findByInputAnswer(answer); //이거 꺼야함
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }


    private void tryBuy() {
        while (true) {
            try {
                LocalDateTime nowDateTime = DateTimes.now();
                orderService.buy(inputToOrderForm(), nowDateTime);
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
