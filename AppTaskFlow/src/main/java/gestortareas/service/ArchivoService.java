/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortareas.service;

import gestortareas.dao.ArchivoDao;
import gestortareas.dao.impl.ArchivoImpl;
import gestortareas.model.ArchivoAdjunto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Michael
 */
public class ArchivoService {

    private final ArchivoDao archivoImplements;
    private final String RUTA_BASE = "tarea/Archivos/";

    public ArchivoService(ArchivoDao archivoDao) {
        this.archivoImplements = archivoDao;
    }

    public boolean createArchivo(List<File> archivos, int idTarea) {

        if (archivos == null || archivos.isEmpty()) {
            System.err.println("Lista de archivos vacía o null");
            return false;
        }

        if (idTarea <= 0) {
            System.err.println("ID de tarea inválido: " + idTarea);
            return false;
        }

        try {
            List<ArchivoAdjunto> listArchivos = mapearAModeloArchivo(archivos, idTarea);

            if (listArchivos.isEmpty()) {
                System.err.println("No se pudo procesar ningún archivo");
                return false;
            }

            boolean ok;

            if (listArchivos.size() == 1) {
                ok = archivoImplements.createArchivo(listArchivos.getFirst(), idTarea);
            } else {
                ok = archivoImplements.createVariosArchivos(listArchivos, idTarea);
            }

            if (ok) {
                System.out.println(listArchivos.size() + " archivo(s) guardado(s) correctamente");
                return true;
            } else {
                System.err.println("Error al insertar archivos en la base de datos");
                return false;
            }

        } catch (Exception ex) {
            System.err.println("Error general en createArchivo: " + ex.getMessage());
            return false;
        }
    }

    private List<ArchivoAdjunto> mapearAModeloArchivo(List<File> archivo, int idTarea) {
        return archivo.stream()
                .map(file -> convertir(file, idTarea))
                .collect(Collectors.toList());
    }

    private ArchivoAdjunto convertir(File file, int idTarea) {
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();

        String nombre = file.getName();
        Long idTask = (long) idTarea;
        String tipoArchivo = tipoArchivo(file);
        Long tamanio = file.length();
        LocalDate fechaSubida = LocalDate.now();

        Path directorioDestino = createCarpetaSiNoExiste();

        if (directorioDestino == null) {
            System.err.println("No se pudo crear el directorio para: " + file.getName());
            return null;
        }

        Path archivoDestino = directorioDestino.resolve(nombre);

        try {
            Files.copy(file.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo guardado en el sistema");
        } catch (IOException ex) {
            System.out.println("Error al guardar el archivo en el sistema: " + ex.getMessage());
            return null;
        }

        archivoAdjunto.setIdTarea(idTask);
        archivoAdjunto.setNombreArchivo(nombre);
        archivoAdjunto.setTipoArchivo(tipoArchivo);
        archivoAdjunto.setTamanio(tamanio);
        archivoAdjunto.setFechaSubida(fechaSubida);
        archivoAdjunto.setRuta(directorioDestino.toString());

        return archivoAdjunto;

    }

    private String tipoArchivo(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private Path createCarpetaSiNoExiste() {
        try {
            // Obtener año y mes actual
            int anio = Year.now().getValue();
            int mes = LocalDate.now().getMonthValue();

            String rutaCompleta = String.format("%s%d/%02d", RUTA_BASE, anio, mes);
            Path directorio = Paths.get(rutaCompleta);

            // Crear directorios si no existen
            Files.createDirectories(directorio);

            System.out.println("Directorio creado/verificado: " + directorio.toAbsolutePath());
            return directorio;

        } catch (IOException ex) {
            System.err.println("Error al crear directorio: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

}
