package modelo;

/**
 * Clase que representa un pedido de comida.
 */

public class PedidoComida extends Pedido implements Cancelable, Despachable {

    /**
     * Constructor de la clase PedidoComida.
     *
     * @param tipoPedido       Tipo de pedido.
     * @param idPedido         Número de identificación del pedido.
     * @param descripcion      Descripción del pedido.
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param distanciaKm      Distancia en kilómetros del lugar de entrega del producto.
     * @param validarMochila   Validación de mochila apta para entrega de comida.
     * @param prioridadPedido  Prioridad del pedido.
     */
    public PedidoComida(String tipoPedido, int idPedido, String descripcion, DireccionEntrega direccionEntrega, int distanciaKm, boolean validarMochila, PrioridadPedido prioridadPedido) {
        super(tipoPedido, idPedido, descripcion, direccionEntrega, distanciaKm, validarMochila, prioridadPedido);

    }

    /**
     * Metodo asignarRepartidor
     *
     * @param nombreRepartidor Nombre del repartidor.
     * @throws IllegalStateException Si el repartidor no cuenta con mochila térmica no es posible asignar el pedido.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        super.asignarRepartidor(nombreRepartidor);
        System.out.println("\nValidando mochila térmica...");

        //Validador de validarMochila.
        if (getValidacion()) {
            System.out.println("Mochila térmica: Sí.");
            System.out.println("Mochila térmica validada correctamente.");
        } else {
            throw new IllegalStateException("Mochila térmica: No.");
        }
    }

    /**
     * Metodo que calcula el tiempo de entrega del pedido.
     *
     * @return tiempo estimado de entrega del pedido.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (2 * getDistanciaKm());
    }

    /**
     * Metodo que registra el pedido.
     */
    @Override
    public void registrar() {
        System.out.println("\nRegistrando Pedido Comida...");
    }

    /**
     * Metodo que cancela el pedido.
     */
    @Override
    public void cancelar() {
        cambiarEstado(EstadoPedido.CANCELADO);
    }

    /**
     * Metodo que despacha el pedido.
     */
    @Override
    public void despachar() {
        System.out.println("\nDespachando Pedido Comida...");
    }
}
