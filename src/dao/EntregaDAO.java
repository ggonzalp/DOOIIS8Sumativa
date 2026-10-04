package dao;

import modelo.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase EntregaDAO que permite el acceso a datos de la tabla Entrega.
 */
public class EntregaDAO {

    /**
     * Inserta una entrega en la base de datos.
     *
     * @param entrega Entrega que asocia un pedido con un repartidor.
     * @return true si la fila se insertó correctamente, de lo contrario devuelve false.
     */
    public boolean guardar(Entrega entrega) {

        String sql = "INSERT INTO entrega (idPedido, idRepartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene las entregas registradas en la base de datos.
     *
     * @return Lista con entregas.
     */
    public List<Entrega> listarTodos() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = "SELECT idPedido, idRepartidor, fecha, hora FROM entrega";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("idPedido");
                int idRepartidor = rs.getInt("idRepartidor");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime hora = rs.getTime("hora").toLocalTime();

                Entrega entrega = new Entrega(idPedido, idRepartidor, fecha, hora);

                entregas.add(entrega);
            }

        } catch (
                SQLException e) {
            System.out.println("Error al listar entregas: " + e.getMessage());
        }
        return entregas;
    }

    /**
     * Cambia el repartidor, la fecha y hora de una entrega.
     * @return true si actualizó una fila, false si hubo error.
     */
    public boolean actualizar(Entrega entrega) {
        String sql = "UPDATE entrega SET idRepartidor = ?, fecha = ?, hora = ? WHERE idPedido = ?";

        try (Connection conexion = ConexionBD.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdRepartidor());
            ps.setDate(2, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(3, java.sql.Time.valueOf(entrega.getHora()));
            ps.setInt(4, entrega.getIdPedido());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarEntrega(int idPedido) {
        String sql = "DELETE FROM entrega WHERE idPedido = ?";
        try (Connection conexion = ConexionBD.conectar();
        PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
            return false;
        }
    }
}
