package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("First test");
		frame.setSize(400, 400);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel test = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(Color.BLACK);
				
				g.setColor(Color.BLUE);
				g.fillRect(50, 50, 300, 300);
			}
		};
		
		frame.add(test);
		
		frame.setVisible(true);
	}

}
