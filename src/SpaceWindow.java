import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class SpaceWindow extends JPanel implements Runnable, KeyListener {
	// Lists
	private List<String> s = new ArrayList<String>();
	private List<ScrollBG> scrollbg = new ArrayList<ScrollBG>();
	private List<Asteroid> a = new ArrayList<Asteroid>();
	private List<Bullet> bullets = new ArrayList<Bullet>();
	// Images
	static BufferedImage boomImg, pImg, pImg2, bImg;
	private BufferedImage bg;
	// Booleans
	private boolean play = true, flashHigh = false, dead = false, addBullet = false;
	// Numbers
	private double time = System.currentTimeMillis();
	static int high, score, preHigh;
	// Utilies and personal classes
	public static Random rand = new Random();
	private Player p;
	private Thread thread;
	private Clip sound = null;

	public SpaceWindow() throws Exception {
		boomImg = ImageIO.read(new File("data/Boom/boom.png"));
		pImg = ImageIO.read(new File("data/Spaceboi/spaceboi.png"));
		pImg2 = ImageIO.read(new File("data/Spaceboi/spaceboi2.png"));
		bImg = ImageIO.read(new File("data/bullet/bullet.png"));
		playMusic("spaceboi", true);
		// player boundaries and draw
		p = new Player();

		// gets the background image
		bg = ImageIO.read(new File("data/Space/bg.png"));
		// scrollbg = ImageIO.read(new File("data/Space/spaceopaque.png"));
		for (int i = 1; i <= 30; i++) {
			scrollbg.add(new ScrollBG(i));
		}

		// grabs the high score and sets it to 0 if there is nothing
		// has a try catch in case the user does not have high.txt,
		// which would then make it create one
		try {
			if (!new Scanner(new File("data/data.txt")).hasNext())
				high = 0;
			else
				high = new Scanner(new File("data/data.txt")).nextInt();
		} catch (Exception e) {
			PrintStream out = new PrintStream("data/data.txt");
			out.print(0);
			high = 0;
		}
		preHigh = high;

		// run method in this class
		thread = new Thread(this);
		thread.start();

		// starts the checker to see if a bullet has collided with an asteroid
		new CheckAstShot().start(); // stands for check asteroidshot

		// astroid generation thread
		new CreateAsteroids().start();

		// timer thread for score
		new Timer().start();

		new AddBullet().start();
	}

	public void paint(Graphics g) {
		repaint();
		if (play) {
			// draws stationary background
			g.drawImage(bg, 0, 0, null);

			// draws moving background
			scrollbg.get(0).x -= p.velX + 5;
			scrollbg.get(1).x -= p.velX + 5;
			scrollbg.get(2).x -= p.velX + 5;
			scrollbg.get(3).x -= p.velX + 5;

			if (scrollbg.get(0).x + scrollbg.get(0).img.getWidth() < 0) {
				ScrollBG tempSBG = scrollbg.get(0);
				scrollbg.remove(0);
				scrollbg.add(tempSBG);
			}
			// g.drawRect(scrollbg.get(0).x, scrollbg.get(0).y, 640, 1080);
			g.drawImage(scrollbg.get(0).img, scrollbg.get(0).x, scrollbg.get(0).y, null);
			if (scrollbg.get(0).x + scrollbg.get(0).img.getWidth() < 1920) {
				scrollbg.get(1).x = scrollbg.get(0).x + scrollbg.get(0).img.getWidth();
				// g.drawRect(scrollbg.get(1).x, scrollbg.get(1).y, 640, 1080);
				g.drawImage(scrollbg.get(1).img, scrollbg.get(1).x, scrollbg.get(1).y, null);
				if (scrollbg.get(1).x + scrollbg.get(1).img.getWidth() <= 1920) {
					scrollbg.get(2).x = scrollbg.get(1).x + scrollbg.get(1).img.getWidth();
					// g.drawRect(scrollbg.get(2).x, scrollbg.get(2).y, 640, 1080);
					g.drawImage(scrollbg.get(2).img, scrollbg.get(2).x, scrollbg.get(2).y, null);
					if (scrollbg.get(2).x + scrollbg.get(2).img.getWidth() <= 1920) {
						scrollbg.get(3).x = scrollbg.get(2).x + scrollbg.get(2).img.getWidth();
						// g.drawRect(scrollbg.get(3).x, scrollbg.get(3).y, 640, 1080);
						g.drawImage(scrollbg.get(3).img, scrollbg.get(3).x, scrollbg.get(3).y,
								null);
					}
				}
			}
			// draws all of the asteroids
			for (int i = 0; i < a.size(); i++) {
				a.get(i).draw(g);
			}

			// draws bullets
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).draw(g);
			}

			// draws player and boundary
			p.draw(g);

			// draws time
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 60));
			g.drawString("Time // "
					+ ((double) Math.round((System.currentTimeMillis() - time) / 10)) / 100, 20,
					60);
			if (high < score)
				high = score;

			// draws score and flashes the color if you get a highscore
			if (!flashHigh) {
				g.setColor(Color.WHITE);
			}
			if (flashHigh) {
				g.setColor(Color.RED);
			}
			g.drawString("Score // " + score, SpaceMain.GWIDTH / 2 - 500, 950);
			g.drawString("Highscore // " + (high), SpaceMain.GWIDTH / 2, 950);
		}
	}

	private class Timer extends Thread {
		public void run() {
			while (true) {
				if (!dead && play) {
					slep(1, this);
					time += 0.001;
				}
			}
		}
	}

	public void slep(int time, Thread t) {
		try {
			t.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class CreateAsteroids extends Thread {
		public void run() {
			while (true) {
				int wait = rand.nextInt(3) + 1;
				slep(wait * 1000, this);
				try {
					a.add(new Asteroid());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void gameUpdate() throws Exception {
		if (play) {
			if (score % 8 < 4) {
				p.image = pImg;
			} else
				p.image = pImg2;
			p.update();
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).update();
			}
			for (int i = 0; i < a.size(); i++) {
				a.get(i).update();
				if (a.get(i).getBounds().intersects(p.getBounds())) { // collision
					playMusic("boom", false);
					System.currentTimeMillis();
					dead = true;
					p.image = boomImg;
					if (score > preHigh) {
						playMusic("levelup", false);
						preHigh = score;
						high = score;
						flashHigh = true;
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom2.png"));
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom3.png"));
						flashHigh = false;
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom4.png"));
						Thread.sleep(500);
						flashHigh = true;
						Thread.sleep(500);
						flashHigh = false;
					} else {
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom2.png"));
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom3.png"));
						Thread.sleep(250);
						p.image = ImageIO.read(new File("data/Boom/boom4.png"));
						Thread.sleep(250);
					}
					bullets.clear();
					playMusic("spaceboi", true);
					p.image = pImg;
					time = System.currentTimeMillis();
					score = 0;
					p.setup();
					a.removeAll(a);
					dead = false;
				}
			}
			
		}
	}

	public class CheckAstShot extends Thread {
		public void run() {
			while (true) {
				for (int i = 0; i < bullets.size(); i++) {
					for (int j = 0; j < a.size(); j++) {
						if (bullets.get(i).b.intersects(a.get(j).b)) {
							bullets.remove(i);
							a.get(j).health--;
							j = a.size() - 1;
							i--;
						}
					}
				}
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {
		while (true) {
			int fps = 200;
			long msPerFrame = 1000 * 1000000 / fps;
			long lastTime = 0;
			long elapsed;

			int msSleep;
			int nanoSleep;

			while (true) {
				try {
					gameUpdate();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				repaint();
				elapsed = (lastTime + msPerFrame - System.nanoTime());
				msSleep = (int) (elapsed / 1000000);
				nanoSleep = (int) (elapsed % 1000000);
				if (msSleep <= 0) {
					lastTime = System.nanoTime();
					continue;
				}
				try {
					Thread.sleep(msSleep, nanoSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lastTime = System.nanoTime();
			}
		}
	}

	public void saveHigh() {
		PrintStream out = null;
		try {
			out = new PrintStream(new File("data/data.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.print(high + "");
	}

	public class AddBullet extends Thread {
		public void run() {
			while (true) {
				if (addBullet) {
					try {
						bullets.add(new Bullet(p.r.x + 35, p.r.y + 98, bImg));
						bullets.add(new Bullet(p.r.x + 35, p.r.y + 35, bImg));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			try {
				addBullet = true;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (key == KeyEvent.VK_C) {
			saveHigh();
			System.exit(0);
		}
		if (key == KeyEvent.VK_R) {
			preHigh = high = 0;
		}
		if (key == KeyEvent.VK_DOWN) {
			if (!s.contains("d")) {
				s.add("d");
				p.velY = p.speed;
			}
		}
		if (key == KeyEvent.VK_UP) {
			if (!s.contains("u")) {
				s.add("u");
				p.velY = -p.speed;
			}
		}
		if (key == KeyEvent.VK_RIGHT) {
			if (!s.contains("r")) {
				s.add("r");
				p.velX = p.speed;
			}
		}
		if (key == KeyEvent.VK_LEFT) {
			if (!s.contains("l")) {
				s.add("l");
				p.velX = -p.speed;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			addBullet = false;
		}
		if (key == KeyEvent.VK_UP) {
			s.remove("u");
			if (s.contains("d"))
				p.velY = p.speed;
			else
				p.velY = 0;
		}
		if (key == KeyEvent.VK_DOWN) {
			s.remove("d");
			if (s.contains("u"))
				p.velY = -p.speed;
			else
				p.velY = 0;
		}
		if (key == KeyEvent.VK_RIGHT) {
			s.remove("r");
			if (s.contains("l"))
				p.velX = -p.speed;
			else
				p.velX = 0;
		}
		if (key == KeyEvent.VK_LEFT) {
			s.remove("l");
			if (s.contains("r"))
				p.velX = p.speed;
			else
				p.velX = 0;
		}
	}

	public void playMusic(String soundName, boolean isMusic) throws Exception {
		if (sound != null)
			sound.stop();
		AudioInputStream noise = AudioSystem
				.getAudioInputStream(new File("data/Sound/" + soundName + ".wav"));
		sound = AudioSystem.getClip();
		sound.open(noise);
		if (isMusic)
			sound.loop(Clip.LOOP_CONTINUOUSLY);
		else
			sound.start();
	}

	// has to be implemented but not used
	public void keyTyped(KeyEvent e) {
	}
}
