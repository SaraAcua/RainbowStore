/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;


import com.Sara.TiendaArcoiris.Entity.Client;
import com.Sara.TiendaArcoiris.Entity.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author SARA
 */
@Controller
public class homeController {
    
  
    @RequestMapping("/")
    public String Home(){
        return "index";
    }
    
    @RequestMapping("/shopping-cart")
    public String ShoppingCart(Model model){
        model.addAttribute("name", "Carrito de Compras");
        return "shopping-cart";
    }
    

  
    
    @RequestMapping("/checkout")
    public String Checkout(Model model){
        
        return "checkout";
    }
    @RequestMapping("/order")
    public String Order(Model model){
        
        return "order";
    }
    @RequestMapping("/shop")
    public String ShopSidebar(Model model){
        
        return "shop-right-sidebar";
    }
      @RequestMapping("/faq")
    public String Faq(Model model){
        
        return "faq";
    }
     @RequestMapping("/blog")
    public String Blog(Model model){
        
        return "blog-no-sidebar";
  
    }
     @RequestMapping("/about")
    public String About(Model model){
        
        return "about";
  
    }
      @RequestMapping("/contact")
    public String Contact(Model model){
        
        return "contact";
  
    }
      @RequestMapping("/tracking")
    public String Tracking(Model model){
        
        return "tracking";
  
    }
      @RequestMapping("/wishlist")
    public String Wishlist(Model model){
        
        return "wishlist";
  
    }
     @RequestMapping("/form-product")
    public String FormProduct(Model model){
        
        return "form-product";
}
     @RequestMapping("/dashboard")
    public String Dashboard(Model model){
        
        return "templatesAdmin/dashboard";
}

    @RequestMapping("/formProduct")
    public String FormProductS(Model model){
        
        return "templatesAdmin/form-Products";
}
        @RequestMapping("/listProducts")
    public String ListProducts(Model model){
        
        return "templatesAdmin/list-Products";
}
           @RequestMapping("/listClients")
    public String ListClients(Model model){
        
        return "templatesAdmin/list-clients";
}

    
}
