package vista;

import controlador.Controlador;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class VentanaEntrega extends JFrame {

    private final Controlador controlador;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);


    private DefaultTableModel tableModel;
    private JPanel ventanaEntrega;
    private JTable tablaEntregas;
    private JButton botonGuardar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonLimpiar;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JComboBox <OpcionCombo> comboPedido;
    private JComboBox <OpcionCombo> comboRepartidor;

    public VentanaEntrega(Controlador controlador) {
        this.controlador = controlador;

        setContentPane(ventanaEntrega);

        configurarVentana();
        configurarTabla();
        configurarBotones();

        cargarCombos();
        actualizarTabla();
        limpiar();
    }

    //Configura la ventana
    private void configurarVentana() {
        setTitle("SPEEDFAST - GESTIÓN DE ENTREGAS");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void configurarTabla() {
        String[] columnas = {"Id Pedido", "Id Repartidor", "Fecha", "Hora"};

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEntregas.setModel(tableModel);
    }

    private void configurarBotones() {
        botonGuardar.addActionListener(e -> registrarEntrega());
        botonEditar.addActionListener(e -> editar());
        botonEliminar.addActionListener(e -> eliminar());
        botonLimpiar.addActionListener(e -> limpiar());
    }

    //Carga los combos de pedidos y repartidores desde la base de datos.
    private void cargarCombos() {
        comboPedido.removeAllItems();
        comboPedido.addItem(new OpcionCombo(0, "Seleccione un pedido"));
        for (Pedido pedido : controlador.obtenerPedidos()) {
            comboPedido.addItem(new OpcionCombo(pedido.getIdPedido(), pedido.getIdPedido() + " - " + pedido.getDescripcion()));
        }

        comboRepartidor.removeAllItems();
        comboRepartidor.addItem(new OpcionCombo(0, "Seleccione un repartidor"));
        for (Repartidor repartidor : controlador.obtenerRepartidores()) {
            comboRepartidor.addItem(new OpcionCombo(repartidor.getIdRepartidor(), repartidor.getIdRepartidor() + " - " + repartidor.getNombreRepartidor()));
        }
    }

    private int leerIdElegido(JComboBox<OpcionCombo> combo) {
        OpcionCombo opcion = (OpcionCombo) combo.getSelectedItem();

        if (opcion == null) {
            return 0;
        }

        return opcion.getId();
    }

    private void registrarEntrega() {
        try {
            //Recibe los ids elegidos en los combos desde la base de datos.
            int idPedido = leerIdElegido(comboPedido);
            int idRepartidor = leerIdElegido(comboRepartidor);

            //Valida que se haya elegido un pedido y un repartidor.
            if (idPedido == 0 || idRepartidor == 0) {
                throw new IllegalArgumentException("Seleccione un pedido y un repartidor.");
            }

            //Lee y valida fecha y hora.
            LocalDate fecha = leerFecha();
            LocalTime hora = leerHora();

            //Registro de la entrega.
            boolean guardado = controlador.registrarEntrega(idPedido, idRepartidor, fecha, hora);

            if (guardado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Entrega registrada correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la entrega. Revise datos ingresados.",
                        "Registro fallido",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editar() {
        try {
            //Verifica que haya una entrega seleccionada.
            int fila = tablaEntregas.getSelectedRow();

            if (fila == -1) {
                throw new IllegalArgumentException("Seleccione una entrega de la tabla.");
            }

            //Recibe el id clave desde la tabla.
            int idPedido = Integer.parseInt(tablaEntregas.getValueAt(fila, 0).toString());


            //Los nuevos datos los recibe desde el formulario.
            int idRepartidor = leerIdElegido(comboRepartidor);

            if (idRepartidor == 0) {
                throw new IllegalArgumentException("Seleccione Id del repartidor.");
            }

            LocalDate fecha = leerFecha();
            LocalTime hora = leerHora();

            //Edición de la entrega.
            boolean editado = controlador.editarEntrega(idPedido, idRepartidor, fecha, hora);

            if (editado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente.",
                        "Edición exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la entrega. Revise que el repartidor exista.",
                        "Edición fallida",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    //ACTUALIZA el contenido de la tabla.
    private void actualizarTabla() {
        tableModel.setRowCount(0);

        for (Entrega entrega : controlador.obtenerEntregas()) {
            Object[] fila = {
                    entrega.getIdPedido(),
                    entrega.getIdRepartidor(),
                    entrega.getFecha(),
                    entrega.getHora()
            };

            tableModel.addRow(fila);
        }
    }

    //Limpia los datos en el formulario.
    private void limpiar(){
        comboPedido.setSelectedIndex(0);
        comboRepartidor.setSelectedIndex(0);
        txtFecha.setText("");
        txtHora.setText("");
        tablaEntregas.clearSelection();
    }

    private LocalDate leerFecha(){
        String texto = txtFecha.getText().trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Ingrese la fecha de la entrega.");
        }

        try {
            return LocalDate.parse(texto, FORMATO_FECHA);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("La fecha debe ser válida y tener el formato solicitado.");
        }
    }

    private LocalTime leerHora(){
        String texto = txtHora.getText().trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Ingrese la hora de la entrega.");
        }

        try {
            return LocalTime.parse(texto, FORMATO_HORA);

        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(("La hora debe ser válida y en el formato solicitado."));
        }
    }

    private void eliminar() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una entrega de la tabla antes de eliminar.");
            return;
        }

        int idPedido = Integer.parseInt(tablaEntregas.getValueAt(fila,0).toString());

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar la entrega seleccionada?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = controlador.eliminarEntrega(idPedido);

            if(eliminado) {
                JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.");
                limpiar();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this,"No fue posible eliminar la entrega",
                        "Eliminación fallida",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
