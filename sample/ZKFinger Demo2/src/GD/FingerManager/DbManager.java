package GD.FingerManager;

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
        if (connect.isClosed()){
            throw new Exception("Data Base Connection Failed, Please Try Again");
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
    public void dbInsert(int id, byte[] name, byte[] sno, byte[] grade,int permission, byte[] tutor, byte[] team,
                         int status, byte[] password) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        PreparedStatement sql = connect.prepareStatement("insert into Staff values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        sql.setInt(1, id);
        sql.setBytes(2, name);
        sql.setBytes(3, sno);
        sql.setBytes(4, grade);
        sql.setInt(5, permission);
        sql.setBytes(6, tutor);
        sql.setBytes(7, team);
        sql.setInt(8, status);
        sql.setBytes(9, password);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, java.sql.Date outime) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        PreparedStatement sql = connect.prepareStatement("insert into AttendanceRecord (id, outime) values(?, ?)");
        sql.setInt(1, id);
        sql.setDate(2, outime);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, java.sql.Date occurtime, byte[] behavior) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        PreparedStatement sql = connect.prepareStatement("insert into IlegalList values(?, ?, ?)");
        sql.setInt(1, id);
        sql.setDate(2, occurtime);
        sql.setBytes(3, behavior);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, byte[] template, int memoryid) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        PreparedStatement sql = connect.prepareStatement("insert into FingerBase values(?, ?, ?)");
        sql.setInt(1, id);
        sql.setBytes(2, template);
        sql.setInt(3, memoryid);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, int week, int totaltime, byte[] comment) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        PreparedStatement sql = connect.prepareStatement("insert into AttendanceStatistics values(?, ?, ?, ?)");
        sql.setInt(1, id);
        sql.setInt(2, week);
        sql.setInt(3, totaltime);
        sql.setBytes(4, comment);
        sql.executeUpdate();
        sql.close();
    }
    public void dbUpdate(String table, String[] field, String[] value, String[] condition) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        Statement statement = connect.createStatement();
        for (int i = 0; i < value.length; i++) {
            statement.executeQuery("update " + table + " set " + field[i] + "=" + value[i] + " where " + condition[i]);
        }
    }
    public void dbDelete(String table, String[] field, String[] value)throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        Statement statement = connect.createStatement();
        for (int i = 0; i < value.length; i++) {
            statement.executeQuery("delete from  " + table + " where " + field[i] + "=" + value[i]);
        }
    }
    public ResultSet dbSearch(String table, String field, String condition ) throws Exception{
        if (connect.isClosed()){
            throw new Exception("Connect Has closed");
        }
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery("select " + field + " from " + table + " order by id  "+ condition );
        return result;
    }
    public static void main(String[] args){
       try{
           DbManager test = new DbManager();
           test.dbConnect("root", "");
           test.dbInsert(0, "欧阳炳濠".getBytes(), "PB16120517".getBytes(), "大四".getBytes(), 0, "王永".getBytes(),
                   "AI开发".getBytes(), 0, "tlsf,hhzj".getBytes());
           ResultSet kk =  test.dbSearch("staff","*" ,"");
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