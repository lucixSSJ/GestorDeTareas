package utilidad;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;

/**
 * Clase para mantener el estado de las ventanas entre diferentes interfaces
 */
public class WindowStateManager {
    private static WindowStateManager instance;
    private Dimension windowSize;
    private Point windowLocation;
    private int windowState; // JFrame.NORMAL, JFrame.MAXIMIZED_BOTH, etc.
    private boolean isInitialized = false;
    
    private WindowStateManager() {
        // Valores por defecto
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.windowSize = new Dimension((int)(screenSize.width * 0.95), (int)(screenSize.height * 0.9));
        this.windowLocation = new Point(
            (screenSize.width - windowSize.width) / 2,
            (screenSize.height - windowSize.height) / 2
        );
        this.windowState = JFrame.NORMAL;
    }
    
    public static WindowStateManager getInstance() {
        if (instance == null) {
            instance = new WindowStateManager();
        }
        return instance;
    }
    
    /**
     * Configura una ventana con el estado guardado
     */
    public void configureWindow(JFrame frame) {
        if (!isInitialized) {
            initializeFromScreen();
        }
        
        // Configurar propiedades básicas
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        
        // Aplicar tamaño y posición
        if (windowState == JFrame.MAXIMIZED_BOTH) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setSize(windowSize);
            frame.setLocation(windowLocation);
            frame.setExtendedState(JFrame.NORMAL);
        }
        
        // Configurar tamaño mínimo
        frame.setMinimumSize(new Dimension(1000, 700));
        
        // Añadir listeners para actualización automática del estado
        addWindowListeners(frame);
    }
    
    /**
     * Guarda el estado actual de una ventana antes de cerrarla
     */
    public void saveWindowState(JFrame frame) {
        this.windowState = frame.getExtendedState();
        
        // Solo guardar tamaño y posición si no está maximizada
        if (windowState != JFrame.MAXIMIZED_BOTH) {
            this.windowSize = frame.getSize();
            this.windowLocation = frame.getLocation();
        }
        
        isInitialized = true;
    }
    
    /**
     * Transfiere el estado de una ventana a otra
     */
    public void transferState(JFrame fromFrame, JFrame toFrame) {
        saveWindowState(fromFrame);
        configureWindow(toFrame);
    }
    
    private void initializeFromScreen() {
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (windowSize.width > screenSize.width || windowSize.height > screenSize.height) {
            this.windowSize = new Dimension(
                Math.min(windowSize.width, (int)(screenSize.width * 0.95)),
                Math.min(windowSize.height, (int)(screenSize.height * 0.9))
            );
        }
        isInitialized = true;
    }
    
    // Getters para información de estado
    public Dimension getWindowSize() {
        return new Dimension(windowSize);
    }
    
    public Point getWindowLocation() {
        return new Point(windowLocation);
    }
    
    public int getWindowState() {
        return windowState;
    }
    
    public boolean isMaximized() {
        return windowState == JFrame.MAXIMIZED_BOTH;
    }
    
    /**
     * Añade listeners automáticos para mantener el estado sincronizado
     */
    private void addWindowListeners(JFrame frame) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Actualizar estado automáticamente cuando se redimensiona
                windowState = frame.getExtendedState();
                if (windowState != JFrame.MAXIMIZED_BOTH) {
                    windowSize = frame.getSize();
                }
                
                // Forzar revalidación completa del layout
                forceLayoutUpdate(frame);
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
                // Actualizar posición automáticamente cuando se mueve
                if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    windowLocation = frame.getLocation();
                }
            }
        });
    }
    
    /**
     * Fuerza la actualización completa del layout de una ventana
     */
    private void forceLayoutUpdate(JFrame frame) {
        // Revalidar todos los componentes de forma recursiva
        java.awt.Container contentPane = frame.getContentPane();
        invalidateComponentTree(contentPane);
        frame.revalidate();
        frame.repaint();
    }
    
    /**
     * Invalida recursivamente todos los componentes para forzar redistribución
     */
    private void invalidateComponentTree(java.awt.Container container) {
        container.invalidate();
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof java.awt.Container) {
                invalidateComponentTree((java.awt.Container) component);
            }
        }
    }
}