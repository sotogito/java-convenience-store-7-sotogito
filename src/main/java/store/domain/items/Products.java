package store.domain.items;

import java.util.ArrayList;
import java.util.List;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

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
        }
        int stock = 0;
        for (Product product : sameNameProduct) {
            stock += product.getQuantity();
        }

        if (resultProduct == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
        } else if (stock == 0 || stock < quantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        return resultProduct;
    }

    public Product getGeneralProduct(String name, int quantity) {
        for (Product product : products) {
            if (product instanceof PromotionProduct) {
                continue;
            }
            if (product.isSameName(name)) {
                if (product.isSufficientStock(quantity)) {
                    return product;
                }
                throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        }
        throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    public Product getGeneralProductByName(String name) {
        for (Product product : products) {
            if (product instanceof PromotionProduct) {
                continue;
            }
            if (product.isSameName(name)) {
                return product;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
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
