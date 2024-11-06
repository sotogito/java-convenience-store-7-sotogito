package store.domain;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.order.OrderForm;

class OrderParserTest {

    @Test
    void 주문_문자열_분리_확인() {
        String inputOrder = "[사이다-2],[감자칩-1]";

        List<OrderForm> parseResult = OrderParser.parse(inputOrder);
        int actual = parseResult.size();
        int expect = 2;

        Assertions.assertEquals(actual, expect);

    }

}