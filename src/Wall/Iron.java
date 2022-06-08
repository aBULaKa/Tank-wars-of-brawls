package Wall;

import model.Wall;

/**
 * This class models Iron. It extends {@link Wall} and
 * @author ÔøÊ«º­
 * @version 1.0.0
 * @see Wall
 */
public class Iron extends Wall{
	/**
	 * Construct a <code>Iron<code> object
	 * 
	 * @param x  the x coordinate of picture.
	 * @param y  the y coordinate of picture.
	 * @param url the filename of picture.
	 */
	public Iron(int x,int y) {
		
		super(x,y,"wall\\iron.png");
		
	}

}
