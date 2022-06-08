package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * �ؿ���ʾ���
 * @author ��ʫ��
 */
public class LevelPanel extends JPanel{

	private int numberOflevel=0;//�ؿ���
	private MainFrame frame;//��ʼ����
	private String levelStr;//�ؿ��ַ�
	
	private GameType type;//��Ϸģʽ
	private String ready = "";//׼����ʾ
	/*
	 * �������
	 * @param numberOflever
	 * 					�ؿ���
	 * @param frame
	 * 					������
	 * @param type
	 * 					��Ϸģʽ	
	 */					
	public LevelPanel(int numberOflevel,MainFrame frame,GameType type) {
		this.frame=frame;//��ǰ����
		this.numberOflevel=numberOflevel;//��ǰ�ȼ�
		levelStr="�ؿ�"+numberOflevel;//�ȼ���ʾ
		this.type=type;//ģʽ����
		Thread t=new LevelPanelThread();
		t.start();//�����߳�
	}
	/**
	 * paint��
	 */
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 775, 600);//��ɫ�������
		g.setColor(Color.white);//�����óɰ�ɫ
		g.setFont(new Font("����",Font.BOLD,50));
		g.drawString(levelStr, 320, 270);//�ȼ���ʾ
		g.setColor(Color.red);
		g.drawString("׼����",320,320);//׼����ʾ
	}
	/**
	 * ת����Ϸ����
	 */
	private void gotoGamePanel() {
		System.gc();
		frame.setPanel(new GamePanel(frame, numberOflevel, type));// ��������ת���˹ؿ���Ϸ���
	}
	/**
	 * �ؿ����涯���߳�
	 * 
	 * @author z
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// ѭ��6��
				if (i % 2 == 0) {// ���ѭ��������ż��
					levelStr = "�ؿ�" + numberOflevel;// �ؿ��ַ���������ʾ
				} else {
					levelStr = "";// �ؿ��ַ�������ʾ�κ�����
				}
				if (i == 4) {// ���ѭ����������
					ready = "׼��!";// ׼����ʾ��ʾ����
				}
				repaint();// �ػ����
				try {
					Thread.sleep(500);// ����0.5��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoGamePanel();// ��ת����Ϸ���
		}
	}
	
}
