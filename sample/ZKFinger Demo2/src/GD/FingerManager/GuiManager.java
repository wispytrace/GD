package GD.FingerManager;

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
import java.util.Date;

public class GuiManager {
    private static final int TOP_CONTAINER_HEIGHT = 960;
    private static final int TOP_CONTAINER_WIDTH = 1280;
    private static final int WIDITH_FIX = 18;
    private static final double DIVIDE_RATIO = 0.2;
    private static JFrame topContainer = null;
    private static JTabbedPane paneNavigation = null;
    private static JPanel logoHeader = null;
    public static JButton login = null;
    private static JScrollPane statusScroll = null;
    private static JPanel statusContainer = null;

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

        JButton login = new JButton("登陆");
        login.setContentAreaFilled(false);

        format.fill = GridBagConstraints.BOTH;
        format.weightx = 10;
        format.weighty = 1;
        format.gridwidth = 10;
        headerPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        format.gridwidth = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        headerPanel.add(login, format);
        format.weighty = 5;
        headerPanel.add(createSeizePanel(), format);
        return headerPanel;
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
        JPanel statusPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(8, 8));
        for (int i=0; i<64; i++) {
            gridPanel.add(new JButton("欧阳炳濠  在线"));
        }
        statusPanel.add(gridPanel);
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
//        searchPanel.setBackground(Color.red);
        layout.addLayoutComponent(searchPanel, format);

        JPanel resultPanel = createDetailResult();
