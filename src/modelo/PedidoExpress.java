package modelo;
/**
 * Clase que representa un pedido express
 */

public class PedidoExpress extends Pedido implements Cancelable, Despachable {

    /**
     * Constructor de la clase PedidoExpress
     * @param tipoPedido Tipo de pedido.
     * @param idPedido Número de identificación del pedido.
     * @param descripcion Descripción del pedido.
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param distanciaKm Distancia en kilómetros del lugar de entrega del producto.
     * @param validarRepartidorCercano Valida la cercanía del repartidor al punto de entrega.
     * @param prioridadPedido Prioridad del pedido.
     */

    public PedidoExpress(String tipoPedido, int idPedido, String descripcion, DireccionEntrega direccionEntrega, int distanciaKm, boolean validarRepartidorCercano, PrioridadPedido prioridadPedido) {
        super(tipoPedido, idPedido, descripcion, direccionEntrega, distanciaKm, validarRepartidorCercano ,prioridadPedido);
    }

    /**
     * Metodo asignarRepartidor
     * @param nombreRepartidor Nombre del repartidor.
     * @throws IllegalStateException Si el repartidor está lejos de la ubicación de la solicitud de reparto, no es posible asignar el pedido.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("\nValidando cercanía del repartidor... ");

        //Validador de validarRepartidorCercano.
        if (getValidacion()) {
            System.out.println("Repartidor cercano: Sí.");
            System.out.println("Cercanía validada con GPS correctamente.");
        } else {
            throw new IllegalStateException("Repartidor cercano: No.");
        }
    }

    /**
     * Metodo que calcula el tiempo de entrega del pedido
     * @return tiempo estimado de entrega del pedido.
     */
    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 10;
        if (getDistanciaKm() > 5) {
            tiempo = tiempo + 5;
        }
        return tiempo;
    }

    /**
     * Metodo que registra el pedido.
     */
    @Override
    public void registrar() {
        System.out.println("\nRegistrando Pedido Express...");
    }

    /**
     * Metodo que cancela el pedido.
     */
    @Override
    public void cancelar() {
        System.out.println("\nCancelando Pedido Express...");
        cambiarEstado(EstadoPedido.CANCELADO);
    }

    /**
     * Metodo que despacha el pedido.
     */
    @Override
    public void despachar() {
        System.out.println("\nDespachando Pedido Express...");
    }
}

