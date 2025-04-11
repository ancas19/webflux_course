package co.com.ancas.playground.sec09.mapper;

import co.com.ancas.playground.sec09.dto.Product;
import co.com.ancas.playground.sec09.entity.ProductEntity;

public class ProductMapper {
    public static Product toProductDTO(ProductEntity product) {
        return new Product(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static ProductEntity toProductEntity(Product product) {
        return new ProductEntity(
                product.id(),
                product.description(),
                product.price()
        );
    }
}
