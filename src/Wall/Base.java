package Wall;

import model.Wall;

/**
 * 基地
 *
 */
public class Base extends Wall {
	/**
	 * 基地构造方法
	 * 
	 * @param x
	 *              基地横坐标
	 * @param y
	 *              基地纵坐标
	 */
	public Base(int x, int y) {
		super(x, y, "wall\\base.png");// 调用父类构造方法，使用默认基地图片
	}

	

}
