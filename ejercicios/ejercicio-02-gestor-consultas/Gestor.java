import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

// Guarda los registros de la tabla productos en un arreglo de objetos
// y hace las consultas desde Java
public class Gestor {

    private ArrayList<Producto> productos = new ArrayList<Producto>();

    // lee la tabla productos y llena el arreglo de objetos
    public void cargar(Connection con) throws Exception {
        productos.clear();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, nombre, precio, stock FROM productos");
        while (rs.next()) {
            productos.add(new Producto(rs.getInt("id"), rs.getString("nombre"),
                    rs.getDouble("precio"), rs.getInt("stock")));
        }
    }

    public int cantidad() {
        return productos.size();
    }

    // Consulta al arreglo, como un SELECT:
    //   campos          -> que campos mostrar                (SELECT campo1, campo2 ...)
    //   campoCondicion  -> campo que debe cumplir condicion  (WHERE campo operador valor)
    //                      si esta vacio, no hay condicion
    //   campoOrden      -> campo por el que se ordena        (ORDER BY campo), vacio = sin orden
    //   ascendente      -> true = ASC, false = DESC
    //   limite          -> cantidad maxima de registros      (LIMIT n), 0 = sin limite
    public ArrayList<Producto> consultar(String[] campos, String campoCondicion, String operador,
                                         String valor, String campoOrden, boolean ascendente,
                                         int limite) {
        // se revisan los datos que dio el usuario
        for (int i = 0; i < campos.length; i++) {
            if (!Producto.campoValido(campos[i])) {
                throw new IllegalArgumentException("El campo '" + campos[i] + "' no existe.");
            }
        }
        if (!campoCondicion.equals("") && !Producto.campoValido(campoCondicion)) {
            throw new IllegalArgumentException("El campo '" + campoCondicion + "' no existe.");
        }
        if (!campoOrden.equals("") && !Producto.campoValido(campoOrden)) {
            throw new IllegalArgumentException("El campo '" + campoOrden + "' no existe.");
        }

        // 1) WHERE: se copian solo los productos que cumplen la condicion
        ArrayList<Producto> resultado = new ArrayList<Producto>();
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (campoCondicion.equals("") || cumple(p, campoCondicion, operador, valor)) {
                resultado.add(p);
            }
        }

        // 2) ORDER BY: ordenamiento burbuja sobre la lista filtrada
        if (!campoOrden.equals("")) {
            for (int i = 0; i < resultado.size() - 1; i++) {
                for (int j = 0; j < resultado.size() - 1 - i; j++) {
                    int comparacion = comparar(resultado.get(j), resultado.get(j + 1), campoOrden);
                    boolean cambiar = ascendente ? comparacion > 0 : comparacion < 0;
                    if (cambiar) {
                        Producto auxiliar = resultado.get(j);
                        resultado.set(j, resultado.get(j + 1));
                        resultado.set(j + 1, auxiliar);
                    }
                }
            }
        }

        // 3) LIMIT: se quitan los registros que sobran
        if (limite > 0) {
            while (resultado.size() > limite) {
                resultado.remove(resultado.size() - 1);
            }
        }
        return resultado;
    }

    // muestra solo los campos pedidos
    public void mostrar(ArrayList<Producto> lista, String[] campos) {
        String titulo = "";
        for (int i = 0; i < campos.length; i++) {
            titulo = titulo + campos[i] + "   ";
        }
        System.out.println(titulo);
        for (int i = 0; i < lista.size(); i++) {
            String fila = "";
            for (int j = 0; j < campos.length; j++) {
                fila = fila + lista.get(i).getTexto(campos[j]) + "   ";
            }
            System.out.println(fila);
        }
        System.out.println("(" + lista.size() + " registro(s))");
    }

    // true si el producto cumple "campo operador valor"
    private boolean cumple(Producto p, String campo, String operador, String valor) {
        if (Producto.esNumerico(campo)) {
            double numero;
            try {
                numero = Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El valor '" + valor + "' no es un numero.");
            }
            double actual = p.getNumero(campo);
            if (operador.equals("=")) {
                return actual == numero;
            } else if (operador.equals("!=")) {
                return actual != numero;
            } else if (operador.equals(">")) {
                return actual > numero;
            } else if (operador.equals("<")) {
                return actual < numero;
            } else if (operador.equals(">=")) {
                return actual >= numero;
            } else if (operador.equals("<=")) {
                return actual <= numero;
            }
        } else {
            // el campo nombre es texto: solo se puede comparar si es igual o distinto
            if (operador.equals("=")) {
                return p.getTexto(campo).equalsIgnoreCase(valor);
            } else if (operador.equals("!=")) {
                return !p.getTexto(campo).equalsIgnoreCase(valor);
            }
        }
        throw new IllegalArgumentException("El operador '" + operador
                + "' no se puede usar con el campo " + campo + ".");
    }

    // negativo si a va antes que b, 0 si son iguales, positivo si a va despues de b
    private int comparar(Producto a, Producto b, String campo) {
        if (Producto.esNumerico(campo)) {
            return Double.compare(a.getNumero(campo), b.getNumero(campo));
        }
        return a.getTexto(campo).compareToIgnoreCase(b.getTexto(campo));
    }
}
