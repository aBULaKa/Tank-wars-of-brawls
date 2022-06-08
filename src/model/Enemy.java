package model;

import com.sun.javafx.scene.traversal.Direction;

import frame.GamePanel;
import frame.CharacterType;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

/**
 * ������
 * Ϊ���ƽ�ɫ�Ѷȣ����Խ�ɫ������ϵĸ���Ӧ�ÿ���λ�����������С
 * ���ҵ��Խ�ɫ���ÿ���ƶ��ķ��򶼲���ͬ������ĳһ���������ƶ���ʱ��Ҳ����ͬ
 *
 *
 */

public class Enemy extends Character{
    private Random random=new Random();
    private Direction dir;
    private  int freshTime=GamePanel.FRESHTIME; // ˢ��ʱ�䣬����
    private int moveTimer=0;//�ƶ���ʱ��

    private boolean pause=false;//���Խ�ɫ��ͣ״̬

    /**
     * ��ȡ���Խ�ɫ��ͣ״̬
     *
     */
    public boolean isPause(){
        return pause;
    }

    /**
     * ���õ��Խ�ɫ��ͣ״̬
     *
     */
    public void setPause(boolean pause){
        this.pause=pause;
    }

    /**
     * ���Խ�ɫ���췽��
     *
     */
    public Enemy(int x,int y,GamePanel gamePanel,CharacterType  type){
        super(x,y,"enemy//left_enemy_jurne1.png",gamePanel,type); //filenameΪ�����ǽ�ɫͼƬ·��
        dir=Direction.DOWN;
        setAttackCoolDownTime(1000);//���ù�����ȴʱ��

    }

    /**
     * ���Խ�ɫչ���ж��ķ���
     *
     */
    public void  go(){
        if(isAttackCoolDown()){
            attack();
        }
        if(moveTimer>random.nextInt(1000)+500){//����ƶ���ʱ�����������0.5~1.5�룬�����һ������
            dir=randomDirection();
            moveTimer=0;
        }else {
            moveTimer+=freshTime;
        }

        switch (dir){
            case UP:
                upWard();
                break;
            case DOWN:
                downWard();
                break;
            case LEFT:
                leftWard();
                break;
            case RIGHT:
                rightWard();
                break;
        }
    }

    /**
     * ��ȡ�������
     * @return
     */

    private Direction randomDirection() {
        Direction[] dirs=Direction.values(); //��ȡ�������ö��ֵ
        Direction oldDir=dir; //����ԭ���ķ���
        Direction newDir=dirs[random.nextInt(4)];
        if(oldDir==newDir||newDir==Direction.UP){
            return dirs[random.nextInt(4)];
        }
        return newDir;
    }

    /**
     *��д�ƶ������ı߽��¼�
     */
    @Override
    protected void moveToBorder() {
        if(x<0){
            x=0;
            dir=randomDirection();
        }else if(x>gamePanel.getWidth()-width){
            x=gamePanel.getWidth()-width;
            dir=randomDirection();
        }
        if(y<0){
            y=0;
            dir=randomDirection();

        }else if(y>gamePanel.getHeight()-height){
            y=gamePanel.getHeight()-height;
            dir=randomDirection();
        }
    }
    /**
     * ��д������ɫ����
     */
    @Override
    boolean hitCharacter(int x,int y){
        Rectangle next=new Rectangle(x,y,width,height);
        List<Character> characters=gamePanel.getCharacters();
        for(int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(!this.equals(c)){
                if(c.isAlive()&&c.hit(next)){
                    if(c instanceof Enemy){//����Է�Ҳ�ǵ���
                        dir=randomDirection();//��������ƶ�����
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ��д����������ÿ�ι���ֻ��4%���ʻᴥ�����๥������
     */
    @Override
    public void attack(){
        int randomNum=random.nextInt(100);
        if(randomNum<4){
            super.attack();//ִ�и��๥������
        }
    }
}
