package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.constants.OrderInputForm;
import store.constants.messages.ErrorMessage;
import store.domain.record.OrderForm;


public class OrderParser {
    private final static int INCLUSION_CRITERIA = 1;

    public static List<OrderForm> parse(String inputOrder) {
        String[] nameAndQuantity = inputOrder.split(OrderInputForm.ORDER_DELIMITER.get());

        return loopAsOrderQuantity(nameAndQuantity);
    }

    private static List<OrderForm> loopAsOrderQuantity(String[] nameAndQuantity) {
        List<OrderForm> result = new ArrayList<>();
        for (String string : nameAndQuantity) {
            String removeParentheses = string.trim();
            validateOneOrderForm(removeParentheses);
            String[] splitNameAneQuantity = getSplitNameAneQuantityValue(removeParentheses);
            result.add(createOrderForm(splitNameAneQuantity));
        }
        return result;
    }

    private static OrderForm createOrderForm(String[] splitNameAneQuantity) {
        try {
            String name = splitNameAneQuantity[0].trim();
            int quantity = Integer.parseInt(splitNameAneQuantity[1].trim());
            return new OrderForm(name, quantity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_INCORRECT_ORDER_FORM.get());
        }
    }

    private static String[] getSplitNameAneQuantityValue(String removeParentheses) {
        removeParentheses = removeParentheses.replace(
                OrderInputForm.OPENER.get(), "").replace(OrderInputForm.CLOSER.get(), "");
        return removeParentheses.split(OrderInputForm.NAME_QUANTITY_DELIMITER.get());
    }


    private static void validateOneOrderForm(String removeParentheses) {
        validateNoParentheses(removeParentheses);
        validateNoHyphen(removeParentheses);
    }

    private static void validateNoParentheses(String splitCommaValue) {
        int openCount = countContains(splitCommaValue, OrderInputForm.OPENER.get());
        int closeCount = countContains(splitCommaValue, OrderInputForm.CLOSER.get());

        if (openCount != INCLUSION_CRITERIA || closeCount != INCLUSION_CRITERIA) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_INCORRECT_ORDER_FORM.get());
        }
    }

    private static void validateNoHyphen(String splitCommaValue) {
        int containCount = countContains(splitCommaValue, OrderInputForm.NAME_QUANTITY_DELIMITER.get());
        if (containCount != INCLUSION_CRITERIA) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_INCORRECT_ORDER_FORM.get());
        }
    }


    private static int countContains(String orderForm, String delimiter) {
        char delimiterChar = delimiter.charAt(0);
        return (int) orderForm.chars()
                .filter(c -> c == delimiterChar)
                .count();
    }

}
