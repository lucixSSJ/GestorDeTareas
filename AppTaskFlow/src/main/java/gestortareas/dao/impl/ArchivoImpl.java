package gestortareas.dao.impl;

import gestortareas.dao.ArchivoDao;
import gestortareas.model.ArchivoAdjunto;
import gestortareas.utilidad.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Michael
 */
public class ArchivoImpl implements ArchivoDao {

    @Override
    public boolean createArchivo(ArchivoAdjunto archivo, int idTarea) {
        if (archivo == null || idTarea <= 0) {
            System.err.println("Archivo o ID de tarea inválido");
            return false;
        }

        String sql = "INSERT INTO archivos_adjuntos(id_tarea,nombre_archivo,tipo_archivo,ruta_archivo,tamaño,fecha_subida) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setInt(1, idTarea);
            ps.setString(2, archivo.getNombreArchivo());
            ps.setString(3, archivo.getTipoArchivo());
            ps.setString(4, archivo.getRuta());
            ps.setLong(5, archivo.getTamanio());
            ps.setTimestamp(6, new Timestamp(new Date().getTime()));

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException ex) {
            System.out.println("error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createVariosArchivos(List<ArchivoAdjunto> archivos, int idTarea) {
        if (archivos == null || archivos.isEmpty()) {
            System.err.println("Lista de archivos vacía o null");
            return false;
        }

        if (idTarea <= 0) {
            System.err.println("ID de tarea inválido");
            return false;
        }

        String sql = "INSERT INTO archivos_adjuntos(id_tarea,nombre_archivo,tipo_archivo,ruta_archivo,tamaño,fecha_subida) VALUES(?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            conn.setAutoCommit(false);
            Timestamp fechaSubida = new Timestamp(System.currentTimeMillis());

            for (ArchivoAdjunto archivo : archivos) {

                if (archivo == null) {
                    System.err.println("Archivo invalido");
                    continue;
                }

                ps.setInt(1, idTarea);
                ps.setString(2, archivo.getNombreArchivo());
                ps.setString(3, archivo.getTipoArchivo());
                ps.setString(4, archivo.getRuta());
                ps.setLong(5, archivo.getTamanio());
                ps.setTimestamp(6, fechaSubida);

                ps.addBatch();
            }

            int[] filas = ps.executeBatch();

            if (filas.length == archivos.size()) {
                conn.commit();
                System.out.println("Archivos insertados correctamente");
                return true;
            } else {
                conn.rollback();
                System.out.println("Solo insertaron algunos archivos");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("error: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rolLException) {
                    System.out.println("Revirtiendo cambios por error SQL: " + rolLException.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar recursos: " + closeEx.getMessage());
            }
        }
    }
}
