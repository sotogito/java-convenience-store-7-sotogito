package store.domain.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.ConvenienceStoreroom;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;

class ReceiptTest {
    private Receipt receipt;
    private ConvenienceStoreroom convenienceStoreroom =
            new ConvenienceStoreroom(new PromotionReader(), new ProductReader());

    ReceiptTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        receipt = new Receipt(new Cart());
    }

    @Test
    void 구매_상품_가격_확인() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(
                convenienceStoreroom.findProductByName("콜라"), 2));
        orders.add(new Order(
                convenienceStoreroom.findProductByName("감자칩"), 1));

        for (Order order : orders) {
            receipt.addOrder(order);
        }

        receipt.calculateFinalAmount();

        int expect = 3500;
        int actual = receipt.getFinalAmount();

        Assertions.assertEquals(expect, actual);
    }

    @DisplayName("일반 상품과, 프로모션 상품 따로 관리")
    @Test
    void 프로모션_상품_구분_저장_확인() {
        Order order = new Order(
                convenienceStoreroom.findProductByName("오렌지주스"), 2);

        receipt.addOrder(order);

        Assertions.assertTrue(receipt.hasPromotionProduct());
    }

    @Test
    void 일반_상품_프로모션_상품에_미저장_확인() {
        Order order = new Order(
                convenienceStoreroom.findProductByName("에너지바"), 2);

        receipt.addOrder(order);

        Assertions.assertFalse(receipt.hasPromotionProduct());
    }

    @Test
    void 프로모션_상품_기간_유효성_확인() {
        Cart cart = new Cart();
        Order promotionProduct = new Order(
                convenienceStoreroom.findProductByName("콜라"), 2);

        cart.addProduct(promotionProduct);

        Assertions.assertTrue(receipt.canPromotion(DateTimes.now()));
    }


}