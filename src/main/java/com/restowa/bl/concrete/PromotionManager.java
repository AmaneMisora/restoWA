/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restowa.bl.concrete;

import com.restowa.domain.model.Promotion;
import com.restowa.domain.repository.PromotionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Amane
 */
@Component
public class PromotionManager {

    private PromotionRepository repo;
    
    @Autowired
    public PromotionManager(PromotionRepository repo)
    {
        this.repo  = repo;
    }
    
    public Promotion savePromotion(Promotion promotion)
    {
        return this.repo.save(promotion);
    }
    
    public List<Promotion> getAllPromotions() {
        return this.repo.findAll();
    }
    
    public void deletePromotion(Promotion promotion)
    {
        this.repo.delete(promotion);
    }
    
    public Promotion getPromotionById(int id)
    {
        return this.repo.findById(id).get();
    }
}
