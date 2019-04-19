package info;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import manager.ConfigManager;
import res.Res;

import java.awt.*;
import java.io.InputStream;

public class StageInfo
{
    private int stageID = 0;
    private int playerID = 0;
    private boolean is_city = false;
    private int num_area = 1;
    private int now_area = 0;

    private Image background = null;
    private Image pause_menu = null;
    private Image sketch = null;
    private Image player_photo = null;
    private Image hp = null;
    private Image mp = null;
    private Image success_bar = null;

    public int cube_num = 0;

    private Image[] arrow = null;
    private int now_frame = -1;

    public InputStream bgm = null;

    public Image black = null;

    public int deadnum = 0;
    public int allnum = 0;
    public boolean check = false;

    JsonArray jsonArray_area = null;

    private Player player = null;
    private Monster[] monsters = null;
    private String end_text = null;

    public StageInfo(int stageID)
    {
        this.stageID = stageID;

        Init();
    }

    public Image getArrow()
    {
        now_frame++;
        if (now_frame>5)
        {
            now_frame=0;
        }
        return arrow[now_frame];
    }

    private void Init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        JsonObject scene = ConfigManager.getConfig().get("stage").getAsJsonArray().get(stageID).getAsJsonObject();

        background = toolkit.getImage(Res.getScene(scene.get("background").getAsString()));
        black = toolkit.getImage(Res.getScene("black.png"));
        success_bar = toolkit.getImage(Res.getLogo("success.png"));

        cube_num = scene.get("cube").getAsInt();

        end_text = scene.get("end").getAsString();

        arrow = new Image[6];
        arrow[0] = toolkit.getImage(Res.getLogo("next_1.png"));
        arrow[1] = toolkit.getImage(Res.getLogo("next_1.png"));
        arrow[2] = toolkit.getImage(Res.getLogo("next_2.png"));
        arrow[3] = toolkit.getImage(Res.getLogo("next_2.png"));
        arrow[4] = toolkit.getImage(Res.getLogo("next_3.png"));
        arrow[5] = toolkit.getImage(Res.getLogo("next_3.png"));

        playerID = scene.get("player").getAsInt();
        player = new Player(playerID);
        player.setX_pos(scene.get("x_player").getAsInt());
        player.setY_pos(scene.get("y_player").getAsInt());
        player.initFaceTo((scene.get("player_faceto").getAsInt()==0)?true:false);
        player.v = scene.get("v_player").getAsInt();

        Global.gap_attack = scene.get("gap_attack").getAsInt();
        Global.steady = scene.get("steady").getAsInt();

        bgm = Res.getBGM(scene.get("bgm").getAsString());

        is_city = scene.get("is_city").getAsBoolean();
        player.set_is_in_city(is_city);

        sketch = toolkit.getImage(Res.getLogo("player_info.png"));

        switch (playerID)
        {
            case 0:
                player_photo = toolkit.getImage(Res.getPlayer("blademaster/photo.png"));
                break;
        }

        Global.hp_player_max = scene.get("hp").getAsInt();
        Global.mp_player_max = scene.get("mp").getAsInt();
        Global.hp_player = Global.hp_player_max;
        Global.mp_player = Global.mp_player_max;
        Global.atk_player = scene.get("atk").getAsInt();

        hp = toolkit.getImage(Res.getLogo("hp.png"));
        mp = toolkit.getImage(Res.getLogo("mp.png"));

        pause_menu = toolkit.getImage(Res.getScene(scene.get("pause_menu").getAsString()));

        //Monster[]的初始化
        if (!is_city)
        {
            jsonArray_area = scene.get("monster").getAsJsonArray();
            num_area = jsonArray_area.size();
            now_area = 0;

            nextArea();
        }
    }

    public int getStageID()
    {
        return stageID;
    }

    public Image getSuccess_bar()
    {
        return success_bar;
    }

    public String getEnd_text()
    {
        return end_text;
    }

    public void setStageID(int stageID)
    {
        this.stageID = stageID;
    }

    public Image getBackground()
    {
        return background;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Image getSketch()
    {
        return sketch;
    }

    public Image getPlayer_photo()
    {
        return player_photo;
    }

    public Image getHp()
    {
        return hp;
    }

    public Image getBlack()
    {
        return black;
    }

    public Image getMp()
    {
        return mp;
    }

    public Image getPause_menu()
    {
        return pause_menu;
    }

    public Monster[] getMonsters()
    {
        return monsters;
    }

    public void nextArea()
    {
        //变化下一关的Monster[]
        if (now_area<num_area)
        {
            JsonArray jsonArray_monster = jsonArray_area.get(now_area).getAsJsonArray();

            int num_monster = jsonArray_monster.size();

            allnum = num_monster;
            deadnum = 0;
            check = false;

            monsters = new Monster[num_monster];
            for (int i=0;i<num_monster;i++)
            {
                monsters[i]=new Monster(jsonArray_monster.get(i).getAsJsonObject(),player,this);
            }

            now_area++;
        }
        else
        {
            monsters = null;
        }

        player.setMonsters(monsters);
    }

    public void die_monster()
    {
        deadnum++;
        if (deadnum>=allnum)
        {
            check = true;
        }
    }

    public boolean isIs_city()
    {
        return is_city;
    }
}
