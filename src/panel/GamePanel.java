package panel;

import info.Global;
import info.Monster;
import info.StageInfo;
import manager.MusicManager;
import manager.SceneManager;
import res.Res;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;

public class GamePanel extends BasePanel implements Runnable
{
    private SceneManager parent = null;
    private StageInfo stageInfo = null;

    private int width = 1000;
    private int height = 600;

    public boolean is_running = false;

    private VolatileImage iBuffer = null;
    private Graphics gBuffer = null;

    public JButton btn_menu = null;
    public JButton btn_next = null;

    private Monster[] monsters = null;
    private boolean is_city = false;
    private boolean wait_for_replay = false;
    private boolean wait_for_paint = false;

    private boolean is_success = false;

    private boolean is_reboening = false;

    public GamePanel(SceneManager parent, StageInfo stageinfo)
    {
        this.parent = parent;
        this.stageInfo = stageinfo;
        width = Util.width;
        height = Util.height;

        setBounds(0,0,width,height);
        setLayout(null);

        InitUI();
        Init();
    }

    private void InitUI()
    {
        {
            Cursor cursor = getToolkit().createCustomCursor(new ImageIcon(Res.getLogo("cursor.png")).getImage(),new Point(5,5),"norm");
            setCursor(cursor);
        }

        {
            btn_next = new JButton();
            btn_next.setSize(90,34);
            btn_next.setLocation(width-200,500);
            btn_next.setIcon(new ImageIcon(Res.getLogo("btn_next_normal.png")));
            btn_next.setRolloverIcon(new ImageIcon(Res.getLogo("btn_next_hover.png")));
            btn_next.setBorderPainted(false);
            btn_next.setFocusPainted(false);
            btn_next.setContentAreaFilled(false);
            btn_next.setFocusable(true);
            btn_next.setVisible(false);
            add(btn_next);
            btn_next.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Global.cube+=stageInfo.cube_num;
                    stageInfo.getPlayer().x_pos=800;
                    btn_next.setVisible(false);
                    is_success = false;
                    parent.storeData();
                    parent.ChangeTo(SceneManager.NEXT_GAME_STAGE);
                }
            });
        }

        {
            btn_menu = new JButton();
            btn_menu.setSize(50,50);
            btn_menu.setLocation(width-90,30);
            btn_menu.setIcon(new ImageIcon(Res.getLogo("btn_menu_normal.png")));
            btn_menu.setRolloverIcon(new ImageIcon(Res.getLogo("btn_menu_hover.png")));
            btn_menu.setBorderPainted(false);
            btn_menu.setFocusPainted(false);
            btn_menu.setContentAreaFilled(false);
            btn_menu.setFocusable(true);
            add(btn_menu);
            btn_menu.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    showMenu();
                    btn_menu.setVisible(false);
                }
            });
        }
    }

    private void showMenu()
    {
        {
            SmallMenuPanel panel = new SmallMenuPanel(parent,this);
            int w = width-width/10;
            int h = height-height/10;
            panel.setBounds(width/20-5,height/20,w,h);
            add(panel);
            is_running = false;
            is_reboening = false;
        }
    }

    private void showReborn()
    {
        {
            SmallRebornPanel panel = new SmallRebornPanel(parent,this);
            int w = width-width/7;
            int h = height-height/7;
            panel.setBounds(width/20-5,height/20,w,h);
            add(panel);
            is_running = false;
            is_reboening = true;
        }
    }

    private void Init()
    {
        is_running = true;
        is_city = stageInfo.isIs_city();

        //获取第一个区域的Monsters
        monsters = stageInfo.getMonsters();

        new Thread(this).start();

        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        stageInfo.getPlayer().player_run_left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        stageInfo.getPlayer().player_run_right();
                        break;
                    case KeyEvent.VK_X:
                        stageInfo.getPlayer().player_attack();
                        break;
                    case KeyEvent.VK_C:
                        stageInfo.getPlayer().player_jump();
                        break;
                    case KeyEvent.VK_DOWN:
                        stageInfo.getPlayer().player_backjump();
                        break;
                    case KeyEvent.VK_Z:
                        stageInfo.getPlayer().player_skill_upattack();
                        break;
                    case KeyEvent.VK_A:
                        stageInfo.getPlayer().player_skill_guizhan();
                        break;
                    case KeyEvent.VK_S:
                        stageInfo.getPlayer().player_skill_liebozhan();
                        break;
                    case KeyEvent.VK_D:
                        stageInfo.getPlayer().player_skill_badaozhan();
                        break;
                }

                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        stageInfo.getPlayer().player_stop();
                        break;
                    case KeyEvent.VK_RIGHT:
                        stageInfo.getPlayer().player_stop();
                        break;
                    case KeyEvent.VK_X:
                        stageInfo.getPlayer().vx = 0;
                        break;
                }

                super.keyReleased(e);
            }
        });

        MusicManager.stopAll();
        MusicManager.loadBGM(stageInfo.bgm);
        MusicManager.playBGM();
        MusicManager.setVol(-10.0f);
    }

    @Override
    public void run()
    {
        while (Util.thread_running)
        {
            try
            {
                Thread.sleep(Global.frame_rate);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (!wait_for_replay && Global.hp_player<=0)
            {
                wait_for_replay = true;
                stageInfo.getPlayer().alive = false;
                MusicManager.stopBGM();
                Util.log("Player死亡，等待重新开始");
                showReborn();
                btn_menu.setVisible(false);
            }

            stageInfo.getPlayer().computCD();
            stageInfo.getPlayer().resumeMP();

            stageInfo.getPlayer().x_pos+=stageInfo.getPlayer().vx;
            stageInfo.getPlayer().y_pos+=stageInfo.getPlayer().vy;

            if (stageInfo.getPlayer().x_pos<-100)
            {
                stageInfo.getPlayer().x_pos = -100;
            }
            else if (stageInfo.getPlayer().x_pos>900)
            {
                if (stageInfo.check)
                {
                    //【下一关】
                    goToNextArea();
                    if (monsters!=null)
                    {
                        stageInfo.getPlayer().x_pos=0;
                    }
                    else
                    {
                        is_success = true;
                    }
                }
                else
                {
                    stageInfo.getPlayer().x_pos=900;
                }
            }

            if (stageInfo.getPlayer().y_pos<0)
            {
                stageInfo.getPlayer().y_pos=350;
                stageInfo.getPlayer().vy=0;
            }

            stageInfo.getPlayer().controlJump();
            stageInfo.getPlayer().controlBackJump();
            stageInfo.getPlayer().controlUpAir();
            if (monsters!=null)
            {
                for (int i=0;i<monsters.length;i++)
                {
                    monsters[i].x_pos+=monsters[i].vx;
                    monsters[i].y_pos+=monsters[i].vy;
                    monsters[i].upairControl();

                    if (monsters[i].x_pos<-50)
                    {
                        monsters[i].x_pos=-50;
                    }
                    else if (monsters[i].x_pos>900)
                    {
                        monsters[i].x_pos=900;
                    }

                    if (monsters[i].y_pos<0)
                    {
                        monsters[i].y_pos=monsters[i].floor;
                    }
                }
            }

            if (Global.hp_player<0)
            {
                Global.hp_player=0;
            }

            if (Global.mp_player<0)
            {
                Global.mp_player=0;
            }

            repaint();
        }
    }

    private void goToNextArea()
    {
        stageInfo.nextArea();
        monsters = stageInfo.getMonsters();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (is_running)
        {
            requestFocus();
            // 初始化双缓冲模式
            if (iBuffer == null) {
                iBuffer = createVolatileImage(width, height);
                gBuffer = iBuffer.getGraphics();
            }

            // 刷新并绘制背景
            gBuffer.drawImage(stageInfo.getBackground(), 0, 0, this);

            //开始绘制具体内容

            //绘制player
            gBuffer.drawImage(stageInfo.getPlayer().getImage(), stageInfo.getPlayer().getX_pos(), stageInfo.getPlayer().getY_pos(), this);

            if (monsters!=null)
            {
                //绘制Monster
                for (int i=0;i<monsters.length;i++)
                {
                    if (monsters[i].isAlive())
                    {
                        gBuffer.drawImage(monsters[i].getImage(),monsters[i].x_pos,monsters[i].y_pos,this);
                        gBuffer.drawImage(stageInfo.getHp(),monsters[i].x_pos+10,monsters[i].y_pos-10,(int) (100 * monsters[i].getHPDivMax()),10,this);
                    }
                }
            }

            //绘制大技能
            Image big_skill = stageInfo.getPlayer().getSkillImage();
            if (big_skill!=null)
            {
                gBuffer.drawImage(big_skill,0, 50, this);
            }

            //绘制箭头
            if (stageInfo.check)
            {
                gBuffer.drawImage(stageInfo.getArrow(), 800, 100, 130,100, this);
            }

            //绘制头像和“双条”
            gBuffer.drawImage(stageInfo.getSketch(), 0, 0, this);
            gBuffer.drawImage(stageInfo.getPlayer_photo(), 20, 20, this);
            gBuffer.drawImage(stageInfo.getHp(), 110, 28, (int) (117 * ((float) Global.hp_player / Global.hp_player_max)), 12, this);
            gBuffer.drawImage(stageInfo.getMp(), 110, 58, (int) (117 * ((float) Global.mp_player / Global.mp_player_max)), 12, this);

            //绘制前和无色小晶块
            gBuffer.setColor(Color.white);
            gBuffer.drawString(Global.money+" G",140,100);
            gBuffer.drawString(Global.cube+" 个",200,100);

            //绘画通关显示
            if (is_success)
            {
                gBuffer.drawImage(stageInfo.getSuccess_bar(), width-300, 0, this);
                gBuffer.drawString(stageInfo.getEnd_text(),width-280,430);
                btn_next.setVisible(true);
            }

            // 从缓冲区写出到屏幕
            g.drawImage(iBuffer, 0, 0, this);
        }
        else
        {
            if (is_reboening)
            {
                g.drawImage(stageInfo.getBlack(),0,0,this);
            }
            else
            {
                g.drawImage(stageInfo.getPause_menu(),0,0,this);
            }
        }
    }

    @Override
    public void update(Graphics g)
    {
        paintComponent(g);
    }
}