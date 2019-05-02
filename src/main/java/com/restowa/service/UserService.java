/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restowa.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.restowa.bl.concrete.UserAccountManager;
import com.restowa.domain.model.Address;
import com.restowa.domain.model.TypeEnum;
import com.restowa.domain.model.UserAccount;
import com.restowa.utils.TokenManagement;
import javax.annotation.Resource;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Paul
 */
@RestController
@RequestMapping("/api/user")
public class UserService {
    
    @Resource
    UserAccountManager uamanager;
    
    
    public UserService(){    
    }
    
    
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String register(@RequestBody String obj) throws ParseException { 
        JSONParser parser = new JSONParser(); 
        JSONObject json = (JSONObject) parser.parse(obj);
        Address address = new Address((String)json.get("Street"),(String)json.get("City"),(String)json.get("State"),(int)json.get("ZipCode"),(String)json.get("Country"));
        UserAccount useraccount = new UserAccount((String)json.get("firstname"),(String)json.get("lastname"),(String)json.get("email"),(String)json.get("password"),(String)json.get("phoneNumber"),(TypeEnum)json.get("type"),address);
        uamanager.saveUserAccount(useraccount);

        return "user created";
    }
    

    /* utiliser postam pour tester le login
     * obj est un string de la forme d'un json
     * HttpStatus.OK; renvoie si bon
     * HttpStatus.404 si mauvais etc
     */
    @RequestMapping(value = "/loginVerify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public String verifyLogin(@RequestBody String obj, @RequestHeader HttpHeaders headers) throws ParseException { 
        String stringToken = headers.get("authentificationToken").get(0);
        //if (!TokenManagement.verifyToken(headers.get("authentificationToken").get(0))) {
       //       throws new NotAuthorizedException("Invalid token");
       // }
        JSONParser parser = new JSONParser(); 
        JSONObject json = (JSONObject) parser.parse(obj);
        UserAccount ua = uamanager.getUserAccountByEmail((String)json.get("email")).get(0);
        //ça marche car ya pas de user account
        JSONObject resultLogin = new JSONObject();
        if (ua.getPassword().equals((String)json.get("password"))){
            resultLogin.put("login", true); 
        } else {
            resultLogin.put("login", false);
        }
       //deplacer la verif de token dans get user info
        return stringToken;   //retourner un string en fonction du résultat de la classe de lucie 
                        //HttpStatus.BAD_REQUEST;
    }

    /* a besoin d'un json avec l'id de l'utilisateur et un JWToken dans le header
     * verifie si le token est bon et si il est bon va chercher l'utilisateur avec la fonction de lucie getuserbyid
     * puis met toutes les info de l'utilisateur dans un json que l'on renvoie
     */
    //@JWTTokenNeeded
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public JSONObject getUserInfo(@RequestBody int userId){ 
    
 
        //UserAccount user = UserAccountDAO.getUserById(userId);
        
        ObjectMapper mapper = new ObjectMapper();
        
        JSONObject userInfo = new JSONObject();
        //JSONObject userInfo = mapper.writeValueAsString(user);
        //pas sur que le mapper marche quand on enregistre dans json object
        //si ça marche pas enregistrer dans un string puis le transformer en jsonobject avec un parser
        return userInfo;
    }
    
}
