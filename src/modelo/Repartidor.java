package modelo;

import java.util.Random;

/**
 * Clase que representa un repartidor.
 */

public class Repartidor implements Runnable {

    private ZonaDeCarga zonaDeCarga;
    private int idRepartidor;
    private String nombreRepartidor;
    private final Random random = new Random();

    /**
     * Constructor para un repartidor nuevo con id generado por la base de datos (autoincrementable).
     *
     * @param nombreRepartidor Nombre del repartidor.
     */
    public Repartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    /**
     * Constructor para un repartidor reconstruido desde la base de datos.
     *
     * @param idRepartidor
     * @param nombreRepartidor
     */
    public Repartidor(int idRepartidor, String nombreRepartidor) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
    }

    //Metodo getter.
    public int getIdRepartidor() {
        return idRepartidor;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    //Metodo setter.
    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    /**
     * Metodo run() recorre la ruta del repartidor y cambia el estado de entrega del pedido.
     */
    @Override
    public void run() {

        while (true) {
            Pedido pedido = zonaDeCarga.asignarPedido();

            if (pedido == null) {
                System.out.println("[Repartidor: " + nombreRepartidor + "] No quedan más pedidos.");
                break;
            }

            if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO) {
                System.out.println("[Repartidor: " + nombreRepartidor + "] Pedido # " + pedido.getIdPedido() + " está cancelado, no sale a reparto.");
                continue;
            }

            System.out.println("[Repartidor: " + nombreRepartidor + "] Tomando pedido  #" + pedido.getIdPedido() + " (" + pedido.getTipoPedido() + ")");

            try {
                pedido.asignarRepartidor(nombreRepartidor);

                pedido.cambiarEstado(EstadoPedido.EN_REPARTO);

                //Simulación de tiempo que podría tardar un repartidor
                Thread.sleep(1000 + random.nextInt(1000));

                if (pedido instanceof Despachable despachable) {
                    despachable.despachar();
                }

                pedido.cambiarEstado(EstadoPedido.ENTREGADO);
                System.out.println("[Repartidor: " + nombreRepartidor + "] Pedido #" + pedido.getIdPedido() + " entregado.");

            } catch (IllegalStateException e) {
                System.out.println("[Repartidor: " + nombreRepartidor + "] Pedido #" + pedido.getIdPedido() + " no pudo entregarse: " + e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Repartidor: " + nombreRepartidor + "] fue interrumpido.");
                return;
            }
        }

        System.out.println("[Repartidor: " + nombreRepartidor + "] terminó su ruta de reparto.");
    }

    /**
     * Muestra id y nombre del repartidor en la interfaz gráfica.
     * @return
     */
    @Override
    public String toString() {
        return idRepartidor + " - " + nombreRepartidor;
    }

}
