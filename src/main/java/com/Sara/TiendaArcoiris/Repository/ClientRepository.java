/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Repository;

import com.Sara.TiendaArcoiris.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author SARA
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {


  @Query("from Client c where c.email = :email ")
  public Client getClientByEmail(String email);
  
  @Query(value="select count(*) from clients c where c.email = :email",nativeQuery = true)
  public int clientByEmail(String email);
  
    
}
