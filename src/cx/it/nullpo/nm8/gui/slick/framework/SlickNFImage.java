package cx.it.nullpo.nm8.gui.slick.framework;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import cx.it.nullpo.nm8.gui.framework.NFGraphics;
import cx.it.nullpo.nm8.gui.framework.NFImage;
import cx.it.nullpo.nm8.gui.framework.NFSystem;

/**
 * Slick implementation of NFImage
 */
public class SlickNFImage implements NFImage {
	private static final long serialVersionUID = 7635534276019899998L;

	/** Slick native image */
	protected Image nativeImage;

	/** SlickNFGraphics of this image */
	protected SlickNFGraphics g;

	/** NFSystem */
	protected NFSystem sys;

	/**
	 * Constructor
	 * @param nativeImage Slick native image
	 * @deprecated Use SlickNFImage(Image nativeImage, NFSystem sys) instead
	 */
	public SlickNFImage(Image nativeImage) {
		this.nativeImage = nativeImage;
	}

	/**
	 * Constructor
	 * @param nativeImage Slick native image
	 * @param sys NFSystem
	 */
	public SlickNFImage(Image nativeImage, NFSystem sys) {
		this.nativeImage = nativeImage;
		this.sys = sys;
	}

	/**
	 * Get Slick native image
	 * @return Slick native image
	 */
	public Image getNativeImage() {
		return nativeImage;
	}

	public NFGraphics getGraphics() {
		try {
			if(g != null) return g;
			g = new SlickNFGraphics(nativeImage.getGraphics(), sys);
			return g;
		} catch (SlickException e) {
			throw new RuntimeException("Can't create Graphics context of the Image", e);
		}
	}

	public int getHeight() {
		return nativeImage.getHeight();
	}

	public int getWidth() {
		return nativeImage.getWidth();
	}

	public NFImage getSubImage(int x, int y, int width, int height) {
		NFGraphics g = getGraphics();
		NFSystem sys = g.getNFSystem();
		NFImage newImage = sys.createImage(width, height);
		newImage.getGraphics().drawImage(this, 0, 0, width, height, x, y, x+width, y+height);
		return newImage;
	}
}
