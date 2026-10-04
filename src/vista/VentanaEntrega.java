package vista;

import controlador.Controlador;
import modelo.Entrega;

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


    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Id Pedido", "Id Repartidor", "Fecha", "Hora"}, 0) {
        @Override
        public boolean isCellEditable(int row, int colum) {
            return false;
        }
    };

    private JPanel ventanaEntrega;
    private JTable tablaEntregas;
    private JButton botonGuardar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonLimpiar;
    private JTextField txtIdPedido;
    private JTextField txtIdRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    public VentanaEntrega(Controlador controlador) {
        this.controlador = controlador;

        setContentPane(ventanaEntrega);

        configurarVentana();
        configurarTabla();
        configurarBotones();

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

    private void registrarEntrega() {
        try {
            //Recibe información del formulario.
            String textoIdPedido = txtIdPedido.getText().trim();
            String textoIdRepartidor = txtIdRepartidor.getText().trim();

            //Valida campos obligatorios.
            if (textoIdPedido.isEmpty() || textoIdRepartidor.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el id del pedido y el id del repartidor.");
            }

            //Convierte los textos a sus tipos.
            int idPedido = leerEntero(textoIdPedido, "Id del pedido");
            int idRepartidor = leerEntero(textoIdRepartidor, "Id del repartidor");
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
                        "No se pudo registrar la entrega.",
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
            String textoIdRepartidor = txtIdRepartidor.getText().trim();

            if (textoIdRepartidor.isEmpty()) {
                throw new IllegalArgumentException("Ingrese Id del repartidor.");
            }

            int idRepartidor = leerEntero(textoIdRepartidor, "Id del repartidor");
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

    //ACTUALIZA el contenidod e la tabla.
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
        txtIdPedido.setText("");
        txtIdRepartidor.setText("");
        txtFecha.setText("");
        txtHora.setText("");

    }

    private int leerEntero(String texto, String nombreCampo) {
        try {
            return Integer.parseInt(texto);

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + "debe contener solo números enteros.");
        }
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
                "¿Desea eliminar el pedido seleccionado?",
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
