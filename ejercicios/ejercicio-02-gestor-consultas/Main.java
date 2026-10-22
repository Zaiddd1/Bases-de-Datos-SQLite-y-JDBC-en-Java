import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

// Carga la tabla productos en un arreglo y permite hacer consultas indicando,
// en tiempo de ejecucion, los campos a mostrar, una condicion, el orden y el limite.
public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:tienda.db");
            crearDatosDePrueba(con);

            Gestor gestor = new Gestor();
            gestor.cargar(con);
            con.close();
            System.out.println("Se cargaron " + gestor.cantidad() + " productos en el arreglo.");

            String seguir = "s";
            while (seguir.equalsIgnoreCase("s")) {
                System.out.println();
                System.out.println("===== NUEVA CONSULTA =====");
                System.out.println("Campos de la tabla: id, nombre, precio, stock");

                System.out.print("a) Campos a mostrar (separados por coma, o 'todos'): ");
                String texto = teclado.nextLine().trim();
                if (texto.equalsIgnoreCase("todos")) {
                    texto = "id,nombre,precio,stock";
                }
                String[] campos = texto.split(",");
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].trim();
                }

                System.out.print("b) Campo de la condicion (Enter = sin condicion): ");
                String campoCondicion = teclado.nextLine().trim();
                String operador = "";
                String valor = "";
                if (!campoCondicion.equals("")) {
                    System.out.print("   Operador (=, !=, >, <, >=, <=): ");
                    operador = teclado.nextLine().trim();
                    System.out.print("   Valor: ");
                    valor = teclado.nextLine().trim();
                }

                System.out.print("c) Campo para ordenar (Enter = sin orden): ");
                String campoOrden = teclado.nextLine().trim();
                boolean ascendente = true;
                if (!campoOrden.equals("")) {
                    System.out.print("   Orden (asc o desc): ");
                    ascendente = !teclado.nextLine().trim().equalsIgnoreCase("desc");
                }

                System.out.print("d) Cantidad maxima de registros (0 = todos): ");
                int limite = 0;
                try {
                    limite = Integer.parseInt(teclado.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("   No es un numero, se mostraran todos.");
                }

                System.out.println();
                try {
                    ArrayList<Producto> resultado = gestor.consultar(campos, campoCondicion, operador, valor,
                            campoOrden, ascendente, limite);
                    gestor.mostrar(resultado, campos);
                } catch (IllegalArgumentException e) {
                    System.out.println("Consulta no valida: " + e.getMessage());
                }

                System.out.print("\nOtra consulta? (s/n): ");
                seguir = teclado.nextLine().trim();
            }
            System.out.println("Fin del programa.");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // si la tabla no existe o esta vacia, se llena con productos de ejemplo
    static void crearDatosDePrueba(Connection con) throws Exception {
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS productos "
                + "(id integer PRIMARY KEY, nombre text, precio real, stock integer)");
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM productos");
        rs.next();
        if (rs.getInt(1) > 0) {
            return;
        }

        String[] nombres = {"Laptop", "Mouse", "Teclado", "Monitor", "Audifonos", "Impresora"};
        double[] precios = {2500.0, 35.5, 80.0, 650.0, 120.0, 480.0};
        int[] stocks = {8, 50, 30, 12, 25, 5};
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO productos (id, nombre, precio, stock) VALUES (?, ?, ?, ?)");
        for (int i = 0; i < nombres.length; i++) {
            ps.setInt(1, i + 1);
            ps.setString(2, nombres[i]);
            ps.setDouble(3, precios[i]);
            ps.setInt(4, stocks[i]);
            ps.executeUpdate();
        }
    }
}
