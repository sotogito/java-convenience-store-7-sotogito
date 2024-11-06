package store.service;

import java.util.ArrayList;
import java.util.List;
import store.domain.items.ConvenienceStoreroom;
import store.domain.items.Product;
import store.domain.order.Order;
import store.domain.order.OrderForm;
import store.domain.order.Receipt;
import store.enums.AnswerWhether;

public class OrderService {
    /**
     * 사용자가 [상품-개수]를 입력하면 Order를 생성하고 수량을 체크 - 기본
     */

    private final ConvenienceStoreroom convenienceStoreroom; //재고확인
    private final Receipt receipt; //추가 저장
    private boolean isPurchase;
    private boolean isContainPromotionProduct;


    public OrderService(ConvenienceStoreroom convenienceStoreroom, Receipt receipt) {
        this.convenienceStoreroom = convenienceStoreroom;
        this.receipt = receipt;
        isPurchase = true;
        isContainPromotionProduct = false;
    }

    public void updatePurchaseProgress(String inputAnswer) {
        this.isPurchase = AnswerWhether.findMeaningByAnswer(inputAnswer);
    }

    public void membershipApplication(String inputAnswer) {
        boolean answer = AnswerWhether.findMeaningByAnswer(inputAnswer);
        //멤버십 계산 및 적용
        // 아니면 바로 출력
    }


    /**
     * 물건 파싱 order만들기 가격 계산하기 1. 창고에서 물건 확인 + 메시지
     * <p>
     * 2. 프로모션인지 확인함 3. 재고를 확인함 + 메시지
     * <p>
     * <p>
     * 4. List<Orderㅡ>를 만듦 5. 영수증에 저장 + 가격 설정함
     * <p>
     * 6. 멤버십 여부 물어봄 + 메시지 7. 멤버십 계산해서 레시피 가격에 적용함
     * <p>
     * - 던져질 수 있는 예외 1. 물건이 존재하지 않음 2. 수량이 부족함 3. 프로모션인데 수량을 부족하게 가져옴 4.
     */

    public void buy(List<OrderForm> orders) { //todo try-catch 다시 입력받기. 여기서 예외처리 다해야됨
        List<Order> orderResult = changeToOrders(orders);
        for (Order order : orderResult) {
            receipt.addOrder(order);
        }
    }

    public boolean isContainPromotionProduct() {
        return receipt.hasPromotionProduct();
        //프로모션 아닐경우 바로 영수증 출력
    }


    private List<Order> changeToOrders(List<OrderForm> orders) {
        List<Order> orderList = new ArrayList<Order>();

        for (OrderForm order : orders) {
            Product product = convenienceStoreroom.findProductByName(order.name());
            int quantity = order.quantity();

            validateStock(product, quantity);
            orderList.add(new Order(product, quantity));
        }
        return orderList;
    }

    private void validateStock(Product product, int quantity) {
        if (!product.isInStock(quantity)) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }


}
