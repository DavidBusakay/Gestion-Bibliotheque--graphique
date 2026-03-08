import javax.swing.SwingUtilities;

import gui.BibliothequeGUI;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new BibliothequeGUI());
  }
}
