package Wall;

import model.Wall;

public class Grass extends Wall {
	/**
	 * 草地构造方法
	 * 
	 * @param x
	 *              初始化横坐标
	 * @param y
	 *              初始化纵坐标
	 */
	public Grass(int x, int y) {
		super(x, y, "wall\\grass.png");// 调用父类构造方法，使用默认草地图片
	}
}