package store.domain.items;

import java.util.ArrayList;
import java.util.List;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class Products {
    private List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    /**
     * 프로모션 재고가 있는데, 부족할 경우 -> 그냥 프로모션으로 넘김 프로모션 재고가 있는데, 일반상품까지 부족할 경우 -> 재고 없음 프로모션 재고가 없는데, 일반 재고도 없는 경우 -> 재고 없음
     * 프로모션 재고가 없는데, 일반 상푸은 있는경우 -> 일반 상품으로 취금
     * <p>
     * 1. 프로모션 재고가 있나ㅏ? 2. 일반 상품 재고가 있나? 3. -> 재고가 없음
     * <p>
     * 프로모션 상품이 재고가 있으면 프로모션 상품으로 하고 프로모션이 잭과 없으면 일반 상푸믕로
     */
    public Product getProductByName(String name, int quantity) {
        List<Product> sameNameProduct = new ArrayList<>();
        Product resultProduct = null;

        for (Product product : products) {
            if (product.isSameName(name)) {
                if (product.isOutOfStock()) {
                    continue;
                }
                if (product instanceof PromotionProduct promotionProduct) { //note 프로모션 재고가 있음
                    resultProduct = promotionProduct;
                    sameNameProduct.add(product);
                    continue;
                }
                if (resultProduct == null) {
                    resultProduct = product;
                }
                sameNameProduct.add(product);
                continue;
            }
            throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
        int stock = 0;
        for (Product product : sameNameProduct) {
            stock += product.getQuantity();
        }

        if (stock == 0 || stock < quantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        return resultProduct;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : products) {
            stringBuilder.append(product);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


}
