package ru.archvlad;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameKeyAdapter extends KeyAdapter{
	
	public void keyPressed(KeyEvent e) {
		System.out.println("KeyPressed()");
		int xin = e.getKeyCode();
		if (xin == e.VK_LEFT) {
			Player.x -= Player.speed;
		}
		if (xin == e.VK_RIGHT) {
			Player.x += Player.speed;
		}
		if (xin == e.VK_UP) {
			Player.y -= Player.speed;
		}
		if (xin == e.VK_DOWN) {
			Player.y += Player.speed;
		}
	}
}
