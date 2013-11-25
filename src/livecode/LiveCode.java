package livecode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;

/**
 *
 * @author Austin
 */
public class LiveCode {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ResultSet currentUser = null;
    
    public void LiveCode() {
        
    }
    
    public void mysqlConnect() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://austinh100.com/"+Constants.db_database+"?"
                        + "user="+Constants.db_user+"&password="+Constants.db_pass);
            statement = connect.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public Boolean checkLogin(String user, String pass) {
        mysqlConnect();
        try {
            user = user.trim();
            pass = pass.trim();
            preparedStatement = connect.prepareStatement("SELECT * FROM `users` WHERE `username`='"+user+"' LIMIT 1");
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Boolean valid = (hash(pass).equals(resultSet.getString("password")))?true:false;
                System.out.println(hash(pass));
                System.out.println(resultSet.getString("password"));
                if(valid)
                    currentUser = resultSet;
                return valid;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public String hash(String pass) {
        return sha1(pass+pass.substring(0,3));
    }
    
    public String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02X", 0xff & messageDigest[i]));
            return hexString.toString().toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
}
    
    private void close() {
        try {
          if (resultSet != null) {
            resultSet.close();
          }

          if (statement != null) {
            statement.close();
          }

          if (connect != null) {
            connect.close();
          }
        } catch (SQLException e) {

        }
   }
}