/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.Client;
import com.Sara.TiendaArcoiris.Repository.ClientRepository;
import com.Sara.TiendaArcoiris.Security.MD5Encrypt;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author SARA
 */
@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    
        
     @RequestMapping("/listClients")
    public String ListClient(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "templatesAdmin/list-clients";
    }

    @PostMapping("client/api/save")
    @ResponseBody
    public ResponseEntity saveClient(@RequestBody Client client) {
        Map<String, Object> response = new HashMap<>();
        if (client.getId() != null) {
            try {
                if (clientRepository.findById(client.getId()) == null) {
                    response.put("status", "error");
                    response.put("message", "No existe el cliente !");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                } else {
                    Client oldClient = clientRepository.findById(client.getId()).get();
                    client.setCreate_at(oldClient.getCreate_at());
                    clientRepository.save(client);
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
                clientRepository.save(client);
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

    @PostMapping("client/save")
    public ModelAndView ClientSave(HttpServletRequest request, @ModelAttribute("client") Client client, BindingResult result) {
       MD5Encrypt md5 = new MD5Encrypt();
        try {
            System.out.println("Nuero de clientes : " + clientRepository.clientByEmail(client.getEmail()));
            if (clientRepository.clientByEmail(client.getEmail()) > 0) {
                return getLoginAndRegister(request, new Client(), 0, "Ya existe el email a registrar", "new");
            } else {
                client.setPassword(md5.getMD5(client.getPassword()));
                client.setStatus(1);
                clientRepository.save(client);
                return getLoginAndRegister(request, new Client(), 1, "Se registro correctamente", "new");
            }

        } catch (Exception ex) {
            return getLoginAndRegister(request, new Client(), 0, "Error al registrar : " + ex.getStackTrace()[0].toString(), "new");
        }
    }

    public ModelAndView getLoginAndRegister(HttpServletRequest request, Client client, int status, String msg, String mode) {

        ModelAndView view = new ModelAndView("login_register");
        view.addObject("client", client);
        view.addObject("status", status);
        view.addObject("mode", mode);
        view.addObject("msg", msg);

        return view;
    }

    @RequestMapping("/login")
    public ModelAndView viewLoginAndRegister(HttpServletRequest request) {
        return getLoginAndRegister(request, new Client(), 2, "", "new");
    }

    @PostMapping("/login")
    public ModelAndView login(HttpSession session, @RequestParam("email") String email, @RequestParam("password") String password) {
        MD5Encrypt md5 = new MD5Encrypt();
        Client client = clientRepository.getClientByEmail(email);
        if (client != null) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                if (md5.getMD5(password).equals(client.getPassword())) {
                    session.setAttribute("user", client);
                    return getLoginAndRegister(null, new Client(), 4,"Bienvenido !!!!", "new");
                } else {
                    return getLoginAndRegister(null, new Client(), 3,"Error al ingresar contraseña", "new");
                }
            }else{
                 return getLoginAndRegister(null, new Client(), 3,"Usuario no encontrado", "new");
            }
        }else{
            return getLoginAndRegister(null, new Client(), 3,"Usuario no encontrado", "new");
        }
    }

    @GetMapping("api/client/getAll")
    @ResponseBody
    public ResponseEntity getAllClient() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("clients", clientRepository.findAll());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException ex) {
            response.put("status", "error");
            response.put("message", ex.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable("id") int id) {
        Optional<Client> clientes = clientRepository.findById(id);

        if (clientes.isPresent()) {
            return new ResponseEntity<>(clientes.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/client/delete/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") int id) {
        try {
            clientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
