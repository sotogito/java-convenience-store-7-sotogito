package store.domain.items;

import java.util.ArrayList;
import java.util.List;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.constants.messages.ErrorMessage;

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

    public List<Product> getSameNameProducts(String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.isSameName(name)) {
                result.add(product);
            }
        }
        return result;
    }

    public boolean isAllOutOfStock(List<Product> sameNameProduct) {
        List<Product> outOfStockProducts = new ArrayList<>();
        for (Product product : sameNameProduct) {
            if (product.isOutOfStock()) {
                outOfStockProducts.add(product);
            }
        }
        sameNameProduct.removeAll(outOfStockProducts);
        return !outOfStockProducts.isEmpty();
    }

    public int calculateAllSameNameProductStock(List<Product> sameNameProduct) {
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


    private void validateProductState(int quantity, List<Product> sameNameProduct, Boolean isAllOutOfStock, int stock) {
        if (sameNameProduct.isEmpty()) {
            if (isAllOutOfStock) {
                throw new IllegalArgumentException(ErrorMessage.INPUT_INSUFFICIENT_STOCK_ORDER.get());
            }
            throw new IllegalArgumentException(ErrorMessage.INPUT_NOT_EXIST_ORDER_PRODUCT.get());
        } else if (stock == 0 || stock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.INPUT_INSUFFICIENT_STOCK_ORDER.get());
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
        throw new IllegalArgumentException(ErrorMessage.INPUT_NOT_EXIST_ORDER_PRODUCT.get());
    }

    private Product getSufficientStockGeneralProduct(int quantity, Product product) {
        if (product.isSufficientStock(quantity)) {
            return product;
        }
        throw new IllegalArgumentException(ErrorMessage.INPUT_INSUFFICIENT_STOCK_ORDER.get());
    }


    public Product getGeneralProductByName(String name) {
        for (Product product : products) {
            if (product instanceof PromotionProduct) {
                continue;
            } else if (product.isSameName(name)) {
                return product;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.INPUT_NOT_EXIST_ORDER_PRODUCT.get());
    }

    public boolean isSufficientPromotionStockByNameAndQuantity(String name, int afterGetPromotionQuantity) {
        int productStock = 0;
        for (Product product : getSameNameProducts(name)) {
            if (product instanceof PromotionProduct promotionProduct) {
                productStock += promotionProduct.getQuantity();
            }
        }
        return productStock >= afterGetPromotionQuantity;
    }

    public boolean isSufficientStockByNameAndQuantity(String name, int orderQuantity) {
        int productStock = 0;
        for (Product product : getSameNameProducts(name)) {
            productStock += product.getQuantity();
        }
        return productStock >= orderQuantity;
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
