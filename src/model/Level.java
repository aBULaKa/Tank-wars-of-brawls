package model;

import java.io.File;
import java.io.FileNotFoundException;

import util.MapIO;
/**
 * �ؿ�
 * @author zsh
 */
public class Level {
	private static int allLevel;//�ؿ�����
	private static int nextLevel=1;//��һ����
	private static int previsousLevel = 1;// ��һ�ؼ�¼
	static{
		readLevel();
	}
	/**
	 * ��ȡ���йؿ���
	 */
	private static void readLevel() {
		try {
			File fileOfMap=new File(MapIO.PathOfMapData);
			if(!fileOfMap.exists()) {
				throw new FileNotFoundException("δ�ҵ���ͼ�ļ�");
			}
			File Map[]=fileOfMap.listFiles();
			allLevel=Map.length;
			if(allLevel==0) {
				throw new FileNotFoundException("δ�ҵ���ͼ�ļ�");
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ��һ��
	 * @return
	 * ��һ�عؿ���
	 */
	public static int getNextLevel() {
		nextLevel++;
		if(nextLevel>allLevel) {
			nextLevel=1;
		}
		return nextLevel;
	}
	/**
	 * ��ȡ�ܹؿ���
	 * @return
	 * �ܹؿ���
	 */
	public static int getAllLevel() {
		return allLevel;
	}
	public static int previsousLevel() {
		return previsousLevel;// ������һ�ص�ֵ
	}
}
