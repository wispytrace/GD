package GD.FingerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class EnrollThread extends Thread{
    private DemoUi demoUi = null;
    private FingerManager fingerManager = null;
    private DbManager dbManager = null;
    public void EnrooThread(DemoUi demoUi, FingerManager fingerManager, DbManager dbManager){
        this.demoUi = demoUi;
        this.fingerManager = fingerManager;
        this.dbManager = dbManager;
    }

    public void run() {
        super.run();
        int fid = 0;
        byte[] img = new byte[fingerManager.figureHeight * fingerManager.figureWidth];
        byte[] template = new byte[fingerManager.templateLen];
        byte[][] preRegTemplate = new byte[fingerManager.CONFIRM_TIMES][fingerManager.templateLen];
        int[] regLen = {fingerManager.CONFIRM_TIMES};
        int recordTimes = 0;
        //********************Set invoke user to press finger message
        while (recordTimes < 3){
            try{
                fingerManager.figureAcquire(img, preRegTemplate[recordTimes]);
                fingerManager.fingerFileWrite(img, "temp.bmp");
                demoUi.btnImg.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
                if (recordTimes >= 1){
                    fingerManager.fingerMatch(preRegTemplate[recordTimes-1], preRegTemplate[recordTimes]);
                }
                fingerManager.lightControl("green");
                recordTimes = recordTimes + 1;
            }catch (IOException e){
                try {
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    //*************Set error meesage
                }
            }catch (Exception e){
                //*************Set error meesage
            }
        }
        try{
            fingerManager.fingerMerge(preRegTemplate, template);
            fid = dbManager.dbGetMaxId();//暂定fid的获取来源
            fingerManager.fingerAdd(fid, template);
            dbManager.dbInsert(fid, template, 0);
            //*************Set successful message
        }catch (Exception e){
            //*************Set error meesage
        }
    }
}
