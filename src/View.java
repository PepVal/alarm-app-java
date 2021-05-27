import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

public class View extends JFrame {

    private JButton btnInitTime;
    public JTextField txtInitTime;
    public JTextField txtAlarmTime;
    public JLabel lblTime, lblMessage1;
    public JCheckBox ckSound, ckAlert;
    public JFrame container;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Boolean activeAlert, activeSound;
    public String timeAlarm;

    public void initView() {
        container = this;
        this.setTitle("Alarm App");

        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblTime = new JLabel("--:--:--");
        lblTime.setBounds(5, 5, 290, 20);
        this.add(lblTime);

        // txtInitTime = new JTextField();
        // txtInitTime.setBounds(5, 30, 125, 25);
        // this.add(txtInitTime);

        lblMessage1 = new JLabel("Alarm Time (HH:mm:ss)");
        lblMessage1.setBounds(5, 40, 290, 25);
        this.add(lblMessage1);

        txtAlarmTime = new JTextField();
        txtAlarmTime.setBounds(5, 60, 125, 25);
        this.add(txtAlarmTime);

        ckSound = new JCheckBox("Alarm with sound");
        ckSound.setBounds(5, 80, 150, 25);
        this.add(ckSound);

        ckAlert = new JCheckBox("Alarm with message");
        ckAlert.setBounds(5, 100, 150, 25);
        this.add(ckAlert);

        btnInitTime = new JButton("Init Time");
        btnInitTime.setBounds(5, 130, 125, 25);
        this.add(btnInitTime);

        this.setVisible(true);
    }

    public void controllers() {

        // init clock
        btnInitTime.addActionListener(e -> {
            // setup alarm
            activeAlert = ckAlert.isSelected();
            activeSound = ckSound.isSelected();
            timeAlarm = txtAlarmTime.getText();
            // active service
            timeService();
        });
    }

    public void timeService() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Boolean loop = true;
                while (loop) {
                    try {
                        Thread.sleep(500);
                        if (timeAlarm.equals(timeFormatter.format(LocalDateTime.now()))) {
                            if (activeSound) {
                                playSound();
                            }
                            if (activeAlert) {
                                JOptionPane.showMessageDialog(container, "alarmaaaa");
                            }
                            loop = false;
                        } else {
                            lblTime.setText(timeFormatter.format(LocalDateTime.now()));
                        }
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

}
