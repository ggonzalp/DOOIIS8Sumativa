package modelo;

public class DireccionEntrega {

    private int numero;
    private String calle;
    private String ciudad;

    public DireccionEntrega (int numero, String calle, String ciudad) {
        this.numero = numero;
        this.calle = calle;
        this.ciudad = ciudad;
    }

    public int getNumero() {
        return numero;
    }

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return  "\n#" + numero + ", calle " + calle + ", " + ciudad;
    }
}
