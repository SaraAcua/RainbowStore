/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author SARA
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 300, nullable = true)
    private String description;
    private int stock;
    private int status;
    private int price;

    @OneToMany(mappedBy = "product")
    private List<ImageProduct> images;

    public Product() {
        this.status = 1;
        images = new ArrayList();
    }

    public Product(Integer id, String name, String description, int stock, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }

    public int getStatus() {
        return status;
    }
     public void setStatus(int status) {
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public List<ImageProduct> getImages() {
        return images;
    }

    public void setImages(List<ImageProduct> images) {
        this.images = images;
    }

}
