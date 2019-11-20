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
    private DemoUi demoUi = null;
    private FingerManager fingerManager = null;
    private DbManager dbManager = null;
    public void WorkThread(DemoUi demoUi, FingerManager fingerManager, DbManager dbManager){
        this.demoUi = demoUi;
        this.fingerManager = fingerManager;
        this.dbManager = dbManager;
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
            //****************Error Message
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true){
            try{
                fingerManager.figureAcquire(img, template);
                fingerManager.fingerFileWrite(img, "temp.bmp");
                demoUi.btnImg.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
                fingerManager.fingerIdentity(template, fid);
                String nowTime = dateFormat.format(new Date());
                if (fingerManager.Online.containsKey(fid[0])){
                    fingerManager.lightControl("red");
                    fingerManager.Online.remove(fid[0]);
                    dbManager.dbUpdate("AttendanceRecord","outime", nowTime, "recordid="+fingerManager.Online.get(fid[0]));
                    dbManager.dbUpdate("Staff", "status", "0", "id="+fid[0]);
                    //***********Set come out message
                }
                else {
                    fingerManager.lightControl("green");
                    fingerManager.Online.put(fid[0], recordid);
                    dbManager.dbInsert(recordid, fid[0], new java.sql.Date(new Date().getTime()));
                    recordid++;
                    dbManager.dbUpdate("Staff", "status", "1", "id="+fid[0]);
                    //**********Set come in message
                }
            }catch (IOException e){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    //*************Set error meesage
                }
            }catch (Exception e){
                //*************Set error meesage
            }
        }
    }
}
