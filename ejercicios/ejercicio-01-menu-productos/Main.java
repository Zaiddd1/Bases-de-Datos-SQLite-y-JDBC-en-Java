import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// Menu por consola para manejar la tabla productos (id, nombre, precio, stock) de tienda.db.
// Los cambios (ingresar, borrar, actualizar) solo se confirman si el usuario escribe la clave correcta.
public class Main {

    static final String CLAVE = "1234";
    static Connection con;
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:tienda.db");
            Statement stmt = con.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS productos "
                    + "(id integer PRIMARY KEY, nombre text, precio real, stock integer)");

            // los cambios quedan pendientes hasta el commit o el rollback
            con.setAutoCommit(false);

            int opcion = 0;
            while (opcion != 5) {
                System.out.println();
                System.out.println("===== MENU DE PRODUCTOS =====");
                System.out.println("1. Ingresar producto");
                System.out.println("2. Borrar producto");
                System.out.println("3. Actualizar producto");
                System.out.println("4. Mostrar productos");
                System.out.println("5. Salir");
                opcion = leerEntero("Elija una opcion: ");

                try {
                    if (opcion == 1) {
                        ingresar();
                    } else if (opcion == 2) {
                        borrar();
                    } else if (opcion == 3) {
                        actualizar();
                    } else if (opcion == 4) {
                        mostrar();
                    } else if (opcion != 5) {
                        System.out.println("Opcion no valida.");
                    }
                } catch (SQLException e) {
                    // si algo falla, se cancelan los cambios pendientes
                    System.out.println("Error en la base de datos: " + e.getMessage());
                    con.rollback();
                }
            }

            con.close();
            System.out.println("Fin del programa.");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    static void ingresar() throws SQLException {
        int id = leerEntero("Id: ");
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();
        double precio = leerDecimal("Precio: ");
        int stock = leerEntero("Stock: ");

        PreparedStatement ps = con.prepareStatement("INSERT INTO productos (id, nombre, precio, stock) VALUES (?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, nombre);
        ps.setDouble(3, precio);
        ps.setInt(4, stock);
        ps.executeUpdate();
        confirmar();
    }

    static void borrar() throws SQLException {
        int id = leerEntero("Id del producto a borrar: ");

        PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE id = ?");
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        if (filas == 0) {
            System.out.println("No existe un producto con ese id.");
            con.rollback();
        } else {
            confirmar();
        }
    }

    static void actualizar() throws SQLException {
        int id = leerEntero("Id del producto a actualizar: ");
        System.out.print("Nuevo nombre: ");
        String nombre = teclado.nextLine();
        double precio = leerDecimal("Nuevo precio: ");
        int stock = leerEntero("Nuevo stock: ");

        PreparedStatement ps = con.prepareStatement("UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id = ?");
        ps.setString(1, nombre);
        ps.setDouble(2, precio);
        ps.setInt(3, stock);
        ps.setInt(4, id);
        int filas = ps.executeUpdate();
        if (filas == 0) {
            System.out.println("No existe un producto con ese id.");
            con.rollback();
        } else {
            confirmar();
        }
    }

    static void mostrar() throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, nombre, precio, stock FROM productos ORDER BY id");
        System.out.println("ID   NOMBRE   PRECIO   STOCK");
        int cantidad = 0;
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "   " + rs.getString("nombre") + "   "
                    + rs.getDouble("precio") + "   " + rs.getInt("stock"));
            cantidad++;
        }
        if (cantidad == 0) {
            System.out.println("(la tabla esta vacia)");
        }
    }

    // pide la clave: si es correcta se hace commit, si no se hace rollback
    static void confirmar() throws SQLException {
        System.out.print("Ingrese la clave para confirmar los cambios: ");
        String clave = teclado.nextLine();
        if (clave.equals(CLAVE)) {
            con.commit();
            System.out.println("Clave correcta: cambios confirmados (commit).");
        } else {
            con.rollback();
            System.out.println("Clave incorrecta: cambios revertidos (rollback).");
        }
    }

    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escriba un numero entero.");
            }
        }
    }

    static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escriba un numero (ejemplo 12.5).");
            }
        }
    }
}
