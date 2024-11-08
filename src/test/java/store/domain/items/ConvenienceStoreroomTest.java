package store.domain.items;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.ConvenienceStoreroom;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;

class ConvenienceStoreroomTest {
    private ConvenienceStoreroom storeroom;

    @BeforeEach
    void setUp() throws IOException {
        storeroom = new ConvenienceStoreroom(
                new PromotionReader(), new ProductReader()
        );
    }

    @DisplayName("리소스 파일 기반의 첫 데이터")
    @Test
    void 첫_보유_상품_목록_출력() {
        String expect =
                "- 콜라 1,000원 10개 탄산2+1\n"
                        + "- 콜라 1,000원 10개\n"
                        + "- 사이다 1,000원 8개 탄산2+1\n"
                        + "- 사이다 1,000원 7개\n"
                        + "- 오렌지주스 1,800원 9개 MD추천상품\n"
                        + "- 오렌지주스 1,800원 재고 없음\n"
                        + "- 탄산수 1,200원 5개 탄산2+1\n"
                        + "- 탄산수 1,200원 재고 없음\n"
                        + "- 물 500원 10개\n"
                        + "- 비타민워터 1,500원 6개\n"
                        + "- 감자칩 1,500원 5개 반짝할인\n"
                        + "- 감자칩 1,500원 5개\n"
                        + "- 초코바 1,200원 5개 MD추천상품\n"
                        + "- 초코바 1,200원 5개\n"
                        + "- 에너지바 2,000원 5개\n"
                        + "- 정식도시락 6,400원 8개\n"
                        + "- 컵라면 1,700원 1개 MD추천상품\n"
                        + "- 컵라면 1,700원 10개"
                        + "\n";

        Assertions.assertEquals(expect, storeroom.toString());


    }

}