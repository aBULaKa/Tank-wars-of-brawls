package frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Wall.Base;
import Wall.Brick;
import Wall.Grass;
import Wall.Iron;
import Wall.River;
import model.Level;
import model.Map;
import model.Wall;
import util.MapIO;


/**
 * 
 * @author �Խ���
 * @version 1.0.0
 */

public class MapEditorPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;

	//ǽ��ͼƬ����
	private Image[] wallImgs= {
		Toolkit.getDefaultToolkit().createImage("Wall\\brick.png"),
		Toolkit.getDefaultToolkit().createImage("Wall\\grass.png"),
		Toolkit.getDefaultToolkit().createImage("Wall\\iron.png"),
		Toolkit.getDefaultToolkit().createImage("Wall\\river.png")
	};

	private WallType wallType;
	private Graphics gra;

	int count=Level.getAllLevel();
	int level=1;
	public static List<Wall> walls=Map.getWalls();
	private Base base;
	private MainFrame frame;

	
	public  MapEditorPanel(final MainFrame frame) {
		this.frame=frame;
		this.addMouseListener(this);
		
		base = new Base(360, 520);
		//��ʼ����ͼ
		initWalls();
		
		JButton save=new JButton("����");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				count++;
				Boolean b = MapIO.writeMap(count+"");
				if(b) {
					JOptionPane.showMessageDialog(null, "����ɹ�");
				}
				repaint();
			}
		});
		JButton back=new JButton("����");
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.requestFocus();
				gotoLoginPanel();
			}

			
		});
		this.add(save);
		this.add(back);
		
	}
	private void gotoLoginPanel() {
		frame.removeMouseListener(this);
		frame.setPanel(new LoginPanel(frame));
	}
	@Override
	public void paint(Graphics g) {
		super.setBackground(Color.BLACK);
		super.paint(g);
		gra=g;
		
		g.setColor(Color.ORANGE);
		
		g.drawString("��ǰ�ؿ���"+level, 0, 12);
		g.drawString("�ؿ�������"+count, 0, 24);
		g.setColor(Color.CYAN);
		// ���������߶Σ��ο���
		for(int i=0;i<560;i+=40) {
			g.drawLine(0, i, 760, i);
		}
		// ���������߶�
		for(int j=0;j<780;j+=40) {
			//g.drawLine(x1, y1, x2, y2);
			g.drawLine(j, 0, j, 600);
		}
		// ���������̶���ǽ��ͼ
		g.drawImage(wallImgs[0], 762, 0, this);
		g.drawImage(wallImgs[1], 762, 20, this);
		g.drawImage(wallImgs[2], 762, 40, this);
		g.drawImage(wallImgs[3], 762, 60, this);
		
		// ��һ������
		g.setColor(Color.MAGENTA);
//		
		g.drawRect(762, 80, 20, 19);

		//������ͼ
		paintWalls();
	}
	/**
	 * ����ǽ��
	 */
	private void paintWalls() {
		for (int i = 0; i < walls.size(); i++) {// ѭ������ǽ�鼯��
			Wall w = walls.get(i);// ��ȡǽ�����
			if(w.x>=760) {
				w.setAlive(false);
				walls.remove(w);
			}
			if (w.isAlive()&&(w.x<760)) {// ���ǽ����Ч
				gra.drawImage(w.getImage(), w.x, w.y, this);// ����ǽ��
			} else {// ���ǽ����Ч
				walls.remove(i);// �ڼ����Єh����ǽ��
				i--;// ѭ������-1����֤�´�ѭ��i��ֵ������i+1���Ա���Ч�������ϣ��ҷ�ֹ�±�Խ��
			}
		}
	}
	/**
	 * ��ʼ��ǽ��
	 */
	public void initWalls() {
		Random r=new Random();
		level=r.nextInt(count)+1;// �����ȡһ���ؿ�
		Map.getMap(level);// ��ȡ��ǰ�ؿ��ĵ�ͼ����
		walls.add(base);// ǽ�鼯����ӻ���
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point clickedPoint=e.getPoint();
		if(clickedPoint.x>=762&&clickedPoint.y<=100) {
			if(clickedPoint.y>0&&clickedPoint.y<20) {
				wallType=WallType.BRICK;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>20&&clickedPoint.y<40) {
				wallType=WallType.GRASS;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>40&&clickedPoint.y<60) {
				wallType=WallType.IRON;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>60&&clickedPoint.y<80) {
				wallType=WallType.RIVER;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>80&&clickedPoint.y<100) {
				wallType=null;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point p=new Point();
		p=e.getPoint();//��ȡ�����ĵ�ǰ�ͷŵ�
		p=new Point((p.x-p.x%20),(p.y-p.y%20));//�����ĵ�ǰ���µ��ʽ��Ϊ20�ı�������Ϊǽ����20x20��
		Point base1=new Point(360,520);
		Point base2=new Point(380,520);
		Point base3=new Point(360,540);
		Point base4=new Point(380,540);
		if((p.x<760)&&!p.equals(base1)&&!p.equals(base2)&&!p.equals(base3)&&!p.equals(base4)) {
			//�������ϣ��ж��ͷŵ��Ƿ���ǽ�飬����оͲ���
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.x>=760||(w.x==p.x&&w.y==p.y&&!w.equals(base))) {
					//w.setAlive(false);
					walls.remove(w);
					repaint();
				}
			}
			//��������д�����쳣�����Ǹ��߼��쳣������쳣���������޸��쳣
//			for(Wall w:walls) {
//				if(w.x==p.x&&w.y==p.y) {
//					walls.remove(w);
//					repaint();
//				}
//			}
			if(wallType!=null) {//���ǽ�����Ͳ�Ϊ�գ����ǽ��
				addWall(wallType, p);
			}
		}
	}
	private void addWall(WallType type,Point p) {
		switch(type) {
		case BRICK:
			Brick b=new Brick(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(b)) {
					walls.remove(w);
				}
			}
			walls.add(b);
			break;
		case GRASS:
			Grass grass=new Grass(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(grass)) {
					walls.remove(w);
				}
			}
			walls.add(grass);
			break;
		case IRON:
			Iron iron=new Iron(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(iron)) {
					walls.remove(w);
				}
			}
			walls.add(iron);
			break;
		case RIVER:
			River river=new River(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(river)) {
					walls.remove(w);
				}
			}
			walls.add(river);
			break;
		default:
			break;
		}
		repaint();
	}

}
