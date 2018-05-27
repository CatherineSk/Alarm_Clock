import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.*;

public class JavaAlarmClock extends JFrame {
    private ClockPanel panelClock;
    private ClockPanel.DigitalClock digit;
    private ClockPanel.ChoiceSignal panelChoiceSignal;
    private JLabel labelClock;
    private JLabel currentTimeLabel;
    private JLabel timeSignal;
    private ClockPanel.JSpinnerTimerPanel timerClock;

    public void javaStart() throws IOException {
        String command = "java "+getClass().getName();
        BufferedReader buf = new BufferedReader(new StringReader(command));
        PrintWriter out;
        out = new PrintWriter(new BufferedWriter(new FileWriter("JavaAlarmClock.bat")));
        String str;
        while( (str = buf.readLine()) != null){
            out.println(command);
        }
        buf.close();
        out.close();
    }

    public JavaAlarmClock() throws IOException{
        setLayout(null);
        Container content = getContentPane();
        getContentPane().setBackground(Color.white);

        panelClock = new ClockPanel();
        panelClock.startClock();
        panelClock.setBounds(10,25,170,170);

        labelClock = new JLabel(""+panelClock.getFullDate());
        labelClock.setForeground(Color.orange);
        labelClock.setBounds(15,5,150,15);

        currentTimeLabel = new JLabel("Текущее время");
        currentTimeLabel.setBounds(235,5,100,15);
        currentTimeLabel.setForeground(Color.orange);

        panelChoiceSignal = new ClockPanel.ChoiceSignal();
        panelChoiceSignal.setBounds(10,195,350,85);

        timeSignal = new JLabel("Время срабатывания");
        timeSignal.setBounds(220,130,130,15);
        timeSignal.setForeground(Color.orange);

        timerClock = new ClockPanel.JSpinnerTimerPanel();
        timerClock.setBounds(215,145,133,38);

        digit = new ClockPanel.DigitalClock();
        digit.setBounds(200,20,160,100);

        content.add(panelClock);
        content.add(labelClock);
        content.add(currentTimeLabel);
        content.add(timeSignal);
        content.add(timerClock);
        content.add(digit);
        content.add(panelChoiceSignal);

        getContentPane().add(new JColorChooser());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowStateListener(new WindowStateListener(){
            public void windowStateChanged(WindowEvent e){
                if(e.getNewState() == JFrame.ICONIFIED){ //This state checks whether the frame is minimized or not.
                    setVisible(false);
                }
            }
        });
        PopupMenu popupMenu = new PopupMenu();
        MenuItem showItem = new MenuItem("Восстановить");
        MenuItem exitItem = new MenuItem("Закрыть");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        showItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                setState(JFrame.NORMAL);

            }
        });
        popupMenu.add(exitItem);
        popupMenu.add(showItem);

    }
    public static void main(String[] args) throws IOException{
        JFrame.setDefaultLookAndFeelDecorated(true);
        try{
            UIManager.setLookAndFeel(new MetalLookAndFeel()); // like the windows system
        }
        catch ( UnsupportedLookAndFeelException e ) {
            System.exit(0);
        }
        JavaAlarmClock frame = new JavaAlarmClock();
        frame.javaStart();
        frame.setTitle("JavaAlarmClock");
        frame.setSize(375,320);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
