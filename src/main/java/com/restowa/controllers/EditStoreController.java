/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restowa.controllers;

import com.restowa.bl.concrete.StoreManager;
import com.restowa.domain.model.Address;
import com.restowa.domain.model.OpeningHours;
import com.restowa.domain.model.Schedule;
import com.restowa.domain.model.Store;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author yanis
 */

@Controller
public class EditStoreController {
    
    @Resource
    private StoreManager storeManager;
    
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    
    @GetMapping("/editStore")
    public ModelAndView store() {
        ModelAndView model = new ModelAndView("editStore");
        model.addObject("store", new Store());
        model.addObject("openingHours", new OpeningHours());
        model.addObject("address", new Address());

        return model;
    }
    
    @PostMapping("/editStore")
    public String editStore(@Valid Store store, BindingResult storeResult, @Valid OpeningHours openingHours, BindingResult openingHoursResult, @Valid Address address, BindingResult addressResult, Model model) {

        
        if (storeResult.hasErrors() || addressResult.hasErrors() || openingHoursResult.hasErrors()) {
            return "editStore";
        }
        
        
        store.setAddress(address);
        store.setKeyStore("test");
        store.setLastModificationDate(LocalDateTime.now());
        store.setLastModifiedBy(null);
        store.setOpeningHours(openingHours);
        
        storeManager.saveStore(store);
        
        /*
        else if(!uamanager.getUserAccountByEmail(userAccount.getEmail()).isEmpty()) {
            userAccountResult.addError(new FieldError("UserAccount", "email", "Cet email est déjà utilisé"));
            return "register";
        }

        Date date = new Date();
        
        userAccount.setActive(true);
        userAccount.setAddress(address);
        userAccount.setCreationDate(date);
        userAccount.setIsRemoved(false);
        userAccount.setLastModificationDate(date);
        userAccount.setType(TypeEnum.Client);
        uamanager.saveUserAccount(userAccount);
*/
        
        
        // /!\/!\/!\ Retourner vers la page après connexion lorsqu'elle existe /!\/!\/!\
        return "redirect:/";
    }
    
}