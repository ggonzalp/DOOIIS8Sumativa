package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionBD que establece conexión con MySQL y la base de datos.
 */
public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/speedfast_db";

    private static final String USER = "root";
    private static final String PASSWORD = "BaseDatos2026";

    /**
     * Metodo conectar
     * @return una conexión activa.
     * @throws SQLException
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
