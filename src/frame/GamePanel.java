package frame;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Wall.Base;
import model.Boom;
import model.Bullet;
import model.Enemy;
import model.Level;
import model.Wall;
import model.Character;
import util.AudioPlayer;
import util.AudioUtil;
import model.Map;
import model.Tool;;

public class GamePanel extends JPanel implements KeyListener{
	public static final int FRESHTIME = 20;
	MainFrame frame;//面板
	GameType type;//游戏类型
	AudioPlayer music;
	private List<Bullet> bullets;//子弹
	private List<Character> Tanks;//坦克
	private List<Character> EnemyTanks;//敌人坦克
	private List<Character> Players;//玩家
	private List<Wall> walls;//所有墙
	private List<Boom> booms;//所有爆炸效果
	private BufferedImage image;//背景图片
	private Graphics g;//绘图
	private Character play1 ,play2,survivor;//玩家1和玩家2
	private int enemyCount=5;
	private int EnemyRestCount=enemyCount;//初始坦克数
	private int EnemyReadyCount=enemyCount;//总坦克数
	private Base base;//基地
	private boolean finish=false;//游戏结束的标识
	private Random random=new Random();
	private int EnemyTimer=0;//敌方坦克刷新时间
	private int[]  EnemyX= {10,367,754};//敌方坦克出生横坐标
	private List<AudioClip> audios=AudioUtil.getAudios();
	private boolean y_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num1_key;//按键操作
	private	int toolTimer=0;//道具出现的计时器
	private Random r = new Random();// 道具随机刷新
	private Tool tool=Tool.getToolInstance(r.nextInt(500),r.nextInt(500));
	private int level=Level.previsousLevel();// 关卡值
	private int pauseTimer=0;// 敌方暂停计时器
	/**
	 * 面板构造
	 * @param level
	 * 				关卡
	 * @param frame
	 * 				窗体
	 * @param gameType
	 * 				游戏模式
	 */
	public GamePanel(MainFrame frame,int level,GameType gameType) {
		this.level=level;
		this.frame=frame;
		setBackground(Color.black);
		frame.setSize(775, 600);
		this.level=level;
		this.type=gameType;
		init();//初始化
		
		Thread t=new FreshThread();
		t.start();
		music=new AudioPlayer(AudioUtil.START);//音效播放路径
		music.new AudioThread().start();//开始播放
		
		
		
		addListener();//开启监听
		
	}

	/**
	 * 初始化
	 */
	
	private void init() {
		bullets=new ArrayList<>();//实例化子弹
		Tanks=new ArrayList<>();//实例化坦克
		walls=new ArrayList<>();//实例化墙
		booms=new ArrayList<>();//实例化爆炸效果
		image=new BufferedImage(794, 572, BufferedImage.TYPE_INT_BGR);
		g=image.getGraphics();//获取图片
		Players=new Vector<>();//实例化玩家
		
		play1=new Character(278, 537, "player//left_player_1.png", this, CharacterType.PLAYER1);//玩家初始位置
		Players.add(play1);
		if(type==GameType.TWO_PLAYER) {
			play2=new Character(448,573,"player//left_player2.png",this,CharacterType.PLAYER2);
			Players.add(play2);
		}
		
		
		EnemyTanks=new ArrayList<>();
		EnemyTanks.add(new Enemy(10,1,this,CharacterType.ENEMY));
		EnemyTanks.add(new Enemy(367,1,this,CharacterType.ENEMY));
		EnemyTanks.add(new Enemy(754,1,this,CharacterType.ENEMY));
		EnemyReadyCount=EnemyReadyCount-3;
		Tanks.addAll(Players);
		Tanks.addAll(EnemyTanks);
		base=new Base(360,520);
		
		initWalls();
	}
	/**
	 * 监听
	 */
	private void addListener() {
		frame.addKeyListener( this);
	}
	/**
	 * 墙块
	 */
	public void initWalls() {
		Map map=Map.getMap(level);//构造地图
		walls.addAll(map.getWalls());//所有墙
		walls.add(base);//所有基地
	}
	/**
	 * 绘图
	 */
	public void paint(Graphics g) {
		paintTankAction();//坦克行为
		createEnemy();//创造敌人
		paintImage();
		g.drawImage(image,0,0,this);
		System.gc();
	}
	
