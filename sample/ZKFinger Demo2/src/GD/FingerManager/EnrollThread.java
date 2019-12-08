package GD.FingerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class EnrollThread extends Thread{
    private GuiManager guiManager = null;
    private FingerManager fingerManager = null;
    private DbManager dbManager = null;
    private byte[] template = null;
    private JButton imgBtn = null;
    public void EnrollThread(GuiManager guiManager, FingerManager fingerManager, DbManager dbManager, JButton imgBtn){
        this.guiManager = guiManager;
        this.fingerManager = fingerManager;
        this.dbManager = dbManager;
        this.imgBtn = imgBtn;
    }

    public void run() {
        super.run();
        byte[] img = new byte[fingerManager.figureHeight * fingerManager.figureWidth];
        byte[][] preRegTemplate = new byte[fingerManager.CONFIRM_TIMES][fingerManager.templateLen];
        int[] regLen = {fingerManager.CONFIRM_TIMES};
        int recordTimes = 0;
        //********************Set invoke user to press finger message
        while (recordTimes < 3){
            try{
                fingerManager.figureAcquire(img, preRegTemplate[recordTimes]);
                fingerManager.fingerFileWrite(img, "temp.bmp");
                imgBtn.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
                imgBtn.revalidate();
                if (recordTimes >= 1){
                    fingerManager.fingerMatch(preRegTemplate[recordTimes-1], preRegTemplate[recordTimes]);
                }
                fingerManager.lightControl("green");
                recordTimes = recordTimes + 1;
                if (recordTimes != 3){
                    guiManager.showMessageDialog("请再次输入指纹" + (3-recordTimes) + "次" );
                }
            }catch (IOException e){
                try {
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    guiManager.showErrorDialog(ie.getMessage());
                }
            }catch (Exception e){
                    guiManager.showErrorDialog(e.getMessage());
            }
        }
        try{
            template = new byte[fingerManager.templateLen];
            fingerManager.fingerMerge(preRegTemplate, template);
            guiManager.showMessageDialog("指纹录入成功！");
        }catch (Exception e){
            template = null;
            guiManager.showErrorDialog(e.getMessage());
        }
    }
    public byte[] getTemplate(){
        return this.template;
    }
}
