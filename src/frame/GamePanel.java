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
	MainFrame frame;//���
	GameType type;//��Ϸ����
	AudioPlayer music;
	private List<Bullet> bullets;//�ӵ�
	private List<Character> Tanks;//̹��
	private List<Character> EnemyTanks;//����̹��
	private List<Character> Players;//���
	private List<Wall> walls;//����ǽ
	private List<Boom> booms;//���б�ըЧ��
	private BufferedImage image;//����ͼƬ
	private Graphics g;//��ͼ
	private Character play1 ,play2,survivor;//���1�����2
	private int enemyCount=5;
	private int EnemyRestCount=enemyCount;//��ʼ̹����
	private int EnemyReadyCount=enemyCount;//��̹����
	private Base base;//����
	private boolean finish=false;//��Ϸ�����ı�ʶ
	private Random random=new Random();
	private int EnemyTimer=0;//�з�̹��ˢ��ʱ��
	private int[]  EnemyX= {10,367,754};//�з�̹�˳���������
	private List<AudioClip> audios=AudioUtil.getAudios();
	private boolean y_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num1_key;//��������
	private	int toolTimer=0;//���߳��ֵļ�ʱ��
	private Random r = new Random();// �������ˢ��
	private Tool tool=Tool.getToolInstance(r.nextInt(500),r.nextInt(500));
	private int level=Level.previsousLevel();// �ؿ�ֵ
	private int pauseTimer=0;// �з���ͣ��ʱ��
	/**
	 * ��幹��
	 * @param level
	 * 				�ؿ�
	 * @param frame
	 * 				����
	 * @param gameType
	 * 				��Ϸģʽ
	 */
	public GamePanel(MainFrame frame,int level,GameType gameType) {
		this.level=level;
		this.frame=frame;
		setBackground(Color.black);
		frame.setSize(775, 600);
		this.level=level;
		this.type=gameType;
		init();//��ʼ��
		
		Thread t=new FreshThread();
		t.start();
		music=new AudioPlayer(AudioUtil.START);//��Ч����·��
		music.new AudioThread().start();//��ʼ����
		
		
		
		addListener();//��������
		
	}

	/**
	 * ��ʼ��
	 */
	
	private void init() {
		bullets=new ArrayList<>();//ʵ�����ӵ�
		Tanks=new ArrayList<>();//ʵ����̹��
		walls=new ArrayList<>();//ʵ����ǽ
		booms=new ArrayList<>();//ʵ������ըЧ��
		image=new BufferedImage(794, 572, BufferedImage.TYPE_INT_BGR);
		g=image.getGraphics();//��ȡͼƬ
		Players=new Vector<>();//ʵ�������
		
		play1=new Character(278, 537, "player//left_player_1.png", this, CharacterType.PLAYER1);//��ҳ�ʼλ��
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
	 * ����
	 */
	private void addListener() {
		frame.addKeyListener( this);
	}
	/**
	 * ǽ��
	 */
	public void initWalls() {
		Map map=Map.getMap(level);//�����ͼ
		walls.addAll(map.getWalls());//����ǽ
		walls.add(base);//���л���
	}
	/**
	 * ��ͼ
	 */
	public void paint(Graphics g) {
		paintTankAction();//̹����Ϊ
		createEnemy();//�������
		paintImage();
		g.drawImage(image,0,0,this);
		System.gc();
	}
	
	/**
	 * ������ҪͼƬ
	 */
	private void paintImage() {
	
		g.setColor(Color.black);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());//�������
		paintBoom();//��ըЧ��
		paintEnemyCount();//��ʾʣ��̹��
		paintEnemy();//��ʾ�з�̹��
		paintPlayerTanks();
		Tanks.addAll(Players);
		Tanks.addAll(EnemyTanks);
		paintWalls();//����ǽ
		paintBullets();//�����ӵ�
		paintTool();
		
		if(EnemyRestCount==0) {//���û��ʣ��̹��
			stopThread();//�����߳�
			paintEnemyCount();
			g.setFont(new Font("����",Font.BOLD,50));
			g.setColor(Color.green);
			g.drawString("ʤ��", 300,350);
			
			gotoNextLevel();//ȥ��һ��
		}
		
		if(type==GameType.ONE_PLAYER) {//����ģʽ
			if(!play1.isAlive()&&play1.getLife()==0) {
				stopThread();
				booms.add(new Boom(play1.x,play1.y));
				paintBoom();
				paintGameOver();
				gotoPreviousLevel();
			}
		}
		else if(type==GameType.TWO_PLAYER) {//˫��ģʽ
			if (play1.isAlive() && !play2.isAlive() && play2.getLife()==0) {
				survivor = play1;//ֻ�����1���
			} else if (!play1.isAlive() && play1.getLife()==0 && play2.isAlive()) {
				survivor = play2;//ֻ�����2���
			} else if (!(play1.isAlive() || play2.isAlive())) {//��û�д��
				stopThread();
				booms.add(new Boom(survivor.x, survivor.y));//���Ʊ�ըͼ��
				paintBoom();
				paintGameOver();
				gotoPreviousLevel();
			}
		}
		if(!base.isAlive()) {
			stopThread();
			paintGameOver();//��ʾ��ʾ��Ϸʧ��
			base.setImage("wall\\break_base.png");
			gotoPreviousLevel();
		}
		g.drawImage(base.getImage(), base.x, base.y, this);
		
	}
	/**
	 * ʧ����ʾ
	 */
	private void paintGameOver() {
		g.setFont(new Font("����", Font.BOLD, 50));// ���û�ͼ����
		g.setColor(Color.RED);// ���û�ͼ��ɫ
		g.drawString("Game Over !", 250, 400);// ��ָ�������������
		new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();//�½�һ����Ч�̣߳����ڲ�����Ч
	}
	/**
	 * ���Ƶ���̹������
	 */
	private void paintEnemyCount() {
		g.setColor(Color.WHITE);
		g.drawString("�з�̹��ʣ�ࣺ" + EnemyReadyCount, 337, 15);
	}
	/**
	 * ��ըЧ��
	 */
	private void paintBoom() {
		for (int i = 0; i < booms.size(); i++) {
			Boom boom = booms.get(i);
			if (boom.isAlive()) {
				AudioClip blast=audios.get(2);//��ȡ��ը��Ч����
				blast.play();// ���ű�ը��Ч
				boom.show(g);// ��ʾ��ըЧ��
			} else {// �����ըЧ����Ч
				booms.remove(i);// �ڼ����Єh���˱�ը����
				i--;
			}
		}
	}
	/**
	 * ����ǽ
	 */
	private void paintWalls() {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (w.isAlive()) {// 
				g.drawImage(w.getImage(), w.x, w.y, this);// ����ǽ��
			} else {// ���ǽ����Ч
				walls.remove(i);// �ڼ����Єh����ǽ��
				i--;
			}
		}
	}
	/**
	 * �����ӵ�
	 */
	private void paintBullets() {
		for(int i=0;i<bullets.size();i++) {
			Bullet b=bullets.get(i);
			if(b.isAlive()) {
				b.move();//�ӵ��ƶ�
				b.hitBase();//�жϻ��л��ط�
				b.hitWall();//�жϻ���ǽ��
				b.hitCharacters();//�жϻ���̹�˷�
				b.hitBullet();//�ж��ӵ��໥���е�����
				g.drawImage(b.getImage(),b.x,b.y,this);
			}
			else {
				bullets.remove(i);
				i--;
			}
		}
	}
	/**
	 * ���Ƶз�̹��
	 */
	private void paintEnemy() {
		for(int i=0;i<EnemyTanks.size();i++) {
			Enemy enemy=(Enemy)EnemyTanks.get(i);
			if(enemy.isAlive()) {//������
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
					g.drawImage(enemy.getImage(),enemy.x,enemy.y,this);//����̹��
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
	 * ����player
	 */
	private void paintPlayerTanks() {
		for(int i=0;i<Players.size();i++) {
			Character t=(Character)Players.get(i);
			if(t.isAlive()) {//������ 
				t.hitTool();
				t.addStar();
				g.drawImage(t.getImage(), t.x, t.y, this);
			}
			else {//�������
				Players.remove(i);
				booms.add(new Boom(t.x,t.y));
				AudioClip blast=audios.get(2);//������ը��Ч
				blast.play();
				t.setLife();
				if(t.getLife()>0) {//���¿�ʼ
					
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
	 * ����
	 */
	private void paintTool() {
		if(toolTimer>=4500) {
			toolTimer=0;//�����߳��ֺ�ˢ��ʱ��
			tool.changeToolType();//�����´γ��ֵĵ���
		}
		else {
			toolTimer+=FRESHTIME;
		}
		if(tool.getAlive()) {
			tool.draw(g);
		}
	}
	/**
	 * ����
	 */
	private void stopThread() {
		frame.removeKeyListener(this);
		finish=true;
	}
	/**
	 * ��Ϸ����ˢ��
	 */
	private class FreshThread extends Thread{
		public void run() {
			while(!finish) {//��Ϸδֹͣ
				repaint();
				System.gc();//
				try {
					Thread.sleep(FRESHTIME);//ָ��ʱ���ػ����
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ���̹��
	 */
	private void createEnemy() {
		int index=random.nextInt(3);//̹�������������
		EnemyTimer+=FRESHTIME;
		if(EnemyTanks.size()<6&&EnemyReadyCount>0&&EnemyTimer>1500) {
			Rectangle bornRect=new Rectangle(EnemyX[index],1,35,35);//����ͼ���������
			for(int i=0;i<Tanks.size();i++) {
				Character t=Tanks.get(i);
				if(t.isAlive()&&t.hit(bornRect)) {
					return ;
				}
			}
			EnemyTanks.add(new Enemy(EnemyX[index],1,GamePanel.this,CharacterType.ENEMY));//���λ�ô���̹��
			new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			EnemyReadyCount--;
			EnemyTimer=0;
		}
	}
	/**
	 * ������һ��
	 */
	private void gotoNextLevel() {
		Thread jump=new JumpPageThread(Level.getNextLevel());//����һ�ص��߳�
		jump.start();//�����߳�
	}
	/**
	 * ���¿�ʼ����
	 */
	private void gotoPreviousLevel() {
		Thread jump = new JumpPageThread(Level.previsousLevel());// ���¿�ʼ�����߳�
		jump.start();// �����߳�
	}
	/**
	 * �ص���¼ҳ��
	 */
	private void gotoLoginPanel() {
		frame.setPanel(new LoginPanel(frame));
	}
	
	/**
	 * ����̹������
	 */
	public void decreaseEnemy() {
		EnemyRestCount--;
	}
	/**
	 * ��������
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			gotoLoginPanel();
		case KeyEvent.VK_Y://�������Y
			y_key=true;//��ִ��Y����
			break;
		//case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key=true;//����HOME��С����1����ִ����Ҷ�����
			
		case KeyEvent.VK_W://������W,��ִ��W��Ӧ��������������������ִ��
			w_key=true;
			a_key=false;
			s_key=false;
			d_key=false;
			break;
		case KeyEvent.VK_A://������A,��ִ��A��Ӧ��������������������ִ��
			w_key=false;
			a_key=true;
			s_key=false;
			d_key=false;
			break;
		case KeyEvent.VK_S://������S,��ִ��S��Ӧ��������������������ִ��
			w_key=false;
			a_key=false;
			s_key=true;
			d_key=false;
			break;
		case KeyEvent.VK_D://������D,��ִ��W��Ӧ��������������������ִ��
			w_key=false;
			a_key=false;
			s_key=false;
			d_key=true;
			break;
		case KeyEvent.VK_UP://�������ϼ�ͷ,ִ�����ϲ�����������ִ��
			up_key=true;//
			down_key=false;
			right_key=false;
			left_key=false;
			break;
		case KeyEvent.VK_DOWN://�������¼�ͷ,ִ�����²�����������ִ��
			up_key=false;//
			down_key=true;
			right_key=false;
			left_key=false;
			break;
		case KeyEvent.VK_RIGHT://�������Ҽ�ͷ,ִ�����Ҳ�����������ִ��
			up_key=false;//
			down_key=false;
			right_key=true;
			left_key=false;
			break;
		case KeyEvent.VK_LEFT://���������ͷ,ִ�����������������ִ��
			up_key=false;//
			down_key=false;
			right_key=false;
			left_key=true;
			break;
		}
		
		
	}
	/**
	 *����ִ�в��� 
	 */
	private void paintTankAction() {
		if(y_key) {//������¡�Y������Ϊ����ģʽ
			play1.attack();
		}
		if(w_key) {//���һ����
			play1.upWard();
		}
		if(s_key) {//���һ����
			play1.downWard();
		}
		if(a_key) {//���һ����
			play1.leftWard();
		}
		if(d_key) {//���һ����
			play1.rightWard();
		}
		if(type==GameType.TWO_PLAYER) {
			if(num1_key) {//������£���������
				play2.attack();
			}
			if(up_key) {//���2����
				play2.upWard();
			}
			if(down_key) {//���2����
				play2.downWard();
			}
			if(left_key) {//���2����
				play2.leftWard();
			}
			if(right_key) {//���2����
				play2.rightWard();
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:// ̧����ǡ�Y��
			y_key = false;// ��Y����Ϊfalse
			break;
		case KeyEvent.VK_W:// ̧����ǡ�W��
			w_key = false;// ��W����Ϊfalse
			break;
		case KeyEvent.VK_A:// ��̧����ǡ�A��
			a_key = false;// ��A����Ϊfalse
			break;
		case KeyEvent.VK_S:// ��̧����ǡ�S��
			s_key = false;// ��S����Ϊfalse
			break;
		case KeyEvent.VK_D:// ��̧����ǡ�D��
			d_key = false;// ��D����Ϊfalse
			break;
		//case KeyEvent.VK_HOME:// ��̧����ǡ�HOME��
		case KeyEvent.VK_NUMPAD1:// ��̧�����С����1
			num1_key = false;// С����1��Ϊfalse
			break;
		case KeyEvent.VK_UP:// ��̧����ǡ�����
			up_key = false;// ��������Ϊfalse
			break;
		case KeyEvent.VK_DOWN:// ��̧����ǡ�����
			down_key = false;// ��������Ϊfalse
			break;
		case KeyEvent.VK_LEFT:// ��̧����ǡ�����
			left_key = false;// ��������Ϊfalse
			break;
		case KeyEvent.VK_RIGHT:// ��̧����ǡ�����
			right_key = false;// ��������Ϊfalse
			break;
		}
	}
	/**
	 * ��������ӵ�
	 * @param b
	 * ����ӵ�
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
	/**
	 * ��ȡǽ��
	 * @return 
	 * @return ǽ��
	 */
	public List<Wall> getWalls() {
		return walls;
	}
	/**
	 * ��ȡ����
	 * @return ����
	 */
	public Base getBase() {
		return base;
	}
	/**
	 * ��ȡ̹��
	 * @return 
	 * @return ̹��
	 */
	public List<model.Character> getCharacters() {
		return Tanks;
	}
	/**
	 * ��ȡ����
	 * @return 
	 */
	public List<Character> getEnemy() {
		return EnemyTanks;
	}
	/**
	 * ��ȡ�ӵ�
	 */
	public List<Bullet> getBullets(){
		return bullets;
	}
	/**
	 * ��ȡ���
	 * @return 
	 */
	
	public List<model.Character> getPlayers() {
		return Players;
	}
	/**
	 * ��ȡ����
	 * @return
	 */
	public Tool getTool() {
		return tool;
	}
	/**
	 * ��Ϸ����
	 * @author z8065
	 *
	 */
	private class JumpPageThread extends Thread{
		int level;
		/**
		 * ���췽��
		 * @param level
		 */
		public JumpPageThread(int level) {
			this.level=level;
		}
		/**
		 * �̷߳���
		 */
		public void run() {
			try {
				Thread.sleep(1000);//һ��
				
				frame.setPanel(new LevelPanel(level,frame,type));//�ؿ���ת
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		/**
		 * ���밴��
		 */
		
	}

	
		public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
