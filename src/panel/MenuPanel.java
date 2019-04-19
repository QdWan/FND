package panel;

import manager.SceneManager;
import manager.SoundManager;
import res.Res;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends BasePanel
{
    private SceneManager parent = null;
    private int width = 1000;
    private int height = 600;

    private Image img_bg = null;
    private Image img_logo = null;
    private Image img_btn_new[] = null;
    private Image img_btn_load[] = null;
    private Image img_btn_about[] = null;
    private Image img_btn_exit[] = null;

    private SoundManager soundManager = null;

    public MenuPanel(SceneManager parent)
    {
        width = Util.width;
        height = Util.height;
        setBounds(0,0,width,height);
        this.parent = parent;
        setLayout(null);

        Init();
        AddComp();
    }

    private void Init()
    {
        soundManager = new SoundManager();
        soundManager.addSound("click","btn_click.wav");

        Toolkit toolkit = getToolkit();
        img_bg = toolkit.getImage(Res.getLogo("mainmenu_bgimg.jpg"));
        img_logo = toolkit.getImage(Res.getLogo("mainmenu_img_logo.png"));

        img_btn_new = new Image[3];
        img_btn_new[0] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_new_normal.png"));
        img_btn_new[1] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_new_hover.png"));
        img_btn_new[2] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_new_press.png"));

        img_btn_load = new Image[3];
        img_btn_load[0] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_load_normal.png"));
        img_btn_load[1] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_load_hover.png"));
        img_btn_load[2] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_load_press.png"));

        img_btn_about = new Image[3];
        img_btn_about[0] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_about_normal.png"));
        img_btn_about[1] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_about_hover.png"));
        img_btn_about[2] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_about_press.png"));

        img_btn_exit = new Image[3];
        img_btn_exit[0] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_exit_normal.png"));
        img_btn_exit[1] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_exit_hover.png"));
        img_btn_exit[2] = toolkit.getImage(Res.getLogo("mainmenu_img_btn_exit_press.png"));
    }


    private void AddComp()
    {
        int y_add = height/6;
        int x = (width-300)/2;
        int y_new = (height-50)/3;
        int y_load = y_new + y_add;
//        int y_about = y_load + y_add;
        int y_exit = y_load + y_add;

        //设置“新的游戏”按钮
        {
            JButton button = new JButton();
            button.setSize(300,50);
            button.setLocation(x,y_new);
            button.setIcon(new ImageIcon(img_btn_new[0]));
            button.setRolloverIcon(new ImageIcon(img_btn_new[1]));
            button.setPressedIcon(new ImageIcon(img_btn_new[2]));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusable(true);
            add(button);
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    soundManager.play("click");
                    parent.ChangeTo(SceneManager.NEW_GAME_STAGE);
                    parent = null;
                }
            });
        }

        //设置“载入存档”按钮
        {
            JButton button = new JButton();
            button.setSize(300,50);
            button.setLocation(x,y_load);
            button.setIcon(new ImageIcon(img_btn_load[0]));
            button.setRolloverIcon(new ImageIcon(img_btn_load[1]));
            button.setPressedIcon(new ImageIcon(img_btn_load[2]));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusable(true);
            add(button);
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    soundManager.play("click");
                    parent.reStart();
                }
            });
        }

//        //设置“关于游戏”按钮
//        {
//            JButton button = new JButton();
//            button.setSize(300,50);
//            button.setLocation(x,y_about);
//            button.setIcon(new ImageIcon(img_btn_about[0]));
//            button.setRolloverIcon(new ImageIcon(img_btn_about[1]));
//            button.setPressedIcon(new ImageIcon(img_btn_about[2]));
//            button.setBorderPainted(false);
//            button.setFocusPainted(false);
//            button.setContentAreaFilled(false);
//            button.setFocusable(true);
//            add(button);
//            button.addActionListener(new ActionListener()
//            {
//                @Override
//                public void actionPerformed(ActionEvent e)
//                {
//                    soundManager.play("click");
//                }
//            });
//        }

        //设置“退出游戏”按钮
        {
            JButton button = new JButton();
            button.setSize(300,50);
            button.setLocation(x,y_exit);
            button.setIcon(new ImageIcon(img_btn_exit[0]));
            button.setRolloverIcon(new ImageIcon(img_btn_exit[1]));
            button.setPressedIcon(new ImageIcon(img_btn_exit[2]));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusable(true);
            add(button);
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    soundManager.play("click");
                    parent.dispose();
                    parent.waitForExit();
                }
            });
        }

        //设置LOGO图片
        {
            ImageIcon imageIcon = new ImageIcon(img_logo);
            JLabel jLabel = new JLabel();
            jLabel.setIcon(imageIcon);
            jLabel.setBounds((width-686)/2,y_new-170,686,150);
            add(jLabel);
        }

        //设置背景图片
        {
            ImageIcon imageIcon = new ImageIcon(img_bg);
            JLabel jLabel = new JLabel();
            jLabel.setIcon(imageIcon);
            jLabel.setBounds(0,0,width,height);
            add(jLabel);
        }
    }
}
