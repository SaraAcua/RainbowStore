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
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author SARA
 */
@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/formProducts")
    public ModelAndView FormProduct(Model model) {
        return getViewProduct(null, new Product(), -1, "", "new");
    }

    @RequestMapping("/listProducts")
    public String ListProduct(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "templatesAdmin/list-products";
    }

    @GetMapping("/formProducts/{id}")
    public ModelAndView getProduct(Model model, @PathVariable("id") Integer id) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            return getViewProduct(null, product, -1, "", "update");
        } else {
            return getViewProduct(null, new Product(), -1, "", "new");
        }

    }

    public ModelAndView getViewProduct(HttpServletRequest request, Product product, int status, String msg, String mode) {
        ModelAndView view = new ModelAndView("templatesAdmin/form-Products");

        view.addObject("product", product);
        view.addObject("status", status);
        view.addObject("msg", msg);
        view.addObject("mode", mode);

        return view;
    }

    @PostMapping("/product/save")
    public ModelAndView ProductSave(HttpServletRequest request, @ModelAttribute("product") Product product, BindingResult result) {

        try {
            if (product.getId() != null) {
                productRepository.save(product);
                return getViewProduct(request, product, 1, "Se actualizo correctamente", "update");
            } else {
                product.setStock(0);
                productRepository.save(product);
                return getViewProduct(request, new Product(), 1, "Se registro correctamente", "new");

            }

        } catch (Exception ex) {
            return getViewProduct(request, product, 0, "Error al registrar : " + ex.getStackTrace()[0].toString(), "new");
        }
    }

//    @PostMapping("/product/save")
//    @ResponseBody
//    public ResponseEntity saveProduct(@RequestBody Product product) {
//        Map<String, Object> response = new HashMap<>();
//        if (product.getId() != null) {
//            try {
//                if (productRepository.findById(product.getId()) == null) {
//                    response.put("status", "error");
//                    response.put("message", "No existe el producto !");
//                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
//                } else {
//                    Product oldPorduct = productRepository.findById(product.getId()).get();
//                    product.setId(oldPorduct.getId());
//                    productRepository.save(product);
//                    response.put("status", "success");
//                    response.put("message", "Se actualizó correctamente");
//                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
//                }
//            } catch (DataAccessException ex) {
//                response.put("status", "error");
//                response.put("message", ex.getMostSpecificCause().getMessage());
//                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            try {
//                productRepository.save(product);
//                response.put("status", "success");
//                response.put("message", "Se registro correctamente");
//                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
//            } catch (DataAccessException ex) {
//                response.put("status", "error");
//                response.put("message", ex.getMostSpecificCause().getMessage());
//                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
//
//    }
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

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Optional<Product> products = productRepository.findById(id);

        if (products.isPresent()) {
            return new ResponseEntity<>(products.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/product/delete/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
