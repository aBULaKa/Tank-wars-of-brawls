package frame;




import java.awt.Container;
import java.awt.event.KeyListener;

/**
 * ����������
 * @author ��ʫ��
 */
import javax.swing.*;
public  class MainFrame extends JFrame{
	
	/*
	 * ����һ�����
	 */
	public MainFrame() {
		JFrame frame=new JFrame("̹�˴�ս");//���ñ���
		pack();
		setSize(775, 600);//���ô�С
		setLocation(700, 300);//��������Ļλ��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setPanel(new LoginPanel(this));//��ת����¼ҳ��
		setVisible(true);
		
	}
	
	/**
	 * ���ø������
	 * 
	 * @param panel
	 * 	���������
	 */
	public void setPanel(JPanel panel) {
		Container c = getContentPane();// ��ȡ����������
		if(panel instanceof LoginPanel) {
			panel.addKeyListener((KeyListener) panel);
		}
		c.removeAll();// ɾ���������������
		c.add(panel);// ����������
		c.validate();// ����������֤�������
	}
}
