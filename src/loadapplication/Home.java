package loadapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home {

    private final SplashScreen splash = new SplashScreen();
    private final File file = new File("Configs/SqlConfig.ini");
    private final File storeFile = new File("Store.jar");
    private final MySql sql = new MySql();
    private String ConfigFileStore_App_Version = "";

    private void checkConfigFileExists() throws IOException {
        if (!file.exists()) {
            new File("Configs").mkdir();
            Properties p = new Properties();
            p.put("MySql_Url", "localhost");
            p.put("MySql_Port", "3306");
            p.put("MySql_Db_Name", "Store");
            p.put("MySql_User_Name", "root");
            p.put("MySql_Password", "");
            p.put("Store_App_Version", "");
            p.store(new FileWriter(file), "My Sql Configrations For Ahmed Store Application ");
        }
    }

    private void getConfigurations() {
        try {
            checkConfigFileExists();
            Properties p = new Properties();
            p.load(new FileReader(file));
            sql.setUrl(p.getProperty("MySql_Url", "localhost"));
            sql.setPort(p.getProperty("MySql_Port", "3306"));
            sql.setDbName(p.getProperty("MySql_Db_Name", "Store"));
            sql.setUserName(p.getProperty("MySql_User_Name", "root"));
            sql.setUserPassword(p.getProperty("MySql_Password", ""));
            ConfigFileStore_App_Version = p.getProperty("Store_App_Version", "");
        } catch (IOException ex) {
        }
    }

    private String getStoreAppVersionFromDb(Connection con) {
        String x = "";
        try {
            PreparedStatement stat = con.prepareStatement("Select Version from app order by Date desc limit 1");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                x = rs.getNString(1);
            }
        } catch (SQLException ex) {
        }
        return x;
    }

    private void downloadLoginApp(Connection con) throws FileNotFoundException, SQLException, IOException {
        int sizeInBytes = 0;
        int offest = 1;
        if (storeFile.exists() && !storeFile.delete()) {
            throw new IOException("Can't Delete Old Store File ");
        }
        PreparedStatement stat = con.prepareStatement("select Length(app)as z from app order by Date desc limit 1");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            sizeInBytes = rs.getInt(1);
        }
        int step=100/(sizeInBytes/102400);
        try (FileOutputStream fw = new FileOutputStream(storeFile, true)) {
            for (int i; (i = offest) <= sizeInBytes; i++) {
                int packet = ((sizeInBytes - offest >= 102400) ? 102400 : (sizeInBytes - offest) + 1);
                if (packet == 0) {
                    break;
                }
                stat = con.prepareStatement("select subString(app,?,?) from App Order By Date Desc Limit 1 ");
                stat.setInt(1, offest);
                stat.setInt(2, packet);
                rs = stat.executeQuery();
                while (rs.next()) {
                    byte[] data = rs.getBytes(1);
                    fw.write(data);
                }
                System.out.println(splash.jProgressBar2.getValue());
                splash.jProgressBar2.setValue(splash.jProgressBar2.getValue()+step);
                offest += packet;
            }
            fw.flush();
        }
        rs.close();
        stat.close();
    }

    private void runLoginApp() throws FileNotFoundException, SQLException, IOException {
        if (!storeFile.exists()) {
            String dbLoginAppVersion = getStoreAppVersionFromDb(sql.getConnection());
            downloadLoginApp(sql.getConnection());
            setConfigLoginAppVersion(dbLoginAppVersion);
        }
        Runtime.getRuntime().exec("java -jar " + storeFile.getName());
    }

    private void setConfigLoginAppVersion(String dbCurrentVersion) throws FileNotFoundException, IOException {
        Properties p = new Properties();
        p.load(new FileReader(file));
        p.setProperty("Store_App_Version", dbCurrentVersion);
        p.store(new FileWriter(file), "My Sql Configrations For Ahmed Store Application ");
    }

    private void startLoginApp() {
        sql.openConnection();
        Connection con = sql.getConnection();
        String dbLoginAppVersion = getStoreAppVersionFromDb(con);
        try {
            if (ConfigFileStore_App_Version.equals(dbLoginAppVersion)) {
                runLoginApp();
            } else {
                downloadLoginApp(con);
                setConfigLoginAppVersion(dbLoginAppVersion);
                runLoginApp();
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Home() {
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
        getConfigurations();
        startLoginApp();
        sql.closeConnection();
        splash.jProgressBar2.setValue(100);
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        Home home = new Home();

    }
}
