package modelo;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Clase que controla el sistema de envíos, implementando la interfaz Rastreable que hace posible la visualización del historial de pedidos.
 */

public class ZonaDeCarga implements Rastreable {

    private PriorityBlockingQueue<Pedido> colaPedidos = new PriorityBlockingQueue<>();
    private final List<Pedido> pedidosRegistrados = new ArrayList<>();

    /**
     * Metodo que registra el pedido.
     */
    public synchronized void  registrarPedido(Registrable pedido) {
        Pedido nuevoPedido = (Pedido) pedido;
        pedidosRegistrados.add(nuevoPedido);
        colaPedidos.put(nuevoPedido);
        pedido.registrar();
        System.out.println("Pedido # " + ((Pedido) pedido).getIdPedido() + " registrado");
    }

    /**
     * Metodo cancelar() cancela el pedido
     * @param pedido representa un pedido a cancelar.
     */
    //Metodo que cancela el pedido.
    public void cancelarPedido(Cancelable pedido) {
        pedido.cancelar();
        System.out.println("Pedido #" + ((Pedido) pedido).getIdPedido() + " cancelado.");
    }

    /**
     * Metodo para asignar un pedido a un repartidor.
     * @return la cola de pedidos.
     */
    public synchronized Pedido asignarPedido() {
        return colaPedidos.poll();
    }

    /**
     * Metodo para ver historial de pedidos.
     */
    @Override
    public void verHistorial() {

        int pedidosDespachados = 0;

        for (Pedido pedido : pedidosRegistrados) {
            System.out.println("N° " + pedido.getIdPedido() + "| " + pedido.getTipoPedido() + "|" + pedido.getDireccionEntrega() + "| " + pedido.getDistanciaKm() + "| " + pedido.getEstadoPedido() + "| " + pedido.calcularTiempoEntrega() + "min.");

            if (pedido.getEstadoPedido() == EstadoPedido.ENTREGADO) {
                pedidosDespachados++;
            }
        }

        if (pedidosDespachados == 0) {
            System.out.println("\nAún no hay pedidos despachados.");
        } else {
            System.out.println("\nTotal de pedidos entregados: " + pedidosDespachados);
        }

        if (pedidosRegistrados.isEmpty()) {
            System.out.println("Aún no hay pedidos registrados.");
        }
    }

    /**
     * Metodo obtenerPedidos()
     * @return pedidos registrados.
     */
    public List<Pedido> obtenerPedidos() {
        return pedidosRegistrados;
    }
}