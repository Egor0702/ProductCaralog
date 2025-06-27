package productcatalogwebflux.productcatalog;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import productcatalogwebflux.productcatalog.domain.Product;
import productcatalogwebflux.productcatalog.dto.ProductRequest;
import productcatalogwebflux.productcatalog.dto.ProductResponse;
import productcatalogwebflux.productcatalog.exception.ProductNotFoundException;
import productcatalogwebflux.productcatalog.mapper.ProductMapper;
import productcatalogwebflux.productcatalog.repository.ProductRepository;
import productcatalogwebflux.productcatalog.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validator validator;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductRequest validRequest;
    private Product productEntity;
    private ProductResponse response;

    private Long nonExistentId = 404L;

    @BeforeEach
    void setUp() {
        validRequest = new ProductRequest();
        validRequest.setName("Phone");
        validRequest.setPrice(500.0);
        productEntity = new Product(1L, "Phone", "Samsung", 500.0);
        response = new ProductResponse(1L, "Phone", 500.0);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(validator.validate(validRequest)).thenReturn(Collections.emptySet());
        when(productMapper.toEntity(validRequest)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(Mono.just(productEntity));
        when(productMapper.toResponse(productEntity)).thenReturn(response);

        StepVerifier.create(productService.createProduct(validRequest))
                .expectNext(response)
                .verifyComplete();

        verify(productRepository).save(productEntity);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateProduct() {
        Set<ConstraintViolation<ProductRequest>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(validRequest)).thenReturn(violations);

        assertThrows(ConstraintViolationException.class, () -> {
            productService.createProduct(validRequest);
        });

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(productEntity));
        when(productMapper.toResponse(productEntity)).thenReturn(response);

        StepVerifier.create(productService.findProductById(1L))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void shouldFailToFindProductById() {
        when(productRepository.findById(42L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.findProductById(42L))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        when(validator.validate(validRequest)).thenReturn(Collections.emptySet());
        when(productRepository.findById(1L)).thenReturn(Mono.just(productEntity));
        when(productRepository.save(any())).thenReturn(Mono.just(productEntity));
        when(productMapper.toResponse(productEntity)).thenReturn(response);

        StepVerifier.create(productService.updateProduct(1L, validRequest))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(productEntity));
        when(productRepository.delete(productEntity)).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProductById(1L))
                .verifyComplete();
    }

    @Test
    void shouldFailToDeleteWhenProductNotFound() {
        when(productRepository.findById(nonExistentId)).thenReturn(Mono.empty());
        StepVerifier.create(productService.deleteProductById(nonExistentId))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals("Product with id " + nonExistentId + " not found"))
                .verify();
    }


    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(productEntity));

        StepVerifier.create(productService.getAllProduct())
                .expectNext(productEntity)
                .verifyComplete();
    }
}
