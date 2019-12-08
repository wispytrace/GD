package GD.FingerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateCalendar extends JPanel {
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel dateDsiplay = new JLabel("", JLabel.CENTER);
    JButton[] button = new JButton[49];
    ArrayList checkDays = null;

    public DateCalendar(ArrayList dateList) {
        checkDays = dateList;
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

    private int[] dateParse(String date){
        int[] dateNum = new int[3];
        dateNum[0] = Integer.parseInt(date.substring(0, 4));
        dateNum[1] = Integer.parseInt(date.substring(5, 7)) - 1;
        dateNum[2] = Integer.parseInt(date.substring(8, 10));

        return dateNum;
    }

    private void displayDate() {
        for (int x = 7; x < button.length; x++) {
            button[x].setText("");
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "yyyy MMMM");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        for (int x = 0; x < button.length; x++){
            button[x].setBackground(Color.white);
        }
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
            button[x].setText("" + day);
        }
        for (int i=0; i<checkDays.size(); i++) {
            int[] dateNum =  dateParse(checkDays.get(i).toString());
            if ((dateNum[0] == year) && (dateNum[1] == month)) {
                button[6 + dayOfWeek + dateNum[2] - 1].setBackground(Color.red);
            }
        }
        dateDsiplay.setText(sdf.format(cal.getTime()));
    }
}
