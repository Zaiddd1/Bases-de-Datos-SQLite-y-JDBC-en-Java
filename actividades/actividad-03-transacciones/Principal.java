import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Manejo de transacciones: commit confirma los cambios y rollback los cancela
public class Principal {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:ejemplo.db");

            if (con != null) {
                System.out.println("Se creo y/o abrio la base de datos: ");
            }

            Statement stmt = con.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS emp "
                    + "(id integer PRIMARY KEY, name text, age integer)");
            // se quitan los registros de pruebas anteriores para poder repetir el programa
            stmt.executeUpdate("delete from emp where id in (190, 191, 200)");

            con.setAutoCommit(false);

            stmt.executeUpdate("insert into emp values(190,'jose',77)");
            stmt.executeUpdate("insert into emp values(191,'diana',88)");
            con.commit();

            stmt.executeUpdate("insert into emp values(200,'andy',99)");
            con.rollback();

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
