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

    public Product getProductByNameAndQuantity(String name, int quantity) {
        List<Product> sameNameProduct = getSameNameProducts(name);
        boolean isAllOutOfStock = isAllOutOfStock(sameNameProduct);
        int stock = calculateAllSameNameProductStock(sameNameProduct);
        Product resultProduct = getResultProduct(sameNameProduct);
        validateProductState(quantity, sameNameProduct, isAllOutOfStock, stock);

        return resultProduct;
    }

    private List<Product> getSameNameProducts(String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.isSameName(name)) {
                result.add(product);
            }
        }
        return result;
    }

    private boolean isAllOutOfStock(List<Product> sameNameProduct) { //note 상품은 있는데 재고 없음의 경우
        boolean isAllOutOfStock = false;
        for (Product product : sameNameProduct) {
            if (product.isOutOfStock()) {
                sameNameProduct.remove(product);
                isAllOutOfStock = true;
            }
        }
        return isAllOutOfStock;
    }


    private int calculateAllSameNameProductStock(List<Product> sameNameProduct) {
        int stock = 0;
        for (Product product : sameNameProduct) {
            stock += product.getQuantity();
        }
        return stock;
    }

    private Product getResultProduct(List<Product> sameNameProduct) {
        for (Product product : sameNameProduct) {
            if (product instanceof PromotionProduct promotionProduct) {
                return promotionProduct;
            }
            return product;
        }
        return null;
    }

    private void validateProductState(int quantity, List<Product> sameNameProduct, boolean isAllOutOfStock, int stock) {
        if (sameNameProduct.isEmpty()) {
            if (isAllOutOfStock) {
                throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-2");
            }
            throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
        } else if (stock == 0 || stock < quantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-1");
        }
    }


    public Product getGeneralProductByNameAndQuantity(String name, int quantity) {
        for (Product product : products) {
            if (product instanceof PromotionProduct) {
                continue;
            } else if (product.isSameName(name)) {
                return getSufficientStockGeneralProduct(quantity, product);
            }
        }
        throw new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    private Product getSufficientStockGeneralProduct(int quantity, Product product) {
        if (product.isSufficientStock(quantity)) {
            return product;
        }
        throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.-2");
    }


    public Product getGeneralProductByName(String name) {
        for (Product product : products) {
            if (product instanceof PromotionProduct) {
                continue;
            } else if (product.isSameName(name)) {
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
