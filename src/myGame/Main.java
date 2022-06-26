package myGame;

import javax.swing.*;

public class Main {
  static Double width = 1360.0;
  static Double height = 720.0;

  public static void main(String[] args) throws Exception {
    JFrame screen = new JFrame("Naruto vs Sasuke - by Robert Vitoriano");
    screen.setSize((int) Math.round(width), (int) Math.round(height));
    screen.setVisible(true);
    screen.setLocationRelativeTo(null);
    screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    new Game(screen, width, height);

  }

}
