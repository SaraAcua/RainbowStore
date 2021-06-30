/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Repository;

import com.Sara.TiendaArcoiris.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author SARA
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    
      @Query("from User u where u.email = :email ")
  public User getUserByEmail(String email);
  
     @Query(value="select count(*) from users c where c.email = :email",nativeQuery = true)
  public int userByEmail(String email);
    
}
