import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Experiencia de practica: base de datos propia (practica.db) con una tabla de 3 campos.
// Se hacen las operaciones insertar, recuperar, actualizar y borrar con PreparedStatement
// y se muestra el manejo de transacciones con commit y rollback.
public class Practica {

    static Connection con;

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:practica.db");
            System.out.println("Se creo y/o abrio la base de datos practica.db");

            crearTabla();

            // todas las operaciones siguientes forman parte de una transaccion
            con.setAutoCommit(false);

            System.out.println("\n--- INSERTAR ---");
            insertar(1, "Carlos", 20);
            insertar(2, "Bety", 22);
            insertar(3, "Laura", 25);
            con.commit();
            System.out.println("commit: los 3 registros quedaron guardados");
            mostrar();

            System.out.println("\n--- ACTUALIZAR ---");
            actualizar(2, "Beatriz", 23);
            con.commit();
            mostrar();

            System.out.println("\n--- BORRAR ---");
            borrar(3);
            con.commit();
            mostrar();

            System.out.println("\n--- ROLLBACK ---");
            insertar(4, "Diana", 30);
            borrar(1);
            System.out.println("Antes del rollback:");
            mostrar();
            con.rollback();
            System.out.println("rollback: los dos cambios anteriores se cancelaron");
            mostrar();

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // crea la tabla emp con 3 campos y la deja vacia para poder repetir el programa
    static void crearTabla() throws SQLException {
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS emp (id integer PRIMARY KEY, name text, age integer)");
        stmt.execute("DELETE FROM emp");
    }

    static void insertar(int id, String nombre, int edad) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO emp (id, name, age) VALUES (?, ?, ?)");
        ps.setInt(1, id);
        ps.setString(2, nombre);
        ps.setInt(3, edad);
        ps.executeUpdate();
        System.out.println("Insertado: " + id + " " + nombre + " " + edad);
    }

    static void actualizar(int id, String nombre, int edad) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE emp SET name = ?, age = ? WHERE id = ?");
        ps.setString(1, nombre);
        ps.setInt(2, edad);
        ps.setInt(3, id);
        int filas = ps.executeUpdate();
        System.out.println("Actualizados: " + filas + " registro(s)");
    }

    static void borrar(int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM emp WHERE id = ?");
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        System.out.println("Borrados: " + filas + " registro(s)");
    }

    // recupera todos los registros de la tabla
    static void mostrar() throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT id, name, age FROM emp ORDER BY id");
        ResultSet rs = ps.executeQuery();
        System.out.println("Contenido de la tabla emp:");
        while (rs.next()) {
            System.out.println("  " + rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
        }
    }
}
