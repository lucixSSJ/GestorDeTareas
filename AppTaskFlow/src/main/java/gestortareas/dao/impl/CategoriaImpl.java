package gestortareas.dao.impl;

import gestortareas.dao.ICategoriaDAO;
import gestortareas.model.Categoria;
import gestortareas.utilidad.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaImpl implements ICategoriaDAO {
    @Override
    public boolean existCategoria(String nombre) {

        if(nombre.trim().isEmpty()) return false;

        String SQL = "SELECT * FROM categorias WHERE nombre_categoria = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pStm = connection.prepareStatement(SQL)){

            pStm.setString(1, nombre);
            ResultSet rs = pStm.executeQuery();
            return rs.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean registerCategory(Categoria categoria) {
        return false;
    }
}
