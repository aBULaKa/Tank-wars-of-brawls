package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import Wall.Base;
import Wall.Brick;
import Wall.Grass;
import Wall.Iron;
import Wall.River;
import frame.Direction;
import frame.GamePanel;
import util.AudioPlayer;
import util.AudioUtil;
import frame.CharacterType;

/**
 * �ӵ�
 * @author �Խ���
 *
 */

public class Bullet extends DisplayableImage{
	
	Direction direction;
	static final int LENGTH = 8;// �ӵ��ģ������壩�߳�
	private GamePanel gamePanel;// ��Ϸ���
	private int speed = 7;// �ƶ��ٶ�
	private boolean alive = true;// �ӵ��Ƿ����Ч��
	private boolean isHitIronWall=false;
	CharacterType owner;
	
	/**
	 * 
	 * �ӵ����췽��
	 * 
	 * @param x
	 *              �ӵ��ĳ�ʼ������
	 * @param y
	 *              �ӵ���ʼ������
	 * @param direction
	 *              �ӵ����䷽��
	 * @param gamePanel
	 *              ��Ϸ������
	 * @param owner
	 *              �����ӵ���̹������
	 */
	public Bullet(int x, int y, Direction direction, GamePanel gamePanel,CharacterType type) {
		super(x, y, LENGTH, LENGTH);// ���ø��๹�췽��
		this.direction = direction;
		this.gamePanel = gamePanel;
		this.owner = type;
		init();// ��ʼ�����
	}

	/**
	 * ��ʼ�����
	 */
	private void init() {
		Graphics g = image.getGraphics();// ��ȡͼƬ�Ļ�ͼ����
		g.setColor(Color.BLACK);// ʹ�ú�ɫ��ͼ
		g.fillRect(0, 0, LENGTH, LENGTH);// ����һ����������ͼƬ�ĺ�ɫʵ�ľ���
		g.setColor(Color.BLUE);
		g.fillOval(0, 0, LENGTH, LENGTH);// ����һ����������ͼƬ��ʵ��Բ��
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1);// ��Բ�λ���һ���߿򣬷�ֹ���磬��߼�С1����
	}

	/**
	 * �ӵ��ƶ�
	 */
	public void move() {
		switch (direction) {// �ж��ƶ�����
		case UP:// �������
			upward();// �����ƶ�
			break;
		case DOWN:// �������
			downward();// �����ƶ�
			break;
		case LEFT:// �������
			leftward();// �����ƶ�
			break;
		case RIGHT:// �������
			rightward();// �����ƶ�
			break;
		}
	}

	/**
	 * �����ƶ�
	 */
	private void leftward() {
		x -= speed;// ���������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void rightward() {
		x += speed;// ����������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void upward() {
		y -= speed;// ���������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void downward() {
		y += speed;// ����������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * ����̹��
	 */
	public void hitCharacters() {
		List<Character> tanks = gamePanel.getCharacters();// ��ȡ����̹�˵ļ���
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// ����̹�˼���
			Character t = tanks.get(i);// ��ȡ̹�˶���
			if (t.isAlive() && this.hit(t)) {// ���̹���Ǵ��Ĳ����ӵ�������̹��
				switch (owner) {// �ж��ӵ�����ʲô̹��
				case PLAYER1:// ��������1���ӵ���Ч��ͬ��
				case PLAYER2:// ��������2���ӵ�
					if (t instanceof Enemy) {// ������е�̹���ǵ���
						//alive = false;// �ӵ�����
						this.dispose();
						t.setAlive(false);// ����̹������
					} else if (t instanceof Character) {// ������е������
						//alive = false;// �ӵ�����
						this.dispose();
					}
					break;
				case ENEMY:// ����ǵ��Ե��ӵ�
					if (t instanceof Enemy) {// ������е�̹���ǵ���
						//alive = false;// �ӵ�����
						this.dispose();
					} else if (t instanceof Character) {// ������е������
						//alive = false;// �ӵ�����
						this.dispose();
						t.setAlive(false);// ���̹������
					}
					break;
				default:// Ĭ��
					//alive = false;// �ӵ�����
					this.dispose();
					t.setAlive(false);// ̹������
				}
			}
		}
	}

	/**
	 * ���л���
	 */
	public void hitBase() {
		Base b = gamePanel.getBase();// ��ȡ����
		if (this.hit(b)) {// ����ӵ����л���
			//alive = false;// �ӵ�����
			this.dispose();
			b.setAlive(false);// ��������
		}
	}

	/**
	 * ����ǽ��
	 */
	public void hitWall() {
		List<Wall> walls = gamePanel.getWalls();// ��ȡ����ǽ��
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {// ��������ǽ��
			Wall w = walls.get(i);// ��ȡǽ�����
			if (this.hit(w)) {// ����ӵ�����ǽ��
				if (w instanceof Brick) {// �����שǽ
					new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					this.dispose();
					w.setAlive(false);// שǽ����
				}
				if (w instanceof Iron) {// ����Ǹ�ש
					this.dispose();
					if(this.isHitIronWall) {
						w.setAlive(false);
					}
					new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
				}
				if(w instanceof River) {
					if(this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
				if(w instanceof Grass) {
					if(this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
			}
		}
	}
	/**
	 * �ӵ�����
	 */
	public void hitBullet() {
		List<Bullet> bullets=gamePanel.getBullets();
		for(int i=0;i<bullets.size();i++) {
			Bullet b=bullets.get(i);
			if(this.alive) {
				if(this.hit(b)&&this.owner!=b.owner) {
					b.dispose();//�����ӵ�
					this.dispose();//�����ӵ�
				}
			}
		}
	}
	
	/**
	 * �ƶ������߽�ʱ�����ӵ�
	 */
	private void moveToBorder() {
		if (x < 0 || x > gamePanel.getWidth() - getWidth() || y < 0 || y > gamePanel.getHeight() - getHeight()) {// ����ӵ������뿪��Ϸ���
			this.dispose();// �����ӵ�
		}
	}

	/**
	 * �����ӵ�
	 */
	private synchronized void dispose() {
		this.alive = false;// ����Ч��״̬��Ϊfalse
	}

	/**
	 * ��ȡ�ӵ����״̬
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}
	public void setIsHitIronWall(boolean b) {
		this.isHitIronWall=b;
	}

}
