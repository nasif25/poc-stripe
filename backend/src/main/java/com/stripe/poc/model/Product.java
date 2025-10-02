package com.stripe.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Product model representing different user tier pricing
 */
public class Product {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("price")
    private Long price; // Price in cents
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("users")
    private Integer users;
    
    @JsonProperty("features")
    private String[] features;
    
    @JsonProperty("stripePriceId")
    private String stripePriceId;

    // Constructors
    public Product() {}

    public Product(String id, String name, String description, Long price, 
                   String currency, Integer users, String[] features) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.users = users;
        this.features = features;
    }

    public Product(String id, String name, String description, Long price, 
                   String currency, Integer users, String[] features, String stripePriceId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.users = users;
        this.features = features;
        this.stripePriceId = stripePriceId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Integer getUsers() { return users; }
    public void setUsers(Integer users) { this.users = users; }

    public String[] getFeatures() { return features; }
    public void setFeatures(String[] features) { this.features = features; }

    public String getStripePriceId() { return stripePriceId; }
    public void setStripePriceId(String stripePriceId) { this.stripePriceId = stripePriceId; }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", users=" + users +
                '}';
    }
}
