package ru.archvlad;

import java.awt.Color;
import java.awt.Dimension;
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
	int enemyPointY;

	public static Player player = new Player();
	public static Enemy enemy = new Enemy();
	public static GameBackground gameBackground = new GameBackground();

	public GamePanel() {
		enemyPointY = (int) (64 + Math.random() * 512);
		setFocusable(true);
		requestFocus();
		timer.start();
	}

	public void paintComponent(Graphics g) {
		System.out.println("paintComponent()");

		if (gameBackground.d < -640) {
			gameBackground.d = 0;
		}
		gameBackground.d -= 4;

		for (int i = 0; i < 2; i++) {
			g.drawImage(gameBackground.spriteBackground, 640 * i + gameBackground.d, 0, 640, 640, null);
		}
		
		g.drawImage(player.spritePlayer, player.getX(), player.getY(), null);
		g.setColor(new Color(236, 240, 241));
		g.fillRect(0, 0, 640, 64);
		
		if (enemy.getX() < -64) {
			enemyPointY = (int) (64 + Math.random() * 512);
			enemy.setX(704);
			System.out.println(enemyPointY);
		}
		enemy.setX(enemy.getX() - enemy.getSpeed());
		g.drawImage(enemy.spriteEnemy, enemy.getX(), enemyPointY, null);

		if ((player.getX() > enemy.getX() - 64) && (player.getX() < enemy.getX() + 64)
				&& (player.getY() > enemyPointY - 64) && (player.getY() < enemyPointY + 64)) {
			// Здесь что-то должно быть
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("repaint()");
		repaint();

	}

}

class GameKeyAdapter extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		System.out.println("KeyPressed()");

		int xin = e.getKeyCode();

		if (xin == e.VK_LEFT) {
			GamePanel.player.setX(GamePanel.player.getX() - GamePanel.player.getSpeed());
		}

		if (xin == e.VK_RIGHT) {
			GamePanel.player.setX(GamePanel.player.getX() + GamePanel.player.getSpeed());
		}

		if (xin == e.VK_UP) {
			GamePanel.player.setY(GamePanel.player.getY() - GamePanel.player.getSpeed());
		}

		if (xin == e.VK_DOWN) {
			GamePanel.player.setY(GamePanel.player.getY() + GamePanel.player.getSpeed());
		}
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

}

class Player {

	public Image spritePlayer;

	private int x = 0;
	private int y = 0;
	private int speed = 8;

	public int lives = 3;

	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageAnt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageAnt.png не найден");
		}
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

	private int x = 704;
	private int speed = 6;

	public Enemy() {
		try {
			spriteEnemy = ImageIO.read(new File("img/imageLiquid.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageLiquid.png не найден");
		}
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

}