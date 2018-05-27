import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.Timer;

class ClockPanel extends JPanel implements ActionListener{
    private int minutes, hours, seconds;
    private javax.swing.Timer timer;
    private String today;
    private Calendar calendar;
    private SimpleDateFormat formatter;
    private Formatter fmt;
    private Date currentDate;

    public void startClock(){
        formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy", Locale.getDefault());
        currentDate = new Date();

        formatter.applyPattern("s");
        try{
            seconds = Integer.parseInt(formatter.format(currentDate));
        } catch (NumberFormatException n){
            seconds = 25;
        }
        formatter.applyPattern("m");
        try{
            minutes = Integer.parseInt(formatter.format(currentDate));
        } catch (NumberFormatException n){
            minutes = 10;
        }
        formatter.applyPattern("h");
        try{
            hours = Integer.parseInt(formatter.format(currentDate));
        } catch (NumberFormatException n){
            hours = 21;
        }

        timer = new Timer(1000, this);
        timer.start();
    }

    public String getFullDate(){
        fmt = new Formatter();
        calendar = Calendar.getInstance();
        return fmt.format("%1$tA %1$td %1$tB %1$tY",calendar).toString();
    }
    public String twelveHourFormat(){
        fmt = new Formatter();
        calendar = Calendar.getInstance();
        today = fmt.format("%tr",calendar).toString();
        return today;
    }
    public String twentyFourthHourFormat(){
        fmt = new Formatter();
        calendar = Calendar.getInstance();
        today = fmt.format("%tT",calendar).toString();
        return today;
    }
    public void actionPerformed(ActionEvent e){
        repaint();
        seconds++;
        if(seconds == 60){
            seconds = 0;
            minutes++;
            if(minutes == 60){
                minutes = 0;
                hours++;
            }
            if(hours == 13){
                hours = 1;
            }
        }
    }

   static class DigitalClock extends JPanel {
    private JLabel labelDigitTime;
    private JRadioButton rbTwelveHourFormat;
    private JRadioButton rbTwentyFourthHourFormat;
    private ButtonGroup choiceFormatRadioGroup;
    private String time;
    private ClockPanel panelClock;
    public DigitalClock(){
        setBackground(Color.cyan);
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.blue, 1),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        labelDigitTime = new JLabel();
        choiceFormatRadioGroup = new ButtonGroup();
        rbTwelveHourFormat = new JRadioButton("12-часовой формат");
        choiceFormatRadioGroup.add(rbTwelveHourFormat);
        rbTwelveHourFormat.setBackground(Color.cyan);
        rbTwelveHourFormat.setFocusable(false);
        rbTwentyFourthHourFormat = new JRadioButton("24-часовой формат");
        rbTwentyFourthHourFormat.setSelected(true);
        choiceFormatRadioGroup.add(rbTwentyFourthHourFormat);
        rbTwentyFourthHourFormat.setBackground(Color.cyan);
        rbTwentyFourthHourFormat.setFocusable(false);

