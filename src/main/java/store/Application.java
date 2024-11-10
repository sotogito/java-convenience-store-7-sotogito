package store;

import java.io.IOException;
import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController();
        try {
            convenienceStoreController.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
