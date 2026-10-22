import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// Interfaz PreparedStatement: inserta un registro con parametros (?)
public class Principal {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:ejemplo.db");

            if (con != null) {
                System.out.println("Se creo y/o abrio la base de datos: ");
            }

            // la tabla se crea solo si no existe, asi este programa se ejecuta solo
            Statement crear = con.createStatement();
            crear.execute("CREATE TABLE IF NOT EXISTS emp (id integer PRIMARY KEY, name text, age integer)");

            // INSERT OR REPLACE evita el error si el id 5 ya existe (al ejecutar el programa por segunda vez)
            PreparedStatement stmt = con.prepareStatement("insert or replace into emp values(?,?,?)");
            stmt.setInt(1, 5);
            stmt.setString(2, "Laura");
            stmt.setInt(3, 55);   // en la guia decia setInt(1,55), lo que reemplazaba el id

            int i = stmt.executeUpdate();
            System.out.println(i + " records inserted");

            ResultSet rs = crear.executeQuery("SELECT * FROM emp");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3));
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
