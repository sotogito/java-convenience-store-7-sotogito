package store.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import store.domain.items.Products;
import store.domain.items.Promotions;
import store.domain.items.item.Product;
import store.domain.items.item.PromotionProduct;
import store.domain.reader.ProductReader;
import store.domain.reader.PromotionReader;
import store.enums.ResourcePath;

/**
 * 재고관리 해야될듯 0. 존재하는 상품인지 1. 상품 재고가 있는지 2. 일반상품인지 프로모션인지 3. 프로모션 기간인지 4. 알맞는 재고를 입력했는지
 * <p>
 * -> 이걸 순차적으로 만든게 서비스에
 */
public class ConvenienceStoreroom {
    private final Products products;
    private final Promotions promotions;

    public ConvenienceStoreroom(PromotionReader promotionReader, ProductReader productReader) throws IOException {
        this.promotions = new Promotions(promotionReader.read(ResourcePath.PROMOTION.getPath()));
        List<Product> products1 = productReader.read(ResourcePath.PRODUCT.getPath(), promotions);
        for (Product product : products1) {
            System.out.println(product.getName());
        }

        this.products = new Products(products1);
    }

    public Product findProductByName(String name, int quantity) {
        return products.getProductByName(name, quantity);
    }

    public Product getGeneralProduct(String name, int quantity) {
        return products.getGeneralProduct(name, quantity);

    }

    public Product findGeneralProductByName(String name) {
        return products.getGeneralProductByName(name);
    }

    public void decreaseStock(Map<Product, Integer> productQuantity) {
        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            System.out.println("저장창고");
            System.out.println(product.getName());
            System.out.println(quantity);

            if (product instanceof PromotionProduct promotionProduct) {

                for (int i = 0; i < quantity; i++) {
                    if (promotionProduct.isOutOfStock()) {
                        Product generalProduct = findGeneralProductByName(product.getName());
                        generalProduct.decreaseQuantitySingly();
                    }
                    promotionProduct.decreaseQuantitySingly();
                }
                continue;
            }
            product.decreaseQuantity(quantity);
        }
    }


    @Override
    public String toString() {
        return products.toString();
    }

}
