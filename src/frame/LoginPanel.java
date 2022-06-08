package frame;
import org.omg.CORBA.MARSHAL;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JButton;
import javax.swing.JPanel;


//最后整理时需要写一个音频路径的类

public class LoginPanel extends JPanel implements KeyListener{

    private static final long serialVersionUID=1L;
    private GameType type; //游戏模式
    private Image background;  //背景图片
    private Image tank; //人物图标
    private int y1=260,y2=320,y3=380,y4=440; //人物图标可选择的四个Y坐标
    private int playerY=260; //人物图标Y坐标
    private MainFrame frame;

    /**
     * 登陆面板主窗体
     * @param
     */

    public LoginPanel(MainFrame frame){
        this.frame=frame;
        addListener(); //组件监听
        try {
            background=ImageIO.read(new File("background\\背景.jpg"));  //filename为背景图片在ImageUtil中的路径
            tank=ImageIO.read(new File("player\\right_player2.png"));  //filename为人物图片的路径
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    /**
     * 重写绘图方法
     */

    @Override
    public void paint(Graphics g){
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
        Font font=new Font("黑体",Font.BOLD,35);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("单人游戏",300,290);
        g.drawString("双人游戏",300,350);
        g.drawString("预览关卡地图",300,410);
        g.drawString("自定义地图",300,470);

        g.drawImage(tank,260 ,playerY,this);

    }

    /**
     * 跳转关卡面板
     */
    private void gotoLevelPanel(){
        frame.removeKeyListener(this);
        frame.setPanel(new LevelPanel(1,frame,type));
    }
    private void addListener() {
        frame.addKeyListener(this);
    }

    /**
     * 按下键盘
     * @param e
     */
    public void keyTyped(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        int code=e.getKeyCode();
        switch (code){
            case KeyEvent.VK_UP:
                if(playerY==y1){
                    playerY=y4;
                }
                else if(playerY==y4){
                    playerY=y3;
                }
                else if(playerY==y3){
                    playerY=y2;
                }
                else if(playerY==y2){
                    playerY=y1;
                }
                repaint();  //按下键后需要重新绘图
                break;
            case KeyEvent.VK_DOWN:
                if (playerY == y4) {
                    playerY = y1;
                }else if(playerY ==y1){
                    playerY = y2;
                }else if(playerY == y2){
                    playerY = y3;
                }else if(playerY == y3){
                    playerY=y4;
                }
                repaint();
                break;
            case KeyEvent.VK_ENTER:
                if(playerY==y1){
                    type=GameType.ONE_PLAYER;
                    gotoLevelPanel();
                }
                if (playerY==y2){
                    type=GameType.TWO_PLAYER;
                    gotoLevelPanel();
                }
                if(playerY==y4){
                    type=null;
                    frame.removeKeyListener(this);
                    frame.setPanel(new MapEditorPanel(frame));

                }
                if(playerY==y3){
                    type=null;
                    frame.removeKeyListener(this);
                    frame.setPanel(new MapPreViewPanel(frame));
                }
        }
    }
    public void keyReleased(KeyEvent e){

    }
}

