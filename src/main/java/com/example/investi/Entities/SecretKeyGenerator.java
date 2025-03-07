package com.example.investi.Entities;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {
        public static void main(String[] args) {
            SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
            String base64Key = Encoders.BASE64URL.encode(key.getEncoded()); // Use Encoders.BASE64URL
            System.out.println("Generated Secret Key (Base64 URL-safe): " + base64Key);
        }
}