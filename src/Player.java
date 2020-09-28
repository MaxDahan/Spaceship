import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	Rectangle r = new Rectangle(); // current x, y, width, and height
	Rectangle b = new Rectangle(); // boundaries for collisions
	int velX = 0, velY = 0, speed = 3;
	BufferedImage image;
	private static final double REDUX = 0.06;
	private static final int REPOS = 20;

	public Player() throws Exception {
		image = SpaceWindow.pImg;
		setup();
	}

	public void setup() {
		r = new Rectangle(0, SpaceMain.GHEIGHT / 2 - image.getHeight(), image.getWidth(), image.getHeight());
		b.setBounds((int) (r.getX() + (image.getWidth() * REDUX)), (int) (r.getY() + (image.getHeight() * REDUX)),
				(int) (image.getWidth() - (image.getWidth() * REDUX * 2)),
				(int) (image.getHeight() - (image.getHeight() * REDUX * 2)));
	}

	public void offScreen() {
		if (r.getY() + r.getHeight() + REPOS * 2 < 0) {
			r.setLocation((int) r.getX(), SpaceMain.GHEIGHT);
			b.setLocation((int) r.getX() + (int) (image.getWidth() * REDUX),
					(int) r.getY() + (int) (image.getHeight() * REDUX));
		} else if (r.getY() - REPOS * 2 > SpaceMain.GHEIGHT) {
			r.setLocation((int) r.getX(), 0 - (int) r.getHeight());
			b.setLocation((int) r.getX() + (int) (image.getWidth() * REDUX),
					(int) r.getY() + (int) (image.getHeight() * REDUX));
		}
	}

	public void update() {
		if(r.getX() + velX < -r.getWidth() / 2){
			r.setLocation((int) r.getX(), (int) r.getY() + velY);
			b.setLocation((int) r.getX(), (int) r.getY() + REPOS / 4);
		} else if(r.getX() + velX + r.getWidth()/2 > SpaceMain.GWIDTH) {
			r.setLocation((int) r.getX(), (int) r.getY() + velY);
			b.setLocation((int) r.getX(), (int) r.getY() + REPOS / 4);
		} else {
			r.setLocation((int) r.getX() + velX, (int) r.getY() + velY);
			b.setLocation((int) r.getX() + REPOS / 4, (int) r.getY() + REPOS / 4);
		}
		offScreen();
	}

	public void draw(Graphics g) {
		// draws player
		g.drawImage(image, (int) r.getX(), (int) r.getY(), null);

		// hitboxes
//		g.setColor(Color.RED);
//		g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
	}

	public Rectangle getBounds() {
		return b;
	}
}
