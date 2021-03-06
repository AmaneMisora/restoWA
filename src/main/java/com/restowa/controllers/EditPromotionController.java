package com.restowa.controllers;

import com.restowa.bl.concrete.PromotionManager;
import com.restowa.domain.model.Promotion;
import com.restowa.domain.model.TypeEnum;
import com.restowa.domain.model.UserAccount;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller handling the edit promotion page
 * 
 * @author yanis
 */
@Controller
public class EditPromotionController {
    
    private static final Logger LOGGER = Logger.getLogger(EditPromotionController.class.getName());
    
    @Resource
    private PromotionManager promotionManager;

    /**
     * show promotion page
     * 
     * @param userAccount connected user
     * @param promotionId id of the promotion to modify (optional)
     * @param action action to do (optional)
     * @return 
     */
    @GetMapping("/editPromotion")
    public ModelAndView promotion(@SessionAttribute(name="userAccount", required=false) UserAccount userAccount, Integer promotionId, String action) {
        
        LOGGER.log(Level.INFO, "Start EditPromotionController (promotion)");
        
        // Need to be connected to access this page
        if(userAccount == null) {
            ModelAndView mav = new ModelAndView("accessDenied");
            mav.addObject("errorMessage", "Vous devez vous connecter pour pouvoir avoir accès à cette page !");
            LOGGER.log(Level.SEVERE, "Access right not sufficient");
            LOGGER.log(Level.INFO, "End EditPromotionController");
            return mav;
        }
        if(userAccount.getId() == 0) {
            ModelAndView mav = new ModelAndView("accessDenied");
            mav.addObject("errorMessage", "Vous devez vous connecter pour pouvoir avoir accès à cette page !");
            LOGGER.log(Level.SEVERE, "Access right not sufficient");
            LOGGER.log(Level.INFO, "End EditPromotionController");
            return mav;
        }
        
        // New promotion
        if(promotionId == null && action == null) {
            
            // Only administrator and owner can create a promotion
            if(userAccount.getType() == TypeEnum.Client) {
                ModelAndView mav = new ModelAndView("accessDenied");
                mav.addObject("errorMessage", "Vous n'avez pas les droits pour créer une promotion !");
                LOGGER.log(Level.SEVERE, "Access right not sufficient");
                LOGGER.log(Level.INFO, "End EditPromotionController");
                return mav;
            }
            
            // Initialization of the new store
            ModelAndView model = new ModelAndView("editPromotion");
            model.addObject("promotion", new Promotion());
            
            LOGGER.log(Level.INFO, "End EditPromotionController");
            
            return model;
        } else if(promotionId != null && action.equals("update")) {
            
            // Modify an existing promotion
            if(promotionId >= 0) {
                
                Promotion promotion = promotionManager.getPromotionById(promotionId);
                
                // Check if the user has the rights to modifiy the store
                if(userAccount.getType() == TypeEnum.Administrateur) {
                    ModelAndView model = new ModelAndView("editPromotion");
                    model.addObject("promotion", promotion);
                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return model;
                }else {
                    ModelAndView mav = new ModelAndView("accessDenied");
                    mav.addObject("errorMessage", "Vous n'avez pas les droits de modifier cette promotion !");
                    LOGGER.log(Level.SEVERE, "Access right not sufficient");
                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return mav;
                }
            } else {
                ModelAndView mav = new ModelAndView("accessDenied");
                mav.addObject("errorMessage", "Vous essayer de modifier une promotion qui n'éxiste pas ou qui ne vous appartient pas !");
                LOGGER.log(Level.SEVERE, "Access right not sufficient");
                LOGGER.log(Level.INFO, "End EditPromotionController");
                return mav;
            }
        }
        ModelAndView mav = new ModelAndView("accessDenied");
        mav.addObject("errorMessage", "Un problème est survenu dans l'url !");
        LOGGER.log(Level.SEVERE, "Access right not sufficient");
        LOGGER.log(Level.INFO, "End EditPromotionController");
        return mav;
    }
    
