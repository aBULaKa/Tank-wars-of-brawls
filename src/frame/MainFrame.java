package frame;




import java.awt.Container;
import java.awt.event.KeyListener;

/**
 * 创建主窗口
 * @author 曾诗涵
 */
import javax.swing.*;
public  class MainFrame extends JFrame{
	
	/*
	 * 构造一个面板
	 */
	public MainFrame() {
		JFrame frame=new JFrame("坦克大战");//设置标题
		pack();
		setSize(775, 600);//设置大小
		setLocation(700, 300);//设置在屏幕位置
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setPanel(new LoginPanel(this));//跳转到登录页面
		setVisible(true);
		
	}
	
	/**
	 * 设置更换面版
	 * 
	 * @param panel
	 * 	更换的面版
	 */
	public void setPanel(JPanel panel) {
		Container c = getContentPane();// 获取主容器对象
		if(panel instanceof LoginPanel) {
			panel.addKeyListener((KeyListener) panel);
		}
		c.removeAll();// 删除容器中所有组件
		c.add(panel);// 容器添加面板
		c.validate();// 容器重新验证所有组件
	}
}