	/**
	 * 绘制主要图片
	 */
	private void paintImage() {
	
		g.setColor(Color.black);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());//背景填充
		paintBoom();//爆炸效果
		paintEnemyCount();//显示剩余坦克
		paintEnemy();//显示敌方坦克
		paintPlayerTanks();
		Tanks.addAll(Players);
		Tanks.addAll(EnemyTanks);
		paintWalls();//绘制墙
		paintBullets();//绘制子弹
		paintTool();
		
		if(EnemyRestCount==0) {//如果没有剩余坦克
			stopThread();//结束线程
			paintEnemyCount();
			g.setFont(new Font("楷体",Font.BOLD,50));
			g.setColor(Color.green);
			g.drawString("胜利", 300,350);
			
			gotoNextLevel();//去下一关
		}
		
		if(type==GameType.ONE_PLAYER) {//单人模式
			if(!play1.isAlive()&&play1.getLife()==0) {
				stopThread();
				booms.add(new Boom(play1.x,play1.y));
				paintBoom();
				paintGameOver();
				gotoPreviousLevel();
			}
		}
		else if(type==GameType.TWO_PLAYER) {//双人模式
			if (play1.isAlive() && !play2.isAlive() && play2.getLife()==0) {
				survivor = play1;//只有玩家1存活
			} else if (!play1.isAlive() && play1.getLife()==0 && play2.isAlive()) {
				survivor = play2;//只有玩家2存货
			} else if (!(play1.isAlive() || play2.isAlive())) {//都没有存活
				stopThread();
				booms.add(new Boom(survivor.x, survivor.y));//绘制爆炸图像
				paintBoom();
				paintGameOver();
				gotoPreviousLevel();
			}
		}
		if(!base.isAlive()) {
			stopThread();
			paintGameOver();//显示提示游戏失败
			base.setImage("wall\\break_base.png");
			gotoPreviousLevel();
		}
		g.drawImage(base.getImage(), base.x, base.y, this);
		
	}
	/**
	 * 失败提示
	 */
	private void paintGameOver() {
		g.setFont(new Font("楷体", Font.BOLD, 50));// 设置绘图字体
		g.setColor(Color.RED);// 设置绘图颜色
		g.drawString("Game Over !", 250, 400);// 在指定坐标绘制文字
		new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();//新建一个音效线程，用于播放音效
	}
	/**
	 * 绘制敌人坦克数量
	 */
	private void paintEnemyCount() {
		g.setColor(Color.WHITE);
		g.drawString("敌方坦克剩余：" + EnemyReadyCount, 337, 15);
	}
	/**
	 * 爆炸效果
	 */
	private void paintBoom() {
		for (int i = 0; i < booms.size(); i++) {
			Boom boom = booms.get(i);
			if (boom.isAlive()) {
				AudioClip blast=audios.get(2);//获取爆炸音效对象
				blast.play();// 播放爆炸音效
				boom.show(g);// 显示爆炸效果
			} else {// 如果爆炸效果无效
				booms.remove(i);// 在集合中刪除此爆炸对象
				i--;
			}
		}
	}
	/**
	 * 绘制墙
	 */
	private void paintWalls() {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (w.isAlive()) {// 
				g.drawImage(w.getImage(), w.x, w.y, this);// 绘制墙块
			} else {// 如果墙块无效
				walls.remove(i);// 在集合中刪除此墙块
				i--;
			}
		}
	}
	/**
	 * 绘制子弹
	 */
	private void paintBullets() {
		for(int i=0;i<bullets.size();i++) {
			Bullet b=bullets.get(i);
			if(b.isAlive()) {
				b.move();//子弹移动
				b.hitBase();//判断击中基地否
				b.hitWall();//判断击中墙否
				b.hitCharacters();//判断击中坦克否
				b.hitBullet();//判断子弹相互击中抵消否
				g.drawImage(b.getImage(),b.x,b.y,this);
			}
			else {
				bullets.remove(i);
				i--;
			}
		}
	}
	/**
	 * 绘制敌方坦克
	 */
	private void paintEnemy() {
		for(int i=0;i<EnemyTanks.size();i++) {
			Enemy enemy=(Enemy)EnemyTanks.get(i);
			if(enemy.isAlive()) {//如果存活
				if(!enemy.isPause()) {
					enemy.go();
				}
				if(enemy.isPause()) {
					if(pauseTimer>2500) {
						enemy.setPause(false);
						pauseTimer=0;
					}
					pauseTimer+=FRESHTIME;
				}
					g.drawImage(enemy.getImage(),enemy.x,enemy.y,this);//绘制坦克
				}
			else {
				EnemyTanks.remove(i);
				i--;
				booms.add(new Boom(enemy.x,enemy.y));
				decreaseEnemy();//
			}
		}
	}
	/**
	 * 绘制player
	 */
	private void paintPlayerTanks() {
		for(int i=0;i<Players.size();i++) {
			Character t=(Character)Players.get(i);
			if(t.isAlive()) {//如果存活 
				t.hitTool();
				t.addStar();
				g.drawImage(t.getImage(), t.x, t.y, this);
			}
			else {//如果死亡
				Players.remove(i);
				booms.add(new Boom(t.x,t.y));
				AudioClip blast=audios.get(2);//创建爆炸音效
				blast.play();
				t.setLife();
				if(t.getLife()>0) {//重新开始
					
						play1=new Character(278,537,"player\\left_player_1.png",this,CharacterType.PLAYER1);
						Players.add(play1);
					
					if(t.getCharacterType()==CharacterType.PLAYER2) {
						play2=new Character(448,537,"player\\left_player2.png",this,CharacterType.PLAYER2);
						Players.add(play2);
					}
				}
			}
		}
	}
	/**
	 * 道具
	 */
	private void paintTool() {
		if(toolTimer>=4500) {
			toolTimer=0;//当道具出现后刷新时间
			tool.changeToolType();//更换下次出现的道具
		}
		else {
			toolTimer+=FRESHTIME;
		}
		if(tool.getAlive()) {
			tool.draw(g);
		}
	}
	/**
	 * 结束
	 */
	private void stopThread() {
		frame.removeKeyListener(this);
		finish=true;
	}
	/**
	 * 游戏画面刷新
	 */
	private class FreshThread extends Thread{
		public void run() {
			while(!finish) {//游戏未停止
				repaint();
				System.gc();//
				try {
					Thread.sleep(FRESHTIME);//指定时间重绘界面
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 添加坦克
	 */
	private void createEnemy() {
		int index=random.nextInt(3);//坦克随机出生区域
		EnemyTimer+=FRESHTIME;
		if(EnemyTanks.size()<6&&EnemyReadyCount>0&&EnemyTimer>1500) {
			Rectangle bornRect=new Rectangle(EnemyX[index],1,35,35);//创建图像放置区域
			for(int i=0;i<Tanks.size();i++) {
				Character t=Tanks.get(i);
				if(t.isAlive()&&t.hit(bornRect)) {
					return ;
				}
			}
			EnemyTanks.add(new Enemy(EnemyX[index],1,GamePanel.this,CharacterType.ENEMY));//随机位置创造坦克
			new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			EnemyReadyCount--;
			EnemyTimer=0;
		}
	}
	/**
	 * 进入下一关
	 */
	private void gotoNextLevel() {
		Thread jump=new JumpPageThread(Level.getNextLevel());//到下一关的线程
		jump.start();//启动线程
	}
	/**
	 * 重新开始本关
	 */
	private void gotoPreviousLevel() {
		Thread jump = new JumpPageThread(Level.previsousLevel());// 重新开始本关线程
		jump.start();// 启动线程
	}
	/**
	 * 回到登录页面
	 */
	private void gotoLoginPanel() {
		frame.setPanel(new LoginPanel(frame));
	}
	
	/**
	 * 减少坦克数量
	 */
	public void decreaseEnemy() {
		EnemyRestCount--;
	}
	/**
	 * 按键操作
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			gotoLoginPanel();
		case KeyEvent.VK_Y://如果按下Y
			y_key=true;//可执行Y操作
			break;
		//case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key=true;//按下HOME或小键盘1键，执行玩家二操作
			
		case KeyEvent.VK_W://若按下W,可执行W对应操作，其他按键操作不执行
			w_key=true;
			a_key=false;
			s_key=false;
			d_key=false;
			break;
		case KeyEvent.VK_A://若按下A,可执行A对应操作，其他按键操作不执行
			w_key=false;
			a_key=true;
			s_key=false;
			d_key=false;
			break;
		case KeyEvent.VK_S://若按下S,可执行S对应操作，其他按键操作不执行
			w_key=false;
			a_key=false;
			s_key=true;
			d_key=false;
			break;
		case KeyEvent.VK_D://若按下D,可执行W对应操作，其他按键操作不执行
			w_key=false;
			a_key=false;
			s_key=false;
			d_key=true;
			break;
		case KeyEvent.VK_UP://若按下上箭头,执行向上操作，其他不执行
			up_key=true;//
			down_key=false;
			right_key=false;
			left_key=false;
			break;
		case KeyEvent.VK_DOWN://若按下下箭头,执行向下操作，其他不执行
			up_key=false;//
			down_key=true;
			right_key=false;
			left_key=false;
			break;
		case KeyEvent.VK_RIGHT://若按下右箭头,执行向右操作，其他不执行
			up_key=false;//
			down_key=false;
			right_key=true;
			left_key=false;
			break;
		case KeyEvent.VK_LEFT://若按下左箭头,执行向左操作，其他不执行
			up_key=false;//
			down_key=false;
			right_key=false;
			left_key=true;
			break;
		}
		
		
	}
	/**
	 *动作执行操作 
	 */
	private void paintTankAction() {
		if(y_key) {//如果按下“Y”，则为攻击模式
			play1.attack();
		}
		if(w_key) {//玩家一向上
			play1.upWard();
		}
		if(s_key) {//玩家一向下
			play1.downWard();
		}
		if(a_key) {//玩家一向左
			play1.leftWard();
		}
		if(d_key) {//玩家一向右
			play1.rightWard();
		}
		if(type==GameType.TWO_PLAYER) {
			if(num1_key) {//如果按下，攻击操作
				play2.attack();
			}
			if(up_key) {//玩家2向上
				play2.upWard();
			}
			if(down_key) {//玩家2向下
				play2.downWard();
			}
			if(left_key) {//玩家2向左
				play2.leftWard();
			}
			if(right_key) {//玩家2向右
				play2.rightWard();
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:// 抬起的是“Y”
			y_key = false;// “Y”变为false
			break;
		case KeyEvent.VK_W:// 抬起的是“W”
			w_key = false;// “W”变为false
			break;
		case KeyEvent.VK_A:// 若抬起的是“A”
			a_key = false;// “A”变为false
			break;
		case KeyEvent.VK_S:// 若抬起的是“S”
			s_key = false;// “S”变为false
			break;
		case KeyEvent.VK_D:// 若抬起的是“D”
			d_key = false;// “D”变为false
			break;
		//case KeyEvent.VK_HOME:// 若抬起的是“HOME”
		case KeyEvent.VK_NUMPAD1:// 若抬起的是小键盘1
			num1_key = false;// 小键盘1变为false
			break;
		case KeyEvent.VK_UP:// 若抬起的是“↑”
			up_key = false;// “↑”变为false
			break;
		case KeyEvent.VK_DOWN:// 若抬起的是“↓”
			down_key = false;// “↓”变为false
			break;
		case KeyEvent.VK_LEFT:// 若抬起的是“←”
			left_key = false;// “←”变为false
			break;
		case KeyEvent.VK_RIGHT:// 若抬起的是“→”
			right_key = false;// “→”变为false
			break;
		}
	}
	/**
	 * 不断添加子弹
	 * @param b
	 * 添加子弹
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
	/**
	 * 获取墙体
	 * @return 
	 * @return 墙体
	 */
	public List<Wall> getWalls() {
		return walls;
	}
	/**
	 * 获取基地
	 * @return 基地
	 */
	public Base getBase() {
		return base;
	}
	/**
	 * 获取坦克
	 * @return 
	 * @return 坦克
	 */
	public List<model.Character> getCharacters() {
		return Tanks;
	}
	/**
	 * 获取敌人
	 * @return 
	 */
	public List<Character> getEnemy() {
		return EnemyTanks;
	}
	/**
	 * 获取子弹
	 */
	public List<Bullet> getBullets(){
		return bullets;
	}
	/**
	 * 获取玩家
	 * @return 
	 */
	
	public List<model.Character> getPlayers() {
		return Players;
	}
	/**
	 * 获取道具
	 * @return
	 */
	public Tool getTool() {
		return tool;
	}
	/**
	 * 游戏结束
	 * @author z8065
	 *
	 */
	private class JumpPageThread extends Thread{
		int level;
		/**
		 * 构造方法
		 * @param level
		 */
		public JumpPageThread(int level) {
			this.level=level;
		}
		/**
		 * 线程方法
		 */
		public void run() {
			try {
				Thread.sleep(1000);//一秒
				
				frame.setPanel(new LevelPanel(level,frame,type));//关卡跳转
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 键入按键
		 */
		
	}

	
		public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
