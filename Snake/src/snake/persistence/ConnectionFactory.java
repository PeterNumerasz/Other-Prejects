package snake.persistence;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    private static MysqlConnectionPoolDataSource conn;
    
    private ConnectionFactory(){}
    
    public static Connection getConnection() 
        throws ClassNotFoundException, SQLException{
        if (conn == null){
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver betöltése
            conn = new MysqlConnectionPoolDataSource();
            conn.setServerName("localhost");
            conn.setPort(3306);
            conn.setDatabaseName("Highscores");
            conn.setUser("tanulo");
            conn.setPassword("asd123");
        }
        return conn.getPooledConnection().getConnection();
    }
}
