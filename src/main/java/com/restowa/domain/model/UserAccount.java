package com.restowa.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Email;
import org.springframework.web.client.RestTemplate;

@Entity
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Column(name = "firstname")
    private String firstName;
    
    @NotEmpty
    @Column(name = "lastname")
    private String lastName;
    
    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;
    
    @NotEmpty
    @Column(name = "password")
    private String password;
    
    @NotEmpty
    @Column(name = "phonenumber")
    private String phoneNumber;
    
    @Column(name = "active")
    private boolean active;
    
    @Column(name = "creationdate")
    private LocalDateTime creationDate;
    
    @Column(name = "lastmodificationdate")
    private LocalDateTime lastModificationDate;
    
    @Column(name = "resetpasswordlink")
    private String resetPasswordLink;
    
    @Column(name = "restelinkvalidatedate")
    private LocalDateTime resetLinkValidateDate; 
    
    @Column(name = "isremoved")
    private boolean isRemoved;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeEnum type;
    
    @Embedded
    private Address address;
    
    @OneToMany(mappedBy = "lastModifiedBy")
    private List<Store> modifiedStores;
    
    @OneToMany(mappedBy = "owner")
    private List<Store> stores;
    
    @Column(name = "token", length = 1024)
    private String token;
    
    public UserAccount() {
        
    }
    
    public UserAccount(String firstname,String lastname,String email,String password,String phoneNumber,TypeEnum type,Address address) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.address = address;
        this.stores = new ArrayList<>();
    }
    
    public void setUserAccount(UserAccount userAccount) {
        this.id = userAccount.getId();
        this.firstName = userAccount.getFirstName();
        this.lastName = userAccount.getLastName();
        this.email = userAccount.getEmail();
        this.password = userAccount.getPassword();
        this.phoneNumber = userAccount.getPhoneNumber();
        this.active = userAccount.isActive();
        this.creationDate = userAccount.getCreationDate();
        this.lastModificationDate = userAccount.getLastModificationDate();
        this.resetPasswordLink = userAccount.getResetPasswordLink();
        this.resetLinkValidateDate = userAccount.getResetLinkValidateDate();
        this.isRemoved = userAccount.IsRemoved();
        this.type = userAccount.getType();
        this.address = userAccount.getAddress();
        this.stores = userAccount.getStores();
        this.token = userAccount.getToken();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getResetPasswordLink() {
        return resetPasswordLink;
    }

    public void setResetPasswordLink(String resetPasswordLink) {
        this.resetPasswordLink = resetPasswordLink;
    }

    public LocalDateTime getResetLinkValidateDate() {
        return resetLinkValidateDate;
    }

    public void setResetLinkValidateDate(LocalDateTime resetLinkValidateDate) {
        this.resetLinkValidateDate = resetLinkValidateDate;
    }

    public boolean IsRemoved() {
        return isRemoved;
    }
    
    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }
    
    public TypeEnum getType() {
        return type;
    }
    
    public void setType(TypeEnum type) {
        this.type = type;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Store> getModifiedStores() {
        return modifiedStores;
    }

    public void setModifiedStores(List<Store> modifiedStores) {
        this.modifiedStores = modifiedStores;
    }    
    
    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
