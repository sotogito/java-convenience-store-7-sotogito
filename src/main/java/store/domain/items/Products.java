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
        boolean isOutOfStock = false;

        for (Product product : products) {
            if (product.isSameName(name)) {
                if (product.isOutOfStock()) {
                    isOutOfStock = true;
                    continue;
                }
                sameNameProduct.add(product);
            }
        }

        int stock = 0;
        for (Product product : sameNameProduct) {
            stock += product.getQuantity();
        }

        for (Product product : sameNameProduct) {
            if (product instanceof PromotionProduct promotionProduct) {
                resultProduct = promotionProduct;
                break;
            }
            resultProduct = product;
        }

        if (resultProduct == null) {
            if (isOutOfStock) {
                throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-1");
            }
            throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
        } else if (stock == 0 || stock < quantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-1");
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
                throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-2");
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
        throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.-3");
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
