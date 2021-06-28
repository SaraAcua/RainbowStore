/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.InvoiceDetail;
import com.Sara.TiendaArcoiris.Entity.User;
import com.Sara.TiendaArcoiris.Repository.InvoiceDetailRepository;
import com.Sara.TiendaArcoiris.Repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author SARA
 */
@Controller
@RequestMapping("/invoicedetail")
public class InvoiceDetails {
    
       @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    
   
    
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveInvoiceDetail(@RequestBody InvoiceDetail invoiceDetail){
        Map<String, Object> response = new HashMap<>();
        try{
            invoiceDetailRepository.save(invoiceDetail);
            response.put("status","success");
            response.put("message","Se registro correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        }catch(DataAccessException ex){
            response.put("status","error");
            response.put("message",ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity getAllInvoiceDetails(){
        Map<String, Object> response = new HashMap<>();
        try{
            response.put("invoiceDetails",invoiceDetailRepository.findAll());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch(DataAccessException ex){
            response.put("status","error");
            response.put("message",ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDetail> getIvoiceDetailById(@PathVariable("id") int id) {
    Optional<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findById(id);

    if (invoiceDetails.isPresent()) {
      return new ResponseEntity<>(invoiceDetails.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @PostMapping("/delete/{id}")
  public ResponseEntity<HttpStatus> deleteInvoiceDetail(@PathVariable("id") int id) {
    try {
      invoiceDetailRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
   
    
    
}

    

