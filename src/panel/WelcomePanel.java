package panel;

import com.google.gson.JsonArray;
import info.Global;
import manager.ConfigManager;
import manager.MusicManager;
import manager.SceneManager;
import res.Res;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomePanel extends BasePanel implements Runnable
{
    private SceneManager parent = null;
    private JsonArray config_welcomeplaylist = null;
    private Image[] splashes = null;

    private int now_splash = 0;
    private float splash_alpha = 0f;
    private float alpha_add = 1f;
    private boolean is_adding = true;

//    private Image iBuffer = null;
//    private Graphics2D gBuffer = null;

    public WelcomePanel(SceneManager parent)
    {
        setBounds(0,0,Util.width,Util.height);
        this.parent = parent;
        config_welcomeplaylist = ConfigManager.getConfig().get("welcomeplaylist").getAsJsonArray();

        Init();
        MusicManager.loadBGM(Res.getBGM("welcome.wav"));
        MusicManager.playBGM();
    }

    @Override
    public void run()
    {
        while (Global.is_welcome)
        {
            try
            {
                Thread.sleep(30);
            }
            catch
            (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (is_adding)
            {
                splash_alpha+=alpha_add;
                if (splash_alpha>=100)
                {
                    splash_alpha=100;
                    is_adding=false;
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                splash_alpha-=2*alpha_add;
                if (splash_alpha<=0)
                {
                    splash_alpha=0;
                    is_adding=true;
                    now_splash++;
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if (now_splash>3)
                    {
                        config_welcomeplaylist = null;
                        splashes = null;
//                        iBuffer = null;
//                        gBuffer = null;
                        parent.ChangeTo(SceneManager.MENU_STAGE);
                        parent = null;
                        return;
                    }
                }
            }

            repaint();
        }
    }

    private void Init()
    {
        splashes = new Image[config_welcomeplaylist.size()];
        Toolkit toolkit = getToolkit();
        for (int i=0;i<splashes.length;i++)
        {
            splashes[i]=toolkit.getImage(Res.getSplash(config_welcomeplaylist.get(i).getAsString()));
        }

        new Thread(this).start();

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Global.is_welcome = false;
                config_welcomeplaylist = null;
                splashes = null;
                parent.ChangeTo(SceneManager.MENU_STAGE);
                parent = null;
                return;
            }
        });

        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                Global.is_welcome = false;
                config_welcomeplaylist = null;
                splashes = null;
                parent.ChangeTo(SceneManager.MENU_STAGE);
                parent = null;
                return;
            }
        });
    }

    @Override
    public void paint(Graphics g)
    {
        requestFocus();
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        g2.fillRect(0,0,this.getWidth(),this.getHeight());
        g2.setComposite(AlphaComposite.SrcOver.derive((float)splash_alpha/100f));
        g2.drawImage(splashes[now_splash],0,0,this);
//        if (iBuffer==null)
//        {
//            iBuffer = createImage(this.getSize().width,this.getSize().height);
//            gBuffer = (Graphics2D) iBuffer.getGraphics();
//        }
//
//        gBuffer.setColor(Color.black);
//        gBuffer.fillRect(0,0,this.getSize().width,this.getSize().height);
//        gBuffer.setComposite(AlphaComposite.SrcOver.derive((float)splash_alpha/100f));
//        gBuffer.drawImage(splashes[now_splash],0,0,this);

//        g.drawImage(iBuffer,0,0,this);
    }

//    @Override
//    public void update(Graphics g)
//    {
//        paint(g);
//    }
}
