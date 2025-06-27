package productcatalogwebflux.productcatalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import productcatalogwebflux.productcatalog.domain.Product;
import productcatalogwebflux.productcatalog.repository.ProductRepository;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct() {
        Product product = new Product("Test", 10.0);

        StepVerifier.create(productRepository.save(product))
                .assertNext(saved -> {
                    assertNotNull(saved.getId());
                    assertEquals("Test", saved.getName());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        Long nonExistentId = 999L;

        StepVerifier.create(productRepository.findById(nonExistentId))
                .expectNextCount(0)
                .verifyComplete();
    }
}
