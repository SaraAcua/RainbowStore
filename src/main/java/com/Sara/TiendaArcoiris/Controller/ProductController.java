/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.Product;
import com.Sara.TiendaArcoiris.Repository.ProductRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author SARA
 */
@Controller
@RequestMapping("/product")
public class ProductController {
     @Autowired
    private ProductRepository productRepository;
    
     
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        if (product.getId() != null) {
            try {
                if (productRepository.findById(product.getId()) == null) {
                    response.put("status", "error");
                    response.put("message", "No existe el producto !");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                } else {
                    Product oldPorduct = productRepository.findById(product.getId()).get();
                    product.setId(oldPorduct.getId());
                    productRepository.save(product);
                    response.put("status", "success");
                    response.put("message", "Se actualizó correctamente");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
                }
            } catch (DataAccessException ex) {
                response.put("status", "error");
                response.put("message", ex.getMostSpecificCause().getMessage());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            try {
                productRepository.save(product);
                response.put("status", "success");
                response.put("message", "Se registro correctamente");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
            } catch (DataAccessException ex) {
                response.put("status", "error");
                response.put("message", ex.getMostSpecificCause().getMessage());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity getAllProduct() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("products", productRepository.findAll());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
    Optional<Product> products = productRepository.findById(id);

    if (products.isPresent()) {
      return new ResponseEntity<>(products.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      
    }
  }
     @PostMapping("/delete/{id}")
  public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id) {
    try {
      productRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}


