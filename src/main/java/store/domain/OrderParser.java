package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.record.OrderForm;

//올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.

public class OrderParser {

    public static List<OrderForm> parse(String inputOrder) {
        List<OrderForm> result = new ArrayList<>();

        String[] nameAndQuantity = inputOrder.split(",");

        for (int i = 0; i < nameAndQuantity.length; i++) {
            String removeParentheses = nameAndQuantity[i].trim();

            validateNoParentheses(removeParentheses);
            validateNoHyphen(removeParentheses);

            removeParentheses = removeParentheses.replace("[", "").replace("]", "");
            String[] split = removeParentheses.split("-");

            String name = split[0].trim();
            int quantity = Integer.parseInt(split[1].trim());

            result.add(new OrderForm(name, quantity));
        }
        return result;
    }

    private static void validateNoParentheses(String splitCommaValue) {
        if (!splitCommaValue.contains("[") || !splitCommaValue.contains("]")) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private static void validateNoHyphen(String splitCommaValue) {
        if (!splitCommaValue.contains("-")) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }


}
