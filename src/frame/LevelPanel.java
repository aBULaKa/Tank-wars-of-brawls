package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * 关卡显示面板
 * @author 曾诗涵
 */
public class LevelPanel extends JPanel{

	private int numberOflevel=0;//关卡数
	private MainFrame frame;//初始窗体
	private String levelStr;//关卡字符
	
	private GameType type;//游戏模式
	private String ready = "";//准备提示
	/*
	 * 构造面版
	 * @param numberOflever
	 * 					关卡数
	 * @param frame
	 * 					主窗体
	 * @param type
	 * 					游戏模式	
	 */					
	public LevelPanel(int numberOflevel,MainFrame frame,GameType type) {
		this.frame=frame;//当前窗口
		this.numberOflevel=numberOflevel;//当前等级
		levelStr="关卡"+numberOflevel;//等级提示
		this.type=type;//模式类型
		Thread t=new LevelPanelThread();
		t.start();//开启线程
	}
	/**
	 * paint类
	 */
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 775, 600);//黑色背景填充
		g.setColor(Color.white);//字设置成白色
		g.setFont(new Font("楷体",Font.BOLD,50));
		g.drawString(levelStr, 320, 270);//等级显示
		g.setColor(Color.red);
		g.drawString("准备！",320,320);//准备提示
	}
	/**
	 * 转到游戏界面
	 */
	private void gotoGamePanel() {
		System.gc();
		frame.setPanel(new GamePanel(frame, numberOflevel, type));// 主窗体跳转到此关卡游戏面板
	}
	/**
	 * 关卡版面动画线程
	 * 
	 * @author z
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// 循环6次
				if (i % 2 == 0) {// 如果循环变量是偶数
					levelStr = "关卡" + numberOflevel;// 关卡字符串正常显示
				} else {
					levelStr = "";// 关卡字符串不显示任何内容
				}
				if (i == 4) {// 如果循环变量等于
					ready = "准备!";// 准备提示显示文字
				}
				repaint();// 重绘组件
				try {
					Thread.sleep(500);// 休眠0.5秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoGamePanel();// 跳转到游戏面板
		}
	}
	
}
