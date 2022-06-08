package Wall;

import model.Wall;

/**
 * This class models River. It extends {@link Wall} and
 * @author ÔøÊ«º­
 * @version 1.0.0
 * @see Wall
 */
public class River extends Wall{
	/**
	 * Construct a <code>River<code> object
	 * 
	 * @param x  the x coordinate of picture.
	 * @param y  the y coordinate of picture.
	 * @param url the filename of picture.
	 */
	public River(int x,int y) {
		
		super(x,y,"wall\\river.png");
		
	}

}
