package model;



import java.awt.*;
import java.util.Random;

import frame.GamePanel;

/**
 * 
 * ������
 * 
 *
 */
public class Tool extends DisplayableImage{
	private static String[] imgURL={
			"tool/03.png",
			"tool/04.png",
			"tool/02.png",
			"tool/01.png",
			"tool/05.png",
			"tool/06.png"
	};
	
	private static Image[] toolImages={
			Toolkit.getDefaultToolkit().createImage(imgURL[0]),
			Toolkit.getDefaultToolkit().createImage(imgURL[1]),
			Toolkit.getDefaultToolkit().createImage(imgURL[2]),
			Toolkit.getDefaultToolkit().createImage(imgURL[3]),
			Toolkit.getDefaultToolkit().createImage(imgURL[4]),
			Toolkit.getDefaultToolkit().createImage(imgURL[5])
	};

	private int timer=0;
	private int aliveTime=4500;
	private Random rand=new Random();
	private static int height=20,width=20;
	ToolType type;
	private boolean alive=true;

	/**
	 * ��ȡ����ʵ��
	 */
	private Tool(int x,int y){
		super(x,y,width,height);
		type=ToolType.values()[rand.nextInt(6)];
	}
	public static Tool getToolInstance(int x,int y){
		return new Tool(x,y);
	}
	public void changeToolType(){
		type= ToolType.values()[rand.nextInt(6)];
		x=rand.nextInt(500);
		y=rand.nextInt(500);
		this.alive=true;
	}

	/**
	 * ���Ƶ���
	 */

	public void draw(Graphics g){
		if (timer>aliveTime){
			timer=0;
			setAlive(false);
		}else {
			g.drawImage(toolImages[type.ordinal()],x,y,null );
			timer+=GamePanel.FRESHTIME;
		}
	}

	/**
	 * ���ô��״̬
	 */
	public boolean getAlive(){
		return this.alive;
	}
	public boolean setAlive(boolean alive) {
		this.alive=alive;
		return alive;
	}

}