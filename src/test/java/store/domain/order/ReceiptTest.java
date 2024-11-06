package store.domain.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.items.ConvenienceStoreroom;
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
        receipt = new Receipt();
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

}