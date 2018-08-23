package ru.archvlad;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player {
	
    Image spritePlayer;
	
	private int x = 0;
	private int y = 0;
	private int speed = 8;
	
	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageAnt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���� img/imageAnt.png �� ������");
		}
	}

	public int getX() {
		if (this.x > 576 ) {
			x = 576;
		}
		if (this.x < 0 ) {
			x = 0;
		}
		return x;
	}

	public int getY() {
		if (this.y > 576 ) {
			y = 576;
		}
		if (this.y < 0 ) {
			y = 0;
		}
		return y;
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

	public void setY(int y) {
		this.y = y;
	}
	
}
