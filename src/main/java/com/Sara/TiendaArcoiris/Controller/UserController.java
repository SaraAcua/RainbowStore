/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.Client;
import com.Sara.TiendaArcoiris.Entity.User;
import com.Sara.TiendaArcoiris.Repository.UserRepository;
import com.Sara.TiendaArcoiris.Security.MD5Encrypt;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/formUser")
    public ModelAndView FormUser() {
        return getViewUser(null, new User(), -1,"","new");
    }

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


    @Transactional
    @PostMapping("/user")
    public ModelAndView userSave(HttpServletRequest request, @ModelAttribute("user") User user, BindingResult result) {
        MD5Encrypt md5 = new MD5Encrypt();
        try {
            if (user.getId() != null) {
                User oldUser = userRepository.findById(user.getId()).get();
                user.setPassword(oldUser.getPassword());
                userRepository.save(user);
                return getViewUser(request, new User(), 1, "Registrado correctamente", "new");
            } else {
                if (userRepository.userByEmail(user.getEmail()) > 0) {
                    return getViewUser(request, user, 0, "Ya existe el email a registrar", "new");
                } else {
                    user.setPassword(md5.getMD5(user.getPassword()));
                    user.setStatus(1);
                    userRepository.save(user);
                    return getViewUser(request, new User(), 1, "Se registro correctamente", "new");
                }
            }
        } catch (Exception ex) {
            return getViewUser(request, new User(), 0, "Error al registrar : " + ex.getStackTrace()[0].toString(), "new");
        }
    }

    public ModelAndView getViewUser(HttpServletRequest request, User user, int status, String msg, String mode) {

        ModelAndView view = new ModelAndView("form-User");
        view.addObject("user", user);
        view.addObject("status", status);
        view.addObject("mode", mode);
        view.addObject("msg", msg);

        return view;
    }
}
