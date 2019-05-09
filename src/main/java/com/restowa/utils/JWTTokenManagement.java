/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restowa.utils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Paul
 */
public class JWTTokenManagement {
    
    
    private static RsaJsonWebKey rsaJsonWebKey;
    
    
    public JWTTokenManagement() {
        
    }
    
    public static String generateToken(int userId) throws JoseException{
        // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

        // Give the JWK a Key ID (kid), which is just the polite thing to do
        rsaJsonWebKey.setKeyId("k1");

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();  
        claims.setIssuer("Issuer");  // who creates the token and signs it
        claims.setAudience("Audience"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(3600); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(1); // time before which the token is not yet valid (1 minutes ago)
        claims.setClaim("id",userId); // additional claims/attributes about the subject can be added
        
        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(rsaJsonWebKey.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = jws.getCompactSerialization();
        

        return jwt;
    }
    
    
    public static String verifyToken(String headerJwt, String userJwt){
        String result = "";
        if (headerJwt.equals(userJwt)){
            // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
            // be used to validate and process the JWT.
            // The specific validation requirements for a JWT are context dependent, however,
            // it typically advisable to require a (reasonable) expiration time, a trusted issuer, and
            // and audience that identifies your system as the intended recipient.
            // If the JWT is encrypted too, you need only provide a decryption key or
            // decryption key resolver to the builder.
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
                    .setExpectedAudience("Audience") // to whom the JWT is intended for
                    .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                    .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                            new AlgorithmConstraints(ConstraintType.WHITELIST, // which is only RS256 here
                                    AlgorithmIdentifiers.RSA_USING_SHA256))
                    .build(); // create the JwtConsumer instance

            try
            {
                //  Validate the JWT and process it to the Claims
                JwtClaims jwtClaims = jwtConsumer.processToClaims(headerJwt);
                result = "Token validation succeeded! " + jwtClaims;
            }
            catch (InvalidJwtException e)
            {
                // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
                // Hopefully with meaningful explanations(s) about what went wrong.
                result = "Invalid Token! " + e;

                // Programmatic access to (some) specific reasons for JWT invalidity is also possible
                // should you want different error handling behavior for certain conditions.

                // Whether or not the JWT has expired being one common reason for invalidity
                if (e.hasExpired())
                {
                    try {
                        result = "\n" + "Token expired at " + e.getJwtContext().getJwtClaims().getExpirationTime();
                    } catch (MalformedClaimException ex) {
                        Logger.getLogger(JWTTokenManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // Or maybe the audience was invalid
                if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID))
                {
                    try {
                        result = "\n" + "Token had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience();
                    } catch (MalformedClaimException ex) {
                        Logger.getLogger(JWTTokenManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else{
            result = "Wrong Token! ";
        }
        
        return result;
    }   
    
}
