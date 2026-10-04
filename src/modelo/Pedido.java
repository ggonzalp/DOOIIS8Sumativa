package modelo;

/**
 * Clase base del sistema de pedidos.
 * Representa un pedido genérico.
 */

public abstract class Pedido implements Comparable<Pedido>, Registrable {

    private String tipoPedido;
    private int idPedido;
    private String descripcion;
    private DireccionEntrega direccionEntrega;
    private int distanciaKm;
    private boolean validacion;
    private PrioridadPedido prioridadPedido;
    private EstadoPedido estadoPedido;

    /**
     * Constructor de la clase Pedido:
     *
     * @param tipoPedido       Tipo de pedido.
     * @param idPedido         Número de identificación del pedido.
     * @param descripcion      Descripción del pedido.
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param distanciaKm      Distancia en km del punto de entrega del pedido.
     * @param validacion       Validación correspondiente al tipo de pedido.
     * @param prioridadPedido  Prioridad de entrega del pedido.
     */
    public Pedido(String tipoPedido, int idPedido, String descripcion, DireccionEntrega direccionEntrega, int distanciaKm, boolean validacion, PrioridadPedido prioridadPedido) {

        this.tipoPedido = tipoPedido;
        this.idPedido = idPedido;
        this.descripcion = descripcion;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.validacion = validacion;
        this.prioridadPedido = prioridadPedido;
        this.estadoPedido = EstadoPedido.PENDIENTE;
    }

    //Métodos getter

    public String getTipoPedido() {
        return tipoPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public DireccionEntrega getDireccionEntrega() {
        return direccionEntrega;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }

    public boolean getValidacion() {
        return validacion;
    }

    public PrioridadPedido getPrioridadPedido() {
        return prioridadPedido;
    }


    //Métodos setter.

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDireccionEntrega(DireccionEntrega direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setDistanciaKm(int distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
    }

    public void setPrioridadPedido(PrioridadPedido prioridadPedido) {
        this.prioridadPedido = prioridadPedido;
    }

    /**
     * Metodo concreto mostrarResumen.
     * Muestra los datos en consola.
     */
    public void mostrarResumen() {
        System.out.println("\n[" + tipoPedido + "]" +
                "\nN° " + idPedido +
                "\nDirección de entrega: " + direccionEntrega +
                "\nValidación: " + validacion +
                "\nDistancia: " + distanciaKm + " Km.");
    }

    /**
     * Metodo asignarRepartidor() que permite asignar un repartidor para la entrega de un pedido.
     */
    public void asignarRepartidor() {
    }

    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Buscando repartidor...");
        System.out.println("Repartidor asignado: " + nombreRepartidor + "\n");
    }

    /**
     * Metodo abstracto que calcula el tiempo de entrega del pedido.
     *
     * @return tiempo de espera del pedido.
     */
    public abstract int calcularTiempoEntrega();

    /**
     * Template Method
     */
    public void procesarPedido() {
        mostrarResumen();
        int tiempo = calcularTiempoEntrega();
        System.out.println("\nTiempo estimado de entrega: " + tiempo + " minutos.");
    }

    /**
     * Metodo compareTo
     * @param otro pedido con el que se compara.
     * @return prioridad del pedido.
     */
    @Override
    public int compareTo(Pedido otro) {
        return this.prioridadPedido.compareTo(otro.prioridadPedido);
    }

    /**
     * Metodo toString
     * @return un pedido con su descripción y prioridaad.
     */
    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + descripcion + " - Prioridad: " + prioridadPedido;
    }

    public synchronized EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public synchronized void cambiarEstado (EstadoPedido nuevoEstado) {
        this.estadoPedido = nuevoEstado;
    }
}
