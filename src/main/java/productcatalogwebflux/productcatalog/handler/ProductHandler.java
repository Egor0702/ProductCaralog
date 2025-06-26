package productcatalogwebflux.productcatalog.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import productcatalogwebflux.productcatalog.dto.ProductRequest;
import productcatalogwebflux.productcatalog.service.ProductService;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    private final ProductService productService;

    public ProductHandler(
            ProductService productService
    ) {
        this.productService = productService;
    }

    /**
     * Handle request GET /products
     */
    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
        return productService.getAllProduct()
                .collectList()
                .flatMap(products ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(products)
                );
    }

    /**
     * Handle request POST /products
     */
    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(ProductRequest.class)
                .flatMap(productService::createProduct)
                .flatMap(createdProduct ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(createdProduct)
                );
    }

    /**
     * Handle request GET /products/{id}
     */
    public Mono<ServerResponse> getProductById(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("id"));
        return productService.findProductById(productId)
                .flatMap(productResponse ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(productResponse)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Handle request PUT /products/{id}
     */
    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(ProductRequest.class)
                .flatMap(productRequest -> productService.updateProduct(productId, productRequest))
                .flatMap(productResponse ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(productResponse)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
}

/**
 * Handle request DELETE /products/{id}
 */
public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
    Long productId = Long.valueOf(serverRequest.pathVariable("id"));
    return productService.deleteProductById(productId)
            .then(ServerResponse.noContent().build());
}
}
