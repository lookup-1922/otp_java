package src;
import javax.swing.SwingUtilities;

class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new TOTPFrame();
    });
  }
}
