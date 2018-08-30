package ru.archvlad;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

public class Main {

	public static void main(String[] args) {
		new GameFrame("My Java Game", 646, 675);
	}

}

class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public GameFrame(String title, int sizeWidth, int sizeHeight) {
		int locationX = (screenSize.width - sizeWidth) / 2;
		int locationY = (screenSize.height - sizeHeight) / 2;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setBounds(locationX, locationY, sizeWidth, sizeHeight);
		setResizable(false);
		setFocusable(true);
		requestFocus();
		addKeyListener(new GameKeyAdapter());
		setVisible(true);

		GamePanel gameField = new GamePanel();
		gameField.setBounds(0, 0, 640, 640);
		gameField.add(StatusPanel.newGameButton);
		add(gameField);
	}

}

class GamePanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	Timer timer1 = new Timer(16, this);
	
	boolean running = false;
	
	public static PointLocation pointLocation = new PointLocation();
	
	public static StatusPanel statusPanel = new StatusPanel();
	
	public static GameBackground gameBackground = new GameBackground();
	
	public static Player player = new Player();
	
	public static Enemy enemy1 = new Enemy();
	public static Enemy enemy2 = new Enemy();
	public static Enemy enemy3 = new Enemy();
	
	public static Bullet bullet = new Bullet();
	
	public static Coin coin = new Coin();;
	
	public static boolean shoot = false;

	public GamePanel() {
		setFocusable(true);
		requestFocus();
		timer1.start();
	}
	
	public void paintComponent(Graphics g) {
		// Отрисовка заднего фона
		for (int i = 0; i < 2; i++) {
			gameBackground.paint(g, i);
		}
		
		//Рисуем игрока и врага
		player.paint(g);	
		enemy1.paint(g, player);
		enemy2.paint(g, player);
		enemy3.paint(g, player);
		
		//Рисуем верхнюю панельку состояния игрока
		statusPanel.paint(g, player);
		
		// Проверка на столкновение игрока и врага
		pointLocation.collidedToEnemy(enemy1, player);
		pointLocation.collidedToEnemy(enemy2, player);
		pointLocation.collidedToEnemy(enemy3, player);
		
		//Провереям можно ли стрелять
		if (shoot == true) {
			bullet.paint(g, player);
			
			// Проверка на столкновение пули и врага
			pointLocation.collidedToBullet(enemy1, bullet, coin, g);
			pointLocation.collidedToBullet(enemy2, bullet, coin, g);
			pointLocation.collidedToBullet(enemy3, bullet, coin, g);
			if (bullet.getY() < 64) {
				shoot = false;
			}	
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}

class GameKeyAdapter extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		int xin = e.getKeyCode();

		if (xin == KeyEvent.VK_LEFT) {
			GamePanel.player.setX(GamePanel.player.getX() - GamePanel.player.getSpeed());
		}

		if (xin == KeyEvent.VK_RIGHT) {
			GamePanel.player.setX(GamePanel.player.getX() + GamePanel.player.getSpeed());
		}

		if (xin == KeyEvent.VK_UP) {
			/*if (GamePanel.player.getY() < 0) {
				GamePanel.player.setY(0);
			}
			GamePanel.player.setY(GamePanel.player.getY() - GamePanel.player.getSpeed());*/
		}

		if (xin == KeyEvent.VK_DOWN) {
			/*if (GamePanel.player.getY() > 576) {
				GamePanel.player.setY(576);
			}
			GamePanel.player.setY(GamePanel.player.getY() + GamePanel.player.getSpeed());*/
		}
		
		if (xin == KeyEvent.VK_SPACE) {
			GamePanel.shoot = true;
		}
	}

}

class StatusPanel implements ActionListener  {
	
	public static JButton newGameButton = new JButton("New Game");
	boolean trigger = false;
	
	public StatusPanel() {
		newGameButton.setEnabled(false);
		newGameButton.addActionListener(this);
		newGameButton.setBackground(Color.ORANGE);
		newGameButton.setBounds(435, 10, 195, 44);
	}
	
	public void paint(Graphics g, Player p) {
		// Верхняя панелька
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, 640, 64);
		g.setColor(Color.YELLOW);
		g.fillRect(5, 5, 210, 54);
		g.fillRect(220, 5, 205, 26);
		g.fillRect(220, 33, 205, 26);
		g.fillRect(430, 5, 205, 54);
		
		//Уровень жизни
		g.setColor(Color.RED);
		int sWidth = 23;
		int sY = 0;
		for (int i = 0; i < Player.lives; i++) {
			sY = 225 + sWidth * i; 
			g.fillOval(225 + sWidth * i, 37, 18, 18);
			if (sY > 380) {
				System.out.println(i);
				for (int j = 0; i < Player.lives-1; j++) {
					sY = 225 + sWidth * j; 
					g.setColor(Color.GREEN);
					g.fillOval(sY, 37, 18, 18);
					i++;
					System.out.println(i);
				}
			}
		}

