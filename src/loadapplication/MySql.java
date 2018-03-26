package loadapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySql {

    private Connection con;
    private String url = "localhost";
    private String port = "3306";
    private String dbName = "Store";
    private String userName = "root";
    private String userPassword = "";

    public String getUrl() {
        return url;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Connection getConnection() {
        return this.con;
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException ex) {
        }
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + dbName + "", userName, userPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySql.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(null, "خطأ اثناء فتح الاتصال بقاعده البيانات \n سيتم أغلاق البرنامج تأكد من الاعددات فى ملف \n Config/SqlConfig.ini");
            System.exit(0);
        }
    }

    public MySql() {
    }

}
