package model;



import Wall.Brick;
import Wall.Grass;
import Wall.Iron;
import model.Bullet;
import frame.CharacterType;
import frame.GamePanel;
import util.AudioPlayer;
import util.AudioUtil;
import frame.Direction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;



public class Character extends DisplayableImage{
    GamePanel gamePanel;
    Direction direction; //移动方向
    protected boolean alive=true; //是否存活
    protected int speed=3;
    private boolean attackCoolDown=true; //攻击冷却状态，对应小道具
    private int attackCoolDownTime=500; //攻击冷却时间
    CharacterType type;
    private String upImage;// 向上移动时的图片
    private String downImage;// 向下移动时的图片
    private String rightImage;// 向右移动时的图片
    private String leftImage;// 向左移动时的图片

    private int life=1; //生命数
    private int starNum=0;//吃到的道具星星，吃一颗就加快攻击速度，累积三颗就可以打掉钢墙

    public Character(int x, int y, String url, GamePanel gamepanel, CharacterType type){
        super(x,y,url);
        this.gamePanel=gamepanel;
        this.type=type;
        direction=Direction.UP; //初始方向向上
        switch(type){
            case PLAYER1:
                upImage="player\\left_player_1.png"; //ImageUtil中向上的图片路径
                downImage="player\\right_player_1.png";
                rightImage="player\\left_player_1.png";
                leftImage="player\\right_player_1.png";
                break;
            case PLAYER2:
                upImage="player\\left_player2.png"; //ImageUtil中向上的图片路径
                downImage="player\\right_player2.png";
                rightImage="player\\left_player2.png";
                leftImage="player\\right_player2.png";
                break;
            case ENEMY:
                upImage="enemy\\left_enemy_jurne1.png"; //ImageUtil中向上的图片路径
                downImage="enemy\\right_enemy_jurne1.png";
                rightImage="enemy\\right_enemy_jurne1.png";
                leftImage="enemy\\left_enemy_jurne1.png";
                break;

        }
    }

    @Override
    public Rectangle getRect(){
        return new Rectangle(x,y,width-3,height-3);
        }
    /**
     * 向左移动
     */
    public void leftWard(){
        if(direction!=Direction.LEFT){
            setImage(leftImage);
        }
        direction=Direction.LEFT;
        if(!hitWall(x-speed,y)&&!hitCharacter(x-speed,y)){
            //如果左移之后的位置不会撞到墙块和坦克
            x-=speed; //纵坐标递减
            moveToBorder(); //判断是否移动到面板边界

        }


}
    public void rightWard() {
        if (direction != Direction.RIGHT) {// 如果移动之前的方向不是左移
            setImage(rightImage);// 更换右移图片
        }
        direction = Direction.RIGHT;// 移动方向设为右
        if (!hitWall(x + speed, y) && !hitCharacter(x + speed, y)) {// 如果右移之后的位置不会撞到墙块和坦克
            x += speed;// 横坐标递增
            moveToBorder();// 判断是否移动到面板的边界
        }
    }
    public void upWard(){
        if (direction != Direction.UP) {// 如果移动之前的方向不是上移
            setImage(upImage);// 更换上移图片
        }
        direction = Direction.UP;// 移动方向设为上
        if (!hitWall(x, y - speed) && !hitCharacter(x, y - speed)) {// 如果上移之后的位置不会撞到墙块和坦克
            y -= speed;// 纵坐标递减
            moveToBorder();// 判断是否移动到面板的边界
        }
    }

    public void downWard() {
        if (direction != Direction.DOWN) {// 如果移动之前的方向不是下移
            setImage(downImage);// 更换下移图片
        }
        direction = Direction.DOWN;// 移动方向设为下
        if (!hitWall(x, y + speed) && !hitCharacter(x, y + speed)) {// 如果下移之后的位置不会撞到墙块和坦克
            y += speed;// 纵坐标递增
            moveToBorder();// 判断是否移动到面板的边界
        }
    }

