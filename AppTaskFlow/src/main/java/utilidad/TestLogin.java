package utilidad;

import service.UsuarioService;
import java.util.Scanner;

/**
 * Clase para probar el sistema de login desde consola
 */
public class TestLogin {
    
    public static void main(String[] args) {
        System.out.println("=== TESTEO DEL SISTEMA DE LOGIN ===");
        
        // Crear usuario admin por defecto
        UsuarioService.crearUsuarioAdminPorDefecto();
        
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();
        
        while (true) {
            System.out.println("\n--- LOGIN TEST ---");
            System.out.print("Usuario: ");
            String username = scanner.nextLine().trim();
            
            if ("salir".equalsIgnoreCase(username)) {
                break;
            }
            
            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();
            
            boolean loginExitoso = usuarioService.login(username, password);
            
            if (loginExitoso) {
                System.out.println("✅ LOGIN EXITOSO!");
                System.out.println("Bienvenido: " + UsuarioService.getUsuarioLogueado().getNombreCompleto());
                System.out.println("Email: " + UsuarioService.getUsuarioLogueado().getEmail());
                
                // Cerrar sesión para próxima prueba
                UsuarioService.logout();
                System.out.println("Sesión cerrada para próxima prueba.");
            } else {
                System.out.println("❌ CREDENCIALES INCORRECTAS");
            }
            
            System.out.println("\nEscribe 'salir' como usuario para terminar");
        }
        
        scanner.close();
        System.out.println("Programa terminado.");
    }
}