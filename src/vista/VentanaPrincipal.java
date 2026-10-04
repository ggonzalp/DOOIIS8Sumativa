package vista;

import controlador.Controlador;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Clase que representa la ventana principal del programa.
 */

public class VentanaPrincipal extends JFrame {

    private JPanel panelPrincipal;
    private JButton btnGestionarPedido;
    private JButton btnGestionarRepartidor;
    private JButton btnDespachar;
    private JButton btnGestionarEntrega;

    //Gestor que almacena y administra los pedidos.
    private final Controlador controlador;

    public VentanaPrincipal() {
        controlador = new Controlador();

        setContentPane(panelPrincipal);

        configurarVentana();
        configurarBotones();
    }

    //Configuración de ventana.
    private void configurarVentana() {
        setTitle("SPEEDFAST");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Conecta los botones visuales con el código que se ejecutará al hacer clic.
    private void configurarBotones() {
        btnGestionarPedido.addActionListener(e -> {
            VentanaPedido ventanaRegistro = new VentanaPedido(controlador);
            ventanaRegistro.setVisible(true);
        });

        btnGestionarRepartidor.addActionListener(e -> {
            VentanaRepartidor ventanaRepartidor = new VentanaRepartidor(controlador);
            ventanaRepartidor.setVisible(true);
        });

        btnDespachar.addActionListener(e -> {
            despacharPedido();
        });

        btnGestionarEntrega.addActionListener(e -> {
            VentanaEntrega ventanaEntrega =new VentanaEntrega(controlador);
            ventanaEntrega.setVisible(true);
        });
    }

    //Simula el inicio de la ruta de reparto.
    private void despacharPedido() {

        List<Pedido> pedidos = controlador.obtenerPedidos();
        List<Repartidor> repartidores = controlador.obtenerRepartidores();

        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes de despacho.",
                    "Sin pedidos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores registrados.",
                    "Sin repartidores",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido pedido = (Pedido) JOptionPane.showInputDialog(this,
                "Seleccione el pedido: ", "Despachar pedido",
                JOptionPane.QUESTION_MESSAGE, null,
                pedidos.toArray(), pedidos.get(0));

        if (pedido == null) {
            return;
        }

        Repartidor repartidor = (Repartidor) JOptionPane.showInputDialog(this,
                "Seleccione el repartidor: ", "Despachar pedido",
                JOptionPane.QUESTION_MESSAGE, null,
                repartidores.toArray(), repartidores.get(0));

        if (repartidor == null) {
            return;
        }

        try {
            pedido.asignarRepartidor(repartidor.getNombreRepartidor());

        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo despachar el pedido #" + pedido.getIdPedido() +
                            ":\n" + exception.getMessage(),
                    "Despacho rechazado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean registrada = controlador.registrarEntrega(pedido.getIdPedido(), repartidor.getIdRepartidor(), LocalDate.now(), LocalTime.now());

        if (registrada) {
            controlador.despacharPedido(pedido.getIdPedido());
            JOptionPane.showMessageDialog(this,
                    "Pedido # " + pedido.getIdPedido() + " asignado a " +
                            repartidor.getNombreRepartidor() + ".",
                    "Despacho exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "El pedido ya fue despachado. ",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
        }
    }
}

