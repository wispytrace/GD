package GD.FingerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class WorkThread extends Thread{
    private GuiManager guiManager = null;
    private FingerManager fingerManager = null;
    private DbManager dbManager = null;
    private ComponentManger componentManger = null;
    public void WorkThread(GuiManager guiManager, FingerManager fingerManager, DbManager dbManager, ComponentManger componentManger){
        this.guiManager = guiManager;
        this.fingerManager = fingerManager;
        this.dbManager = dbManager;
        this.componentManger = componentManger;
    }
    public void run() {
        super.run();
        byte[] img = new byte[fingerManager.figureHeight * fingerManager.figureWidth];
        byte[] template = new byte[fingerManager.templateLen];
        int[] fid = new int[1];
        int recordid = 0;
        try {
           recordid = dbManager.dbGetMaxId("AttendanceRecord");
        }catch (Exception e){
            guiManager.showErrorDialog(e.getMessage());
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true){
            try{
                fingerManager.figureAcquire(img, template);
                fingerManager.fingerIdentity(template, fid);
                String nowTime = dateFormat.format(new Date());
                if (fingerManager.Online.containsKey(fid[0])){
                    dbManager.dbUpdate("AttendanceRecord","outime", "'" + nowTime + "'", "recordid="+fingerManager.Online.get(fid[0]));
                    dbManager.dbUpdate("Staff", "status", "0", "id="+fid[0]);
                    ResultSet resultSet =  dbManager.dbSearch("AttendanceRecord", "intime", " where recordid=" + fingerManager.Online.get(fid[0]));
                    resultSet.next();
                    String intime = resultSet.getString("intime");
                    float lastTime = (dateFormat.parse(nowTime).getTime() - dateFormat.parse(intime).getTime()) / (1000 * 60 * 60);
                    dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(lastTime), "recordid="+fingerManager.Online.get(fid[0]));
                    fingerManager.Online.remove(fid[0]);
                    fingerManager.lightControl("red");
                    componentManger.statusDisplay(guiManager.statusPanel);
                    componentManger.doDetailSearch(guiManager.detailResultPanel);
                }
                else {
                    fingerManager.Online.put(fid[0], recordid);
                    dbManager.dbInsert(recordid, fid[0]);
                    recordid++;
                    dbManager.dbUpdate("Staff", "status", "1", "id="+fid[0]);
                    fingerManager.lightControl("green");
                    componentManger.statusDisplay(guiManager.statusPanel);
                    componentManger.doDetailSearch(guiManager.detailResultPanel);
                }
            }catch (IOException e){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    guiManager.showErrorDialog(ie.getMessage());
                }
            }catch (Exception e){
                guiManager.showErrorDialog(e.getMessage());
            }
        }
    }
}
