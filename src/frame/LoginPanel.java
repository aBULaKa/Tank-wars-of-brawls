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


//�������ʱ��Ҫдһ����Ƶ·������

public class LoginPanel extends JPanel implements KeyListener{

    private static final long serialVersionUID=1L;
    private GameType type; //��Ϸģʽ
    private Image background;  //����ͼƬ
    private Image tank; //����ͼ��
    private int y1=260,y2=320,y3=380,y4=440; //����ͼ���ѡ����ĸ�Y����
    private int playerY=260; //����ͼ��Y����
    private MainFrame frame;

    /**
     * ��½���������
     * @param
     */

    public LoginPanel(MainFrame frame){
        this.frame=frame;
        addListener(); //�������
        try {
            background=ImageIO.read(new File("background\\����.jpg"));  //filenameΪ����ͼƬ��ImageUtil�е�·��
            tank=ImageIO.read(new File("player\\right_player2.png"));  //filenameΪ����ͼƬ��·��
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    /**
     * ��д��ͼ����
     */

    @Override
    public void paint(Graphics g){
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
        Font font=new Font("����",Font.BOLD,35);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("������Ϸ",300,290);
        g.drawString("˫����Ϸ",300,350);
        g.drawString("Ԥ���ؿ���ͼ",300,410);
        g.drawString("�Զ����ͼ",300,470);

        g.drawImage(tank,260 ,playerY,this);

    }

    /**
     * ��ת�ؿ����
     */
    private void gotoLevelPanel(){
        frame.removeKeyListener(this);
        frame.setPanel(new LevelPanel(1,frame,type));
    }
    private void addListener() {
        frame.addKeyListener(this);
    }

    /**
     * ���¼���
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
                repaint();  //���¼�����Ҫ���»�ͼ
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

