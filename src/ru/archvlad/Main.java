package ru.archvlad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main {

	public static void main(String[] args) {
		GameFrame gameFrame = new GameFrame("My Java Game", 646, 675);
	}

}

class GameFrame extends JFrame {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public GameFrame(String title, int sizeWidth, int sizeHeight) {
		int locationX = (screenSize.width - sizeWidth) / 2;
		int locationY = (screenSize.height - sizeHeight) / 2;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setBounds(locationX, locationY, sizeWidth, sizeHeight);
		setResizable(false);
		addKeyListener(new GameKeyAdapter());
		setVisible(true);

		GamePanel gameField = new GamePanel();
		gameField.setBounds(0, 0, 640, 640);
		add(gameField);
	}

}

class GamePanel extends JPanel implements ActionListener {

	Timer timer = new Timer(25, this);

	public boolean trigger = false;
	
	public static PointLocation pointLocation = new PointLocation();
	public static StatusPanel statusPanel = new StatusPanel();
	public static GameBackground gameBackground = new GameBackground();

	public GamePanel() {
		setFocusable(true);
		requestFocus();
		timer.start();
	}

	public void paintComponent(Graphics g) {
		for (int i = 0; i < 2; i++) {
			gameBackground.paint(g, i);
			gameBackground.moveBackground();
			Player.score++;
		}
		
		statusPanel.paint(g);
		pointLocation.collided();
		pointLocation.player.paint(g);	
		pointLocation.enemy.paint(g);	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}

class GameKeyAdapter extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		int xin = e.getKeyCode();

		if (xin == e.VK_LEFT) {
			GamePanel.pointLocation.player.setX(GamePanel.pointLocation.player.getX() - GamePanel.pointLocation.player.getSpeed());
		}

		if (xin == e.VK_RIGHT) {
			GamePanel.pointLocation.player.setX(GamePanel.pointLocation.player.getX() + GamePanel.pointLocation.player.getSpeed());
		}

		if (xin == e.VK_UP) {
			GamePanel.pointLocation.player.setY(GamePanel.pointLocation.player.getY() - GamePanel.pointLocation.player.getSpeed());
		}

		if (xin == e.VK_DOWN) {
			GamePanel.pointLocation.player.setY(GamePanel.pointLocation.player.getY() + GamePanel.pointLocation.player.getSpeed());
		}
	}

}

class StatusPanel {

	public void paint(Graphics g) {
		// Верхняя панелька
		g.setColor(new Color(236, 240, 241));
		g.fillRect(0, 0, 640, 64);

		// Текст состояния игрока
		g.setColor(new Color(1, 50, 67));
		g.setFont(new Font("Comic Sans MS", 1, 24));
		g.drawString("Очки: " + String.valueOf(Player.score), 8, 30);
		g.drawString("Жизни: " + String.valueOf(Player.lives), 200, 30);
	}

}

class GameBackground {

	public Image spriteBackground;

	public int d = 0;
	public int d2 = 640;

	public GameBackground() {
		try {
			spriteBackground = ImageIO.read(new File("img/imageBackground.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageBackground.png не найден");
		}
	}

	public void paint(Graphics g, int i) {
		g.drawImage(spriteBackground, 640 * i + d, 0, 640, 640, null);
	}

	public void moveBackground() {
		if (d < -640) {
			d = 0;
		}
		
		d -= 4;
	}

}

class PointLocation {
	
	Player player = new Player();
	Enemy enemy = new Enemy();
	
	public boolean trigger = false;
	
	public void collided() {
		if ((player.getX() > enemy.getX() - 64) && (player.getX() < enemy.getX() + 64)
				&& (player.getY() > enemy.getY() - 64) && (player.getY() < enemy.getY() + 64)) {
			if (!trigger) {
				Player.lives--;
				trigger = true;
			}	
		} else {
			trigger = false;
		}
	}
	
}

class Player {

	public Image spritePlayer;

	public static int score = 0;
	public static int lives = 3;

	private int x = 0;
	private int y = 0;
	private int speed = 8;

	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageAnt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageAnt.png не найден");
		}
	}
	
	public void collided() {
		
	}

	public void paint(Graphics g) {
		g.drawImage(spritePlayer, getX(), getY(), null);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		if (this.x > 576) {
			x = 576;
		}
		if (this.x < 0) {
			x = 0;
		}
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		if (this.y > 576) {
			y = 576;
		}
		if (this.y < 64) {
			y = 64;
		}
		return y;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

}

class Enemy {

	public Image spriteEnemy;

	private int y;
	private int x = 704;
	private int speed = 6;

	public Enemy() {
		setY((int) (64 + Math.random() * 512));

		try {
			spriteEnemy = ImageIO.read(new File("img/imageLiquid.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageLiquid.png не найден");
		}
	}

	public void paint(Graphics g) {
		if (getX() < -64) {
			setY((int) (64 + Math.random() * 512));
			setX(704);
		}
		
		setX(getX() - getSpeed());
		g.drawImage(spriteEnemy, getX(), getY(), null);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}