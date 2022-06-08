package frame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import Wall.Base;
import model.Level;
import model.Map;
import model.Wall;


public class MapPreViewPanel extends JPanel{
    private static final long serialVersionUID=1L;
    int level=1;
    private List<Wall> walls=Map.getWalls();
    private Base base;
    private Graphics g;
    private int count=Level.getAllLevel();
    private MainFrame frame;

    public MapPreViewPanel(final MainFrame frame){
        this.frame=frame;

        base=new Base(360,520);
        //初始化地图
        initWalls();
        JButton levelReduce=new JButton("上一关");
        levelReduce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level--;
                if(level==0){
                    level=count;
                }
                initWalls();
                repaint();
            }
        });
        JButton levelPlus=new JButton("下一关");

        levelPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level++;
                if(level>count){
                    level=1;

                }
                initWalls();
                repaint();
            }
        });
        JButton back=new JButton("返回");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.requestFocus();
                gotoLoginPanel();
            }
        });
        this.add(back);
        this.add(levelReduce);
        this.add(levelPlus);

    }

    private  void gotoLoginPanel(){
        frame.setPanel((new LoginPanel(frame)));

    }
    public void paint(Graphics g){
        super.setBackground(Color.BLACK);
        super.paint((g));
        this.g=g;
        g.setColor(Color.ORANGE);
        g.drawString("当前关卡："+level,0,12);
        g.drawString("关卡总数："+count,0,24);

        paintWalls();
    }
    private void paintWalls(){ 
        for(int i=0;i<walls.size();i++){
            Wall w=walls.get(i);
            if(w.x>=760){
                w.setAlive(false);
            }
            if(w.isAlive()){
                g.drawImage(w.getImage(),w.x,w.y,this);
            }else {
                walls.remove(i);
                i--;
            }
        }
    }
    public void initWalls(){
        Map.getMap(level);
        walls.add(base);
    }

}
