package ru.archvlad;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel implements ActionListener{
	
	Timer timer = new Timer(25, this);
	
	Player p = new Player();
	
	Image spriteBackground;
	
	int d = 0;
	int d2 = 640;
	
	public GamePanel() {
		try {
			spriteBackground = ImageIO.read(new File("img/imageBackground.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageBackground.png не найден");
		}
		setFocusable(true);
		requestFocus();
		timer.start();
	}
	
	public void paintComponent(Graphics g) {		
		System.out.println("paintComponent()");
		
		for (int i = 0; i < 2; i ++) {
			g.drawImage(spriteBackground, 640*i+d, 0, 640, 640, null);
		}
		
		if (d < -640) {
			d = 0;
		}
		d -= 4;
		
		g.drawImage(p.spritePlayer, p.getX(), p.getY(), null);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("repaint()");
		repaint();
		
	}
	
}
