package vista;

import controlador.Controlador;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class VentanaRepartidor extends JFrame {

    private final Controlador controlador;

    private DefaultTableModel tableModel = new DefaultTableModel(
    new Object[]{"Id", "Nombre"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
     };

    private JPanel ventanaRepartidor;
    private JTable tablaRepartidores;
    private JTextField txtNombre;
    private JButton botonGuardar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonLimpiar;

    public VentanaRepartidor(Controlador controlador) {
        this.controlador = controlador;

        setContentPane(ventanaRepartidor);

        configurarVentana();
        configurarTabla();
        configurarBotones();

        actualizarTabla();
    }

    //Configura la ventana donde se visualizará el formulario de registro.
    private void configurarVentana() {
        setTitle("SPEEDFAST - GESTIÓN DE REPARTIDORES");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Nombre Repartidor"};

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaRepartidores.setModel(tableModel);
    }

    //Conecta los botones gráficos con el código que se jecutará al hacer clic.
    private void configurarBotones() {
        botonGuardar.addActionListener(e -> registrarRepartidor());
        botonLimpiar.addActionListener(e -> limpiar());
        botonEditar.addActionListener(e -> editar());
        botonEliminar.addActionListener(e -> eliminarRepartidor());
    }

    //REGISTRO: Registra a un repartidor.
    private void registrarRepartidor() {
        try {
            //Recibe información del formulario.
            String nombreRepartidor = txtNombre.getText().trim();

            //Valida campos obligatorios
            if (nombreRepartidor.isEmpty()) {
                throw new IllegalArgumentException("Error. Ingrese nombre del repartidor.");
            }

            //Registro de repartidor.
            boolean guardado = controlador.registrarRepartidor(nombreRepartidor);

            if (guardado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Repartidor registrado correctamente.",
                        "Registro exitoso.",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el repartidor en la base de datos.",
                        "Registro fallido",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException exception) {
            //Se ejecuta para validar el registro de un repartidor.
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    // EDITA el contenido de la tabla.
    private void editar() {
        try {
            //verifica que haya un repartidor seleccionado.
            int fila = tablaRepartidores.getSelectedRow();

            if (fila == -1) {
                throw new IllegalArgumentException("Seleccione un repartidor de la tabla.");
            }

            //Recibe el id desde la tabla y el nombre nuevo desde el formulario.
            int idRepartidor = Integer.parseInt(tablaRepartidores.getValueAt(fila, 0).toString());
            String nombreRepartidor = txtNombre.getText().trim();

            //Valida campos obligatorios.
            if (nombreRepartidor.isEmpty()) {
                throw new IllegalArgumentException("Error. Ingrese nombre del repartidor.");
            }

            //Edición del repartidor.
            boolean editado = controlador.editarRepartidor(idRepartidor, nombreRepartidor);

            if (editado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Repartidor actualizado correctamente.",
                        "Edición exitosa.",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el repartidor en la base de datos.",
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

        for (Repartidor repartidor : controlador.obtenerRepartidores()) {
            Object[] fila = {
                    repartidor.getIdRepartidor(),
                    repartidor.getNombreRepartidor()
            };

            tableModel.addRow(fila);
        }
    }

    //LIMPIA el contenido de los campos.
    private void limpiar() {
        txtNombre.setText("");
    }

    //ELIMINA el contenido del repartidor seleccionado.
    private void eliminarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un repartidor de la tabla antes de eliminar.");
            return;
        }

        int idRepartidor = Integer.parseInt(tablaRepartidores.getValueAt(fila, 0).toString());

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el repartidor seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = controlador.eliminarRepartidor(idRepartidor);

            JOptionPane.showMessageDialog(this, ok
                    ? "Repartidor eliminado correctamente"
                    : "No fue posible eliminar el repartidor, tiene pedidos asignados.");
            if (ok) {
                limpiar();
                actualizarTabla();
            }
        }
    }
}