    /**
     * Create or modify a promotion
     * 
     * @param userAccount connected user
     * @param promotion promotion to create or modify
     * @param promotionResult possible errors of the promotion
     * @param file image of the promotion to upload
     * @param model
     * @param promotionId id of the promotion to modify (optional)
     * @param action action to do (optional)
     * @return 
     */
    @PostMapping("/editPromotion")
    public ModelAndView editPromotion(@SessionAttribute(name="userAccount", required=false) UserAccount userAccount, @Valid Promotion promotion, BindingResult promotionResult, @RequestParam("file") MultipartFile file, Model model, Integer promotionId, String action) {
        
        LOGGER.log(Level.INFO, "Start EditPromotionController (editPromotion)");
        
        // Need to be connected to access this page
        if(userAccount == null) {
            ModelAndView mav = new ModelAndView("accessDenied");
            mav.addObject("errorMessage", "Vous devez vous connecter pour pouvoir avoir accès à cette page !");
            LOGGER.log(Level.SEVERE, "Access right not sufficient");
            LOGGER.log(Level.INFO, "End EditPromotionController");
            return mav;
        }
        if(userAccount.getId() == 0) {
            ModelAndView mav = new ModelAndView("accessDenied");
            mav.addObject("errorMessage", "Vous devez vous connecter pour pouvoir avoir accès à cette page !");
            LOGGER.log(Level.SEVERE, "Access right not sufficient");
            LOGGER.log(Level.INFO, "End EditPromotionController");
            return mav;
        }
        
        // Client cannot create or modify a promotion
        if(userAccount.getType() == TypeEnum.Client) {
                ModelAndView mav = new ModelAndView("accessDenied");
                mav.addObject("errorMessage", "Vous n'avez pas les droits pour créer ou modifier une promotion !");
                LOGGER.log(Level.SEVERE, "Access right not sufficient");
                LOGGER.log(Level.INFO, "End EditPromotionController");
                return mav;
            }
        
        // Create new or modify old promotion
        if(promotionId == null && action == null) {
        
        
            // If the form contains error
            if (promotionResult.hasErrors()) {
                ModelAndView mav = new ModelAndView("editPromotion");
                mav.addObject("promotion", promotion);
                LOGGER.log(Level.INFO, "End EditPromotionController");
                return mav;
            }
            
            boolean key = false;
            if(promotion.getId() == 0) {
                key = true;
                promotion.setKeyPromotion("temp");
            } else {
                // Only administrator can modifiy a promotion
                if(userAccount.getType() != TypeEnum.Administrateur) {
                    ModelAndView mav = new ModelAndView("accessDenied");
                    mav.addObject("errorMessage", "Vous n'avez pas les droits pour modifier cette promotion !");
                    LOGGER.log(Level.SEVERE, "Access right not sufficient");
                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return mav;
                }
            }
           
            // HERE
            if (!file.isEmpty()) {
                try {
                BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                
                // Generate random String
                String rootPath = System.getProperty("catalina.home");
                File destination = new File(rootPath + File.separator + file.getOriginalFilename());
                ImageIO.write(src, file.getContentType().split("/")[1], destination);
                promotion.setImageURL(rootPath + File.separator + file.getOriginalFilename());
                System.out.println(rootPath + File.separator + file.getOriginalFilename());
                } catch(IOException e) {
                    ModelAndView mav = new ModelAndView("editPromotion");
                    mav.addObject("promotion", promotion);
                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return mav;
                }
            }
            promotion = promotionManager.savePromotion(promotion);
            promotion.setKeyPromotion("PROMO-"+promotion.getId());
            promotionManager.savePromotion(promotion);
            LOGGER.log(Level.INFO, "End EditPromotionController");
            return new ModelAndView("redirect:listPromotion");
        } else if(promotionId != null && action.equals("delete")) {
            
            // Modify an existing store
            if(promotionId >= 0) {
                
                // Check if the user has the rights to modifiy the store
                if(userAccount.getType() == TypeEnum.Administrateur) {
                    
                    promotionManager.deletePromotion(promotionManager.getPromotionById(promotionId));

                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return new ModelAndView("redirect:listPromotion");
                }else {
                    ModelAndView mav = new ModelAndView("accessDenied");
                    mav.addObject("errorMessage", "Vous ne pouvez pas supprimer une promotion qui ne vous appartient pas !");
                    LOGGER.log(Level.SEVERE, "Access right not sufficient");
                    LOGGER.log(Level.INFO, "End EditPromotionController");
                    return mav;
                }
            } else {
                ModelAndView mav = new ModelAndView("accessDenied");
                mav.addObject("errorMessage", "Vous essayer de modifier une promotion qui n'éxiste pas ou qui ne vous appartient pas !");
                LOGGER.log(Level.SEVERE, "Access right not sufficient");
                LOGGER.log(Level.INFO, "End EditPromotionController");
                return mav;
            }
        }
        ModelAndView mav = new ModelAndView("accessDenied");
        mav.addObject("errorMessage", "Un problème est survenu dans l'url !");
        LOGGER.log(Level.SEVERE, "Access right not sufficient");
        LOGGER.log(Level.INFO, "End EditPromotionController");
        return mav;
    }
}
