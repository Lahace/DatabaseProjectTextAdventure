package progettodb;
import java.sql.*;
import com.jcraft.jsch.*;
import javax.swing.JOptionPane;

public class DB {
    private static Connection dbconn = null; //FILL THESE!
    private static String user = null;
    private static String password = null;
    private static String DBName = null;
    private static String location = null;
    
    private static String localUser = null;
    private static String localPassword = null;
    private static String localDBName = null;
    private static String localLocation = null;

    public static Connection connect(boolean locale) throws SQLException
    {
        if(!locale)
        {
            dbconn = null;
            try
            {
                JSch jsch = new JSch();
                Session session = jsch.getSession("xxxx", "xxxx",22);
                session.setPassword("xxxx");
                session.setConfig("StrictHostKeyChecking","no");
                session.connect();
                int port = session.setPortForwardingL(5434,"xxx",5432);
                dbconn = DriverManager.getConnection("jdbc:postgresql://" + location + "/" + DBName + "?user=" + user + "&password=" + password);
            }
            catch(JSchException | SQLException e)
            {
                JOptionPane.showMessageDialog(null,e);
                System.exit(1);
            }
            return dbconn;
        }
        else
        {
            dbconn = null;
            try
            {
                dbconn = DriverManager.getConnection("jdbc:postgresql://" + localLocation + "/" + localDBName + "?user=" + localUser + "&password=" + localPassword);
            }
            catch(SQLException e)
            {
                JOptionPane.showMessageDialog(null,e);
                System.exit(1);
            }
            return dbconn;
        }
    }
    
}