// Un registro de la tabla productos
public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // devuelve true si el campo es un numero (id, precio o stock) y false si es texto (nombre)
    public static boolean esNumerico(String campo) {
        return campo.equals("id") || campo.equals("precio") || campo.equals("stock");
    }

    public static boolean campoValido(String campo) {
        return campo.equals("id") || campo.equals("nombre") || campo.equals("precio") || campo.equals("stock");
    }

    // valor de un campo numerico
    public double getNumero(String campo) {
        if (campo.equals("id")) {
            return id;
        } else if (campo.equals("precio")) {
            return precio;
        }
        return stock;
    }

    // valor de un campo como texto (para mostrarlo en pantalla)
    public String getTexto(String campo) {
        if (campo.equals("id")) {
            return String.valueOf(id);
        } else if (campo.equals("nombre")) {
            return nombre;
        } else if (campo.equals("precio")) {
            return String.valueOf(precio);
        }
        return String.valueOf(stock);
    }
}
