package com;

import org.mindrot.jbcrypt.BCrypt;

public class testhaskpassword {
    public static void main(String[] args) {

        // Hash password
        String password = "123456";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        System.out.println(hashedPassword);
        // Output: $2a$10$abcd...xyz

        // Kiểm tra password khi login
        String inputPassword = "123456";
        boolean isValid = BCrypt.checkpw(inputPassword, hashedPassword);
        System.out.println(isValid);
    }
}
