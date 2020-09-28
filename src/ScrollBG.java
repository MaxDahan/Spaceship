import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScrollBG {
	protected int i = 0, x = 0, y = 0;
	protected BufferedImage img;
	
	public ScrollBG(int i) throws IOException {
		this.i = i;
		img = ImageIO.read(new File("data/Space/cutUp/spaceopaque" + i + ".png"));
	}
}
