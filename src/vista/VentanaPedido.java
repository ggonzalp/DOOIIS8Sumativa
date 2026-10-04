package vista;

import controlador.Controlador;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPedido extends JFrame {

    private final Controlador controlador;

    //Modelo de la tabla.
    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Id", "Tipo Pedido", "Descripción", "Número #", "Calle", "Ciudad", "Distancia", "Validación"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JPanel panelRegistro;
    private JTextField txtDescripcion;
    private JTextField txtNumero;
    private JTextField txtCalle;
    private JTextField txtCiudad;
    private JTextField txtDistancia;
    private JComboBox<String> comboTipoPedido;
    private JComboBox<String> comboPrioridad;
    private JButton botonGuardar;
    private JButton botonLimpiar;
    private JCheckBox checkValidacion;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JTable tablaPedidos;
    private JComboBox <String> comboFiltro;

    public VentanaPedido(Controlador controlador) {
        this.controlador = controlador;

        setContentPane(panelRegistro);

        configurarVentana();
        configurarTabla();
        configurarComponentes();
        configurarBotones();

        actualizarTabla();
    }

    //Configura la ventana donde se visualizará el formulario de registro.
    private void configurarVentana() {
        setTitle("SPEEDFAST - REGISTRO DE PEDIDOS");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //Configura la tabla que muestra el historial.
    private void configurarTabla() {
        String[] columnas = {"Id Pedido", "Tipo Pedido", "Descripción", "N°", "Calle", "Ciudad", "Distancia (km)", "Prioridad", "Estado"};

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos.setModel(tableModel);
    }

    private void configurarComponentes() {

        //Opciones del JComboBox
        comboTipoPedido.setModel(new DefaultComboBoxModel<>(
                new String[]{"Pedido Express", "Pedido Comida", "Pedido Encomienda"}));

        comboPrioridad.setModel(new DefaultComboBoxModel<>(
                new String[]{"ALTA", "MEDIA", "BAJA"}));

        comboFiltro.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Pedido Express", "Pedido Comida", "Pedido Encomienda"}));

        comboFiltro.addActionListener(e -> actualizarTabla());
    }

    //Conecta los botones visuales con el código que se ejecutará al hacer clic.
    private void configurarBotones() {
        botonGuardar.addActionListener(e -> registrarPedido());
        botonLimpiar.addActionListener(e -> limpiar());
        botonEditar.addActionListener(e -> editar());
        botonEliminar.addActionListener(e -> eliminar());
    }

    //REGISTRO: Registra el ingreso de un pedido.
    private void registrarPedido() {
        try {
            //Recibe información del formulario.
            String textoNumero = txtNumero.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String calle = txtCalle.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            String textoDistancia = txtDistancia.getText().trim();

            //Valida campos obligatorios.
            if (descripcion.isEmpty() || textoNumero.isEmpty() || calle.isEmpty() || ciudad.isEmpty() || textoDistancia.isEmpty()) {
                throw new IllegalArgumentException("Todos son campos obligatorios");
            }

            //Convierte el texto a número.
            int numero = leerEntero(textoNumero, "Número dirección de entrega");
            int distancia = leerEntero(textoDistancia, "Distancia en km");

            //Validar números.
            if (numero <= 0) {
                throw new IllegalArgumentException("El número de domicilio ingresado no es válido.");
            }
            if (distancia <= 0) {
                throw new IllegalArgumentException("La distancia ingresada no es válida.");
            }

            //Registra el pedido
            DireccionEntrega direccionEntrega = new DireccionEntrega(numero, calle, ciudad);

            //Lee los combos
            String tipoElegido = (String) comboTipoPedido.getSelectedItem();
            String prioridad = (String) comboPrioridad.getSelectedItem();
            boolean validacion = checkValidacion.isSelected();

            //Lee el checkbox
            boolean guardado = controlador.registrarPedido(tipoElegido, descripcion, direccionEntrega, distancia, validacion, prioridad);

            if (guardado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pedido registrado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "El número de pedido ingresado ya existe.",
                        "Registro fallido",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException exception) {
            //Se ejecuta para validar la entrega del pedido.
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Vacía la tabla y la vuelve a llenar con los pedidos de la zondaDeCarga.
    private void actualizarTabla() {
        tableModel.setRowCount(0);

        String filtro = (String) comboFiltro.getSelectedItem();

        for (Pedido pedido : controlador.obtenerPedidos()) {

            boolean mostrar = filtro.equals("Todos") || pedido.getTipoPedido().equals(filtro);

           if (mostrar) {
            Object[] fila = {
                    pedido.getIdPedido(),
                    pedido.getTipoPedido(),
                    pedido.getDescripcion(),
                    pedido.getDireccionEntrega().getNumero(),
                    pedido.getDireccionEntrega().getCalle(),
                    pedido.getDireccionEntrega().getCiudad(),
                    pedido.getDistanciaKm(),
                    pedido.getPrioridadPedido(),
                    pedido.getEstadoPedido()
            };
            tableModel.addRow(fila);
        }
           }
    }

    //Limpia los datos en el formulario.
    private void limpiar() {
        comboTipoPedido.setSelectedIndex(0);
        txtDescripcion.setText("");
        txtNumero.setText("");
        txtCalle.setText("");
        txtCiudad.setText("");
        txtDistancia.setText("");
        comboPrioridad.setSelectedIndex(0);
        checkValidacion.setSelected(false);
    }

    //EDITA el contenido de la tabla.
    private void editar() {
        try {
            //Verifica que haya un pedido seleccionado
            int fila = tablaPedidos.getSelectedRow();

            if (fila == -1) {
                throw new IllegalArgumentException("Seleccione un pedido de la tabla.");
            }

            //recibe el id desde la tabla y el pedido nuevo desde el formulario.
            String tipoPedido = comboTipoPedido.getActionCommand().trim();
            String descripcion = txtDescripcion.getText().trim();
            int numero = Integer.parseInt(tablaPedidos.getValueAt(fila, 0).toString());
            String calle = txtCalle.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            int distancia = Integer.parseInt(tablaPedidos.getValueAt(fila, 0).toString());
            String prioridad = comboPrioridad.getActionCommand().trim();
            boolean validacion = checkValidacion.isSelected();

            //Valida campos obligatorios.
            if (tipoPedido.isEmpty() || descripcion.isEmpty() || calle.isEmpty() || ciudad.isEmpty() || prioridad.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            boolean editado = controlador.editarPedido(0, tipoPedido, descripcion, numero, calle, ciudad, distancia, prioridad, validacion);

            if (editado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pedido actualizado correctamente.",
                        "Edición exitosa.",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiar();
                actualizarTabla();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el pedido en la base de datos",
                        "Edición fallida",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }


    private int leerEntero(String texto, String nombreCampo) {
        try {
            return Integer.parseInt(texto);

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + " debe contener solo números enteros mayores a 0");
        }
    }

    private void eliminar() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un pedido de la tabla antes de eliminar.");
            return;
        }

        int idPedido = Integer.parseInt(tablaPedidos.getValueAt(fila, 0).toString());

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el pedido seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = controlador.eliminarPedido(idPedido);

            JOptionPane.showMessageDialog(this, ok
                    ? "Pedido eliminado correctamente"
                    : "No fue posible eliminar el pedido.");
            if (ok) {
                limpiar();
                actualizarTabla();
            }
        }
    }
}