package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * This class show the displayable images
 * @author �Խ���
 * @version 1.0.0
 *
 */

public class DisplayableImage {
	
	/**
	 * ͼ�������
	 */
	public int x;
	/**
	 * ͼ��������
	 */
	public int y;
	/**
	 * ͼ��Ŀ�
	 */
	int width;
	/**
	 * ͼ��ĸ�
	 */
	int height;
	/**
	 * ͼ�����
	 */
	BufferedImage image;

	/**
	 * ���췽��
	 * @param x  ������
	 * @param y  ������
	 * @param width  ��
	 * @param height ��
	 */
	public DisplayableImage(int x, int y, int width, int height) {
		this.x = x;// ������
		this.y = y;// ������
		this.width = width;// ��
		this.height = height;// ��
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// ʵ����ͼƬ
	}

	/**
	 * ���췽��
	 * @param x  ������
	 * @param y  ������
	 * @param url  ͼƬ·��
	 */
	public DisplayableImage(int x, int y, String url) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(new File(url));// ��ȡ��·����ͼƬ����
			this.width = image.getWidth();// ��ΪͼƬ��
			this.height = image.getHeight();// ��ΪͼƬ��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DisplayableImage() {
		
	}
	/**
	 * ��ȡͼƬ
	 * @return ����ʾ��ͼƬ
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * ����ͼƬ
	 * @param image   ����ʾ��ͼƬ
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * ����ͼƬ
	 * @param image   ����ʾ��ͼƬ
	 */
	public void setImage(String url) {
		try {
			this.image = ImageIO.read(new File(url));// ��ȡָ��λ�õ�ͼƬ
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ж��Ƿ�����ײ
	 * @param v   Ŀ��ͼƬ����
	 * @return ��������ཻ���򷵻�true�����򷵻�false
	 */
	public boolean hit(DisplayableImage v) {
		return hit(v.getRect());
	}

	/**
	 * �ж��Ƿ�����ײ
	 * @param r   Ŀ��߽�
	 * @return ��������ཻ���򷵻�true�����򷵻�false
	 */
	public boolean hit(Rectangle r) {
		if (r == null) {
			return false;
		}
		return getRect().intersects(r);
	}

	/**
	 * ��ȡ�߽����
	 */
	public Rectangle getRect() {
		
		return new Rectangle(x, y, width, height);
	}

	/**
	 * ��ȡͼ��Ŀ�
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ���ÿ�
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ��ȡ��
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ���ø�
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}