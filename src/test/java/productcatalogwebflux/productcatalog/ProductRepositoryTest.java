package productcatalogwebflux.productcatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import productcatalogwebflux.productcatalog.domain.Product;
import productcatalogwebflux.productcatalog.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@DataR2dbcTest
public class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("product-test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        String r2dbcUrl = String.format(
                "r2dbc:postgresql://%s:%d/%s",
                postgresContainer.getHost(),
                postgresContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresContainer.getDatabaseName()
        );

        registry.add("spring.r2dbc.url", () -> r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanDatabase() {
        System.out.println("delete");
        productRepository.deleteAll().block();
    }

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

    @Test
    void findPage() {
        Product product1 = new Product("Test1", 10.0);
        Product product2 = new Product("Test2", 10.0);
        Product product3 = new Product("Test3", 10.0);
        Product[] products = new Product [] { product1, product2, product3};


        Flux<Product> saveAll = Flux.fromArray(products)
                .concatMap(productRepository::save)
                .thenMany(productRepository.findAfterId(0, 3));

        StepVerifier.create(saveAll).
                assertNext(findedPage -> {
                    assertNotNull(findedPage);
                    assertEquals(product1.name, findedPage.name);
                    assertEquals(product1.price, findedPage.price);
                })
                .assertNext(findedPage -> {
                    assertNotNull(findedPage);
                    assertEquals(product2.name, findedPage.name);
                    assertEquals(product2.price, findedPage.price);
                })
                .assertNext(findedPage -> {
                    assertNotNull(findedPage);
                    assertEquals(product3.name, findedPage.name);
                    assertEquals(product3.price, findedPage.price);
                })
                .verifyComplete();
    }
}
