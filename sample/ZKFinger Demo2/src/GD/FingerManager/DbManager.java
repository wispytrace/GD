package GD.FingerManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.io.File;
import java.text.DateFormat;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Copyright (C), 20019-2020, HeFei.
 * FileName: FingerManager
 * It's a Data Base Manager to control the StudioCheck Data Base.
 *
 * @author Wispytrace
 * @Date   2019/11/15
 * @version 1.00
 */
public class DbManager {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String DBNAME = "StudioCheck";
    private static final String SQLPATH = "C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\src\\GD\\FingerManager\\StudioCheck.sql";
    private  Connection connect;


    public void dbConnect(String user, String password) throws Exception{
        Class.forName(DRIVER);
        connect = DriverManager.getConnection(URL, user, password);
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接失败, 请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        ResultSet ret = statement.executeQuery("show databases like \'" + DBNAME + '\'');
        if (!ret.next()){
            statement.executeQuery("create database" + DBNAME);
            statement.executeQuery("source" + SQLPATH);
        }
        statement.executeQuery("use "+ DBNAME);
//        Properties properties = System.getProperties();
//        System.out.println(properties.getProperty("user.dir"));
//        File directory = new File("");//设定为当前文件夹
//            System.out.println(directory.getCanonicalPath());//获取标准的路径
//            System.out.println(directory.getAbsolutePath());//获取绝对路径
    }
    public void dbInsert(int id, byte[] name, byte[] sno,int permission, byte[] tutor, byte[] team,
                         int status, byte[] phone, byte[] password) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement sql = connect.prepareStatement("insert into Staff values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        sql.setInt(1, id);
        sql.setBytes(2, name);
        sql.setBytes(3, sno);
        sql.setInt(4, permission);
        sql.setBytes(5, tutor);
        sql.setBytes(6, team);
        sql.setInt(7, status);
        sql.setBytes(8, phone);
        sql.setBytes(9, password);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(String table, String field, String values) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement sql = connect.createStatement();
        sql.executeQuery("insert into " + table + " (" + field+") "+ " values " + "(" + values + ")");
    };
    public void dbInsert(int recordid, int id) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement sql = connect.prepareStatement("insert into AttendanceRecord (recordid, id) values(?, ?)");
        sql.setInt(1, recordid);
        sql.setInt(2, id);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, byte[] template) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }

        PreparedStatement sql = connect.prepareStatement("insert into FingerBase values(?, ?)");
        ByteArrayInputStream templateStream = new ByteArrayInputStream(template);
        sql.setInt(1, id);
        sql.setBlob(2, templateStream);
        templateStream.close();
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int recordid, int id, int week, int totaltime, byte[] comment) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement sql = connect.prepareStatement("insert into AttendanceStatistics values(?, ?, ?, ?, ?)");
        sql.setInt(1, recordid);
        sql.setInt(2, id);
        sql.setInt(3, week);
        sql.setInt(4, totaltime);
        sql.setBytes(5, comment);
        sql.executeUpdate();
        sql.close();
    }
    public void dbUpdate(String table, String field, String value, String condition) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        statement.execute("update " + table + " set " + field + "=" + value + " where " + condition);
    }
    public void dbUpdateChar(String table, String field, String value, String condition) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement statement = connect.prepareStatement("update  "+table+" set "+ field + "=? where " + condition);
        statement.setBytes(1, value.getBytes());
        statement.executeUpdate();
        statement.close();
    }
    public void dbDelete(String table, String field, String value)throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        statement.execute("delete from  " + table + " where " + field + "=" + value);
    }
    public ResultSet dbSearch(String table, String field, String condition ) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery("select " + field + " from " + table + " " + condition );
        return result;
    }
    public int dbGetMaxId(String table) throws Exception{
        int result = 0;
        ResultSet resultSet = dbSearch(table, "*", "");
        resultSet.last();
        result = resultSet.getRow()+1;
        return result;
    }
    public static void main(String[] args){
       try{
           DbManager test = new DbManager();
           test.dbConnect("root", "");
           test.dbInsert(2, "欧阳炳濠co".getBytes(), "PB16120517".getBytes(),  0, null,
                   "人工智能组".getBytes(), 0, null, "tlsf,hhzj".getBytes());
           Statement xxx = test.connect.createStatement();
           ResultSet kk =  test.dbSearch("staff","*" ,"where name =  '欧阳炳濠' ");
           while (kk.next()){
               System.out.println(kk.getString("name"));
           }
//           DateFormat mydateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//           Date mydate = mydateFormat.parse("2014-01-01 23:12:11");
//           test.dbInsert(0,new java.sql.Date(mydate.getTime()));
//           test.dbDelete(new String[]{"11", "22"}, new String[]{"22","333"}, new String[]{"kk", "kkk"});
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
