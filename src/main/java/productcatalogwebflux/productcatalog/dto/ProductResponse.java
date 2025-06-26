package productcatalogwebflux.productcatalog.dto;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final Double price;

    public ProductResponse(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Double getPrice() {
        return price;
    }
}
