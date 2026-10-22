import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Conexion a SQLite: crea la base ejemplo.db, una tabla emp, inserta 2 registros y los muestra
public class Principal {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:ejemplo.db");

            if (con != null) {
                System.out.println("Se creo y/o abrio la base de datos: ");
            }

            Statement stmt = con.createStatement();

            // se borra la tabla antes para poder ejecutar el programa varias veces
            stmt.execute("DROP TABLE IF EXISTS emp");
            stmt.execute("CREATE TABLE emp (id integer PRIMARY KEY, name text, age integer);");
            System.out.println("Creacion de tabla exitosa");

            stmt.execute("insert into emp VALUES (1,'Carlos',11)");
            stmt.execute("insert into emp VALUES (2,'Bety',22)");
            System.out.println("Insercion exitosa");

            ResultSet rs = stmt.executeQuery("SELECT * FROM emp");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3));
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
