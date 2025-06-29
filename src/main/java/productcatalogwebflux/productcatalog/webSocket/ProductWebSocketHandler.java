package productcatalogwebflux.productcatalog.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import productcatalogwebflux.productcatalog.domain.Product;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ProductWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<Product> sink;

    public ProductWebSocketHandler(Sinks.Many<Product> sink) {
        this.sink = sink;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                sink.asFlux()
                        .map(product -> session.textMessage(toJson(product)))
        );
    }

    private String toJson(Product product) {
        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
