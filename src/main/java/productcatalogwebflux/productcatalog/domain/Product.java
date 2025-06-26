package productcatalogwebflux.productcatalog.domain;

import org.springframework.data.annotation.Id;

public class Product {
    @Id
    public Long id;
    public String name;
    public String description;
    public Double price;

    public Product() {}

    public Product( String name,
                    Double price) {
        this.name = name;
        this.price = price;
    }

    public Product(Long id,
                   String name,
                   String description,
                   Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
