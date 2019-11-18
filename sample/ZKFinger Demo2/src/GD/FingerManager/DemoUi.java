package GD.FingerManager;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
import com.zkteco.biometric.LightControl;
import com.zkteco.biometric.ZKFPService;

import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;

//待解决:优先级问题?
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.util.HashMap;

public class DemoUi extends JFrame {
    JButton btnOpen = null;
    JButton btnEnroll = null;
    JButton btnVerify = null;
    JButton btnIdentify = null;
    JButton btnRegImg = null;
    JButton btnIdentImg = null;
    JButton btnClose = null;
    JButton btnImg = null;
    private FingerManager fingerManger = new FingerManager();
    private DbManager dbManager = new DbManager();
    private DemoUi demoUi = this;
    private JTextArea textArea;
    private JTextArea personname;
    private JTextArea personid;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private WorkThread workThread = null;
    private EnrollThread enrollThread = null;
    private HashMap<Integer, String> Online = new HashMap<Integer, String>();

    public void launchFrame() {
        this.setLayout(null);
        btnOpen = new JButton("Open");
        this.add(btnOpen);
        int nRsize = 20;
        btnOpen.setBounds(30, 10 + nRsize, 100, 30);

        btnEnroll = new JButton("Enroll");
        this.add(btnEnroll);
        btnEnroll.setBounds(30, 60 + nRsize, 100, 30);

        btnIdentify = new JButton("Identify");
        this.add(btnIdentify);
        btnIdentify.setBounds(30, 110 + nRsize, 100, 30);


        btnClose = new JButton("Close");
        this.add(btnClose);
        btnClose.setBounds(30, 310 + nRsize, 100, 30);


        btnImg = new JButton();
        btnImg.setBounds(150, 5, 256, 300);
        btnImg.setDefaultCapable(false);
        this.add(btnImg);

        textArea = new JTextArea();
        this.add(textArea);
        textArea.setBounds(10, 440, 480, 100);

        personname = new JTextArea();
        this.add(personname);
        personname.setBounds(410, 20, 80, 40);
        personname.setText("your name");

        personid = new JTextArea();
        this.add(personid);
        personid.setBounds(410, 100, 80, 40);
        personid.setText("your id");

        this.setSize(520, 580);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("My Demo");
        this.setResizable(true);

        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    fingerManger.deviceInit();
                    dbManager.dbConnect("root", "");
                    textArea.setText("Init Successful");
                    messageWrite("log.txt", df.format(new Date()) + "\t\tDevice Init Successful \n");
                    workThread = new WorkThread();
                    workThread.start();
                }catch (Exception e){
                    errorMessage(e.getMessage());

                }
            }
        });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    fingerManger.deviceClose();
                    textArea.setText("Close Successful");
                    messageWrite("log.txt", df.format(new Date())  +"\t\t Device Close Successful \n");
                    if(workThread.isAlive()) {
                        workThread.stop();
                    }
                    if(enrollThread != null && enrollThread.isAlive() ) {
                        enrollThread.stop();
                    }
                }catch (Exception e){
                    errorMessage(e.getMessage());
                }
            }
        });
        btnEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (btnEnroll.getText()=="Enroll") {
                        if (workThread != null && workThread.isAlive()) {
                            workThread.stop();
                        }
                        btnEnroll.setText("Stop Enroll");
                        enrollThread = new EnrollThread(demoUi);
                        enrollThread.start();
                    }
                    else {
                        workThread = new WorkThread();
                        btnEnroll.setText("Enroll");
                        textArea.setText("Stop Enroll!\t\tContinue To Identity User");
                        workThread.start();
                        enrollThread.stop();
                    }
                }catch (Exception e){
                    errorMessage(e.getMessage());
                }
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                try {
                    fingerManger.deviceClose();
                }catch (Exception e_){

                }
            }
        });
    }


     public static void messageWrite(String path, String information) throws Exception{
         FileOutputStream log = new FileOutputStream(path, true);
         information = information + "\n";
         log.write(information.getBytes());
        log.close();
     }
     private void errorMessage(String message){
        this.textArea.setText("Error:\t"+message);
     }
     private void setMessage(String message){
        this.textArea.setText(message);
     }
    private class WorkThread extends Thread{
        @Override
        public void run(){
            super.run();
            byte[] img = new byte[fingerManger.figureHeight * fingerManger.figureWidth];
            byte[] template = new byte[fingerManger.templateLen];
            int[] fid = new int[1];
            while (true) {
                try{
                    fingerManger.figureAcquire(img, template);
                    fingerManger.fingerFileWrite(img, "temp.bmp");
                    btnImg.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
                    fingerManger.fingerIdentity(template, fid);
                    if (Online.containsKey(fid[0])){
                        fingerManger.lightControl("red");
                        textArea.setText("User "+ fid[0]+"\tLeave");
                        messageWrite("Check.txt", df.format(new Date()) + "\t\t\tuser"+fid[0] + " Come out");
                        Online.remove(fid[0]);
                    }
                    else {
                        fingerManger.lightControl("green");
                        textArea.setText("Ident successful, welcome user " + fid[0] + "!");
                        messageWrite("Check.txt", df.format(new Date()) + "\t\t\t" + fid[0] + "\tCome in");
                        Online.put(fid[0],"张三");
                    }
                } catch (IOException e){
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException et){
                        errorMessage(et.getMessage());
                    }
                }
                catch (Exception e) {
                    errorMessage(e.getMessage());
                }
            }
        }
    }
    private class EnrollThread extends Thread {
        private DemoUi demoUi;
        public EnrollThread(DemoUi demoUi){
            this.demoUi = demoUi;
        }
        public void run() {
            super.run();
            int[] fid = new int[1];
            byte[] img = new byte[fingerManger.figureHeight * fingerManger.figureWidth];
            byte[] template = new byte[fingerManger.templateLen];
            byte[][] preRegTemplate = new byte[fingerManger.CONFIRM_TIMES][fingerManger.templateLen];
            int[] regLen = {fingerManger.CONFIRM_TIMES};
            int record = 0;
            textArea.setText("Pleae press finger Sensor "+ fingerManger.CONFIRM_TIMES + " times!, green means valid");
            while (record < fingerManger.CONFIRM_TIMES) {
                try {
                    fingerManger.figureAcquire(img, preRegTemplate[record]);
                    fingerManger.fingerFileWrite(img, "temp.bmp");
                    btnImg.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
                    if (record >= 1){
                        fingerManger.fingerMatch(preRegTemplate[record-1], preRegTemplate[record]);
                    }
                    fingerManger.lightControl("green");
                    record++;
                    textArea.setText("Please press finger Sensor"+(3-record)+"times!");
                } catch (IOException e) {
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException ie){
                        errorMessage(e.getMessage());
                    }
                }catch (Exception e){
                    errorMessage(e.getMessage());
                }
            }
            try {
                fingerManger.fingerEnroll(fid, preRegTemplate, img);
                messageWrite("log.txt", df.format(new Date()) + "\t\tUser" + fid[0] + "Enroll Successful!");
                dbManager.dbInsert(Integer.parseInt(personid.getText()), personname.getText().getBytes(), null, null, 0, null, null, 0, null);
                textArea.setText("User" + fid[0] + "\tEnroll Successful!");
            }catch (Exception e){
                errorMessage(e.getMessage());
            }
            btnEnroll.doClick();
        }
    }
    public static void main(String[] args){
        new DemoUi().launchFrame();
    }
}
