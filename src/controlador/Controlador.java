package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 * Clase que controla el flujo de información.
 */

public class Controlador {

    private RepartidorDAO repartidorDAO = new RepartidorDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private EntregaDAO entregaDAO = new EntregaDAO();

    //================
    //    REPARTIDOR
    //================

    /**
     * Metodo que registra un repartidor nuevo.
     *
     * @param nombreRepartidor nombre del repartidor.
     * @return true si se actualizó, false si no lo hizo.
     */
    public boolean registrarRepartidor(String nombreRepartidor) {

        if (nombreRepartidor.isBlank()) {
            return false;
        }

        Repartidor repartidor = new Repartidor(nombreRepartidor);

        return repartidorDAO.guardar(repartidor);
    }

    /**
     * Obtiene los repartidores registrados.
     *
     * @return lista de repartidores.
     */
    public List<Repartidor> obtenerRepartidores() {
        return repartidorDAO.listarTodos();
    }

    /**
     * Edita el repartidor seleccionado.
     *
     * @param id               número de identificación.
     * @param nombreRepartidor Nombre del repartidor.
     * @return true si se actualizó, false si no lo hizo.
     */
    public boolean editarRepartidor(int id, String nombreRepartidor) {
        if (id <= 0 || nombreRepartidor == null || nombreRepartidor.isBlank()) {
            return false;
        }

        Repartidor repartidor = new Repartidor(id, nombreRepartidor.trim());
        return repartidorDAO.actualizar(repartidor);

    }

    public boolean eliminarRepartidor(int idRepartidor) {
        if (idRepartidor <= 0) {
            return false;
        }

        return repartidorDAO.eliminar(idRepartidor);
    }

    //================
    //    PEDIDO
    //================

    /**
     * Valida los datos de un pedido y los guarda.
     *
     * @param tipoPedido       Tipo de pedido.
     * @param descripcion      Descripción del pedido.
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param distanciaKm      Distancia al lugar de entrega expresado en kilómetros.
     * @param validacion       Valida el tipo de pedido.
     * @param prioridadPedido  Define la prioridad del pedido.
     * @return true si el pedido se guardó correctamente, de lo contrario arroja un false.
     */
    public boolean registrarPedido(String tipoPedido, String descripcion, DireccionEntrega direccionEntrega, int distanciaKm, boolean validacion, String prioridadPedido, String estadoPedido) {

        if (tipoPedido.isBlank() || descripcion.isBlank() || prioridadPedido.isBlank()) {
            return false;
        }

        if (direccionEntrega == null || direccionEntrega.getNumero() <= 0 || direccionEntrega.getCalle().isBlank() || direccionEntrega.getCiudad().isBlank()) {
            return false;
        }

        PrioridadPedido prioridad;
        try {
            prioridad = PrioridadPedido.valueOf(prioridadPedido);
        } catch (IllegalArgumentException ex) {
            return false;
        }

        Pedido pedido;

        switch (tipoPedido) {
            case "Pedido Express" -> pedido = new PedidoExpress(
                    tipoPedido, 0, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            case "Pedido Comida" -> pedido = new PedidoComida(
                    tipoPedido, 0, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            case "Pedido Encomienda" -> pedido = new PedidoEncomienda(
                    tipoPedido, 0, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            default -> {
                return false;
            }
        }

        //Permite elegir el estado de un pedido.
        try {
            pedido.cambiarEstado(EstadoPedido.valueOf(estadoPedido));
        } catch (IllegalArgumentException | NullPointerException ex) {
            return false;
        }

        return pedidoDAO.guardar(pedido);
    }

    /**
     * Metodo obtener pedidos
     *
     * @return Lista de pedidos registrados.
     */
    public List<Pedido> obtenerPedidos() {
        return pedidoDAO.listarTodos();
    }

    public boolean editarPedido(int idPedido, String tipoPedido, String descripcion, int numero, String calle, String ciudad, int distancia, boolean validacion, String prioridadPedido, String estadoPedido) {
        if (idPedido <= 0) {
            return false;
        }

        DireccionEntrega direccionEntrega = new DireccionEntrega(numero, calle, ciudad);

        Pedido pedido = crearPedido(idPedido, tipoPedido, descripcion, direccionEntrega, distancia, validacion, prioridadPedido);


        if (pedido == null) {
            return false;
        }

        try {
            pedido.cambiarEstado(EstadoPedido.valueOf(estadoPedido));
        } catch (IllegalArgumentException | NullPointerException ex) {
            return false;
        }

        return pedidoDAO.actualizar(pedido);
    }

    private Pedido crearPedido(int idPedido, String tipoPedido, String descripcion, DireccionEntrega direccionEntrega,
                               int distanciaKm, boolean validacion, String prioridadPedido) {

        if (tipoPedido == null || tipoPedido.isBlank() || descripcion == null || descripcion.isBlank()
                || prioridadPedido == null || prioridadPedido.isBlank() || distanciaKm <= 0) {
            return null;
        }

        if (direccionEntrega == null || direccionEntrega.getNumero() <= 0 || direccionEntrega.getCalle().isBlank()
                || direccionEntrega.getCiudad().isBlank()) {
            return null;
        }

        PrioridadPedido prioridad;
        try {
            prioridad = PrioridadPedido.valueOf(prioridadPedido);
        } catch (IllegalArgumentException ex) {
            return null;
        }

        return switch (tipoPedido) {
            case "Pedido Express" -> new PedidoExpress(
                    tipoPedido, idPedido, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            case "Pedido Comida" -> new PedidoComida(
                    tipoPedido, idPedido, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            case "Pedido Encomienda" -> new PedidoEncomienda(
                    tipoPedido, idPedido, descripcion, direccionEntrega, distanciaKm, validacion, prioridad);
            default -> null;
        };
    }


    public boolean eliminarPedido(int idPedido) {
        if (idPedido <= 0) {
            return false;
        }
        return pedidoDAO.eliminar(idPedido);
    }

    //================
    //    ENTREGA
    //================

    /**
     * Metodo registrar entrega
     *
     * @param idPedido     Identificador del pedido.
     * @param idRepartidor Identifiador del repartidor.
     * @return true si la entrega se registró, de lo contrario arroja false.
     */
    public boolean registrarEntrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {

        Entrega entrega = new Entrega(idPedido, idRepartidor, fecha, hora);

        return entregaDAO.guardar(entrega);
    }

    public List<Entrega> obtenerEntregas() {
        return entregaDAO.listarTodos();
    }

    public boolean editarEntrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        if (idPedido <= 0 || idRepartidor <= 0 || fecha == null || hora == null) {
            return false;
        }
        return entregaDAO.actualizar(new Entrega(idPedido, idRepartidor, fecha, hora));
    }

    public boolean eliminarEntrega(int idPedido) {
        if (idPedido < 0) {
            return false;
        }
        return entregaDAO.eliminarEntrega(idPedido);
    }

    /**
     * Metodo despachar pedido que cambia el estado del mismo.
     *
     * @param idPedido Identificador del pedido
     * @return true si se actualizó la fila, de lo contrario arroja false.
     */
    public boolean despacharPedido(int idPedido) {
        return pedidoDAO.actualizarEstado(idPedido, EstadoPedido.EN_REPARTO.name());
    }
}