    /**
     *判断是否撞到墙块
     * @param x
     * @param y
     * @return 撞到墙块返回true
     */
    private boolean hitWall(int x, int y) {
        Rectangle next=new Rectangle(x,y,width-2,height-3); //坦克移动后的目标位置
        List<Wall> walls=gamePanel.getWalls();
        for (int i=0;i<walls.size();i++){
            Wall w=walls.get(i);
            if(w instanceof Grass){
                continue;
            }else if (w.hit(next)){
                //如果撞到墙块
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否撞到其他角色
     * @param x
     * @param y
     * @return撞到返回true
     */
    boolean hitCharacter(int x, int y) {
        Rectangle next=new Rectangle(x,y,width,height);//坦克移动后都目标区域
        List<Character> characters=gamePanel.getPlayers(); //获取所有角色
        for (int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(!this.equals(c)){//如果此角色和自身不是同一个对象
                if(c.isAlive()&&c.hit(next)){//如果此角色存活且与自身相撞
                    return true; //返回相撞
                }
            }

        }
        return false;

    }

    /**
     * 判断是否吃了道具
     * @return
     */
   public final boolean hitTool(){
        Tool tool=gamePanel.getTool();
        List<Character> characters=gamePanel.getCharacters();//获取所有玩家角色
        for(int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(c.type==CharacterType.PLAYER1||c.type==CharacterType.PLAYER2){//如果是玩家角色1或玩家角色2
                if(c.isAlive()&&c.hit(tool)&&tool.getAlive()){
                    switch (tool.type){
                        case ADD_CHARACTER:
                            c.life++;
                            System.out.println("角色生命增加道具");
                            break;
                        case STAR:
                            c.starNum++;
                            System.out.println("星星道具");
                            if(c.starNum>3){
                                c.starNum=3;
                            }
                            c.addStar();
                            break;
                        case SPADE:
                            System.out.println("钢撬道具");
                            addSpade();
                            break;
                        case TIMER:
                            System.out.println("定时器道具");
                            addTimer();
                            break;
                        case BOMB:
                            System.out.println("爆炸道具");
                            addBomb();
                            break;
                        case GUN:
                            c.starNum=3;
                            System.out.println("钢枪道具");
                            break;
                    }
                    tool.setAlive(false);//让道具消失
                    return true;
                }

            }
        }
        return false;
    }

    public void addStar() {
        if(starNum==1){
           this.setAttackCoolDownTime(400);
        }
        if(starNum==2){
            this.setAttackCoolDownTime(300);
        }
        if(starNum==3){
            this.setAttackCoolDownTime(250);
            //设置可以打掉钢砖
            List<Bullet> bullets=gamePanel.getBullets();
            for(int i=0;i<bullets.size();i++){
                Bullet b=bullets.get(i);
                if(b.isAlive()&&b.owner==type){
                    b.setIsHitIronWall(true);
                }
            }
        }
    }



    private void addTimer() {
        List<Character> enemys=gamePanel.getEnemy();
        for(int i=0;i<enemys.size();i++){
            Enemy e=(Enemy) enemys.get(i);
            e.setPause(true);
        }
    }

    private void addBomb() {
        List<Character> enemys=gamePanel.getEnemy();
        for(int i=0;i<enemys.size();i++){
            Enemy e=(Enemy) enemys.get(i);
            e.setAlive(false);
        }
    }

    private void addSpade() {
        List<Wall> walls=gamePanel.getWalls();
        for (int a=340;a<=400;a+=20){
            //循环基地砖墙的横坐标
            for (int b=500;b<=560;b+=20){
                if(a>=360&&a<380&&b>520){
                    //墙块与基地发生重合
                    continue;
                }else{
                    walls.add(new Iron(a,b));
                }
            }
        }
    }


    /**
     *
     * 角色是否存活
     */

    public boolean isAlive() {
        return alive;
    }

    /**
     * 设置存活状态
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * 设置移动速度
     */
    public void setSpeed(int speed) {
        this.speed=speed;
    }

    protected void moveToBorder() {
        if(x<0){
            x=0;
        }else if(x>gamePanel.getWidth()-width){
            //人物坐标超出了最大值
            x=gamePanel.getWidth()-width;
        }
        if(y<0){
            y=0;
        }
        else if(y>gamePanel.getHeight()-height){
            y=gamePanel.getHeight()-height;
        }

    }

    private Point getHeadPoint(){
        Point p=new Point();
        switch(direction){
            case UP:// 如果向上
                p.x = x + width / 2;// 头点横坐标取x加宽度的一般
                p.y = y;// 头点纵坐标取y
                break;
            case DOWN:// 如果向下
                p.x = x + width / 2;// 头点横坐标取x加宽度的一般
                p.y = y + height;// 头点纵坐标取y加高度的一般
                break;
            case RIGHT:// 如果向右
                p.x = x + width;// 头点横坐标取x加宽度的一般
                p.y = y + height / 2;// 头点纵坐标取y加高度的一般
                break;
            case LEFT:// 如果向左
                p.x = x;// 头点横坐标取x
                p.y = y + height / 2;// 头点纵坐标取y加高度的一般
                break;
            default:// 默认
                p = null;// 头点为null
        }
        return p;// 返回头点
        }
    /**
     * 攻击
     */

    public void attack(){
        if(attackCoolDown){
            //如果攻击功能完成冷却
            Point p=getHeadPoint();//获取人物头点对象
            Bullet b=new Bullet(p.x-Bullet.LENGTH/2,p.y-Bullet.LENGTH/2, direction ,gamePanel,type);//在人物头位置发射子弹
            gamePanel.addBullet(b);
            AudioPlayer fire=new AudioPlayer(AudioUtil.FIRE);  //AudioUtil中音频路径
            fire.new AudioThread().start();
            AttackCD attack=new AttackCD();
            attack.start();
        }
    }

    /**
     * 攻击冷却时间线程
     *
     */
    private class AttackCD extends Thread {
        public void run(){
            //线程主方法
            attackCoolDown=false;
            try {
                Thread.sleep(attackCoolDownTime);
            }catch ( InterruptedException e){
                e.printStackTrace();
            }
            attackCoolDown=true;
        }
    }

    /**
     * 获取攻击功能是否处于冷却
     * @return 攻击是否处于冷却
     */
    public  boolean isAttackCoolDown(){
        return attackCoolDown;
    }

    /**
     * 设置攻击
     */
    public void setAttackCoolDownTime(int attackCoolDownTime){
        this.attackCoolDownTime=attackCoolDownTime;
    }

    /**
     * 获取角色生命数
     *
     */
    public synchronized final int getLife(){
        return this.life;
    }

    public final void setLife(){
        if(life>0){
            life--;
        }
        else {
            return;
        }
    }
    /**
     * 获取角色类型
     */
    public  CharacterType getCharacterType(){
        return  type;
    }
}



