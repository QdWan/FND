package manager;

import com.google.gson.JsonObject;
import info.Global;
import info.StageInfo;
import panel.EndPanel;
import panel.GamePanel;
import panel.MenuPanel;
import panel.WelcomePanel;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class SceneManager extends JFrame
{
    private JPanel main_panel = new JPanel();
    public SceneManager()
    {
        //初始化窗口
        Init();

        //展示欢迎画面
        Global.is_welcome = true;
        ChangeTo(WELCOME_STAGE);

        //展示菜单画面【debug用】
//        ChangeTo(MENU_STAGE);

        //展示新的游戏画面【debug用】
//        ChangeTo(NEW_GAME_STAGE);

//        指定关卡【debug用】
//        TotheStage(0);
    }

    private void Init()
    {
        JsonObject config_frame = ConfigManager.getConfig().get("frame").getAsJsonObject();
        setTitle(config_frame.get("title").getAsString());
        Util.width = config_frame.get("width").getAsInt();
        Util.height = config_frame.get("height").getAsInt();
        Global.frame_rate = config_frame.get("frame_rate").getAsInt();
        Global.stage_num = config_frame.get("stage_num").getAsInt();
        setSize(Util.width,Util.height+30);
        setResizable(false);
        readConfig();

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                waitForExit();
            }
        });

        setContentPane(main_panel);
        main_panel.setLayout(null);
        setVisible(true);
    }

    private void  readConfig()
    {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("./data.cfg");
        } catch (FileNotFoundException e) {

        }

        if (fileReader==null)
        {
            return;
        }

        BufferedReader br = new BufferedReader(fileReader);
        String str = null;
        try {
            str = br.readLine();
        } catch (IOException e) {

        }

        if (str!=null)
        {
            String[] data = str.split(";");
            Global.now_stage = Integer.parseInt(((data[0]).split("="))[1]);
            Global.money = Integer.parseInt(((data[1]).split("="))[1]);
            Global.cube = Integer.parseInt(((data[2]).split("="))[1]);
        }
    }

    public void storeData()
    {
        //【待完成】数据储存
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("./data.cfg");
        } catch (IOException e) {
        }
        try {
            fileWriter.write("now_stage="+(Global.now_stage)+";");
            fileWriter.write("money="+Global.money+";");
            fileWriter.write("cube="+Global.cube);
        } catch (IOException e) {
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
        }
    }

    public void waitForExit()
    {
        //做一些数据存储的工作
        Util.thread_running = false;

        MusicManager.stopAll();
        MusicManager.release();

        storeData();

        Util.log("运行结束");

        System.exit(0);
    }

    private void Welcome()
    {
        main_panel.removeAll();
        main_panel.add(new WelcomePanel(this));
    }

    private void showMenuPanel()
    {
        main_panel.removeAll();
        main_panel.add(new MenuPanel(this));
    }

    private void newGame()
    {
        Global.now_stage=0;
        Global.money = 0;
        Global.cube = 0;

        if (Global.now_stage>=Global.stage_num)
        {
            ChangeTo(END_STAGE);
            return;
        }

        StageInfo stageInfo = new StageInfo(Global.now_stage);

        main_panel.removeAll();
        main_panel.add(new GamePanel(this,stageInfo));
    }

    private void TotheStage(int stage)
    {
        Global.now_stage=stage;
        Global.money = 0;
        Global.cube = 0;

        if (Global.now_stage>=Global.stage_num)
        {
            ChangeTo(END_STAGE);
            return;
        }

        StageInfo stageInfo = new StageInfo(Global.now_stage);

        main_panel.removeAll();
        main_panel.add(new GamePanel(this,stageInfo));
    }

    public void reStart()
    {
        Global.money=0;
        StageInfo stageInfo = new StageInfo(Global.now_stage);

        main_panel.removeAll();
        main_panel.add(new GamePanel(this,stageInfo));
    }

    private void nextGame()
    {
        Global.now_stage++;

        if (Global.now_stage>=Global.stage_num)
        {
            ChangeTo(END_STAGE);
            return;
        }

        StageInfo stageInfo = new StageInfo(Global.now_stage);

        main_panel.removeAll();
        main_panel.add(new GamePanel(this,stageInfo));
    }

    public static final int BLACK_STAGE = -1;
    public static final int WELCOME_STAGE = 0;
    public static final int MENU_STAGE = 1;
    public static final int NEW_GAME_STAGE = 2;
    public static final int NEXT_GAME_STAGE = 3;
    public static final int END_STAGE = 4;

    public void ChangeTo(int type)
    {
        switch (type)
        {
            case BLACK_STAGE:
                main_panel.removeAll();
                main_panel.setBackground(Color.black);
                break;
            case WELCOME_STAGE:
                Welcome();
                break;
            case MENU_STAGE:
                showMenuPanel();
                break;
            case NEW_GAME_STAGE:
                newGame();
                break;
            case NEXT_GAME_STAGE:
                nextGame();
                break;
            case END_STAGE:
                end();
                break;
        }
        main_panel.repaint();
    }

    public void end()
    {
        main_panel.removeAll();
        main_panel.add(new EndPanel(this));
    }
}
