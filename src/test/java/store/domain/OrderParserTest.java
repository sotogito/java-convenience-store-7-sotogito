package store.domain;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.record.OrderForm;

class OrderParserTest {

    @Test
    void 주문_문자열_분리_확인() {
        String inputOrder = "[사이다-2],[감자칩-1]";

        List<OrderForm> parseResult = OrderParser.parse(inputOrder);
        int actual = parseResult.size();
        int expect = 2;

        Assertions.assertEquals(actual, expect);
    }

    @Test
    void 괄호가_없을_경우_예외_처리() {
        String inputOrder = "[사이다-2,[감자칩-1]";

        AssertionsForClassTypes.assertThatThrownBy(() -> OrderParser.parse(inputOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 하이픈이_없을_경우_예외_처리() {
        String inputOrder = "[사이다,2],[감자칩-1]";

        AssertionsForClassTypes.assertThatThrownBy(() -> OrderParser.parse(inputOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

}