		// Текст состояния игрока
		g.setColor(new Color(1, 50, 67));
		//g.setColor(new Color(246, 36, 89));
		g.setFont(new Font("Comic Sans MS", 1, 32));
		g.drawString("Очки: " + String.valueOf(Player.score), 10, 45);
		g.setFont(new Font("Comic Sans MS", 1, 20));
		g.drawString("Жизни: " + String.valueOf(Player.getLives()), 225, 27);
		g.setFont(new Font("Comic Sans MS", 1, 32));
		if (Player.lives == 0) {
			GamePanel.shoot = false;
			Player.score = 0;
			p.setSpeed(4);
			if (p.getY() == 576 && !trigger) {
				p.setY(p.getY() - 576);
				trigger = true;
			}
			p.setY(p.getY() + 1);
			try {
				Player.spritePlayer = ImageIO.read(new File("img/imageEnemySpaceShip.png"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Файл img/imageEnemySpaceShip.png не найден");
			}
			newGameButton.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			Player.lives = 3;
			Player.score = 0;
			GamePanel.player.setSpeed(64);
			int randomPozition = (int) (Math.random() * 640);
			GamePanel.player.setX(randomPozition);
			GamePanel.player.setY(576);
			try {
				Player.spritePlayer = ImageIO.read(new File("img/imageSpaceShip.png"));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Файл img/imageSpaceShip.png не найден");
			}
			trigger = false;
			newGameButton.setEnabled(false);
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
		g.drawImage(spriteBackground, 0, 640 * -i + d, 640, 640, null);
		d += speed;
		if (d > 640) {
			d = 0;
		}
	}

}

class PointLocation {

	public static boolean trigger1 = false;
	public static boolean trigger2 = false;

	public void collidedToEnemy(Enemy e, Player p) {
		if (CollisionToEnemy(e, p)) {
			if (!trigger1) {
				e.setY(0);
				e.getRandomX();
				Player.lives--;
				Player.score -= 2;
				trigger1 = true;
			}
		} else {
			trigger1 = false;
		}
	}
	
	public void collidedToBullet(Enemy e, Bullet b, Coin c, Graphics g) {
		if (CollisionToBullet(e, b)) {
			if (!trigger2) {
				b.setX(-100);
				b.setY(-100);
				e.setY(0);
				e.getRandomX();
				Player.score++;
				trigger2 = true;
			}
		} else {
			trigger2 = false;
		}
	}

	public static boolean CollisionToEnemy(Enemy e, Player p) {
		if ((p.getX() > e.getX() - 64) && (p.getX() < e.getX() + 64)
				&& (p.getY() > e.getY() - 64) && (p.getY() < e.getY() + 64)) {
			return true;
		}
		return false;
	}
	
	public static boolean CollisionToBullet(Enemy e, Bullet b) {
		if ((b.getX() > e.getX() - 64) && (b.getX() < e.getX() + 64)
				&& (b.getY() > e.getY() - 64) && (b.getY() < e.getY() + 64)) {
			return true;
		}
		return false;
	}

}

class Player {

	public static Image spritePlayer;

	public static int score = 0;
	public static int lives = 3;

	private int x = 0;
	private int y = 576;
	private int speed = 64;

	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageSpaceShip.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageSpaceShip.png не найден");
		}
	}

	public void paint(Graphics g) {
		g.drawImage(spritePlayer, getX(), getY(), null);
	}
	
	public static int getLives() {
		if (lives <= 0) {
			lives = 0;
		}
		return lives;
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
	private int x = 640;
	private int speed = 4;

	boolean triggers = false;

	public Enemy() {
		getRandomX();

		try {
			spriteEnemy = ImageIO.read(new File("img/imageEnemySpaceShip.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageEnemySpaceShip.png не найден");
		}
	}

	public void getRandomX() {
		do {
			x = (int) (64 + Math.random() * 576);
		} while (!(x % 64 == 0));
	}

	public void paint(Graphics g, Player p) {
		g.drawImage(spriteEnemy, x, y, 64, 64, null);
		y = y + speed;
		if (getY() > 640) {
			Player.score--;
			getRandomX();
			setY(-64);
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

class Bullet {
	
	private int y = -100;
	private int x = -100;
	private int speed = 32;
	
	public Bullet() {
		
	}
	
	public void paint(Graphics g, Player p) {
		if (y < 64) {
			setLocation(p);
		}
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, 8, 8);
		y -= speed;
	}
	
	public void setLocation(Player p) {
		x = p.getX() + 32 - 4;
		y = p.getY();
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
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

class Coin {
	
	private int y = 100;
	private int x = 100;
	public static int rarity = 1;
	
	Image [] coin = new Image[4];
	
	public Coin() {
		coin[0] = null;

		y = (int) (1 + Math.random() * 3);
		
		for (int i = 1; i < 4; i++) {
			try {
				coin[i] = ImageIO.read(new File("img/imageCoin" + i + ".png"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Файл img/imageCoin не найден");
			}
		}
	}
	
	public void paint(Graphics g, Enemy e) {
		g.drawImage(coin[rarity], e.getX(), e.getY(), null);
		System.out.println(rarity);
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
}