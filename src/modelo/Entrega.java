package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que representa la entrega de un pedido.
 */

public class Entrega {
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    /**
     * Crea una nueva entrega.
     * @param idPedido     Número de identificación del pedido.
     * @param idRepartidor Número de identificación del repartidor.
     * Fecha y hora locales
     */
    public Entrega(int idPedido, int idRepartidor) {
        this(idPedido, idRepartidor, LocalDate.now(), LocalTime.now());
    }


    /**
     * Reconstruye una entrega que ya existe en la base de datos.
     * @param idPedido
     * @param idRepartidor
     * @param fecha
     * @param hora
     */
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora){
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    //Metodos getter
    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    //Metodos setter
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}


