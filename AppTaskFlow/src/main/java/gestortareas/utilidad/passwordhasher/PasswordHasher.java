package gestortareas.utilidad.passwordhasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {

    private static final String SALT = "gestor_tareas_2024_salt_seguro";

    public static String hashPassword(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwordWithSalt = password + SALT;

            byte[] passwordBytes = passwordWithSalt.getBytes(StandardCharsets.UTF_8);

            byte[] hashedPassword = md.digest(passwordBytes);
            String result = Base64.getEncoder().encodeToString(hashedPassword);

            return result;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    public static boolean verificarPassword(String password, String storedHash) {
        try {
            String hashedInput = hashPassword(password);
            return hashedInput.equals(storedHash);

        } catch (Exception e) {
            System.err.println("Error al verificar contraseña: " + e.getMessage());
            return false;
        }
    }

}
