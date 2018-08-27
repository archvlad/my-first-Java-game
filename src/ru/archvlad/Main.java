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
import java.util.TimerTask;

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

	Timer timer1 = new Timer(25, this);

	public static PointLocation pointLocation = new PointLocation();
	public static StatusPanel statusPanel = new StatusPanel();
	public static GameBackground gameBackground = new GameBackground();
	public static Player player = new Player();
	public static Enemy enemy = new Enemy();

	public GamePanel() {
		setFocusable(true);
		requestFocus();
		timer1.start();
	}

	public void paintComponent(Graphics g) {
		// Отрисовка заднего фона
		for (int i = 0; i < 2; i++) {
			gameBackground.paint(g, i);
			gameBackground.moveBackground();
		}

		statusPanel.paint(g);
		// Проверка на столкновение игрока и врага
		pointLocation.collided(enemy, player);
		player.paint(g);
		enemy.paint(g, player);
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

class StatusPanel {

	public void paint(Graphics g) {
		// Верхняя панелька
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, 640, 64);
		g.setColor(Color.YELLOW);
		g.fillRect(5, 5, 200, 54);
		g.fillRect(210, 5, 200, 54);
		g.setColor(Color.RED);

		int sWidth = 25;
		for (int i = 0; i < Player.lives; i++) {
			g.fillOval(380 - sWidth * i, 23, 20, 20);
		}

		// Текст состояния игрока
		g.setColor(new Color(1, 50, 67));
		g.setFont(new Font("Comic Sans MS", 1, 24));
		g.drawString("Очки: " + String.valueOf(Player.score), 10, 40);
		g.drawString("Жизни: " + String.valueOf(Player.lives), 215, 40);
	}

}

class GameBackground {

	public Image spriteBackground;

	public int d = 0;
	public int d2 = 640;
	public int speed = 2;

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

		d -= speed;
	}

}

class PointLocation {

	public static boolean trigger = false;

	public void collided(Enemy enemy, Player player) {
		if (CollisionToEnemy(enemy, player)) {
			if (!trigger) {
				Player.lives--;
				trigger = true;
			}
		} else {
			trigger = false;
		}
	}

	public static boolean CollisionToEnemy(Enemy enemy, Player player) {
		if ((player.getX() > enemy.getX() - 64) && (player.getX() < enemy.getX() + 64)
				&& (player.getY() > enemy.getY() - 64) && (player.getY() < enemy.getY() + 64)) {
			return true;
		}
		return false;
	}

}

class MyTask extends TimerTask {

	@Override
	public void run() {

	}

}

class Player {

	public Image spritePlayer;

	public static int score = 0;
	public static int lives = 3;

	private int x = 0;
	private int y = 0;
	private int speed = 64;

	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageAnt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageAnt.png не найден");
		}
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

	Image[] enemyBang = new Image[7];

	public Image spriteEnemy;

	private int index = 0;

	private int y;
	private int x = 704;
	private int speed = 6;

	boolean triggers = false;

	public Enemy() {
		getRandomY();

		try {
			spriteEnemy = ImageIO.read(new File("img/imageLiquid.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageLiquid.png не найден");
		}

		enemyBang[0] = null;
		for (int i = 1; i < 7; i++) {
			try {
				enemyBang[i] = ImageIO.read(new File("img/Animation/imageLiquidBang" + i + ".png"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Файл img/Animation/imageLiquidBang.png не найден");
			}
		}
	}

	public void getRandomY() {
		do {
			y = (int) (64 + Math.random() * 512);
		} while (!(y % 64 == 0));
	}

	public void paint(Graphics g, Player p) {
		if (getX() < -64) {
			getRandomY();
			setX(704);
		}

		x = x - speed;

		System.out.println(triggers);

		if (!(PointLocation.CollisionToEnemy(this, p))) {
			g.drawImage(spriteEnemy, x, y, null);
		} else {
			speed = 0;
			g.drawImage(enemyBang[index], x, y, null);
			index++;
			if (index == 6) {
				speed = 6;
				x = 704;
				getRandomY();
			}
		}

		if (index > 6) {
			index = 1;
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

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}