        new Thread(){
            public void run(){
                for(;;){
                    try{
                        repaint();
                        panelClock = new ClockPanel();
                        if(rbTwelveHourFormat.isSelected()){
                            time = panelClock.twelveHourFormat();
                        }
                        else{
                            time = panelClock.twentyFourthHourFormat();
                        }
                        labelDigitTime.setText(time);
                        Thread.sleep(500);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
        labelDigitTime.setFont(new Font("Arial", Font.BOLD, 24));
        labelDigitTime.setForeground(Color.white);
        add(labelDigitTime);
        add(rbTwelveHourFormat);
        add(rbTwentyFourthHourFormat);
    }
}
static class ChoiceSignal extends JPanel{
    private Box choice;
    private JPanel left;
    private JPanel right;
    private JPanel south;
    private ButtonGroup choiceRadioGroup;
    private JRadioButton simpleSignal;
    private JCheckBox activeTimer;
    private JSpinnerTimerPanel spinnerTime;
    private ClockPanel panelClock;

    public ChoiceSignal(){
        setLayout(new BorderLayout());
        setBackground(Color.cyan);
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.blue, 1),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        left = new JPanel();
        left.setBackground(Color.cyan);
        left.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.blue, 1),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        choice = Box.createVerticalBox();
        choiceRadioGroup = new ButtonGroup();
        simpleSignal = new JRadioButton("Звуковой сигнал");
        simpleSignal.setSelected(true);
        simpleSignal.setBackground(Color.cyan);
        simpleSignal.setFocusable(false);

        choiceRadioGroup.add(simpleSignal);
        choice.add(simpleSignal);
        left.add(choice);

        south = new JPanel();
        south.setLayout(new BorderLayout());
        south.setBackground(Color.cyan);

        right = new JPanel();
        right.setLayout(null);
        right.setBackground(Color.cyan);
        right.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.blue, 1),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        activeTimer = new JCheckBox("Активизировать");
        activeTimer.setBackground(Color.cyan);
        activeTimer.setFocusable(false);
        activeTimer.setBounds(25,5,150,30);
        right.add(activeTimer);

        spinnerTime = new JSpinnerTimerPanel();
        panelClock = new ClockPanel();
        activeTimer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new Thread(){
                    public void run(){
                        for(;;){
                            try{
                                if(activeTimer.isSelected()){
                                    String currentTime = panelClock.twentyFourthHourFormat();
                                    String signalTime = spinnerTime.getTime();
                                        if(signalTime.equals(currentTime)){
                                            activeTimer.setEnabled(false);
                                            for(int i=0;i<10;i++){
                                                Toolkit.getDefaultToolkit().beep();
                                                Thread.sleep(300);
                                            }
                                            activeTimer.setSelected(false);
                                            activeTimer.setEnabled(true);
                                            break;
                                        }
                                    }
                              //  }
                                Thread.sleep(500);
                            } catch(InterruptedException ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}
static class JSpinnerTimerPanel extends JPanel{
    private String point = "1";
    private JSpinner spinner;
    private JTextArea text1;
    private JTextArea text2;
    private JTextArea text3;
    private JTextArea firstPoint;
    private JTextArea secondPoint;
    public String getTime(){
        return text1.getText()+":"+text2.getText()+":"+text3.getText();
    }
    public JSpinnerTimerPanel() {
        setLayout(null);
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.blue, 1),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        text1 = new JTextArea();
        text1.setEditable(false);
        text1.setBackground(Color.green);
        text1.setForeground(Color.white);
        text1.setText("00");
        text1.setFont(new Font("Arial", Font.BOLD, 24));
        text1.setBounds(2, 2, 32, 34);
        add(text1);

        firstPoint = new JTextArea(":");
        firstPoint.setBackground(Color.green);
        firstPoint.setForeground(Color.white);
        firstPoint.setFont(new Font("Arial", Font.BOLD, 24));
        firstPoint.setEditable(false);
        firstPoint.setBounds(34, 2, 10, 34);
        add(firstPoint);

        text2 = new JTextArea();
        text2.setEditable(false);
        text2.setBackground(Color.green);
        text2.setForeground(Color.white);
        text2.setText("00");
        text2.setFont(new Font("Arial", Font.BOLD, 24));
        text2.setBounds(44, 2, 30, 34);
        add(text2);

        secondPoint = new JTextArea(":");
        secondPoint.setBackground(Color.green);
        secondPoint.setForeground(Color.white);
        secondPoint.setFont(new Font("Arial", Font.BOLD, 24));
        secondPoint.setEditable(false);
        secondPoint.setBounds(74, 2, 10, 34);
        add(secondPoint);

        text3 = new JTextArea();
        text3.setEditable(false);
        text3.setBackground(Color.green);
        text3.setForeground(Color.white);
        text3.setText("00");
        text3.setFont(new Font("Arial", Font.BOLD, 24));
        text3.setBounds(84, 2, 30, 34);
        add(text3);

        spinner = new JSpinner();
//----------------------------------------------------------------
        final String[] hours = new String[26];
        for (int i = 0, j = -1; i < hours.length; i++, j++) {
            if (i == -1) {
                hours[i] = "" + i;
            }
            if (j >= 0 && j < 10) {
                hours[i] = "0" + j;
            } else {
                hours[i] = "" + j;
            }
        }
        final SpinnerListModel model1 = new SpinnerListModel(hours) {
            public Object getNextValue() {
                if (super.getNextValue().toString().equals("24")) {
                    spinner.setValue(hours[1]);
                    text1.setText(super.getValue().toString());
                    return super.getValue();
                }
                if (point.equals("1")) {
                    text1.setText(super.getNextValue().toString());
                }
                return super.getNextValue();
            }

            public Object getPreviousValue() {
                if (super.getPreviousValue().toString().equals("-1")) {
                    spinner.setValue(hours[24]);
                    text1.setText(super.getValue().toString());
                    return super.getValue();
                }
                if (point.equals("1")) {
                    text1.setText(super.getPreviousValue().toString());
                }
                return super.getPreviousValue();
            }
        };
        spinner.setModel(model1);
        spinner.setValue(hours[1]);
        spinner.setBounds(114, 1, 17, 35);
        add(spinner);
//----------------------------------------------------------------
        final String[] minutesAndSeconds = new String[62];
        for (int i = 0, j = -1; i < minutesAndSeconds.length; i++, j++) {
            if (i == -1) {
                minutesAndSeconds[i] = "" + i;
            }
            if (j >= 0 && j < 10) {
                minutesAndSeconds[i] = "0" + j;
            } else {
                minutesAndSeconds[i] = "" + j;
            }
        }
        final SpinnerListModel model2 = new SpinnerListModel(minutesAndSeconds) {
            public Object getNextValue() {
                if (super.getNextValue().toString().equals("60")) {
                    spinner.setValue(minutesAndSeconds[1]);
                    if (point.equals("2")) {
                        text2.setText(super.getValue().toString());
                    } else if (point.equals("3")) {
                        text3.setText(super.getValue().toString());
                    }
                    return super.getValue();
                }
                if (point.equals("2")) {
                    text2.setText(super.getNextValue().toString());
                } else if (point.equals("3")) {
                    text3.setText(super.getNextValue().toString());
                }
                return super.getNextValue();
            }

            public Object getPreviousValue() {
                if (super.getPreviousValue().toString().equals("-1")) {
                    spinner.setValue(minutesAndSeconds[60]);
                    if (point.equals("2")) {
                        text2.setText(super.getValue().toString());
                    } else if (point.equals("3")) {
                        text3.setText(super.getValue().toString());
                    }
                    return super.getValue();
                }
                if (point.equals("2")) {
                    text2.setText(super.getPreviousValue().toString());
                } else if (point.equals("3")) {
                    text3.setText(super.getPreviousValue().toString());
                }
                return super.getPreviousValue();
            }
        };
//----------------------------------------------------------------
        text1.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                spinner.setModel(model1);
                spinner.setValue(hours[1]);
                text1.setText("00");
                point = "1";
            }

            public void focusLost(FocusEvent e) {
            }
        });
        text2.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                spinner.setModel(model2);
                spinner.setValue(minutesAndSeconds[1]);
                text2.setText("00");
                point = "2";
            }

            public void focusLost(FocusEvent e) {
            }
        });
        text3.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                spinner.setModel(model2);
                spinner.setValue(minutesAndSeconds[1]);
                text3.setText("00");
                point = "3";
            }

            public void focusLost(FocusEvent e) {
            }
        });
    }
}
}


