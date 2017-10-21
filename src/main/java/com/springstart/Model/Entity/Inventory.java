package com.springstart.Model.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "ebay")
public class Inventory {

    @Id
    @Indexed
    private String id;

    @Indexed
    private double price;

    @Indexed
    private String title;

    @Indexed
    private String description;

    @Indexed
    private String manufacturer;

    @Indexed
    private Boolean instock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Boolean getInstock() {
        return instock;
    }

    public void setInstock(Boolean instock) {
        this.instock = instock;
    }
}
