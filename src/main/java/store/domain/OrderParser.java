package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.order.OrderForm;

public class OrderParser {

    public static List<OrderForm> parse(String inputOrder) {
        List<OrderForm> result = new ArrayList<>();

        String[] nameAndQuantity = inputOrder.split(",");

        for (int i = 0; i < nameAndQuantity.length; i++) {
            String removeParentheses = nameAndQuantity[i].trim().
                    replace("[", "").
                    replace("]", "");

            String[] split = removeParentheses.split("-");
            String name = split[0].trim();
            int quantity = Integer.parseInt(split[1].trim());

            result.add(new OrderForm(name, quantity));
        }
        return result;
    }
}
