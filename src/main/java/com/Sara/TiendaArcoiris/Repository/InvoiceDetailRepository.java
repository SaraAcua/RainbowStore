/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Repository;

import com.Sara.TiendaArcoiris.Entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author SARA
 */
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
    
}
