package productcatalogwebflux.productcatalog.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import productcatalogwebflux.productcatalog.domain.Product;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("SELECT * FROM product WHERE id > :after ORDER BY id ASC LIMIT :limit")
    Flux<Product> findAfterId(long after, int limit);

    @Query("SELECT * FROM product ORDER BY id ASC LIMIT :limit")
    Flux<Product> findFirstPage(int limit);
}
