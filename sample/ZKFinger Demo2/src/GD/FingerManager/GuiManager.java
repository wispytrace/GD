package GD.FingerManager;

import com.sun.crypto.provider.JceKeyStore;
import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.table.TableCellEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class GuiManager {
    private GuiManager guiManager = this;
    private static final int TOP_CONTAINER_HEIGHT = 960;
    private static final int TOP_CONTAINER_WIDTH = 1280;
    private JFrame topContainer = null;
    private JTabbedPane paneNavigation = null;
    private JPanel logoHeader = null;
    public  JPanel userPanel = null;
    public  JPanel statusPanel = null;
    public  JPanel detailResultPanel = null;
    public  JPanel resultStatis = null;
    public  JPanel personTable = null;
    public String userName = null;
    public int userPemission = 4;
    private DbManager dbManager = null;
    private FingerManager fingerManager = null;
    private WorkThread workThread = null;
    private EnrollThread enrollThread =null;
    private ComponentManger componentManger = null;


    public void GuiInit(){
        try {
        dbManager = new DbManager();
        fingerManager = new FingerManager();
        dbManager.dbConnect("root", "");
        fingerManager.deviceInit();
        componentManger = new ComponentManger(dbManager, this, fingerManager);
        componentManger.fingerLoad();
        workThread = new WorkThread();
        workThread.WorkThread(guiManager, fingerManager, dbManager, componentManger);
        workThread.start();
        this.createMainWindow();
        }catch (Exception e){
            showErrorDialog(e.getMessage());
            System.exit(-1);
        }
    }

    public void createMainWindow(){
        topContainer = new JFrame("USTC振动与控制科学实验室-考勤系统");
        GridBagLayout frameLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        topContainer.setLayout(frameLayout);
        topContainer.setSize(TOP_CONTAINER_WIDTH,TOP_CONTAINER_HEIGHT);
        topContainer.setLocationRelativeTo(null);
        topContainer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\UiResource\\Icon.jpg");
        topContainer.setIconImage(icon.getImage());

        logoHeader = createLogoHeader();
        paneNavigation = createPaneNavigation();

        format.weighty = 1;
        format.weightx = 1;
        format.fill = GridBagConstraints.BOTH;
        format.gridwidth = GridBagConstraints.REMAINDER;
        topContainer.add(logoHeader, format);
        topContainer.add(paneNavigation, format);
        topContainer.setVisible(true);
    }

    private JPanel createSeizePanel(){
        JPanel Panel = new JPanel();
        Panel.setOpaque(false);
        return Panel;
    }

    private JPanel createLogoHeader(){
        JPanel headerPanel = new JPanel();
        GridBagLayout headerLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        headerPanel.setLayout(headerLayout);

        userPanel = new JPanel();
        JButton login = new JButton("登陆");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createLoginDialog();
            }
        });
