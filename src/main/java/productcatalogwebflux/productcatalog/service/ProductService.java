package productcatalogwebflux.productcatalog.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import productcatalogwebflux.productcatalog.domain.Product;
import productcatalogwebflux.productcatalog.dto.ProductRequest;
import productcatalogwebflux.productcatalog.dto.ProductResponse;
import productcatalogwebflux.productcatalog.exception.ProductNotFoundException;
import productcatalogwebflux.productcatalog.mapper.ProductMapper;
import productcatalogwebflux.productcatalog.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Validator validator;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            Validator validator,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.validator = validator;
        this.productMapper = productMapper;
    }

    public Flux<Product> getPage(int page, int size) {
        int offset = page * size;
        return productRepository.findPaged(size, offset);
    }

    @Transactional
    public Mono<ProductResponse> createProduct(ProductRequest productRequest) {
        dataIsValid(productRequest);
        Product product = productMapper.toEntity(productRequest);
        return productRepository.save(product)
                .map(productMapper::toResponse);
    }

    public Mono<ProductResponse> findProductById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .map(productMapper::toResponse);
    }

    public Mono<ProductResponse> updateProduct(Long productId, ProductRequest productRequest) {
        dataIsValid(productRequest);
        return productRepository.findById(productId)
                .flatMap(existing -> {
                    existing.setName(productRequest.getName());
                    existing.setPrice(productRequest.getPrice());
                    return productRepository.save(existing);
                })
                .map(productMapper::toResponse);
    }

    public Mono<Void> deleteProductById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(productRepository::delete);
    }

    /**
     * Check validations and throw ConstraintViolationException if we find violation
     * We handle ConstraintViolationException in GlobalExceptionHandler.java
     */
    private void dataIsValid(ProductRequest productRequest) {
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed", violations);
        }
    }
}