//        resultPanel.setBackground(Color.YELLOW);
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
        JPanel Seize1 = new JPanel();


        today.setBorderPainted(false);
        today.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                today.setBackground(Color.gray);
                yesterday.setBackground(Color.white);
                week.setBackground(Color.white);
                month.setBackground(Color.white);
            }
        });
        yesterday.setBorderPainted(false);
        yesterday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                today.setBackground(Color.white);
                yesterday.setBackground(Color.gray);
                week.setBackground(Color.white);
                month.setBackground(Color.white);
            }
        });
        week.setBorderPainted(false);
        week.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                today.setBackground(Color.white);
                yesterday.setBackground(Color.white);
                week.setBackground(Color.gray);
                month.setBackground(Color.white);
            }
        });
        month.setBorderPainted(false);
        month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                today.setBackground(Color.white);
                yesterday.setBackground(Color.white);
                week.setBackground(Color.white);
                month.setBackground(Color.gray);
            }
        });

        Seize1.setOpaque(false);

        start.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                today.setBackground(Color.white);
                yesterday.setBackground(Color.white);
                week.setBackground(Color.white);
                month.setBackground(Color.white);
                start.setText(new DatePicker(clock).setPickedDate());
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
                today.setBackground(Color.white);
                yesterday.setBackground(Color.white);
                week.setBackground(Color.white);
                month.setBackground(Color.white);
                end.setText(new DatePicker(clock).setPickedDate());
                end.setFocusable(false);
                end.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });

        format.weighty = 1;
        format.weightx = 1;
        searchLayout.addLayoutComponent(time, format);
        searchLayout.addLayoutComponent(today, format);
        searchLayout.addLayoutComponent(yesterday, format);
        searchLayout.addLayoutComponent(week, format);
        searchLayout.addLayoutComponent(month, format);
        searchLayout.addLayoutComponent(start,format);
        searchLayout.addLayoutComponent(end, format);
        format.weightx = 0.3;
        searchLayout.addLayoutComponent(to, format);
        format.weightx = 6;
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchLayout.addLayoutComponent(Seize1, format);
        searchPanel.add(time);
        searchPanel.add(today);
        searchPanel.add(yesterday);
        searchPanel.add(week);
        searchPanel.add(month);
        searchPanel.add(start);
        searchPanel.add(to);
        searchPanel.add(end);
        searchPanel.add(Seize1);


        JLabel status = new JLabel("状态");
        JButton normal = new JButton("正常");
        JButton abnormal = new JButton("异常");
        JPanel Seize2 = new JPanel();
        Seize2.setOpaque(false);
        normal.setBorderPainted(false);
        normal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                normal.setBackground(Color.gray);
                abnormal.setBackground(Color.white);
            }
        });
        abnormal.setBorderPainted(false);
        abnormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                abnormal.setBackground(Color.gray);
                normal.setBackground(Color.white);
            }
        });
        format.weightx = 1;
        format.weighty = 1;
        format.gridwidth = 1;
        searchLayout.addLayoutComponent(status, format);
        searchLayout.addLayoutComponent(normal, format);
        searchLayout.addLayoutComponent(abnormal, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchLayout.addLayoutComponent(Seize2, format);
        searchPanel.add(status);
        searchPanel.add(normal);
        searchPanel.add(abnormal);
        searchPanel.add(Seize2);


        JLabel team = new JLabel("组别");
        JPanel Seize3 = new JPanel();
        Seize3.setOpaque(false);
        String[] teamList = new String[]{"全部", "电机组", "人工智能组", "分数阶控制组"};
        JComboBox<String> teamSelect = new JComboBox<String>(teamList);
//        teamSelect.addItemListener(new ItemListener(){
//            public void itemStateChanged(ItemEvent e) {
//                // 只处理选中的状态
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    System.out.println("选中: " + teamSelect.getSelectedIndex() + " = " + comboBox.getSelectedItem());
//                }
//            }
//        });
        format.gridwidth = 1;
        format.weightx = 1;
        searchLayout.addLayoutComponent(team, format);
        format.gridwidth = 2;
        format.fill = GridBagConstraints.HORIZONTAL;
        searchLayout.addLayoutComponent(teamSelect, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchLayout.addLayoutComponent(Seize3, format);
        searchPanel.add(team);
        searchPanel.add(teamSelect);
        searchPanel.add(Seize3);

        JButton search = new JButton("搜索");
        JButton reset = new JButton("重置");
        JPanel Seize4 = new JPanel();
        Seize4.setOpaque(false);
        format.gridwidth = 8;
        format.weightx = 1;
        format.weighty = 1.5;
        format.fill = GridBagConstraints.NONE;
        searchLayout.addLayoutComponent(Seize4, format);
        format.weightx = 1;
        searchLayout.addLayoutComponent(search, format);
        searchLayout.addLayoutComponent(reset, format);
        searchPanel.add(Seize4);
        searchPanel.add(search);
        searchPanel.add(reset);

        return  searchPanel;
    }

    private JPanel createDetailResult(){
        String[] columnsName = new String[]{"日期", "姓名", "上线时间", "下线时间", "状态", "小组"};
        Object[][] rowData = {
                {1, "张三", 80, 80, 80, 240},
                {2, "John", 70, 80, 90, 240},
                {3, "Sue", 70, 70, 70, 210},
                {4, "Jane", 80, 70, 60, 210},
                {5, "Joe_05", 80, 70, 60, 210},
                {6, "Joe_06", 80, 70, 60, 210},
                {7, "Joe_07", 80, 70, 60, 210},
                {8, "Joe_08", 80, 70, 60, 210},
                {9, "Joe_09", 80, 70, 60, 210},
                {10, "Joe_10", 80, 70, 60, 210},
                {11, "Joe_11", 80, 70, 60, 210},
                {12, "Joe_12", 80, 70, 60, 210},
                {13, "Joe_13", 80, 70, 60, 210},
                {14, "Joe_14", 80, 70, 60, 210},
                {15, "Joe_15", 80, 70, 60, 210},
                {16, "Joe_16", 80, 70, 60, 210},
                {17, "Joe_17", 80, 70, 60, 210},
                {18, "Joe_18", 80, 70, 60, 210},
                {19, "Joe_19", 80, 70, 60, 210},
                {20, "Joe_20", 80, 70, 60, 210},
                {17, "Joe_17", 80, 70, 60, 210},
                {18, "Joe_18", 80, 70, 60, 210},
                {19, "Joe_19", 80, 70, 60, 210},
                {20, "Joe_20", 80, 70, 60, 210}
        };
        JPanel resultPanel = createTable(columnsName, rowData);
        return resultPanel;
    }

    private JPanel createAttenStastis(){
        JPanel statisPanel = new JPanel();
        GridBagLayout statisLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        statisPanel.setLayout(statisLayout);

        JLabel name = new JLabel("姓名:");
        JTextField nameContent = new JTextField(10);
        format.weightx = 0.2;
        format.weighty = 1;
        statisPanel.add(name, format);
        format.weightx = 1;
        statisPanel.add(nameContent, format);
        format.weightx = 30;
        format.gridwidth = GridBagConstraints.REMAINDER;
        statisPanel.add(createSeizePanel(), format);

        JLabel time = new JLabel("时间范围:");
        JButton week = new JButton("上一周");
        JFrame clock = new JFrame();
        JTextField start = new JTextField(10);
        JLabel to = new JLabel("到");
        JTextField end = new JTextField(10);
        JButton search = new JButton("搜索");
        JButton output  = new JButton("导出");
        JButton reset = new JButton("重置");

        start.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                week.setBackground(Color.white);

                start.setText(new DatePicker(clock).setPickedDate());
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

                week.setBackground(Color.white);
                end.setText(new DatePicker(clock).setPickedDate());
                end.setFocusable(false);
                end.setFocusable(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });
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

//        JPanel resultStatis = createPersonalStatis(new String[]{"0", "0", "未达标", "0"});
        JPanel resultStatis = createAllStatis();
        format.weightx = 1;
        format.weighty = 12;
//        format.gridheight = 15;
        format.fill = GridBagConstraints.BOTH;
        statisPanel.add(resultStatis, format);


        return statisPanel;
    }

    private JPanel createAllStatis(){
        // 表头（列名）
        String[] columnNames = {"时间段" ,"姓名", "考勤总时间", "考勤总次数", "评价", "异常记录次数"};
        Object[][] rowData = {
                {1, "张三", 80, 80, 80, 240},
                {2, "John", 70, 80, 90, 240},
                {3, "Sue", 70, 70, 70, 210},
                {4, "Jane", 80, 70, 60, 210},
                {5, "Joe_05", 80, 70, 60, 210},
                {6, "Joe_06", 80, 70, 60, 210},
                {7, "Joe_07", 80, 70, 60, 210},
                {8, "Joe_08", 80, 70, 60, 210},
                {9, "Joe_09", 80, 70, 60, 210},
                {10, "Joe_10", 80, 70, 60, 210},
                {11, "Joe_11", 80, 70, 60, 210},
                {12, "Joe_12", 80, 70, 60, 210},
                {13, "Joe_13", 80, 70, 60, 210},
                {14, "Joe_14", 80, 70, 60, 210},
                {15, "Joe_15", 80, 70, 60, 210},
                {16, "Joe_16", 80, 70, 60, 210},
                {17, "Joe_17", 80, 70, 60, 210},
                {18, "Joe_18", 80, 70, 60, 210},
                {19, "Joe_19", 80, 70, 60, 210},
                {20, "Joe_20", 80, 70, 60, 210},
                {17, "Joe_17", 80, 70, 60, 210},
                {18, "Joe_18", 80, 70, 60, 210},
                {19, "Joe_19", 80, 70, 60, 210},
                {20, "Joe_20", 80, 70, 60, 210}
        };

        return createTable(columnNames, rowData);
    }

    private JPanel createPersonalStatis(String[] message){
        JPanel resultStatis = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        resultStatis.setLayout(layout);
//        resultStatis.setBackground(Color.blue);
        JLabel totalTimeLabel = new JLabel("考勤总时间:");
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

        JPanel calendar = new DateCalendar();
        format.weightx = 1;
        format.weighty = 10;
        format.fill = GridBagConstraints.BOTH;
        resultStatis.add(calendar, format);
        format.weighty = 1;
        resultStatis.add(createSeizePanel(), format);
        return resultStatis;
    }

    private JPanel createPersonMangager(){
        JPanel perosonManger = new JPanel();
        GridBagLayout personLayout = new GridBagLayout();
        perosonManger.setLayout(personLayout);
        GridBagConstraints format = new GridBagConstraints();
        JButton enroll = new JButton("新增成员");
        format.fill = GridBagConstraints.BOTH;
        format.weightx = 1;
        format.weighty = 4;
        format.gridwidth = GridBagConstraints.REMAINDER;
        perosonManger.add(enroll, format);
        format.weighty = 10;
        Object[][] rowData = {
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230},
                {1, "张三", 80, 80, 80, 240,230}
        };
        JPanel personTable = createTable(new String[]{"编号", "姓名", "小组", "导师", "学号", "手机号码", "操作"}, rowData);
        perosonManger.add(personTable, format);
        return perosonManger;
    }

    private JPanel createSystemSet(){
        JPanel sysPanel = new JPanel();
        GridBagLayout sysLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        sysPanel.setLayout(sysLayout);

        JLabel startTime = new JLabel("打卡起始时间");
        JTextField start = new JTextField(10);
        JLabel endTime = new JLabel("打卡终止时间");
        JTextField end = new JTextField(10);
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

        JButton save = new JButton("保存");
        sysPanel.add(save, format);
        format.weighty = 10;
        sysPanel.add(createSeizePanel(), format);




        return sysPanel;
    }

    private JPanel createTable(String[] columnNames, Object[][] rowData){
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
                return columnIndex == 6;
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
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        MyTableCellRenderer renderer = new MyTableCellRenderer();
        for (int i =0; i < columnNames.length; i++ ){
            TableColumn tableColumn = table.getColumn(columnNames[i]);
            // 设置 表格列 的 单元格渲染器
            tableColumn.setCellRenderer(renderer);
        }
        if (columnNames.length>=7){
            table.getColumnModel().getColumn(6).setCellEditor(new MyButtonEditor());
        }        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
//        table.setPreferredScrollableViewportSize(new Dimension(400, 300));
        JScrollPane scrollPane = new JScrollPane(table);
//        format.weightx = 1;
//        format.weighty = 10;
//        format.gridwidth = GridBagConstraints.REMAINDER;
//        format.fill = GridBagConstraints.BOTH;
        tableContainer.add(scrollPane);
//        format.weighty = 2;
//        tableContainer.add(createSeizePanel(), format);
        return tableContainer;
    }
    private static class MyTableCellRenderer extends DefaultTableCellRenderer {
        /**
         * 返回默认的表单元格渲染器，此方法在父类中已实现，直接调用父类方法返回，在返回前做相关参数的设置即可
         */
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
            setToolTipText("提示的内容: " + row + ", " + column);

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
            if (column == 6){
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
        private static final long serialVersionUID = -6546334664166791132L;

        private JPanel panel;

        private JButton button;

        private int num;

        public MyButtonEditor() {
            initButton();
            initPanel();
            panel.add(this.button, BorderLayout.CENTER);
        }

        private void initButton() {
            button = new JButton();

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int res = JOptionPane.showConfirmDialog(null,
                            "Do you want to add 1 to it?", "choose one",
                            JOptionPane.YES_NO_OPTION);

                    if(res ==  JOptionPane.YES_OPTION){
                        num++;
                    }
                    //stopped!!!!
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
            num = (Integer) value;

            button.setText(value == null ? "" : String.valueOf(value));

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return num;
        }
    }







    public static void main(String args[]){
        try {
            GuiManager test = new GuiManager();
            test.createMainWindow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
