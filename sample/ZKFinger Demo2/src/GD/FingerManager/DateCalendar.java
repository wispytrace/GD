package GD.FingerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

public class DateCalendar extends JPanel {
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel dateDsiplay = new JLabel("", JLabel.CENTER);
    JButton[] button = new JButton[49];
    String[] checkDays = new String[]{"2019-11-24", "2019-11-23", "2019-11-21"};

    public DateCalendar() {
        String[] header = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        JPanel p1 = new JPanel(new GridLayout(7, 7));

        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.white);
            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setForeground(Color.red);
            }
            p1.add(button[x]);
        }
        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<< 前一月");
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month--;
                if (month <= 0){
                    month = 12;
                    year--;
                }
                displayDate();
            }
        });
        p2.add(previous);
        JButton next = new JButton("后一月 >>");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month++;
                if (month > 12){
                    month = 1;
                    year++;
                }
                displayDate();
            }
        });
        p2.add(dateDsiplay);
        p2.add(next);
        this.setLayout(new BorderLayout());
        this.add(p1, BorderLayout.CENTER);
        this.add(p2, BorderLayout.NORTH);
        displayDate();
        this.setVisible(true);
    }
    public void parseDate(){
        try {
            DateFormat myFormat = new java.text.SimpleDateFormat("yyyy-MM-MM");
            Date mydate = myFormat.parse(checkDays[0]);
            int myMonth = mydate.getMonth();
            int myDay = mydate.getDay();
            int myYear = mydate.getYear();
        }catch (Exception e){}

    }
    public void displayDate() {
        for (int x = 7; x < button.length; x++)
            button[x].setText("");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "yyyy MMMM");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);


        int myMonth = Integer.parseInt(checkDays[0].substring(5, 7)) - 1;
        int myDay = Integer.parseInt(checkDays[0].substring(8, 10));
        int myYear = Integer.parseInt(checkDays[1].substring(0, 4));



        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
            button[x].setText("" + day);
            button[x].setBackground(Color.white);
        }
        if((myYear == year) && (myMonth == month)){
            button[6+dayOfWeek+myDay-1].setBackground(Color.red);
        }
        dateDsiplay.setText(sdf.format(cal.getTime()));
    }
}
