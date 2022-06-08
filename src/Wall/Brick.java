package Wall;

import model.Wall;

/**
 * This class models wood. It extends {@link Wall} and
 * @author ÔøÊ«º­
 * @version 1.0.0
 * @see Wall
 */
public class Brick extends Wall{
	/**
	 * Construct a <code>Wood<code> object
	 * 
	 * @param x  the x coordinate of picture.
	 * @param y  the y coordinate of picture.
	 * @param url the filename of picture.
	 */
	public Brick(int x,int y) {
		
		super(x,y,"wall\\brick.png");
		
	}
	
}
