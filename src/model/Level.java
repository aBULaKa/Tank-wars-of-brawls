package model;

import java.io.File;
import java.io.FileNotFoundException;

import util.MapIO;
/**
 * 关卡
 * @author zsh
 */
public class Level {
	private static int allLevel;//关卡总数
	private static int nextLevel=1;//下一个数
	private static int previsousLevel = 1;// 上一关记录
	static{
		readLevel();
	}
	/**
	 * 获取所有关卡数
	 */
	private static void readLevel() {
		try {
			File fileOfMap=new File(MapIO.PathOfMapData);
			if(!fileOfMap.exists()) {
				throw new FileNotFoundException("未找到地图文件");
			}
			File Map[]=fileOfMap.listFiles();
			allLevel=Map.length;
			if(allLevel==0) {
				throw new FileNotFoundException("未找到地图文件");
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取下一个
	 * @return
	 * 下一关关卡数
	 */
	public static int getNextLevel() {
		nextLevel++;
		if(nextLevel>allLevel) {
			nextLevel=1;
		}
		return nextLevel;
	}
	/**
	 * 获取总关卡数
	 * @return
	 * 总关卡数
	 */
	public static int getAllLevel() {
		return allLevel;
	}
	public static int previsousLevel() {
		return previsousLevel;// 返回上一关的值
	}
}
