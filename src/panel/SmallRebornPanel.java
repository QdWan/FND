package panel;

import manager.MusicManager;
import manager.SceneManager;
import res.Res;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SmallRebornPanel extends JPanel
{
    private GamePanel parent = null;
    private SceneManager grandparent = null;
    private Image bg = null;
    private SmallRebornPanel me = this;

    public SmallRebornPanel(SceneManager pp, GamePanel p)
    {
        setLayout(null);
        grandparent = pp;
        parent = p;

        Init();
        InitUI();
        repaint();
    }

    private void Init()
    {
        bg = getToolkit().getImage(Res.getLogo("menu_bg.png"));
    }


    private void InitUI()
    {
        //设置“返回”按钮
        {
            JButton button = new JButton();
            button.setSize(90,34);
            button.setLocation(40,30);
            button.setIcon(new ImageIcon(getToolkit().getImage(Res.getLogo("btn_return_normal.png"))));
            button.setRolloverIcon(new ImageIcon(getToolkit().getImage(Res.getLogo("btn_return_hover.png"))));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusable(true);
            add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    grandparent.ChangeTo(SceneManager.MENU_STAGE);
                }
            });
        }

        //设置“复活”按钮
        {
            JButton button = new JButton();
            button.setSize(119,45);
            button.setLocation(380,250);
            button.setIcon(new ImageIcon(getToolkit().getImage(Res.getLogo("btn_reborn_normal.png"))));
            button.setRolloverIcon(new ImageIcon(getToolkit().getImage(Res.getLogo("btn_reborn_hover.png"))));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusable(true);
            add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    grandparent.reStart();
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(bg,0,0,this.getWidth(),this.getHeight(),this);
    }
}
