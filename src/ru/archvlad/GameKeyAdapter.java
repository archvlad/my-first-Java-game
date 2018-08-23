package ru.archvlad;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter{
	public void keyPressed(KeyEvent e) {
		System.out.println("KeyPressed()");
		int xin = e.getKeyCode();
		if (xin == e.VK_LEFT) {
			GamePanel.p.setX(GamePanel.p.getX() - GamePanel.p.getSpeed());
		}
		if (xin == e.VK_RIGHT) {
			GamePanel.p.setX(GamePanel.p.getX() + GamePanel.p.getSpeed());
		}
		if (xin == e.VK_UP) {
			GamePanel.p.setY(GamePanel.p.getY() - GamePanel.p.getSpeed());
		}
		if (xin == e.VK_DOWN) {
			GamePanel.p.setY(GamePanel.p.getY() + GamePanel.p.getSpeed());
		}
	}
}
