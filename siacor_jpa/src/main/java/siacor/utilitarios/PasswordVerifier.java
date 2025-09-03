package siacor.utilitarios;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordVerifier {
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Verificar si la contraseña coincide con el hash
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // public static void main(String[] args) {
    //     String password = "mySecurePassword123";
    //     String hashedPassword = "$2a$10$3e7Uz9zZ8z9z8z9z8z9z8u"; // Hash generado previamente

    //     // Verificar la contraseña
    //     boolean isMatch = verifyPassword(password, hashedPassword);
    //     System.out.println("¿La contraseña coincide? " + isMatch);
    // }
}
