/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utilidad para hashear y verificar contraseñas de forma segura
 * @author Luciano
 */
public class PasswordHasher {

    private static final String SALT = "gestor_tareas_2024_salt_seguro";

    /**
     * Hashea una contraseña usando SHA-256 con salt
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwordWithSalt = password + SALT;
            byte[] passwordBytes = passwordWithSalt.getBytes(StandardCharsets.UTF_8);
            byte[] hashedPassword = md.digest(passwordBytes);
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña coincide con el hash almacenado
     */
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