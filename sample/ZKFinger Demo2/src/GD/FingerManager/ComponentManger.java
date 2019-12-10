package GD.FingerManager;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ComponentManger {

    private DbManager dbManager = null;
    private GuiManager guiManager = null;
    private FingerManager fingerManager = null;
    private final int  DAY = 24 * 60 * 60 * 1000;


    private String beginTime = null;
    private String endTime = null;
    private int isLegal = 0;
    private String teamChoose = "全部";

    private String stasticBeginTime = null;
    private String stasticEndTime = null;
    private String nameChoose = null;

    private final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public  ComponentManger(DbManager dbManager, GuiManager guiManager, FingerManager fingerManager){
        this.dbManager = dbManager;
        this.guiManager = guiManager;
        this.fingerManager = fingerManager;
    }

    public void fingerLoad() throws Exception{
        ResultSet resultSet = dbManager.dbSearch("FingerBase", "*", "");
        while (resultSet.next()){
            fingerManager.fingerAdd(resultSet.getInt("id"), resultSet.getBytes("template"));
        }
    }

    public void login(JTextField name, JTextField password, JDialog loginDialog) throws Exception{
        ResultSet resultSet = dbManager.dbSearch("Staff", "*", "where name = '"+name.getText() +"' " );
        if (resultSet.next()) {
            if (password.getText().compareTo(resultSet.getString("password")) == 0) {
                guiManager.userName = name.getText();
                guiManager.userPemission = resultSet.getInt("permission");
                guiManager.showMessageDialog(name.getText() + "登陆成功, 欢迎您!");
                loginDialog.dispose();
                guiManager.userPanel.remove(0);
                guiManager.userPanel.add(new JLabel(name.getText()));
                JButton cancel = new JButton("注销");
                cancel.setBorderPainted(false);
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        guiManager.userPanel.remove(0);
                        guiManager.userPanel.remove(0);
                        JButton login = new JButton("登陆");
                        login.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                guiManager.createLoginDialog();
                            }
                        });
                        guiManager.userPanel.add(login);
                        guiManager.showMessageDialog("注销成功!");
                        guiManager.userPanel.revalidate();
                    }
                });
                guiManager.userPanel.add(cancel);
                guiManager.userPanel.revalidate();
                return;
            }
        }
        throw new Exception("用户不存在,或输入的密码错误!");
    }

    public void statusDisplay(JPanel statusPanel) throws Exception{
        if (statusPanel.getComponentCount() != 0) {
            statusPanel.remove(0);
        }
        ArrayList onlineStaff = new ArrayList();
        ArrayList offlineStaff = new ArrayList();
        JPanel gridPanel = new JPanel(new GridLayout(8, 8));
        ResultSet resultSet = dbManager.dbSearch("Staff", "*", " " );
        while (resultSet.next()){
            int status = resultSet.getInt("status");
            String name = resultSet.getString("name");
            if (status == 0){
                offlineStaff.add(name);
            }
            else {
                onlineStaff.add(name);
            }
        }
        Iterator it = onlineStaff.iterator();
        while (it.hasNext()){
            JButton staff = new JButton(it.next() + "  在线");
            staff.setForeground(Color.red);
            gridPanel.add(staff);
        }
        it = offlineStaff.iterator();
        while (it.hasNext()){
            JButton staff = new JButton(it.next()+ "  离线");
            gridPanel.add(staff);
        }
        statusPanel.add(gridPanel);
        statusPanel.revalidate();
    }

    public void setDetailTimeSelectButton(JButton today, JButton yesterday, JButton week, JButton month, JTextField begin, JTextField end, int select){
        today.setBackground(null);
        yesterday.setBackground(null);
        week.setBackground(null);
        month.setBackground(null);
        Date Mytoday = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(Mytoday);
        if (select == 4) {
            beginTime = begin.getText();
        }else if (select == 5){
            endTime = end.getText();
        }else {
            begin.setText("");
            end.setText("");
        }
        switch (select){
            case 0:
                today.setBackground(Color.lightGray);
                beginTime = DateFormat.format(Mytoday.getTime());
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                break;
            case 1:
                yesterday.setBackground(Color.lightGray);
                beginTime = DateFormat.format(Mytoday.getTime() - DAY);
                endTime = DateFormat.format(Mytoday.getTime());
                break;
            case 2:
                week.setBackground(Color.lightGray);
                c.setTime(Mytoday);
                int weekday = c.get(Calendar.DAY_OF_WEEK);
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                beginTime = DateFormat.format(Mytoday.getTime() - ((weekday+5)%7) * DAY);
                break;
            case 3:
                month.setBackground(Color.lightGray);
                int monthday = c.get(Calendar.DAY_OF_MONTH);
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                beginTime = DateFormat.format(Mytoday.getTime() - (monthday-1) * DAY);
            default:
                break;
        }
    }

    public void setDetailStatusSelectButton(JButton normal, JButton abnormal, int select){
        if (select == 0){
            normal.setBackground(Color.lightGray);
            abnormal.setBackground(null);
            isLegal = 0;
        }
        else if (select == 1){
            abnormal.setBackground(Color.lightGray);
            normal.setBackground(null);
            isLegal = 1;
        }
    }

    public void setDetailTeamChoose(String team){
        teamChoose = team;
    }

    public String[] getDetailGroupContent() throws Exception{
       List group = new ArrayList<>();
       group.add("全部");
       ResultSet resultSet = dbManager.dbSearch("teamrecord", "team", "");
       while (resultSet.next()){
           group.add(resultSet.getString("team"));
       }
       return (String[])group.toArray(new String[group.size()]);
    }

    public void doDetailSearch(JPanel detailResultPanel) throws Exception{
        if ((beginTime.length() == 0) || (endTime.length() == 0)){
            throw new Exception("请输入正确的时间范围");
        }
        ResultSet resultSet = null;
        if (teamChoose.compareTo("全部") != 0) {
           resultSet = dbManager.dbSearch("AttendanceRecord, Staff ", "name,team,intime,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                    "and isLegal = " + isLegal +
                    " and team = '" + teamChoose + "'" +
                    " and intime between '" + beginTime + "' and '" + endTime + "'");
        }else{
           resultSet = dbManager.dbSearch("AttendanceRecord, Staff ", "name,team,intime,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                    " and isLegal = " + isLegal +
                    " and intime between '" + beginTime + "' and '" + endTime + "'");
        }

        String[] columnsName = new String[]{"日期", "姓名", "上线时间", "下线时间", "状态", "小组", "持续时间/小时"};
        resultSet.last();
        Object[][] rowdata;
        int rowNum = resultSet.getRow();
        if (rowNum !=0) {
            rowdata = new Object[rowNum][7];
        }
        else {
            rowdata = new Object[][]{{"", "", "", "", "", "", ""}};
        }
        resultSet.beforeFirst();

        while (resultSet.next()){
            rowdata[rowNum - resultSet.getRow()][0] = resultSet.getString("intime").substring(0, 10);
            rowdata[rowNum - resultSet.getRow()][1] = resultSet.getString("name");
            rowdata[rowNum - resultSet.getRow()][2] = resultSet.getString("intime").substring(11, 19);
            if (resultSet.getString("outime") == null){
                rowdata[rowNum - resultSet.getRow()][3] = "";
            }
            else {
                rowdata[rowNum - resultSet.getRow()][3] = resultSet.getString("outime").substring(11, 19);
            }
            if (resultSet.getInt("isLegal") == 0) {
                rowdata[rowNum - resultSet.getRow()][4] = "正常";
            }
            else {
                rowdata[rowNum - resultSet.getRow()][4] = "异常";
            }
            rowdata[rowNum - resultSet.getRow()][5] = resultSet.getString("team");
            rowdata[rowNum - resultSet.getRow()][6] = resultSet.getString("lastime");
        }
        if (detailResultPanel.getComponentCount() != 0) {
            detailResultPanel.remove(0);
        }
        detailResultPanel.add(guiManager.createTable(columnsName, rowdata, false));
        detailResultPanel.revalidate();
    }

    public void setStasticNameChoose(String nameText){
        nameChoose = nameText;
    }

    public void setStasticTimeChoose(JButton lastweek, JTextField stasticBegin, JTextField stasticEnd, int select){
        Date Mytoday = new Date();
        lastweek.setBackground(null);
        if (select == 0){
            lastweek.setBackground(Color.lightGray);
            stasticBeginTime = DateFormat.format(Mytoday.getTime());
            stasticEndTime = DateFormat.format(Mytoday.getTime() + DAY);
            stasticBegin.setText("");
            stasticEnd.setText("");
        }
        else if (select == 1){
            stasticBeginTime = stasticBegin.getText();
        }
        else if (select == 2){
            stasticEndTime = stasticEnd.getText();
        }
    }

    public String[] getAllStaffName() throws Exception{
        List group = new ArrayList<>();
        group.add("全部");
        ResultSet resultSet = dbManager.dbSearch("Staff", "name", "");
        while (resultSet.next()){
            group.add(resultSet.getString("name"));
        }
        return (String[])group.toArray(new String[group.size()]);
    }

    public String[] getPersonStastic(String name) throws Exception{
        ResultSet resultSet = dbManager.dbSearch("SystemSet","*", "");
        resultSet.next();
        float excellent = resultSet.getFloat("excellent");
        float good = resultSet.getFloat("good");
        float fair = resultSet.getFloat("fair");

        resultSet = dbManager.dbSearch("AttendanceRecord, Staff ", "name,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" +
                " and outime between '" + stasticBeginTime + "' and '" + stasticEndTime + "'");
        float totalTime = 0;
        int normalFrequency = 0;
        int abnormalFrequency = 0;
        long totalDay = (DateFormat.parse(stasticEndTime).getTime() - DateFormat.parse(stasticBeginTime).getTime()) / DAY;
        while (resultSet.next()){
            totalTime += resultSet.getFloat("lastime");
            if (resultSet.getInt("isLegal") == 0){
                normalFrequency++;
            }
            else {
                abnormalFrequency++;
            }
        }
        float average = totalTime / totalDay;
        String comment = null;
        if ((average - excellent)> 1e-6){
            comment = "优秀";
        }
        else if ((average - good) > 1e-6) {
            comment = "良好";
        }
        else if ((average - fair) > 1e-6){
            comment = "及格";
        }
        else {
            comment = "不合格";
        }
        String[] result = new String[4];
        result[0] = Float.toString(totalTime);
        result[1] = Integer.toString(normalFrequency);
        result[2] = comment;
        result[3] = Integer.toString(abnormalFrequency);
        return result;
    }

    public String[] getPersonStastic(String name, ArrayList record) throws Exception{
        ResultSet resultSet = dbManager.dbSearch("SystemSet","*", "");
        resultSet.next();
        float excellent = resultSet.getFloat("excellent");
        float good = resultSet.getFloat("good");
        float fair = resultSet.getFloat("fair");

        resultSet = dbManager.dbSearch("AttendanceRecord, Staff ", "name,outime,lastime,isLegal,intime", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" +
                " and outime between '" + stasticBeginTime + "' and '" + stasticEndTime + "'");
        float totalTime = 0;
        int normalFrequency = 0;
        int abnormalFrequency = 0;
        long totalDay = (DateFormat.parse(stasticEndTime).getTime() - DateFormat.parse(stasticBeginTime).getTime()) / DAY;
        while (resultSet.next()){
            record.add(resultSet.getString("intime"));
            record.add(resultSet.getString("outime"));
            record.add(resultSet.getString("lastime"));
            if (resultSet.getInt("isLegal") == 0){
                normalFrequency++;
                totalTime += resultSet.getFloat("lastime");
                record.add("正常");
            }
            else {
                abnormalFrequency++;
                record.add("异常");
            }
        }
        float average = totalTime / totalDay;
        String comment = null;
        if ((average - excellent)> 1e-6){
            comment = "优秀";
        }
        else if ((average - good) > 1e-6) {
            comment = "良好";
        }
        else if ((average - fair) > 1e-6){
            comment = "及格";
        }
        else {
            comment = "不合格";
        }
        String[] result = new String[4];
        result[0] = Float.toString(totalTime);
        result[1] = Integer.toString(normalFrequency);
        result[2] = comment;
        result[3] = Integer.toString(abnormalFrequency);
        return result;
    }

    private ArrayList getPersonAllDate(String name) throws Exception{
        ArrayList dateList = new ArrayList();
        ResultSet resultSet = dbManager.dbSearch("AttendanceRecord, Staff ", "outime", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" );
        while (resultSet.next()){
            if (resultSet.getString("outime") != null) {
                dateList.add(resultSet.getString("outime").substring(0, 10));
            }
        }
        return dateList;
    }

    public void doStasticSearch(JPanel resultStastic) throws Exception{
        if (stasticBeginTime == "" || stasticEndTime == ""){
            throw new Exception("请输入合法的查询时间段");
        }
        if (nameChoose == "全部"){
            String[] nameList = getAllStaffName();
            Object[][] rowdate = new Object[nameList.length - 1][6];
            for (int i=1; i<nameList.length; i++ ){
                String[] personStastic = getPersonStastic(nameList[i]);
                rowdate[i - 1][0] = stasticBeginTime + "—" + stasticEndTime;
                rowdate[i - 1][1] = nameList[i];
                rowdate[i - 1][2] = personStastic[0];
                rowdate[i - 1][3] = personStastic[1];
                rowdate[i - 1][4] = personStastic[2];
                rowdate[i - 1][5] = personStastic[3];
            }
            String[] columnNames = {"时间段" ,"姓名", "考勤总时间/小时", "考勤总次数", "评价", "异常记录次数"};
            if (resultStastic.getComponentCount() !=0) {
                resultStastic.remove(0);
            }
            resultStastic.add(guiManager.createTable(columnNames, rowdate, false));
            resultStastic.revalidate();
        }
        else {
            ArrayList dateList = new ArrayList();
            String[] personStastic = getPersonStastic(nameChoose);
            dateList = getPersonAllDate(nameChoose);
            if (resultStastic.getComponentCount() != 0) {
                resultStastic.remove(0);
            }
            resultStastic.add(guiManager.createPersonalStatis(personStastic, dateList));
            resultStastic.revalidate();
        }
    }

    public void doCheckFileOut (File file) throws Exception{
        String[] staffName = getAllStaffName();
        FileWriter fw = new FileWriter(file);
        for (int i = 1; i < staffName.length; i++){
            fw.write(staffName[i]+"\r\n");
            ArrayList recordList = new ArrayList();
            String[] result = getPersonStastic(staffName[i], recordList);
            for (int j = 0; j < recordList.toArray().length ; j = j + 4){
                fw.write("进入时间:\t" + recordList.get(j).toString());
                fw.write("\t离开时间:\t" + recordList.get(j + 1).toString());
                fw.write("\t持续时间:\t" + recordList.get(j + 2).toString());
                fw.write("\t记录状态:\t" + recordList.get(j + 3).toString() + "\r\n");
            }
            fw.write("统计信息如下:\r\n");
            fw.write("总持续时间:\t" + result[0] + "\t总出入次数:\t" + result[1] + "\t考勤评价:\t" + result[2] + "\t异常记录次数:\t" + result[3] + "\r\n\r\n\r\n");
        }
        fw.close();
    }

    public void doPersonEnroll(String[] information, byte[] fingerTemplate) throws Exception{
        if (fingerTemplate == null){
            throw new Exception("请先录入你的指纹");
        }
        if (information[0].length() == 0){
            throw new Exception("姓名不能为空,请输入姓名");
        }
        if (information[1].length() == 0){
            throw new Exception("学号不能为空,请输入学号");
        }
        try{
            if (information[2].length() != 1) {
                throw new Exception();
            }
            if ((Integer.parseInt(information[2])<0) || (Integer.parseInt(information[2]) > 2)){
                throw new Exception();
            }
        }catch (Exception e){
            throw new Exception("请输入正确的权限等级");
        }

        if (information[6].length() == 0){
            throw new Exception("密码不能为空, 请输入密码");
        }
        if (information[7].length() == 0){
            throw new Exception("请再次输入密码");
        }
       if (information[6].compareTo(information[7]) != 0){
            throw new Exception("两次密码输入不一样,请重新输入");
       }
       if (information[0].length() > 20){
            throw new Exception("用户姓名输入过长,请重新输入");
       }
       if (information[1].length() > 15){
            throw new Exception("用户学号输入过长,请重新输入");
       }
       if (information[3].length() > 20){
            throw new Exception("导师名称输入过长, 请重新输入");
       }
       if (information[4].length() > 20){
            throw new Exception("小组名称输入过长, 请重新输入");
       }
       if (information[5].length() > 20){
            throw new Exception("手机号码输入过长,请重新输入");
       }
       if (information[6].length() > 20){
            throw new Exception("密码输入过长, 请重新输入");
       }
       int id = dbManager.dbGetMaxId("Staff");
       dbManager.dbInsert(id, information[0].getBytes(), information[1].getBytes(), Integer.parseInt(information[2]), information[3].getBytes(), information[4].getBytes(), 0, information[5].getBytes(), information[6].getBytes());
       dbManager.dbInsert(id, fingerTemplate);
       fingerManager.fingerAdd(id, fingerTemplate);
       guiManager.showMessageDialog("人员录入成功!");
       this.createPersonTable(guiManager.personTable);
       this.statusDisplay(guiManager.statusPanel);
    }

    public void createPersonTable(JPanel personTable) throws Exception{
        ResultSet resultSet = dbManager.dbSearch("Staff", "*", "");
        resultSet.last();
        Object[][] rowdata = null;
        if (resultSet.getRow() == 0) {
            rowdata = new Object[][]{{"","","","","","",""}};
        }
        else {
            rowdata = new Object[resultSet.getRow()][7];
        }
        String[] columnName =  new String[]{"编号", "姓名", "小组", "导师", "学号", "手机号码", "操作"};
        resultSet.beforeFirst();
        while (resultSet.next()){
            rowdata[resultSet.getRow() - 1][0] = resultSet.getString("id");
            rowdata[resultSet.getRow() - 1][1] = resultSet.getString("name");
            rowdata[resultSet.getRow() - 1][2] = resultSet.getString("team");
            rowdata[resultSet.getRow() - 1][3] = resultSet.getString("tutor");
            rowdata[resultSet.getRow() - 1][4] = resultSet.getString("sno");
            rowdata[resultSet.getRow() - 1][5] = resultSet.getString("phone");
            rowdata[resultSet.getRow() - 1][6] = resultSet.getString("id");
        }
        if (personTable.getComponentCount() != 0){
            personTable.remove(0);
        }
        personTable.add(guiManager.createTable(columnName, rowdata, true));
        personTable.revalidate();
    }

    public void doPersonModyify(String[] information, String id) throws Exception{
        if (information[6].compareTo(information[7]) != 0){
            throw new Exception("两次密码输入不一样,请重新输入");
        }
        if (information[0].length() > 20){
            throw new Exception("用户姓名输入过长,请重新输入");
        }
        if (information[1].length() > 15){
            throw new Exception("用户学号输入过长,请重新输入");
        }
        if (information[3].length() > 20){
            throw new Exception("导师名称输入过长, 请重新输入");
        }
        if (information[4].length() > 20){
            throw new Exception("小组名称输入过长, 请重新输入");
        }
        if (information[5].length() > 20){
            throw new Exception("手机号码输入过长,请重新输入");
        }
        if (information[6].length() > 20){
            throw new Exception("密码输入过长, 请重新输入");
        }
        if (information[0].length()>0){
            dbManager.dbUpdateChar("Staff", "name", information[0], "id="+id);
        }
        if (information[1].length()>0){
            dbManager.dbUpdateChar("Staff", "sno", information[1], "id="+id);
        }
        try{
            if (information[2].length() > 1) {
                throw new Exception();
            }
            if (information[2].length() == 1) {
                if ((Integer.parseInt(information[2]) < 0) || (Integer.parseInt(information[2]) > 2)) {
                    throw new Exception();
                }
                else {
                    dbManager.dbUpdate("Staff", "permission", information[2], "id=" + id);
                }
            }
        }catch (Exception e){
            throw new Exception("请输入正确的权限等级");
        }

        if (information[3].length()>0){
            dbManager.dbUpdateChar("Staff", "tutor", information[3], "id="+id);
        }
        if (information[4].length()>0){
            dbManager.dbUpdateChar("Staff", "team", information[4], "id="+id);
        }
        if (information[5].length()>0){
            dbManager.dbUpdateChar("Staff", "phone", information[5], "id="+id);
        }
        if (information[6].length()>0){
            dbManager.dbUpdateChar("Staff", "phone", information[6], "id="+id);
        }
        guiManager.showMessageDialog("修改成功");
        this.createPersonTable(guiManager.personTable);
    }

    public void deletePersonInfo(String id) throws Exception{
        dbManager.dbDelete("Staff", "id", id);
    }

    public void setCheckTime(String startTime, String endTime) throws Exception{
        dbManager.dbUpdate("SystemSet","beginTime","'"+startTime+"'", "device = 0");
        dbManager.dbUpdate("SystemSet","endTime ","'"+endTime+"'", "device = 0");
    }
    public void addTeam(String team) throws Exception{
        dbManager.dbInsert("TeamRecord", "team ", "'" + team + "'");
    }
    public void deleteTeam(String team) throws Exception{
        dbManager.dbDelete("TeamRecord", "team", "'" + team + "'");
    }
}
