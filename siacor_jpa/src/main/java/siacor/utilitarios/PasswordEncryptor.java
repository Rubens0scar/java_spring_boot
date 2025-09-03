package siacor.utilitarios;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptor {
    public String encryptPassword(String plainPassword) {
        // Generar un hash de la contraseña
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    // public static void main(String[] args) {
    //     String password = "mySecurePassword123";
        
    //     // Encriptar la contraseña
    //     String hashedPassword = encryptPassword(password);
    //     System.out.println("Contraseña encriptada: " + hashedPassword);
    // }
}
