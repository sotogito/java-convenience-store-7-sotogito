package store.view;

import store.domain.ConvenienceStoreroom;

public class OutputView {
    public void printError(String error) {
        System.out.print("[ERROR] " + error);
    }

    public void printOwnedProducts(ConvenienceStoreroom convenienceStoreroom) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        System.out.println(convenienceStoreroom);
    }
    
}
