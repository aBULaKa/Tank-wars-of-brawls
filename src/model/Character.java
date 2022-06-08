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
    Direction direction; //�ƶ�����
    protected boolean alive=true; //�Ƿ���
    protected int speed=3;
    private boolean attackCoolDown=true; //������ȴ״̬����ӦС����
    private int attackCoolDownTime=500; //������ȴʱ��
    CharacterType type;
    private String upImage;// �����ƶ�ʱ��ͼƬ
    private String downImage;// �����ƶ�ʱ��ͼƬ
    private String rightImage;// �����ƶ�ʱ��ͼƬ
    private String leftImage;// �����ƶ�ʱ��ͼƬ

    private int life=1; //������
    private int starNum=0;//�Ե��ĵ������ǣ���һ�žͼӿ칥���ٶȣ��ۻ����žͿ��Դ����ǽ

    public Character(int x, int y, String url, GamePanel gamepanel, CharacterType type){
        super(x,y,url);
        this.gamePanel=gamepanel;
        this.type=type;
        direction=Direction.UP; //��ʼ��������
        switch(type){
            case PLAYER1:
                upImage="player\\left_player_1.png"; //ImageUtil�����ϵ�ͼƬ·��
                downImage="player\\right_player_1.png";
                rightImage="player\\left_player_1.png";
                leftImage="player\\right_player_1.png";
                break;
            case PLAYER2:
                upImage="player\\left_player2.png"; //ImageUtil�����ϵ�ͼƬ·��
                downImage="player\\right_player2.png";
                rightImage="player\\left_player2.png";
                leftImage="player\\right_player2.png";
                break;
            case ENEMY:
                upImage="enemy\\left_enemy_jurne1.png"; //ImageUtil�����ϵ�ͼƬ·��
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
     * �����ƶ�
     */
    public void leftWard(){
        if(direction!=Direction.LEFT){
            setImage(leftImage);
        }
        direction=Direction.LEFT;
        if(!hitWall(x-speed,y)&&!hitCharacter(x-speed,y)){
            //�������֮���λ�ò���ײ��ǽ���̹��
            x-=speed; //������ݼ�
            moveToBorder(); //�ж��Ƿ��ƶ������߽�

        }


}
    public void rightWard() {
        if (direction != Direction.RIGHT) {// ����ƶ�֮ǰ�ķ���������
            setImage(rightImage);// ��������ͼƬ
        }
        direction = Direction.RIGHT;// �ƶ�������Ϊ��
        if (!hitWall(x + speed, y) && !hitCharacter(x + speed, y)) {// �������֮���λ�ò���ײ��ǽ���̹��
            x += speed;// ���������
            moveToBorder();// �ж��Ƿ��ƶ������ı߽�
        }
    }
    public void upWard(){
        if (direction != Direction.UP) {// ����ƶ�֮ǰ�ķ���������
            setImage(upImage);// ��������ͼƬ
        }
        direction = Direction.UP;// �ƶ�������Ϊ��
        if (!hitWall(x, y - speed) && !hitCharacter(x, y - speed)) {// �������֮���λ�ò���ײ��ǽ���̹��
            y -= speed;// ������ݼ�
            moveToBorder();// �ж��Ƿ��ƶ������ı߽�
        }
    }

    public void downWard() {
        if (direction != Direction.DOWN) {// ����ƶ�֮ǰ�ķ���������
            setImage(downImage);// ��������ͼƬ
        }
        direction = Direction.DOWN;// �ƶ�������Ϊ��
        if (!hitWall(x, y + speed) && !hitCharacter(x, y + speed)) {// �������֮���λ�ò���ײ��ǽ���̹��
            y += speed;// ���������
            moveToBorder();// �ж��Ƿ��ƶ������ı߽�
        }
    }

    /**
     *�ж��Ƿ�ײ��ǽ��
     * @param x
     * @param y
     * @return ײ��ǽ�鷵��true
     */
    private boolean hitWall(int x, int y) {
        Rectangle next=new Rectangle(x,y,width-2,height-3); //̹���ƶ����Ŀ��λ��
        List<Wall> walls=gamePanel.getWalls();
        for (int i=0;i<walls.size();i++){
            Wall w=walls.get(i);
            if(w instanceof Grass){
                continue;
            }else if (w.hit(next)){
                //���ײ��ǽ��
                return true;
            }
        }
        return false;
    }

    /**
     * �ж��Ƿ�ײ��������ɫ
     * @param x
     * @param y
     * @returnײ������true
     */
    boolean hitCharacter(int x, int y) {
        Rectangle next=new Rectangle(x,y,width,height);//̹���ƶ���Ŀ������
        List<Character> characters=gamePanel.getPlayers(); //��ȡ���н�ɫ
        for (int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(!this.equals(c)){//����˽�ɫ��������ͬһ������
                if(c.isAlive()&&c.hit(next)){//����˽�ɫ�������������ײ
                    return true; //������ײ
                }
            }

        }
        return false;

    }

    /**
     * �ж��Ƿ���˵���
     * @return
     */
   public final boolean hitTool(){
        Tool tool=gamePanel.getTool();
        List<Character> characters=gamePanel.getCharacters();//��ȡ������ҽ�ɫ
        for(int i=0;i<characters.size();i++){
            Character c=characters.get(i);
            if(c.type==CharacterType.PLAYER1||c.type==CharacterType.PLAYER2){//�������ҽ�ɫ1����ҽ�ɫ2
                if(c.isAlive()&&c.hit(tool)&&tool.getAlive()){
                    switch (tool.type){
                        case ADD_CHARACTER:
                            c.life++;
                            System.out.println("��ɫ�������ӵ���");
                            break;
                        case STAR:
                            c.starNum++;
                            System.out.println("���ǵ���");
                            if(c.starNum>3){
                                c.starNum=3;
                            }
                            c.addStar();
                            break;
                        case SPADE:
                            System.out.println("���˵���");
                            addSpade();
                            break;
                        case TIMER:
                            System.out.println("��ʱ������");
                            addTimer();
                            break;
                        case BOMB:
                            System.out.println("��ը����");
                            addBomb();
                            break;
                        case GUN:
                            c.starNum=3;
                            System.out.println("��ǹ����");
                            break;
                    }
                    tool.setAlive(false);//�õ�����ʧ
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
            //���ÿ��Դ����ש
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
            //ѭ������שǽ�ĺ�����
            for (int b=500;b<=560;b+=20){
                if(a>=360&&a<380&&b>520){
                    //ǽ������ط����غ�
                    continue;
                }else{
                    walls.add(new Iron(a,b));
                }
            }
        }
    }


    /**
     *
     * ��ɫ�Ƿ���
     */

    public boolean isAlive() {
        return alive;
    }

    /**
     * ���ô��״̬
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * �����ƶ��ٶ�
     */
    public void setSpeed(int speed) {
        this.speed=speed;
    }

    protected void moveToBorder() {
        if(x<0){
            x=0;
        }else if(x>gamePanel.getWidth()-width){
            //�������곬�������ֵ
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
            case UP:// �������
                p.x = x + width / 2;// ͷ�������ȡx�ӿ�ȵ�һ��
                p.y = y;// ͷ��������ȡy
                break;
            case DOWN:// �������
                p.x = x + width / 2;// ͷ�������ȡx�ӿ�ȵ�һ��
                p.y = y + height;// ͷ��������ȡy�Ӹ߶ȵ�һ��
                break;
            case RIGHT:// �������
                p.x = x + width;// ͷ�������ȡx�ӿ�ȵ�һ��
                p.y = y + height / 2;// ͷ��������ȡy�Ӹ߶ȵ�һ��
                break;
            case LEFT:// �������
                p.x = x;// ͷ�������ȡx
                p.y = y + height / 2;// ͷ��������ȡy�Ӹ߶ȵ�һ��
                break;
            default:// Ĭ��
                p = null;// ͷ��Ϊnull
        }
        return p;// ����ͷ��
        }
    /**
     * ����
     */

    public void attack(){
        if(attackCoolDown){
            //����������������ȴ
            Point p=getHeadPoint();//��ȡ����ͷ�����
            Bullet b=new Bullet(p.x-Bullet.LENGTH/2,p.y-Bullet.LENGTH/2, direction ,gamePanel,type);//������ͷλ�÷����ӵ�
            gamePanel.addBullet(b);
            AudioPlayer fire=new AudioPlayer(AudioUtil.FIRE);  //AudioUtil����Ƶ·��
            fire.new AudioThread().start();
            AttackCD attack=new AttackCD();
            attack.start();
        }
    }

    /**
     * ������ȴʱ���߳�
     *
     */
    private class AttackCD extends Thread {
        public void run(){
            //�߳�������
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
     * ��ȡ���������Ƿ�����ȴ
     * @return �����Ƿ�����ȴ
     */
    public  boolean isAttackCoolDown(){
        return attackCoolDown;
    }

    /**
     * ���ù���
     */
    public void setAttackCoolDownTime(int attackCoolDownTime){
        this.attackCoolDownTime=attackCoolDownTime;
    }

    /**
     * ��ȡ��ɫ������
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
     * ��ȡ��ɫ����
     */
    public  CharacterType getCharacterType(){
        return  type;
    }
}



