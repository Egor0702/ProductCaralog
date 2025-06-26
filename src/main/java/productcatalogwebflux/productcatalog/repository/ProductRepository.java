package productcatalogwebflux.productcatalog.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import productcatalogwebflux.productcatalog.domain.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}
