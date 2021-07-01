/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Repository;

import com.Sara.TiendaArcoiris.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author SARA
 */
public interface ProductRepository extends JpaRepository<Product, Integer>{
    
    
     @Query("from Product p where p.id = :id ")
  public Product getProductById(String id);
    
    
    @Query(value="select count(*) from products p where p.id = :id",nativeQuery = true)
  public int ProductById(String id);
}
