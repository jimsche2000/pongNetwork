package main;

import javax.swing.JOptionPane;

import hauptmenu.PongFrame;

public class PongMain {

	public static void main(String[] args) {
		/*
		 * Creating new PongFrame Object
		 * 
		 * PongFram is more than a JFrame.
		 * 
		 * It starts all needed Panels and Threads
		 * 
		 * TODO: Learn more on Multithreading: http://www.ntu.edu.sg/home/ehchua/programming/java/j5e_multithreading.html
		 * 
		 */
		
		new PongFrame();
	}
}