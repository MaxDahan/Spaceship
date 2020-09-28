import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Asteroid {
	Rectangle r = new Rectangle(); // current x, y, width, and height
	Rectangle b = new Rectangle(); // boundaries for collisions
	public int speed, velX = -speed, velY = 0, ranY, ranS, rotate, rSpeed, health;
	public BufferedImage image;
	private static final double REDUX = 0.125;

	public Asteroid() throws Exception {	
		rotate = SpaceWindow.rand.nextInt(360);
		int rMuliplier = 30;
		rSpeed = SpaceWindow.rand.nextInt(rMuliplier);
		rSpeed -= rMuliplier/2;
		
		int ranY = SpaceWindow.rand.nextInt(SpaceMain.GHEIGHT + 200) - 100; //random y spawn
		int ranS = SpaceWindow.rand.nextInt(8);//random size
		if(ranS < 7) {
			health = 2;
			image = ImageIO.read(new File("data/Potate/spotate.png"));
			speed = (SpaceWindow.rand.nextInt(2)+1)*2;
		} else if(ranS == 7) {
			health = 6;
			image = ImageIO.read(new File("data/Potate/botate.png"));
			speed = 1;
		}
		
		r = new Rectangle(SpaceMain.GWIDTH, ranY, image.getWidth(), image.getHeight());
		
		b = new Rectangle(
				(int) (r.getX() + r.getWidth()*REDUX),
				(int) (r.getY() + r.getHeight()*REDUX),
				(int) (r.getWidth()*(1-REDUX*2)),
				(int) (r.getHeight()*(1-REDUX*2)));
	}

	public void update() {
		velX = -speed;
		r.setLocation((int) r.getX() + velX, (int) r.getY() + velY);
		b.setLocation((int) (r.getX() + r.getWidth() * REDUX), (int)(r.getY() + r.getHeight() * REDUX));
		if (r.getX() + r.getWidth() < 0 || health <= 0) { //if flew outta screen
			SpaceWindow.score++;
			r.setLocation(SpaceMain.GWIDTH, SpaceWindow.rand.nextInt(SpaceMain.GHEIGHT + 200) - 100);
			health = 2;
			if (ranS != 7) {
				speed = (SpaceWindow.rand.nextInt(2)+1)*2;
			}
			else {
				SpaceWindow.score += 2;
				health = 6;
			}
		}
	}
	
	public void draw(Graphics g) {
		rotate += rSpeed;
		
		// draws the asteroid image
		
		//some bullshit
		double rotationRequired = Math.toRadians (rotate);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		//op.filter(image, null)
		AffineTransformOp op = new AffineTransformOp( AffineTransform.getRotateInstance(rotationRequired, locationX, locationY), AffineTransformOp.TYPE_BILINEAR);
		
		g.drawImage(op.filter(image, null), (int) r.getX(), (int) r.getY(), null);

		// hitboxes
//		g.setColor(Color.RED);
//		g.drawRect((int) b.getX(), (int) b.getY(), (int)b.getWidth(), (int)b.getHeight());
	}
	
	public Rectangle getBounds() {
		return b;
	}
}
