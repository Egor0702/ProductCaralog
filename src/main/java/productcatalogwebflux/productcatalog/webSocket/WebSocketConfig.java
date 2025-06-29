package productcatalogwebflux.productcatalog.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import productcatalogwebflux.productcatalog.domain.Product;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public Sinks.Many<Product> productSink() {
        return Sinks.many().multicast().onBackpressureBuffer(100 , false);
    }

    @Bean
    public HandlerMapping handlerMapping(ProductWebSocketHandler webSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/products", webSocketHandler);
        return new SimpleUrlHandlerMapping(map, -1);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