//        login.setContentAreaFilled(false);
        userPanel.add(login);

        format.fill = GridBagConstraints.BOTH;
        format.weightx = 10;
        format.weighty = 1;
        format.gridwidth = 10;
        headerPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        format.gridwidth = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        headerPanel.add(userPanel, format);
        format.weighty = 5;
        headerPanel.add(createSeizePanel(), format);
        return headerPanel;
    }

    public void createLoginDialog(){
        JDialog loginDialog = new JDialog(topContainer, "考勤系统登陆", true);
        loginDialog.setSize(TOP_CONTAINER_WIDTH/2, TOP_CONTAINER_HEIGHT/3);
        loginDialog.setResizable(false);
        ImageIcon icon = new ImageIcon("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\UiResource\\Icon.jpg");
        loginDialog.setIconImage(icon.getImage());
        JLabel name = new JLabel("姓名:");
        JLabel password = new JLabel("密码:");
        JTextField nameContent = new JTextField(15);
        JPasswordField passwordConten = new JPasswordField(15);
        JButton confirm = new JButton("确定");
        JButton cancel = new JButton("取消");

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    componentManger.login(nameContent, passwordConten, loginDialog);
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loginDialog.dispose();
            }
        });




        GridBagLayout loginLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        loginDialog.setLayout(loginLayout);


        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weighty = 0.5;
        loginDialog.add(createSeizePanel(), format);
        format.gridwidth = 1;
        format.weightx = 2;
        format.weighty = 1;
        loginDialog.add(createSeizePanel(), format);
        format.weightx = 1.5;
        loginDialog.add(name, format);
        loginDialog.add(nameContent,format);
        format.weightx = 2;
        format.gridwidth = GridBagConstraints.REMAINDER;
        loginDialog.add(createSeizePanel(), format);

        format.gridwidth = 1;
        format.weightx = 2;
        loginDialog.add(createSeizePanel(), format);
        format.weightx = 1;
        loginDialog.add(password, format);
        loginDialog.add(passwordConten,format);
        format.weightx = 2;
        format.gridwidth = GridBagConstraints.REMAINDER;
        loginDialog.add(createSeizePanel(), format);

        format.weightx = 2;
        format.gridwidth = 1;
        loginDialog.add(createSeizePanel(), format);
        format.weightx = 1;
        loginDialog.add(confirm, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        loginDialog.add(cancel,format);

        loginDialog.setLocationRelativeTo(null);
        loginDialog.setVisible(true);

    }

    private JTabbedPane createPaneNavigation(){
        JTabbedPane navigationPanel  = new JTabbedPane(JTabbedPane.LEFT);
        ImageIcon icon = new ImageIcon("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\UiResource\\Icon.jpg");
        navigationPanel.addTab("考勤状态", icon, ceeatePersonStatus(),"查看考勤状态信息");
        navigationPanel.addTab("考勤明细", icon, createAttenDetail(),"查看近期考勤信息");
        navigationPanel.addTab("考勤统计", icon, createAttenStastis(), "查看考勤统计信息");
        navigationPanel.addTab("人员管理", icon, createPersonMangager(), "管理人员录入与移出");
        navigationPanel.addTab("系统设置", icon, createSystemSet(), "更改系统设置");
        navigationPanel.setSelectedIndex(0);
        return navigationPanel;
    }

    private JPanel ceeatePersonStatus(){
        statusPanel = new JPanel(new BorderLayout());
        try {
            componentManger.statusDisplay(statusPanel);
        }catch (Exception e){
            showErrorDialog(e.getMessage());
        }
        return statusPanel;
    }

    private  JPanel createAttenDetail(){
        JPanel detailPanel = new JPanel();
        GridBagLayout layout = new  GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        detailPanel.setLayout(layout);

        JPanel searchPanel = createDetailSearch();
        format.weighty = 1;
        format.weightx = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.fill = GridBagConstraints.BOTH;
        layout.addLayoutComponent(searchPanel, format);

        JPanel resultPanel = createDetailResult();
        format.weightx = 1;
        format.weighty = 6;
        layout.addLayoutComponent(resultPanel, format);


        detailPanel.add(searchPanel);
        detailPanel.add(resultPanel);
        return detailPanel;
    }

    private  JPanel createDetailSearch(){
        JPanel searchPanel = new JPanel();
        GridBagLayout searchLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        searchPanel.setLayout(searchLayout);

        JLabel time = new JLabel("时间范围");
        JButton today = new JButton("今天");
        JButton yesterday = new JButton("昨天");
        JButton week = new JButton("本周");
        JButton month = new JButton("本月");
        JFrame clock = new JFrame();
        JTextField start = new JTextField(10);
        JLabel to = new JLabel("到");
        JTextField end = new JTextField(10);

        today.setBorderPainted(false);
        today.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 0);
            }
        });
        yesterday.setBorderPainted(false);
        yesterday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 1);

            }
        });
        week.setBorderPainted(false);
        week.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 2);
            }
        });
        month.setBorderPainted(false);
        month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 3);

            }
        });

        start.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                start.setText(new DatePicker(clock).setPickedDate());
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 4);
                start.setFocusable(false);
                start.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });
        end.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                end.setText(new DatePicker(clock).setPickedDate());
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 5);
                end.setFocusable(false);
                end.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });
        componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 0);
        //LayOut
        format.weighty = 1;
        format.weightx = 1;
        searchPanel.add(time, format);
        searchPanel.add(today, format);
        searchPanel.add(yesterday, format);
        searchPanel.add(week, format);
        searchPanel.add(month, format);
        searchPanel.add(start,format);
        format.weightx = 0.3;
        searchPanel.add(to, format);
        format.weightx = 1;
        searchPanel.add(end, format);
        format.weightx = 6;
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);


        JLabel status = new JLabel("状态");
        JButton normal = new JButton("正常");
        JButton abnormal = new JButton("异常");
        normal.setBorderPainted(false);
        normal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailStatusSelectButton(normal, abnormal, 0);
            }
        });
        abnormal.setBorderPainted(false);
        abnormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailStatusSelectButton(normal, abnormal, 1);
            }
        });
        componentManger.setDetailStatusSelectButton(normal, abnormal, 0);

        //LayOut
        format.weightx = 1;
        format.weighty = 1;
        format.gridwidth = 1;
        searchPanel.add(status, format);
        searchPanel.add(normal, format);
        searchPanel.add(abnormal, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);


        JLabel team = new JLabel("组别");
        try {
            String[] teamList = componentManger.getDetailGroupContent();
            JComboBox<String> teamSelect = new JComboBox<String>(teamList);

        teamSelect.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    componentManger.setDetailTeamChoose(teamSelect.getSelectedItem().toString());
                }
            }
        });
        format.gridwidth = 1;
        format.weightx = 1;
        searchPanel.add(team, format);
        format.gridwidth = 2;
        format.fill = GridBagConstraints.HORIZONTAL;
        searchPanel.add(teamSelect, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);

        JButton search = new JButton("搜索");
        JButton reset = new JButton("重置");

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    componentManger.doDetailSearch(detailResultPanel);
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setDetailTimeSelectButton(today, yesterday, week, month, start, end, 0);
                componentManger.setDetailStatusSelectButton(normal, abnormal, 0);
                componentManger.setDetailTeamChoose("全部");
                teamSelect.setSelectedIndex(0);
            }
        });
        format.gridwidth = 8;
        format.weightx = 1;
        format.weighty = 1.5;
        format.fill = GridBagConstraints.NONE;
        searchPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        searchPanel.add(search, format);
        searchPanel.add(reset, format);


        }catch (Exception e){
            showErrorDialog(e.getMessage());
        }

        return  searchPanel;
    }

    private JPanel createDetailResult(){
        detailResultPanel = new JPanel(new BorderLayout());
        try {
            componentManger.doDetailSearch(detailResultPanel);
        }catch (Exception e){
            showErrorDialog(e.getMessage());
        }
        return detailResultPanel;
    }

    private JPanel createAttenStastis(){
        JPanel statisPanel = new JPanel();
        GridBagLayout statisLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        statisPanel.setLayout(statisLayout);
        try{
        JLabel name = new JLabel("姓名:");
            String[] nameList = componentManger.getAllStaffName();
            JComboBox<String> nameSelect = new JComboBox<String>(nameList);

            nameSelect.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    // 只处理选中的状态
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        componentManger.setStasticNameChoose(nameSelect.getSelectedItem().toString());
                    }
                }
            });
        format.weightx = 0.2;
        format.weighty = 1;
        statisPanel.add(name, format);
        format.weightx = 1;
        statisPanel.add(nameSelect, format);
        format.weightx = 30;
        format.gridwidth = GridBagConstraints.REMAINDER;
        statisPanel.add(createSeizePanel(), format);


        JLabel time = new JLabel("时间范围:");
        JButton week = new JButton("今日");
        JFrame clock = new JFrame();
        JTextField start = new JTextField(10);
        JLabel to = new JLabel("到");
        JTextField end = new JTextField(10);
        JButton search = new JButton("搜索");
        JButton output  = new JButton("导出");
        JButton reset = new JButton("重置");

        week.setBorderPainted(false);

        week.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                componentManger.setStasticTimeChoose(week, start, end, 0);
            }
        });
        start.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                start.setText(new DatePicker(clock).setPickedDate());
                componentManger.setStasticTimeChoose(week, start, end, 1);
                start.setFocusable(false);
                start.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });
        end.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                end.setText(new DatePicker(clock).setPickedDate());
                componentManger.setStasticTimeChoose(week, start, end, 2);
                end.setFocusable(false);
                end.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nameSelect.setSelectedIndex(0);
                componentManger.setStasticNameChoose(nameSelect.getSelectedItem().toString());
                componentManger.setStasticTimeChoose(week, start, end, 0);
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    componentManger.doStasticSearch(resultStatis);
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });
        output.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    createOutputDialog();
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });

        nameSelect.setSelectedIndex(0);
        componentManger.setStasticNameChoose(nameSelect.getSelectedItem().toString());
        componentManger.setStasticTimeChoose(week, start, end, 0);


        format.gridwidth = 1;
        format.weightx = 0.2;
        statisPanel.add(time, format);
        format.weightx = 1;
        statisPanel.add(week, format);
        statisPanel.add(start, format);
        format.weightx = 0.1;
        statisPanel.add(to, format);
        format.weightx = 1;
        statisPanel.add(end, format);
        format.weightx = 30;
        format.gridwidth = GridBagConstraints.REMAINDER;
        statisPanel.add(createSeizePanel(), format);
        format.gridwidth = 10;
        format.weightx = 20;
        statisPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        statisPanel.add(search, format);
        statisPanel.add(reset, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        statisPanel.add(output, format);

//        resultStatis = createPersonalStatis(new String[]{"0", "0", "未达标", "0"});
        resultStatis = createAllStatis();
        format.weightx = 1;
        format.weighty = 12;
        format.fill = GridBagConstraints.BOTH;
        statisPanel.add(resultStatis, format);

        }catch (Exception e){
            showErrorDialog(e.getMessage());
        }
        return statisPanel;
    }

    private void createOutputDialog() throws Exception {
        JFileChooser fileChooser = new JFileChooser();

        // 设置打开文件选择框后默认输入的文件名
        fileChooser.setSelectedFile(new File("考勤明细.txt"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showSaveDialog(topContainer);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"保存", 则获取选择的保存路径
            File file = fileChooser.getSelectedFile();
            componentManger.doCheckFileOut(file);
            System.out.println("保存到文件: " + file.getAbsolutePath() + "\n\n");
        }
    }

    private JPanel createAllStatis(){
        // 表头（列名）
        String[] columnNames = {"时间段" ,"姓名", "考勤总时间/小时", "考勤总次数", "评价", "异常记录次数"};
        Object[][] rowData = {{"","","","","",""}};
        return createTable(columnNames, rowData, false);
    }

    public JPanel createPersonalStatis(String[] message, ArrayList dateList){
        JPanel resultStatis = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        resultStatis.setLayout(layout);
        JLabel totalTimeLabel = new JLabel("考勤总时间/小时:");
        JLabel totalTimesLabel = new JLabel("进出总次数:");
        JLabel commentLabel = new JLabel("评价:");
        JLabel abornomalLabel = new JLabel("异常记录次数");
        JLabel totalTime = new JLabel(message[0]);
        JLabel totalTimes = new JLabel(message[1]);
        JLabel comment = new JLabel(message[2]);
        JLabel abornomal = new JLabel(message[3]);
        format.weighty = 1;
        format.weightx = 0.5;
        resultStatis.add(totalTimeLabel, format);
        format.weightx = 1;
        resultStatis.add(totalTime, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(totalTimesLabel, format);
        format.weightx = 1;
        resultStatis.add(totalTimes, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(commentLabel, format);
        format.weightx = 1;
        resultStatis.add(comment, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(abornomalLabel, format);
        format.weightx = 1;
        resultStatis.add(abornomal, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);

        JPanel calendar = new DateCalendar(dateList);
        format.weightx = 1;
        format.weighty = 10;
        format.fill = GridBagConstraints.BOTH;
        resultStatis.add(calendar, format);
        format.weighty = 1;
        resultStatis.add(createSeizePanel(), format);
        return resultStatis;
    }



    public void createEnrollDialog(){
        JDialog enrollDialog = new JDialog(topContainer, "人员录入", true);
        enrollDialog.setSize(TOP_CONTAINER_WIDTH*3/5, TOP_CONTAINER_HEIGHT*3/5);
        enrollDialog.setResizable(false);
        GridBagLayout enrollLayout = new GridBagLayout();
        enrollDialog.setLayout(enrollLayout);
        GridBagConstraints format = new GridBagConstraints();
        ImageIcon icon = new ImageIcon("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\UiResource\\Icon.jpg");
        enrollDialog.setIconImage(icon.getImage());

        JLabel message = new JLabel("请输入下列相关信息,并输入三次指纹。含有*号的选项不可以为空,权限设置中(0-最高级, 1-管理级, 2-一般用户)");
        message.setFont(new Font(null, Font.BOLD, 14));
        format.weightx = 2;
        format.weighty = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        enrollDialog.add(message, format);

        JPanel imgPanel = new JPanel(new BorderLayout());
        JButton imgbtn = new JButton();
        imgbtn.setDefaultCapable(false);
        imgPanel.add(imgbtn);

        JPanel infPanel = new JPanel(new GridBagLayout());
        JLabel name = new JLabel("姓名*");
        JTextField nameContent = new JTextField(20);
        JLabel sno = new JLabel("学号*");
        JTextField snoContent = new JTextField(20);
        JLabel permission = new JLabel("权限设置*");
        JTextField permissionContent = new JTextField(20);
        JLabel tutor = new JLabel("导师");
        JTextField tutorContent = new JTextField(20);
        JLabel team = new JLabel("小组");
        JTextField teamContent = new JTextField(20);
        JLabel phone = new JLabel("手机号码");
        JTextField phoneContent = new JTextField(20);
        JLabel password = new JLabel("密码设置*");
        JPasswordField passwordContent = new JPasswordField(20);
        JLabel passwrodAgain = new JLabel("请再确认密码*");
        JPasswordField passwordAgainContent = new JPasswordField(20);
        quickLayout(infPanel, name, nameContent);
        quickLayout(infPanel, sno, snoContent);
        quickLayout(infPanel, permission, permissionContent);
        quickLayout(infPanel, tutor, tutorContent);
        quickLayout(infPanel, team, teamContent);
        quickLayout(infPanel, phone, phoneContent);
        quickLayout(infPanel, password, passwordContent);
        quickLayout(infPanel, passwrodAgain, passwordAgainContent);

        format.weighty = 2;
        format.gridwidth = 1;
        format.weightx = 1;
        format.fill = GridBagConstraints.BOTH;
        enrollDialog.add(infPanel, format);
        format.weightx = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        enrollDialog.add(imgPanel, format);

        JButton confirm = new JButton("确认");
        JButton cancel = new JButton("取消");

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    byte[] template = enrollThread.getTemplate();
                    componentManger.doPersonEnroll(new String[]{nameContent.getText(), snoContent.getText(), permissionContent.getText(),
                            tutorContent.getText(), teamContent.getText(), phoneContent.getText(), passwordContent.getText(), passwordAgainContent.getText()},template);
                    if ((enrollThread != null) && (enrollThread.isAlive())){
                        enrollThread.stop();
                        enrollThread = null;
                    }
                }catch (Exception e){
                    showMessageDialog(e.getMessage());
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ((enrollThread != null )&&(enrollThread.isAlive())) {
                    enrollThread.stop();
                    enrollThread = null;
                }
                workThread = new WorkThread();
                workThread.WorkThread(guiManager, fingerManager, dbManager, componentManger);
                workThread.start();
                enrollDialog.dispose();
            }
        });

        format.fill = GridBagConstraints.NONE;
        format.weighty = 1;
        format.gridwidth = 1;
        enrollDialog.add(confirm, format);
        enrollDialog.add(cancel, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        enrollDialog.add(createSeizePanel(), format);


        enrollDialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
                if ((workThread != null )&&(workThread.isAlive())) {
                    workThread.stop();
                }
                if ((enrollThread == null) || (!enrollThread.isAlive())){
                    enrollThread = new EnrollThread();
                    enrollThread.EnrollThread(guiManager, fingerManager, dbManager, imgbtn);
                    enrollThread.start();
                }
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if ((enrollThread != null )&&(enrollThread.isAlive())) {
                    enrollThread.stop();
                    enrollThread = null;
                }
                workThread = new WorkThread();
                workThread.WorkThread(guiManager, fingerManager, dbManager, componentManger);
                workThread.start();
                enrollDialog.dispose();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
        enrollDialog.setLocationRelativeTo(null);
        enrollDialog.setVisible(true);
    }

    private void quickLayout(JPanel infPanel ,JComponent label, JComponent content){
        GridBagConstraints format = new GridBagConstraints();
        format.weightx = 1;
        infPanel.add(label, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        infPanel.add(content, format);
        infPanel.add(createSeizePanel(), format);
    }

    private JPanel createPersonMangager(){
        JPanel perosonManger = new JPanel();
        GridBagLayout personLayout = new GridBagLayout();
        perosonManger.setLayout(personLayout);
        GridBagConstraints format = new GridBagConstraints();
        JButton enroll = new JButton("新增成员");
        enroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createEnrollDialog();
            }
        });

        format.fill = GridBagConstraints.BOTH;
        format.weightx = 1;
        format.weighty = 4;
        format.gridwidth = GridBagConstraints.REMAINDER;
        perosonManger.add(enroll, format);
        format.weighty = 10;
        try {
            personTable = new JPanel(new BorderLayout());
            componentManger.createPersonTable(personTable);
            perosonManger.add(personTable, format);
        }catch (Exception e){
            showErrorDialog(e.getMessage());
        }
        return perosonManger;
    }

    public void createModifyDialog(String id){
        JDialog modyfyDialog = new JDialog(topContainer, "人员信息修改", true);
        modyfyDialog.setSize(TOP_CONTAINER_WIDTH*3/5, TOP_CONTAINER_HEIGHT*3/5);
        modyfyDialog.setResizable(false);
        GridBagLayout enrollLayout = new GridBagLayout();
        modyfyDialog.setLayout(enrollLayout);
        GridBagConstraints format = new GridBagConstraints();
        ImageIcon icon = new ImageIcon("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\UiResource\\Icon.jpg");
        modyfyDialog.setIconImage(icon.getImage());

        JLabel message = new JLabel("请修改下列的相关信息");
        message.setFont(new Font(null, Font.BOLD, 14));
        format.weightx = 2;
        format.weighty = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        modyfyDialog.add(message, format);

        JPanel infPanel = new JPanel(new GridBagLayout());
        JLabel name = new JLabel("姓名*");
        JTextField nameContent = new JTextField(20);
        JLabel sno = new JLabel("学号*");
        JTextField snoContent = new JTextField(20);
        JLabel permission = new JLabel("权限设置*");
        JTextField permissionContent = new JTextField(20);
        JLabel tutor = new JLabel("导师");
        JTextField tutorContent = new JTextField(20);
        JLabel team = new JLabel("小组");
        JTextField teamContent = new JTextField(20);
        JLabel phone = new JLabel("手机号码");
        JTextField phoneContent = new JTextField(20);
        JLabel password = new JLabel("密码设置*");
        JPasswordField passwordContent = new JPasswordField(20);
        JLabel passwrodAgain = new JLabel("请再确认密码*");
        JPasswordField passwordAgainContent = new JPasswordField(20);
        quickLayout(infPanel, name, nameContent);
        quickLayout(infPanel, sno, snoContent);
        quickLayout(infPanel, permission, permissionContent);
        quickLayout(infPanel, tutor, tutorContent);
        quickLayout(infPanel, team, teamContent);
        quickLayout(infPanel, phone, phoneContent);
        quickLayout(infPanel, password, passwordContent);
        quickLayout(infPanel, passwrodAgain, passwordAgainContent);

        format.weighty = 2;
        format.gridwidth = 1;
        format.weightx = 1;
        format.fill = GridBagConstraints.BOTH;
        format.gridwidth = GridBagConstraints.REMAINDER;
        modyfyDialog.add(infPanel, format);
        format.weightx = 1;

        JButton confirm = new JButton("确认");
        JButton cancel = new JButton("取消");
        JButton delete = new JButton("删除");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    componentManger.doPersonModyify(new String[]{nameContent.getText(), snoContent.getText(), permissionContent.getText(),
                            tutorContent.getText(), teamContent.getText(), phoneContent.getText(), passwordContent.getText(), passwordAgainContent.getText()},id);
                }catch (Exception e){
                    showMessageDialog(e.getMessage());
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                modyfyDialog.dispose();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    componentManger.deletePersonInfo(id);
                }catch (Exception e){
                    guiManager.showErrorDialog(e.getMessage());
                }
            }
        });
        format.fill = GridBagConstraints.NONE;
        format.weighty = 1;
        format.gridwidth = 1;
        modyfyDialog.add(confirm, format);
        modyfyDialog.add(cancel, format);
        modyfyDialog.add(delete, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        modyfyDialog.add(createSeizePanel(), format);

        modyfyDialog.setLocationRelativeTo(null);
        modyfyDialog.setVisible(true);
    }

    private JPanel createSystemSet(){
        JPanel sysPanel = new JPanel();
        GridBagLayout sysLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        sysPanel.setLayout(sysLayout);
        JLabel startTime = new JLabel("打卡起始时间");
        String[] timeList = new String[]{"6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00",
        "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00",
        "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "24:00"};
        JComboBox<String> start = new JComboBox<String>(timeList);
        JLabel endTime = new JLabel("打卡终止时间");
        JComboBox<String> end = new JComboBox<String>(timeList);
        start.setSelectedIndex(5);
        end.setSelectedIndex(35);
        format.weightx = 1;
        format.weighty = 1;
        sysPanel.add(startTime, format);
        sysPanel.add(start, format);
        format.weightx = 8;
        format.gridwidth = GridBagConstraints.REMAINDER;
        sysPanel.add(createSeizePanel(), format);

        format.weightx = 1;
        format.weighty = 1;
        format.gridwidth = 1;
        sysPanel.add(endTime, format);
        sysPanel.add(end, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        sysPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        format.gridwidth = 2;
        JButton save = new JButton("保存");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if ((end.getSelectedIndex() - start.getSelectedIndex()) <= 0){
                        throw new Exception("请选择正确的时间范围");
                    }
                    componentManger.setCheckTime(start.getSelectedItem().toString(), end.getSelectedItem().toString());
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });
        sysPanel.add(save, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        sysPanel.add(createSeizePanel(), format);


        JLabel group = new JLabel("小组设置");
        JTextField groupContent = new JTextField(20);
        format.weightx = 1;
        format.gridwidth = 1;
        sysPanel.add(group, format);
        sysPanel.add(groupContent, format);
        format.weightx = 8;
        format.gridwidth = GridBagConstraints.REMAINDER;
        sysPanel.add(createSeizePanel(), format);

        JButton delete = new JButton("删除");
        JButton add = new JButton("增加");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    componentManger.addTeam(groupContent.getText());
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    componentManger.deleteTeam(groupContent.getText());
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });

        format.weightx = 1;
        format.gridwidth = 1;
        sysPanel.add(add, format);
        sysPanel.add(delete, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        sysPanel.add(createSeizePanel(), format);

        format.weighty = 10;
        sysPanel.add(createSeizePanel(), format);



        return sysPanel;
    }

    public void showErrorDialog(String message){
        JOptionPane.showMessageDialog(
                topContainer,
                message,
                "错误提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void showMessageDialog(String message){
        JOptionPane.showMessageDialog(
                topContainer,
                message,
                "消息提示",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public JPanel createTable(String[] columnNames, Object[][] rowData, boolean isOperation){
        JPanel tableContainer = new JPanel(new BorderLayout());
//        GridBagLayout tableLayout = new GridBagLayout();
//        GridBagConstraints format = new GridBagConstraints();
//        tableContainer.setLayout(tableLayout);


        // 表格所有行数据

        // 创建一个表格，指定 表头 和 所有行数据
        JTable table = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rowData.length;
            }

            @Override
            public int getColumnCount() {
                return rowData[0].length;
            }
            @Override
            public String getColumnName(int column) {
                return columnNames[column].toString();
            }
            @Override
            public Object getValueAt(int i, int i1) {
                return rowData[i][i1];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if ((columnIndex == 6) && (isOperation == true)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });
//        table.getColumnModel().getColumn(6).setCellEditor();
        // 设置表格内容颜色
        table.setForeground(Color.BLACK);                   // 字体颜色
        table.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        table.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        table.setGridColor(Color.GRAY);                     // 网格颜色

        // 设置表头
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        table.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

        // 设置行高
        table.setRowHeight(40);

        // 第一列列宽设置为40
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        MyTableCellRenderer renderer = new MyTableCellRenderer(isOperation);
        for (int i =0; i < columnNames.length; i++ ){
            TableColumn tableColumn = table.getColumn(columnNames[i]);
            // 设置 表格列 的 单元格渲染器
            tableColumn.setCellRenderer(renderer);
        }
        if (isOperation){
            table.getColumnModel().getColumn(6).setCellEditor(new MyButtonEditor(guiManager));
        }        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        JScrollPane scrollPane = new JScrollPane(table);

        tableContainer.add(scrollPane);
        return tableContainer;
    }
    private static class MyTableCellRenderer extends DefaultTableCellRenderer {
        /**
         * 返回默认的表单元格渲染器，此方法在父类中已实现，直接调用父类方法返回，在返回前做相关参数的设置即可
         */
        private boolean isOperation = false;
        public MyTableCellRenderer(boolean isOperation){
            this.isOperation = isOperation;
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // 偶数行背景设置为白色，奇数行背景设置为灰色
            if (row % 2 == 0) {
                setBackground(Color.WHITE);
            } else {
                setBackground(Color.LIGHT_GRAY);
            }

            // 第一列的内容水平居中对齐，最后一列的内容水平右对齐，其他列的内容水平左对齐
            if (column == 0) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == (table.getColumnCount() - 1)) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            // 设置提示文本，当鼠标移动到当前(row, column)所在单元格时显示的提示文本
//            setToolTipText("提示的内容: " + row + ", " + column);

            // PS: 多个单元格使用同一渲染器时，需要自定义的属性，必须每次都设置，否则将自动沿用上一次的设置。

            /*
             * 单元格渲染器为表格单元格提供具体的显示，实现了单元格渲染器的 DefaultTableCellRenderer 继承自
             * 一个标准的组件类 JLabel，因此 JLabel 中相应的 API 在该渲染器实现类中都可以使用。
             *
             * super.getTableCellRendererComponent(...) 返回的实际上是当前对象（this），即 JLabel 实例，
             * 也就是以 JLabel 的形式显示单元格。
             *
             * 如果需要自定义单元格的显示形式（比如显示成按钮、复选框、内嵌表格等），可以在此自己创建一个标准组件
             * 实例返回。
             */

            // 调用父类的该方法完成渲染器的其他设置
            if ((column == 6)&&isOperation){
                JButton update = new JButton("修改");
                return update;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    private static class MyButtonEditor extends AbstractCellEditor implements TableCellEditor {
        /**
         * serialVersionUID
         */
//        private static final long serialVersionUID = -6546334664166791132L;
        private JPanel panel;
        private JButton button;
        private String  id;
        private GuiManager guiManager = null;
        public MyButtonEditor(GuiManager guiManager) {
            initButton();
            initPanel();
            panel.add(this.button, BorderLayout.CENTER);
            this.guiManager = guiManager;
        }

        private void initButton() {
            button = new JButton("修改");

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    //stopped!!!!
                    guiManager.createModifyDialog(id);
                    fireEditingStopped();
                }
            });
        }

        private void initPanel() {
            panel = new JPanel();

            panel.setLayout(new BorderLayout());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            id = value.toString();
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return id;
        }
    }





    public static void main(String args[]){
        try {
            GuiManager test = new GuiManager();
            test.GuiInit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
