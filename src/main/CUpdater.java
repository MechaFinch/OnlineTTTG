package main;

import java.awt.Canvas;

//Thread that calls the game canvas's paint method periodically
public class CUpdater extends Thread{
	Canvas c;
	int frameRate;
	
	//Give it the canvas and framerate
	public CUpdater(Canvas c, int frameRate) {
		this.c = c;
		this.frameRate = frameRate;
	}
	
	//Method run when the thread is started
	public void run() {
		while(true) {
			//Call paint with the proper graphics context
			c.paint(c.getBufferStrategy().getDrawGraphics());
			
			//Wait for the next frame
			try {
				Thread.sleep(1000 / frameRate);
			} catch(InterruptedException e) {
				System.out.println("Canvas updater interrupted");
			}
		}
	}
}
