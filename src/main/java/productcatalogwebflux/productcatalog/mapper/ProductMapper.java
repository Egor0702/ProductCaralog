package productcatalogwebflux.productcatalog.mapper;

import org.springframework.stereotype.Component;
import productcatalogwebflux.productcatalog.domain.Product;
import productcatalogwebflux.productcatalog.dto.ProductRequest;
import productcatalogwebflux.productcatalog.dto.ProductResponse;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
