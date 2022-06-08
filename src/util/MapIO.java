package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import Wall.Brick;
import Wall.Grass;
import Wall.Iron;
import Wall.River;
import frame.MapEditorPanel;
import frame.WallType;
import model.Wall;

/**
 * ��ͼ��Ϣ
 * @author zsh
 *
 */
public class MapIO {
	public final static String PathOfMapData="mapdata\\";//��ͼ����������ļ��ڵ���������
	public final static String PathOfMapImage="level\\";//�����ؿ��ĵ�ͼͼ��·��
	
	/**
	 * ��ȡÿ�ŵ�ͼ��Ӧ��ǽ��ļ���
	 */
	public static List<Wall> readMap(String mapName){
		File file=new File(PathOfMapData+mapName+".map");//���������ļ���׺Ϊ.map,����������Ϊ��ͼ��+��׺
		return readMap(file);
	}
	/**
	 * ��ǽ�鼯�ϵ�ָ���ļ�
	 */
	public static boolean writeMap(String mapName) {
		File file =new File(PathOfMapData+mapName+".map");
		return writeMap(file);
	}
	/**
	 * д��ǽ�鼯�ϵ�ָ���ļ�
	 */
	public static boolean writeMap(File file) {
		StringBuffer brickBuffer=new StringBuffer("BRICK=");//��������שǽ������
		StringBuffer grassBuffer=new StringBuffer("GRASS=");//�������вݵص�����
		StringBuffer ironBuffer=new StringBuffer("IRON=");//�����������������
		StringBuffer riverBuffer=new StringBuffer("RIVER=");//�������к���������
		List<Wall> walls=MapEditorPanel.walls;//��ȡ��ͼ�༭�����ǽ�鼯��
		for(int i=0;i<walls.size();i++) {
			Wall w=walls.get(i);//��ȡÿ��ǽ�����
			if(w.isAlive()) {//������
				if(w instanceof Brick) {
					brickBuffer.append(w.x+","+w.y+";");//�ӵ�שǽ�����
				}
				else if(w instanceof Grass) {
					grassBuffer.append(w.x+","+w.y+";");//�ӵ��ݵ������
				}
				else if(w instanceof Iron) {
					ironBuffer.append(w.x+","+w.y+";");//�ӵ���ǽ�����
				}
				else if(w instanceof River) {
					riverBuffer.append(w.x+","+w.y+";");//�ӵ����������
				}
			}
		}
		BufferedWriter writer=null;//�ļ�д��
		try {
			
			writer=new BufferedWriter(new FileWriter(file));
			if(!brickBuffer.toString().equals("BRICK=")) {
				writer.write(brickBuffer.toString().toCharArray());
				writer.newLine();
				writer.flush();
			}
			if(!grassBuffer.toString().equals("GRASS=")) {
				writer.write(brickBuffer.toString().toCharArray());
				writer.newLine();
				writer.flush();
			}
			if(!ironBuffer.toString().equals("IRON=")) {
				writer.write(brickBuffer.toString().toCharArray());
				writer.newLine();
				writer.flush();
			}
			if(!riverBuffer.toString().equals("RIVER=")) {
				writer.write(brickBuffer.toString().toCharArray());
				writer.newLine();
				writer.flush();
			}
			if(writer!=null) {
				writer.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * ��ȡ��ͼ����ǽ��
	 */
	public static List<Wall> readMap(File file){
		Properties pro = new Properties();// �������Լ�����
		List<Wall> walls = new ArrayList<>();// ������ǽ�鼯��
		try {
			pro.load(new FileInputStream(file));// ���Լ������ȡ��ͼ�ļ�
			String brickStr = (String) pro.get(WallType.BRICK.name());// ��ȡ��ͼ�ļ���שǽ�������Ե��ַ�������
			String grassStr = (String) pro.get(WallType.GRASS.name());// ��ȡ��ͼ�ļ��вݵ��������Ե��ַ�������
			String riverStr = (String) pro.get(WallType.RIVER.name());// ��ȡ��ͼ�ļ��к����������Ե��ַ�������
			String ironStr = (String) pro.get(WallType.IRON.name());// ��ȡ��ͼ�ļ�����ǽ�������Ե��ַ�������
			if (brickStr != null) {// �����ȡ��שǽ���ݲ���null
				walls.addAll(readWall(brickStr, WallType.BRICK));// �������ݣ����������н�������ǽ�鼯����ӵ���ǽ�鼯����
			}
			if (grassStr != null) {// �����ȡ�Ĳݵ����ݲ���null
				walls.addAll(readWall(grassStr, WallType.GRASS));// �������ݣ����������н�������ǽ�鼯����ӵ���ǽ�鼯����
			}
			if (riverStr != null) {// �����ȡ�ĺ������ݲ���null
				walls.addAll(readWall(riverStr, WallType.RIVER));// �������ݣ����������н�������ǽ�鼯����ӵ���ǽ�鼯����
			}
			if (ironStr != null) {// �����ȡ����ǽ���ݲ���null
				walls.addAll(readWall(ironStr, WallType.IRON));// �������ݣ����������н�������ǽ�鼯����ӵ���ǽ�鼯����
			}
			return walls;// ������ǽ�鼯��
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static List<Wall> readWall(String data, WallType type) {
		String walls[] = data.split(";");// ʹ�á�;���ָ��ַ���
		Wall wall;// ����ǽ�����
		List<Wall> w = new ArrayList<>();// ����ǽ�鼯��
		switch (type) {// �ж�ǽ������
		case BRICK:// �����שǽ
			for (String wStr : walls) {// �����ָ���
				String axes[] = wStr.split(",");// ʹ�á�,���ָ��ַ���
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ָ�ĵڶ���ֵΪ������
				wall = new Brick(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));// �ڴ������ϴ���שǽ����
				w.add(wall);// ��������Ӵ�ǽ��
			}
			break;
		case RIVER:// ����Ǻ���
			for (String wStr : walls) {// �����ָ���
				String axes[] = wStr.split(",");// ʹ�á�,���ָ��ַ���
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ָ�ĵڶ���ֵΪ������
				wall = new River(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));// �ڴ������ϴ�����������
				w.add(wall);// ��������Ӵ�ǽ��
			}
			break;
		case GRASS:// ����ǲݵ�
			for (String wStr : walls) {// �����ָ���
				String axes[] = wStr.split(",");// ʹ�á�,���ָ��ַ���
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ָ�ĵڶ���ֵΪ������
				wall = new Grass(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));// �ڴ������ϴ����ݵض���
				w.add(wall);// ��������Ӵ�ǽ��
			}
			break;
		case IRON:// �������ǽ
			for (String wStr : walls) {// �����ָ���
				String axes[] = wStr.split(",");// ʹ�á�,���ָ��ַ���
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ָ�ĵڶ���ֵΪ������
				wall = new Iron(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));// �ڴ������ϴ�����ǽ����
				w.add(wall);// ��������Ӵ�ǽ��
			}
			break;
		default:
			break;
		}
		return w;// ����ǽ�鼯��
	}
	public static void removeDuplicate(List list) {
		HashSet h=new HashSet(list);
		list.clear();
		list.addAll(h);
	}
}
