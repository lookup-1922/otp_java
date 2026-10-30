package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;

class TOTPFrame extends JFrame implements ActionListener {
  private JPanel panel;
  private JTextField secretKeyTextField;
  private JButton generateButton;
  private JButton copyButton;
  private JLabel otpLabel, remainingTimeLabel;
  private Timer timer;
  private TOTPGenerator generator;
  private long previousCounter = -1;

  public TOTPFrame() {
    this.setTitle("TOTP Generator");
    this.setSize(420, 350);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("TOTP Generator");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(title);
    panel.add(Box.createVerticalStrut(20));

    secretKeyTextField = new JTextField(25);
    panel.add(secretKeyTextField);
    panel.add(Box.createVerticalStrut(20));

    generateButton = new JButton("Generate");
    generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(generateButton);
    generateButton.addActionListener(this);

    otpLabel = new JLabel("------");
    otpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(otpLabel);

    remainingTimeLabel = new JLabel("残り時間：--秒");
    remainingTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(remainingTimeLabel);

    copyButton = new JButton("Copy");
    copyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    copyButton.setEnabled(false);
    panel.add(copyButton);
    copyButton.addActionListener(this);

    generator = new TOTPGenerator();

    timer = new Timer(1000, event -> {
      long currentTimeSeconds = System.currentTimeMillis() / 1000;

      int remainingSeconds = 30 - (int) (currentTimeSeconds % 30);

      remainingTimeLabel.setText(
          "残り時間：" + remainingSeconds + "秒");

      long currentCounter = currentTimeSeconds / 30;

      if (currentCounter != previousCounter) {
        updateOTP();
      }
    });

    this.add(panel);
    this.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == generateButton) {
      updateOTP();
    }

    if (e.getSource() == copyButton) {
      copyOTP();
    }
  }

  private void updateOTP() {
    String secretKeyText = secretKeyTextField.getText();

    try {
      long currentTimeSeconds = System.currentTimeMillis() / 1000;

      String otp = generator.generateTOTP(secretKeyText);

      otpLabel.setText(otp);

      previousCounter = currentTimeSeconds / 30;

      int remainingSeconds = 30 - (int) (currentTimeSeconds % 30);

      remainingTimeLabel.setText(
          "残り時間：" + remainingSeconds + "秒");

      copyButton.setEnabled(true);

      timer.start();

    } catch (IllegalArgumentException ex) {
      otpLabel.setText("------");
      remainingTimeLabel.setText("残り時間：--秒");
      copyButton.setEnabled(false);
      timer.stop();
      previousCounter = -1;

      JOptionPane.showMessageDialog(
          this,
          "不正な秘密鍵です",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void copyOTP() {
    String otp = otpLabel.getText();

    if (otp.equals("------")) {
      return;
    }

    StringSelection selection = new StringSelection(otp);

    Toolkit.getDefaultToolkit()
        .getSystemClipboard()
        .setContents(selection, null);
  }
}
