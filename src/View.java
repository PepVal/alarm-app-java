import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;

public class View extends JFrame {

    private JButton btnStart;
    public JTextField txtInitTime;
    public JTextField txtAlarmTime;
    public JLabel lblTime, lblAlarmTime, lblStartTime;
    public JCheckBox ckSound, ckAlert;
    public JFrame container;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Boolean activeAlert, activeSound;
    public String timeAlarm, startTime;
    public long millisTimeAlarm, millisStartTime;

    public void initView() {
        container = this;
        this.setTitle("Alarm App");

        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblTime = new JLabel("--:--:--");
        lblTime.setFont(new FontUIResource("Serif", 1, 40));
        lblTime.setBounds(200, 5, 290, 40);
        this.add(lblTime);

        lblStartTime = new JLabel("Start Time (HH:mm:ss)");
        lblStartTime.setBounds(5, 40, 290, 25);
        this.add(lblStartTime);

        txtInitTime = new JTextField();
        txtInitTime.setBounds(5, 60, 125, 25);
        this.add(txtInitTime);

        lblAlarmTime = new JLabel("Alarm Time (HH:mm:ss)");
        lblAlarmTime.setBounds(5, 80, 290, 25);
        this.add(lblAlarmTime);

        txtAlarmTime = new JTextField();
        txtAlarmTime.setBounds(5, 100, 125, 25);
        this.add(txtAlarmTime);

        ckSound = new JCheckBox("Alarm with sound");
        ckSound.setBounds(5, 130, 150, 25);
        this.add(ckSound);

        ckAlert = new JCheckBox("Alarm with message");
        ckAlert.setBounds(5, 160, 150, 25);
        this.add(ckAlert);

        btnStart = new JButton("Start");
        btnStart.setBounds(5, 200, 125, 25);
        this.add(btnStart);

        this.setVisible(true);
    }

    public void controllers() {
        // start clock
        btnStart.addActionListener(e -> {
            // init time
            startTime = txtInitTime.getText();
            // setup alarm
            activeAlert = ckAlert.isSelected();
            activeSound = ckSound.isSelected();
            timeAlarm = txtAlarmTime.getText();
            // validations
            if (!Utils.isValidTime(startTime)) {
                showMessage("The start time format is invalid");
            } else if (!Utils.isValidTime(timeAlarm)) {
                showMessage("The alarm time format is invalid");
            } else if (activeAlert || activeSound) {
                startService();
            } else {
                showMessage("You must select at least one type of alarm");
            }
        });
    }

    public void startService() {
        millisStartTime = Utils.parseDateToMillis(startTime);
        millisTimeAlarm = Utils.parseDateToMillis(timeAlarm);
        // active service
        timeService();
    }

    private void timeService() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Boolean loop = true;
                while (loop) {
                    try {
                        // timeAlarm.equals(timeFormatter.format(LocalDateTime.now()))
                        if (millisTimeAlarm == millisStartTime) {
                            if (activeSound) {
                                playSound();
                            }
                            if (activeAlert) {
                                showMessage("Alarma ⏰⏰⏰");
                            }
                            loop = false;
                        } else {
                            // lblTime.setText(timeFormatter.format(LocalDateTime.now()));
                            incrementTime();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread hilo = new Thread(runnable);
        hilo.start();
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File("assets/alarm-clock.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error al reproducir el sonido.");
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(container, message);
    }

    public void incrementTime() {
        millisStartTime += 1000;
        lblTime.setText(Utils.parseMillisToDate(millisStartTime));
    }

}
