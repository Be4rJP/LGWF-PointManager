package be4r.lgwf.pointmanager.sql;

import be4r.lgwf.pointmanager.Main;
import java.sql.*;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Be4rJP
 */
public class SQLDriver {
    private final String ip, port, database, username, password;
    
    private Connection connection = null;
    private boolean connected = false;
    
    public SQLDriver(String ip, String port, String database, String username, String password, boolean debug) throws Exception{
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        connection = DriverManager.getConnection("jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.database + "?autoReconnect=true&useSSL=false", this.username, this.password);
        if (connection != null) {
                FileConfiguration conf = Main.config.getConfig();
                if(debug)
                    System.out.println("Connected to MySQL database.");
                this.connected = true;
        } else {
                System.out.println("Failed to connect to MySQL database.");
                this.connected = false;
        }
    }
    
    public int getInt(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
            resultSet.next();
        }
        int i = resultSet.getInt(column);
        resultSet.close();
        statement.close();
        return i;
    }
    
    public String getString(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet.close();
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        }
        String s = resultSet.getString(column);
        resultSet.close();
        statement.close();
        return s;
    }
    
    public void updateValue(String tableName, String Value, String search) throws Exception{
        execute("UPDATE " + tableName +  " SET " + Value + " WHERE " + search + ";");
    }
    
    public void execute(String cmd) throws Exception{
        Statement statement = connection.createStatement();
        statement.execute(cmd);
        statement.close();
    }
    
    public void close() throws Exception{
        this.connection.close();
    }
    
    public boolean getConnected(){return this.connected;}
}
