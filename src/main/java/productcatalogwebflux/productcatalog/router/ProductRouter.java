package productcatalogwebflux.productcatalog.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import productcatalogwebflux.productcatalog.handler.ProductHandler;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions
                .route()
                .GET("/products", handler::getPage)
                .GET("/products/{id}", handler::getProductById)
                .POST("/products", handler::createProduct)
                .PUT("/products/{id}", handler::updateProduct)
                .DELETE("/products/{id}", handler::deleteProduct)
                .build();
    }
}
