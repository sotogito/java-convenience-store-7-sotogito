package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.NoSuchElementException;

public class InputView {

    public String inputOrderProducts() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        try {
            return Console.readLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw e;
        }
    }

    public String inputWhether(String message) {
        System.out.println(message);
        try {
            return Console.readLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw e;
        }
    }

}
