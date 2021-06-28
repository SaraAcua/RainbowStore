/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.Client;
import com.Sara.TiendaArcoiris.Entity.Invoice;
import com.Sara.TiendaArcoiris.Repository.InvoiceRepository;
import java.util.HashMap;
import java.util.Map;
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
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveInvoice(@RequestBody Invoice invoice) {
        Map<String, Object> response = new HashMap<>();
        try {
            invoiceRepository.save(invoice);
            response.put("status", "success");
            response.put("message", "Se registro correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity getAllInvoices() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("invoices", invoiceRepository.findAll());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public ResponseEntity getInvoiceById(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (invoiceRepository.findById(id).get() != null) {
                response.put("invoice", invoiceRepository.findById(id).get());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("invoice", null);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteInvoice(@PathVariable("id") int id) {
        try {
            invoiceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
