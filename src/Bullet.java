import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
	BufferedImage img;
	int velX = 5, velY = 0;
	Rectangle r = new Rectangle(); // current x, y, width, and height
	Rectangle b = new Rectangle(); // boundaries for collisions
	
	public Bullet(int X, int Y, BufferedImage IMG) throws Exception {
		img = IMG;
		r = new Rectangle(X, Y, img.getWidth(), img.getHeight());
		b = new Rectangle(X, Y, img.getWidth(), img.getHeight());
	}
	
	public void update() {
		r.setLocation((int)r.getX() + velX, (int)r.getY() + velY);
		b = r;
	}
	
	public void draw(Graphics g) {
		// draws actual bullet
		g.drawImage(img, (int)r.getX(), (int)r.getY(), null);
		
		// draws hitbox
//		g.drawRect((int) b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
	}
}
