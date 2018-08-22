package ru.archvlad;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

class GameFrame extends JFrame{
	
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
