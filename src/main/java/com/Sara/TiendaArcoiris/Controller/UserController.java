/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.User;
import com.Sara.TiendaArcoiris.Repository.UserRepository;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        if (user.getId() != null) {
            try {
                if (userRepository.findById(user.getId()) == null) {
                    response.put("status", "error");
                    response.put("message", "No existe el usuario !");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                } else {
                    User oldUser = userRepository.findById(user.getId()).get();
                    user.setUsername(oldUser.getUsername());
                    userRepository.save(user);
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
                userRepository.save(user);
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
    public ResponseEntity getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("users", userRepository.findAll());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public ResponseEntity getUserById(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userRepository.findById(id).get() != null) {
                response.put("user", userRepository.findById(id).get());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("user", null);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
