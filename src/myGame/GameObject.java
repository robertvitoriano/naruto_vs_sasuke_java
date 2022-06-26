package myGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameObject extends JPanel {
  BufferedImage image = null;
  Color color = null;
  String imagePath;
  Double xPosition;
  Double yPosition;
  Double width;
  Double height;
  int speed = 4;
  int size = 0;
  int speedRate = 1;
  int speedX = 0;
  int speedY = 0;

  public int getSpeedAbsoluteX() {
    return speedAbsoluteX;
  }

  public void setSpeedAbsoluteX(int speedAbsoluteX) {
    this.speedAbsoluteX = speedAbsoluteX;
  }

  public int getSpeedAbsoluteY() {
    return SpeedAbsoluteY;
  }

  public void setSpeedAbsoluteY(int speedAbsoluteY) {
    SpeedAbsoluteY = speedAbsoluteY;
  }

  int speedAbsoluteX = 0;
  int SpeedAbsoluteY = 0;
  boolean movingDown;
  boolean movingUp;
  boolean movingLeft;
  boolean movingRight;

  public GameObject(String imagePath,
      Double xPosition,
      Double yPosition,
      Double width,
      Double height) {
    this.imagePath = imagePath;
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.width = width;
    this.height = height;
    readImage();
  }

  public GameObject(Color color,
      Double xPosition,
      Double yPosition,
      Double width,
      Double height) {
    this.color = color;
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.width = width;
    this.height = height;
  }

  public GameObject(String imagePath, Double width, Double height) {
    this.imagePath = imagePath;
    this.width = width;
    this.height = height;
  }

  public GameObject(String imagePath) {
    this.imagePath = imagePath;
  }

  public int getSpeedX() {
    return speedX;
  }

  public void setSpeedX(int speedX) {
    this.speedX = speedX;
  }

  public int getSpeedY() {
    return speedY;
  }

  public void setSpeedY(int speedY) {
    this.speedY = speedY;
  }

  public int getComponentSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isMovingLeft() {
    return movingLeft;
  }

  public void setMovingLeft(boolean movingLeft) {
    this.movingLeft = movingLeft;
  }

  public boolean isMovingRight() {
    return movingRight;
  }

  public void setMovingRight(boolean movingRight) {
    this.movingRight = movingRight;
  }

  public int getSpeedRate() {
    return speedRate;
  }

  private void readImage() {
    try {
      image = ImageIO.read(new File(this.imagePath));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public boolean isMovingDown() {
    return movingDown;
  }

  public void setMovingDown(boolean movingDown) {
    this.movingDown = movingDown;
  }

  public boolean isMovingUp() {
    return movingUp;
  }

  public void setMovingUp(boolean MovingUp) {
    this.movingUp = MovingUp;
  }

  public void setSpeedRate(int SpeedRate) {
    this.speedRate = SpeedRate;
  }

  public BufferedImage getImage() {
    return image;
  }

  public Double getXPosition() {
    return xPosition;
  }

  public Double getObjectWidth() {
    return width;
  }

  public Double getObjectHeight() {
    return height;
  }

  public void setXPosition(Double XPosition) {
    this.xPosition = XPosition;
  }

  public void setYPosition(Double YPosition) {
    this.yPosition = YPosition;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public Double getYPosition() {
    return yPosition;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public int getSpeed() {
    return speed;
  }

  public void drawImage(Graphics2D g) throws Exception {
    try {
      if (image == null) {
        throw new Exception("Image not found");
      }
      g.drawImage(image, (int) Math.round(xPosition), (int) Math.round(yPosition), (int) Math.round(width),
          (int) Math.round(height), null);

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void drawOval(Graphics2D g) {
    g.setColor(color);
    g.fillOval((int) Math.round(xPosition), (int) Math.round(yPosition), size, size);
  }
}
