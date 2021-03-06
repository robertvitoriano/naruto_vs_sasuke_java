package myGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements  KeyListener, ActionListener {
	private Double width;
	private Double height;
	private int lives = 5;
	private int initialLives = 5;
	private int bestScore;
	private int hitWallSound = 0;
	private int score = 0;
	private Double narutoXPosition = 0.02;
	private Double sasukeXPosition = 0.86;

	private boolean pause = false;

	private Random randomNumber = new Random();

	private GameObject naruto;
	private GameObject sasuke;
	private GameObject life;
	private GameObject background;

	private Clip mainMusicClip;

	private Timer timer = new Timer(2, this);

	public Game(JFrame screen, Double width, Double height) throws Exception {
		this.width = width;
		this.height = height;
		this.setBounds(0, 0, (int) Math.round(width), (int) Math.round(height));
		this.setVisible(true);
		screen.setLayout(null);
		screen.add(this);
		screen.setResizable(true);
		screen.addKeyListener(this);
		BufferedReader reader;
		reader = new BufferedReader(new FileReader("bestScore.txt"));
		String FileContent = reader.readLine();
		bestScore = Integer.parseInt(FileContent);
		reader.close();
		File f = new File("./Naruto Konoha Peace.wav");
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
		mainMusicClip = AudioSystem.getClip();
		mainMusicClip.open(audioIn);
		FloatControl gainControl = (FloatControl) mainMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(0.2));
		mainMusicClip.loop(Clip.LOOP_CONTINUOUSLY);

		background = new GameObject("konoha_background.jpg", 0.0, 0.0, width, height);
		naruto = new GameObject("mini_naruto.png", (width * narutoXPosition), 140.0, 100.0, 140.0);
		sasuke = new GameObject("mini_sasuke.png", (width * sasukeXPosition), 190.0, 100.0, 140.0);

		life = new GameObject("heart-icon.png", 0.0, 23.0, 25.0, 25.0);
		sasuke.setSpeedX(1.0);
		sasuke.setSpeedY(0.5);
		sasuke.setSpeedRate(2.0);
		
		timer.setInitialDelay(0);
		timer.start();
	}

	public void updateGame() {
		sasukeCollisions();
		narutoSasukeCollisions();
		movimentation();
		touchWall();
		narutoColision();
		GameOver();
	}

	public void narutoColision() {
		if (naruto.getYPosition() <= 0){
			naruto.setYPosition(0.0);	
		}
		else if (naruto.getYPosition() >= this.height - naruto.getObjectHeight()){
			naruto.setYPosition(this.height - naruto.getObjectHeight());
		}
	}

	public void GameOver() {
		if (lives < 1) {
			try {
				mainMusicClip.stop();
				playSound("loseSound.wav");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (score > bestScore) {
				FileWriter writer;
				try {
					writer = new FileWriter("bestScore.txt");
					writer.write("" + score);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			int resposta;
			int lastScore = bestScore;
			if (score < lastScore) {
				resposta = JOptionPane.showConfirmDialog(this, "Voc?? perdeu!!" +
						"Voc?? fez " + score + " pontos " +
						"e a " +
						"melhor pontu????o at?? agora foi " + lastScore + " pontos. " +
						"  Gostaria de jogar novamente");
			} else {
				resposta = JOptionPane.showConfirmDialog(this, "Voc?? perdeu, mas est?? de parab??ns." +
						"Voc?? fez " + score + " pontos " +
						"e a " +
						"melhor pontua????o at?? agora tinha si " + lastScore + " pontos." +
						"  Gostaria de jogar novamente");
			}
			if (resposta == JOptionPane.OK_OPTION) {
				restartGame();
			} else if (resposta == JOptionPane.NO_OPTION)
				mainMusicClip.close();
			System.exit(lives);
		}
	}

	public void restartGame() {
		mainMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
		lives = initialLives;
		score = 0;
		sasuke.setXPosition(450.0);
		sasuke.setYPosition(190.0);
		sasuke.setSpeedX(5.0);
		sasuke.setSpeedY(5.0);
		sasuke.setSpeedRate(2.0);
		naruto.setXPosition(50.0);
		naruto.setYPosition(140.0);
		naruto.setSpeed(3.0);
		naruto.setMovingUp(false);
		naruto.setMovingDown(false);
	}

	public void touchWall() {
		if (sasuke.getXPosition() <= 0) {
			lives -= 1;
			sasuke.setYPosition(Double.valueOf(randomNumber.nextInt((int) Math.round((400.0) + 30.0))));
			sasuke.setXPosition(Double.valueOf(randomNumber.nextInt((int) Math.round((250.0) + 200.0))));
			try {
				playSound("missedSound.wav");
			} catch (Exception e) {
					e.printStackTrace();
			}
		}
	}

	void playSound(String soundFile) throws Exception, IOException {
		File f = new File("./" + soundFile);
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
	}

	public void movimentation() {
		if (naruto.isMovingUp()) {
			naruto.setYPosition(naruto.getYPosition() - naruto.getSpeed());
		}
		if (naruto.isMovingDown()) {
			naruto.setYPosition(naruto.getYPosition() + naruto.getSpeed());
		}
	}

	public void narutoSasukeCollisions() {
		if (sasuke.getXPosition() <= naruto.getXPosition() + naruto.getObjectWidth()) {
			if (sasuke.getYPosition() >= naruto.getYPosition()
					&& sasuke.getYPosition() <= naruto.getYPosition() + naruto.getObjectHeight()) {
				if (bestScore < score)
					bestScore = score;
				sasuke.setSpeedX(sasuke.getSpeedX() * -1);
				sasuke.setSpeedY(sasuke.getSpeedY() * -1);
				try {
					playSound("sasuke-screaming.wav");
				} catch (IOException e) {
							e.printStackTrace();
				} catch (Exception e) {
							e.printStackTrace();
				}
				score++;
				naruto.setSpeed(naruto.getSpeed() + naruto.getSpeedRate());
				if (score % 4 == 0) {
					if (sasuke.getSpeedY() <= 0)
						sasuke.setSpeedY(sasuke.getSpeedY() - sasuke.getSpeedRate());
					else
						sasuke.setSpeedY(sasuke.getSpeedY() + sasuke.getSpeedRate());
					if (sasuke.getSpeedX() <= 0)
						sasuke.setSpeedX(sasuke.getSpeedX() - sasuke.getSpeedRate());
					else
						sasuke.setSpeedX(sasuke.getSpeedX() + sasuke.getSpeedRate());
				}
			}
		}
	}

	public void sasukeCollisions() {
		sasuke.setYPosition(sasuke.getYPosition() + sasuke.getSpeedY());
		if (sasuke.getXPosition() >= this.width - 25 || sasuke.getXPosition() <= 0) {
			sasuke.setSpeedX(sasuke.getSpeedX() * -1);
		}
		if (sasuke.getYPosition() >= this.height - sasuke.getObjectHeight()|| sasuke.getYPosition() <= 0) {
			sasuke.setSpeedY(sasuke.getSpeedY() * -1);
		}
		if (sasuke.getSpeedX() < 0) {
			sasuke.setSpeedAbsoluteX(sasuke.getSpeedX()* -1);
		}
		else {
			sasuke.setSpeedAbsoluteX(sasuke.getSpeedX());
		}
		if (sasuke.getSpeedY() < 0) {
			sasuke.setSpeedAbsoluteY(-sasuke.getSpeedY());
		} else {
			sasuke.setSpeedAbsoluteY(sasuke.getSpeedY());
		}
	}

	public void playHitWallSound() {
		try {
			if (hitWallSound == 2) {
				playSound("hitWall1.wav");
				hitWallSound = 1;
			} else if (hitWallSound == 1) {
				playSound("hitWall2.wav");
				hitWallSound = 2;
			} else {
				hitWallSound = 1;
				playSound("hitWall1.wav");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		try {
			if(background == null){
				return;
			}
			background.drawImage(g2d);
			naruto.drawImage(g2d);
			drawLives(g2d);
			drawScore(g2d);
			sasuke.drawImage(g2d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawLives(Graphics2D g) throws Exception {
		for (int i = 0; i <= lives - 1; i++) {
			life.setXPosition(Double.valueOf(60 + 25 * i));
			life.drawImage(g);
		}
		g.setColor(Color.BLUE);
		g.drawString("Vidas:  ", 20, 40);
		g.setColor(Color.RED);
	}

	public void drawScore(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawString("Placar: " + score + " ponto(s)", (int) (this.width - 185), (int) 40);
		if (bestScore > score) {
			g.drawString("Melhor Pontua????o: " + bestScore + " pontos", (int) (this.width - 185), (int) 80);
		} else
			g.drawString("Melhor Pontua????o: " + score + " pontos", (int) (this.width - 185), (int) 80);
		if (pause == true) {
			g.setColor(Color.BLACK);
			g.drawString("Aperte espa??o para voltar", 200, 200);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			naruto.setMovingUp(true);
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			naruto.setMovingDown(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			pause = !pause;

			if (pause) {
				mainMusicClip.stop();
			} else {
				mainMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			naruto.setMovingUp(false);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			naruto.setMovingDown(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateGame();
		repaint();		
	}
}
