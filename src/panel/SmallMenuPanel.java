package panel;

import manager.MusicManager;
import manager.SceneManager;
import res.Res;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SmallMenuPanel extends JPanel
{
    private SceneManager grandparent = null;
    private GamePanel parent = null;
    private Image bg = null;
    private SmallMenuPanel me = this;

    public SmallMenuPanel(SceneManager grandparent, GamePanel parent)
    {
        this.setLayout(null);
        this.grandparent = grandparent;
        this.parent = parent;

        Init();
        InitUI();
        repaint();
    }

    private void Init()
    {
        MusicManager.setVol(-15.0f);
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
                public void actionPerformed(ActionEvent e) {
                    MusicManager.setVol(-10.0f);
                    parent.is_running = true;
                    parent.btn_menu.setVisible(true);
                    parent.remove(me);
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
