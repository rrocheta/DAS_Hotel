package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconn {

    private static String url = "jdbc:sqlserver://ctespbd.dei.isep.ipp.pt;databaseName=2022_F_LP3_G4;TrustServerCertificate=True";
    private static String user = "2022_F_LP3_G4";
    private static String pass = "123+qwe*123";

    /**
     * A função getConn serve para fazer a conexão á base de dados
     *
     * @return devolve a ligação da base de dados
     */
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return conn;
    }
}
