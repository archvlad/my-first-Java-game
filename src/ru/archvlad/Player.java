package ru.archvlad;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player {
	
    Image spritePlayer;
	
	public static int x = 0;
	public static int y = 0;
	public static int speed = 8;
	
	public Player() {
		try {
			spritePlayer = ImageIO.read(new File("img/imageAnt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл img/imageAnt.png не найден");
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
	
}
