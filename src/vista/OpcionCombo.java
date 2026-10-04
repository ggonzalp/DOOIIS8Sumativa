package vista;

/**
 * Opción de un JComboBox que muestra un texto y guarda su id.
 */

public class OpcionCombo {

    private final int id;
    private final String texto;

    public OpcionCombo(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    /**
     * @return ID que retorna el combo.
     */
    public int getId() {
        return id;
    }


    /**
     * @return Texto que muestra el combo.
     */
    @Override
    public String toString() {
        return texto;
    }
}
