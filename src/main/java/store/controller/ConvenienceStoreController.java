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
import store.service.OrderService;

public class ConvenienceStoreController {
    /**
     * Conveni 출력에 필요한 객체 : Recipt
     */
    private OrderService orderService;

    public void run() throws IOException {
        ConvenienceStoreroom storeroom = new ConvenienceStoreroom(new PromotionReader(), new ProductReader());
        Cart cart = new Cart();
        Receipt receipt = new Receipt(cart);
        orderService = new OrderService(storeroom, receipt, cart);

        while (orderService.isPurchase()) {
            tryBuy();

            if (orderService.isContainPromotionProduct()) { //todo 최소수량도 넘는지 봐야됨
                //로직
                if (orderService.isPromotionDate(DateTimes.now())) {

                    /**
                     * 1. 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
                     * or
                     * 1.현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
                     */

                    List<Order> ShortageStockPromotionOrders = orderService.getShortagePromotionalStock();
                    if (!ShortageStockPromotionOrders.isEmpty()) {
                        for (Order order : ShortageStockPromotionOrders) {
                            String name = order.getProductName();
                            int shortageQuantity = order.getNoPromotionQuantity();
                            String answer = "인풋 값 넣어줘야함";

                            orderService.processShortagePromotionalStock(
                                    answer, order, shortageQuantity
                            );
                        }
                    }



                    /*
                    //note 부족한 수량 체울거냐 물어보고 업데이트 하는 로직
                    for (Order order : orderService.getLackQuantityPromotionOrders()) {
                        String name = order.getProductName();
                        int insufficientQuantity = order.getLackQuantityPromotionOrders();
                        //출력, 결과받고
                        orderService.processUpdatePromotionOrderStock("N", order, insufficientQuantity);
                    }
                    //프로모션  재고 확인

                     */
                }
                //일반으로 꼐산
            }
            //멤버심
            //계속할거냐 물어보기
        }
        //영수증 출력

    }

    private void tryBuy() {
        while (true) {
            try {
                orderService.buy(inputToOrderForm());
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<OrderForm> inputToOrderForm() {
        return OrderParser.parse("[사이다-2],[감자칩-1]");
    }


}
