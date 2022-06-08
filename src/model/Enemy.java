package model;

import com.sun.javafx.scene.traversal.Direction;

import frame.GamePanel;
import frame.CharacterType;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

/**
 * 敌人类
 * 为控制角色难度，电脑角色随机向上的概率应该控制位比其他方向更小
 * 并且电脑角色最好每次移动的方向都不相同，向着某一方向连续移动的时间也不相同
 *
 *
 */

public class Enemy extends Character{
    private Random random=new Random();
    private Direction dir;
    private  int freshTime=GamePanel.FRESHTIME; // 刷新时间，采用
    private int moveTimer=0;//移动计时器

    private boolean pause=false;//电脑角色暂停状态

    /**
     * 获取电脑角色暂停状态
     *
     */
    public boolean isPause(){
        return pause;
    }

    /**
     * 设置电脑角色暂停状态
     *
     */
    public void setPause(boolean pause){
        this.pause=pause;
    }

    /**
     * 电脑角色构造方法
     *
     */
    public Enemy(int x,int y,GamePanel gamePanel,CharacterType  type){
        super(x,y,"enemy//left_enemy_jurne1.png",gamePanel,type); //filename为向下是角色图片路径
        dir=Direction.DOWN;
        setAttackCoolDownTime(1000);//设置攻击冷却时间

    }

    /**
     * 电脑角色展开行动的方法
     *
     */
    public void  go(){
        if(isAttackCoolDown()){
            attack();
        }
        if(moveTimer>random.nextInt(1000)+500){//如果移动计时器超过随机的0.5~1.5秒，则随机一个方向
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
     * 获取随机方向
     * @return
     */

    private Direction randomDirection() {
        Direction[] dirs=Direction.values(); //获取出方向的枚举值
        Direction oldDir=dir; //保存原来的方向
        Direction newDir=dirs[random.nextInt(4)];
        if(oldDir==newDir||newDir==Direction.UP){
            return dirs[random.nextInt(4)];
        }
        return newDir;
    }

    /**
     *重写移动到面板的边界事件
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
     * 重写碰到角色方法
     */
    @Override
    boolean hitCharacter(int x,int y){
        Rectangle next=new Rectangle(x,y,width,height);
        List<Character> characters=gamePanel.getCharacters();
        for(int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(!this.equals(c)){
                if(c.isAlive()&&c.hit(next)){
                    if(c instanceof Enemy){//如果对方也是电脑
                        dir=randomDirection();//随机调整移动方向
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 重写攻击方法，每次攻击只有4%概率会触发父类攻击方法
     */
    @Override
    public void attack(){
        int randomNum=random.nextInt(100);
        if(randomNum<4){
            super.attack();//执行父类攻击方法
        }
    }
}
