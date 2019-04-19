package info;

import com.google.gson.JsonObject;
import manager.VoiceManager;
import res.Res;
import util.Util;

import java.awt.*;
import java.util.Random;

public class Monster
{
    private int monsterID = 0;
    public int x_pos = 0;
    public int y_pos = 0;
    public int v_add = 0;
    public int vx = 0;
    public int vy = 0;
    public boolean is_face_right = false;
    public boolean is_upair = false;
    public int floor = 0;
    public int atk = 0;
    public int level = 0;
    public int worth_money = 0;

    private int add_hp = 0;

    private boolean is_down = false;
    private int downing_time = 0;

    private Player player = null;

    public boolean is_bati = false;
    public boolean is_wait_for_end_bati = false;

    public MonsterState state = null;
    public MonsterAnim anim = null;
    public MonsterAI ai = null;
    public VoiceManager voiceManager = null;
    public StageInfo parent = null;

    public Monster(JsonObject object, Player player, StageInfo parent)
    {
        JsonObject arg_monster = null;
        arg_monster = object;
        this.player = player;
        this.parent = parent;

        state = new MonsterState(this);

        monsterID = arg_monster.get("id").getAsInt();
        state.initHp(arg_monster.get("hp").getAsInt());
        x_pos = arg_monster.get("x_pos").getAsInt();
        y_pos = arg_monster.get("y_pos").getAsInt();
        floor = y_pos;
        is_face_right = arg_monster.get("facetoright").getAsBoolean();
        v_add = arg_monster.get("v").getAsInt();
        atk = arg_monster.get("atk").getAsInt();
        level = arg_monster.get("level").getAsInt();
        worth_money = arg_monster.get("money").getAsInt();

        Init();
    }

    public void resumehp()
    {
        if (state.isAlive())
        {
            add_hp++;
            if (add_hp>=2)
            {
                state.hp++;
                add_hp=0;
            }
        }
    }

    public float getHPDivMax()
    {
        return state.gettheHP();
    }

    public void die()
    {
        Global.money+=worth_money;
    }

    private void Init()
    {
        anim = new MonsterAnim(monsterID,this);
        ai = new MonsterAI(this,monsterID,player);

        anim.setFaceTo(is_face_right);
        anim.action_stop();

        //初始化音效
        initVoice();
    }

    public boolean isAlive()
    {
        return state.isAlive();
    }

    private void initVoice()
    {
        voiceManager = new VoiceManager(monsterID,1);
    }

    public Image getImage()
    {
        return anim.getFrame();
    }

    public void monster_stop()
    {
        if (anim.is_dead)
        {
            return;
        }

        if (anim.now_state==MonsterAnim.ATTACKED_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE_OVER)
        {
            return;
        }

        if (anim.now_state!=MonsterAnim.STOP_STATE_LEFT && anim.now_state!=MonsterAnim.STOP_STATE_RIGHT)
        {
            anim.action_stop();
        }
    }

    public void monster_run_left()
    {
        if (anim.is_dead)
        {
            return;
        }

        if (anim.now_state==MonsterAnim.UPAIR_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE_OVER)
        {
            return;
        }

        vx=-v_add;

        if (anim.now_state!=MonsterAnim.RUN_STATE_LEFT)
        {
            anim.action_run_left();
        }
    }

    public void monster_run_right()
    {
        if (anim.is_dead)
        {
            return;
        }

        if (anim.now_state==MonsterAnim.UPAIR_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE_OVER)
        {
            return;
        }

        vx=v_add;

        if (anim.now_state!=MonsterAnim.RUN_STATE_RIGHT)
        {
            anim.action_run_right();
        }
    }

    public void monster_attacked(int value)
    {
        if (anim.is_dead)
        {
            return;
        }

        if (is_down)
        {
            return;
        }

        state.attacked(value);
        voiceManager.play("attacked");

        if (anim.now_state==MonsterAnim.UPAIR_STATE)
        {
            vy=-v_add;
            return;
        }

        if (is_face_right)
        {
            anim.face_to_right=true;
            x_pos-=8;
            if (x_pos<0)
            {
                x_pos=0;
            }
        }
        else
        {
            anim.face_to_right=false;
            x_pos+=8;
            if (x_pos>800)
            {
                x_pos=800;
            }
        }

        if (is_bati)
        {
            return;
        }

        anim.action_attacked();
    }

    public void monster_upaired(int value)
    {
        if (anim.is_dead)
        {
            return;
        }

        state.attacked(value);

        if (is_down)
        {
            return;
        }

        voiceManager.play("attacked");

        if (anim.now_state==MonsterAnim.UPAIR_STATE)
        {
            vy=-10*v_add;
            return;
        }

        is_upair = true;
        vy=-10*v_add;
        if (is_face_right)
        {
            anim.face_to_right=true;
        }
        else
        {
            anim.face_to_right=false;
        }
        vx = 0;

        anim.action_upair();

        downing_time = 1000;
    }

    public void monster_atkANDupair(int value)
    {
        if (anim.is_dead)
        {
            return;
        }

        state.attacked(value);
        voiceManager.play("attacked");

        is_upair = true;
        vy=-10*v_add;
        if (is_face_right)
        {
            anim.face_to_right=true;
            vx=-10*v_add;
        }
        else
        {
            anim.face_to_right=false;
            vx=10*v_add;
        }

        anim.action_upair();

        downing_time = 1000;
    }

    public void upairControl()
    {
        if (is_upair)
        {
            if (y_pos<150)
            {
                y_pos=150;
                vy=v_add;
            }
            vy+=v_add;
            if (y_pos>floor-10)
            {
                y_pos=floor;
                vy=0;
                vx=0;
                is_upair=false;

                if (!is_down)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            is_down = true;

                            while (downing_time>=0)
                            {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                downing_time-=10;
                            }

                            anim.action_stop();

                            downing_time = 0;
                            is_down = false;
                        }
                    }).start();
                }
            }
            if (x_pos<0)
            {
                x_pos=0;
            }
            else if (x_pos>800)
            {
                x_pos=800;
            }
        }
    }

    public void monster_attack()
    {
        if (anim.now_state==MonsterAnim.UPAIR_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE || anim.now_state==MonsterAnim.ATTACKED_STATE_OVER)
        {
            return;
        }

        if (anim.now_state<4 || anim.now_state>7)
        {
            anim.action_attack();
        }
    }
}

class MonsterState
{
    private int hp_max = 0;
    public int hp = 0;
    private boolean alive = false;
    private Monster parent = null;

    public MonsterState(Monster parent)
    {
        this.parent = parent;
    }

    public float gettheHP()
    {
        return (float) hp/hp_max;
    }

    public void initHp(int hp)
    {
        this.hp_max = hp;
        this.hp = hp;
        alive = true;
    }

    public void attacked(int value)
    {
        if (alive)
        {
            hp-=value;
            if (hp<=0)
            {
                hp=0;
                alive=false;
                parent.anim.is_dead = true;
                parent.voiceManager.play("dead");
                parent.parent.die_monster();
                parent.die();
            }
        }
    }

    public boolean isAlive()
    {
        return alive;
    }
}

class MonsterAnim
{
    public static final int STOP_STATE_LEFT = 0;
    public static final int STOP_STATE_RIGHT = 1;
    public static final int RUN_STATE_LEFT = 2;
    public static final int RUN_STATE_RIGHT = 3;
    public static final int ATTACK_STATE_LEFT = 4;
    public static final int ATTACK_STATE_RIGHT = 5;
    public static final int ATTACK_STATE_LEFT_OVER = 6;
    public static final int ATTACK_STATE_RIGHT_OVER = 7;
    public static final int ATTACKED_STATE = 8;
    public static final int ATTACKED_STATE_OVER = 9;
    public static final int UPAIR_STATE = 10;

    public int now_state = STOP_STATE_LEFT;
    public int now_frame = 0;
    public boolean face_to_right = false;
    public boolean is_dead = false;

    private boolean is_wait_for_end = false;
    private boolean attacked_for_end = false;
    private int attacked_over_time = 0;

    private Monster parent = null;

    private int monsterID = 0;
    private String type = "goblin";
    public int steady = Global.steady;

    private Toolkit toolkit = null;

    private Image[] stop_anim_left = null;
    private Image[] stop_anim_right = null;
    private Image[] run_anim_left = null;
    private Image[] run_anim_right = null;
    private Image[] attack_anim_left = null;
    private Image[] attack_anim_right = null;
    private Image[] attacked_anim_left = null;
    private Image[] attacked_anim_right = null;
    private Image[] upair_anim_left = null;
    private Image[] upair_anim_right = null;

    public MonsterAnim(int id, Monster monster)
    {
        monsterID = id;
        parent = monster;
        toolkit = Toolkit.getDefaultToolkit();

        Init();
    }

    public void setFaceTo(boolean face_to_right)
    {
        this.face_to_right = face_to_right;
    }

    public Image getFrame()
    {
        Image result = null;

        if (is_dead)
        {
            if (face_to_right)
            {
                return upair_anim_right[upair_anim_right.length-1];
            }
            else
            {
                return upair_anim_left[upair_anim_left.length-1];
            }
        }

        switch (now_state)
        {
            case STOP_STATE_RIGHT:
                now_frame++;
                if (now_frame>=stop_anim_right.length)
                {
                    now_frame=0;
                }
                result = stop_anim_right[now_frame];
                break;
            case RUN_STATE_LEFT:
                now_frame++;
                if (now_frame>=run_anim_left.length)
                {
                    now_frame=0;
                }
                result = run_anim_left[now_frame];
                break;
            case RUN_STATE_RIGHT:
                now_frame++;
                if (now_frame>=run_anim_right.length)
                {
                    now_frame=0;
                }
                result = run_anim_right[now_frame];
                break;
            case ATTACK_STATE_LEFT:
                now_frame++;
                if (now_frame>=attack_anim_left.length)
                {
                    now_state = ATTACK_STATE_LEFT_OVER;
                    now_frame = attack_anim_left.length-1;
                }
                result = attack_anim_left[now_frame];
                break;
            case ATTACK_STATE_RIGHT:
                now_frame++;
                if (now_frame>=attack_anim_right.length)
                {
                    now_state = ATTACK_STATE_RIGHT_OVER;
                    now_frame = attack_anim_right.length-1;
                }
                result = attack_anim_right[now_frame];
                break;
            case ATTACK_STATE_LEFT_OVER:
                result = attack_anim_left[3];
                if (!is_wait_for_end)
                {
                    if (monsterID==4 || 0<=monsterID && monsterID<=2)
                    {
                        parent.ai.attack(face_to_right);
                    }
                    else if (monsterID==3)
                    {
                        parent.ai.upair(face_to_right,parent.level);
                    }
                    else if (monsterID==5)
                    {
                        parent.ai.atkANDup(face_to_right,parent.level);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            is_wait_for_end=true;
                            try
                            {
                                Thread.sleep(steady);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            is_wait_for_end=false;
                            action_stop();
                        }
                    }).start();
                }
                break;
            case ATTACK_STATE_RIGHT_OVER:
                result = attack_anim_right[3];
                if (!is_wait_for_end)
                {
                    if (monsterID==4 || 0<=monsterID && monsterID<=2)
                    {
                        parent.ai.attack(face_to_right);
                    }
                    else if (monsterID==3)
                    {
                        parent.ai.upair(face_to_right,parent.level);
                    }
                    else if (monsterID==5)
                    {
                        parent.ai.atkANDup(face_to_right,parent.level);
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            is_wait_for_end=true;
                            try
                            {
                                Thread.sleep(steady);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            is_wait_for_end=false;
                            action_stop();
                        }
                    }).start();
                }
                break;
            case ATTACKED_STATE:
                now_frame++;
                if (now_frame>=attacked_anim_left.length)
                {
                    now_state = ATTACKED_STATE_OVER;
                    now_frame = attacked_anim_left.length-1;
                }

                if (face_to_right)
                {
                    result = attacked_anim_right[now_frame];
                }
                else
                {
                    result = attacked_anim_left[now_frame];
                }
                break;
            case ATTACKED_STATE_OVER:
                if (face_to_right)
                {
                    result = attacked_anim_right[attacked_anim_right.length-1];
                }
                else
                {
                    result = attacked_anim_left[attacked_anim_left.length-1];
                }
                if (!attacked_for_end)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            attacked_for_end=true;

                            while (attacked_over_time>0)
                            {
                                try
                                {
                                    Thread.sleep(1000);
                                    attacked_over_time--;
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            attacked_for_end = false;
                            if (now_state==ATTACKED_STATE_OVER) {
                                action_stop();
                            }
                        }
                    }).start();
                }
                break;
            case UPAIR_STATE:
                now_frame++;
                if (now_frame>=upair_anim_right.length)
                {
                    now_frame = upair_anim_left.length-1;
                }

                if (face_to_right)
                {
                    result = upair_anim_right[now_frame];
                }
                else
                {
                    result = upair_anim_left[now_frame];
                }

                break;
        }

        return result;
    }

    public void action_stop()
    {
        if (face_to_right)
        {
            now_state = STOP_STATE_RIGHT;
        }
        else
        {
            now_state = STOP_STATE_LEFT;
        }
        now_frame = -1;
    }

    public void action_run_left()
    {
        now_state = RUN_STATE_LEFT;
        face_to_right = false;
        now_frame = -1;
    }

    public void action_run_right()
    {
        now_state = RUN_STATE_RIGHT;
        now_frame = -1;
        face_to_right = true;
    }

    public void action_attack()
    {
        if (face_to_right)
        {
            now_state = ATTACK_STATE_RIGHT;
        }
        else
        {
            now_state = ATTACK_STATE_LEFT;
        }
        now_frame = -1;
    }

    public void action_upair()
    {
        now_frame = -1;
        now_state = UPAIR_STATE;
    }

    public void action_attacked()
    {
        attacked_over_time = 1;
        if (now_state==ATTACKED_STATE_OVER)
        {
            attacked_over_time = 2;
        }
        now_frame = -1;
        now_state = ATTACKED_STATE;
    }

    private void Init()
    {
        checkType();

        LOAD_STOP();
        LOAD_RUN();
        LOAD_ATTACK();
        LOAD_ATTACKED();
        LOAD_UPAIR();
    }

    private void checkType()
    {
        switch (monsterID)
        {
            case 0:
                //哥布林
                type = "goblin";
                steady = steady*3;
                break;
            case 1:
                type = "gbl";
                steady = steady*2;
                break;
            case 2:
                type = "hadis";
                steady = steady*2;
                break;
            case 3:
                type = "bufx";
                steady = steady*5;
                break;
            case 4:
                type = "cat";
                steady = steady;
                break;
            case 5:
                type = "zombie";
                steady = 2*steady;
                break;
        }
    }

    private void LOAD_STOP()
    {
        stop_anim_left = new Image[4];
        stop_anim_right = new Image[4];

        stop_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"normal_left_2.png"));
        stop_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"normal_left_1.png"));
        stop_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"normal_left_2.png"));
        stop_anim_left[3]=toolkit.getImage(Res.getMonster(type+"/"+"normal_left_3.png"));

        stop_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"normal_right_2.png"));
        stop_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"normal_right_1.png"));
        stop_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"normal_right_2.png"));
        stop_anim_right[3]=toolkit.getImage(Res.getMonster(type+"/"+"normal_right_3.png"));
    }

    private void LOAD_RUN()
    {
        if (monsterID==0 || monsterID==2 || monsterID==3 || monsterID==4 || monsterID ==5)
        {
            run_anim_left = new Image[12];
            run_anim_right = new Image[12];

            run_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_1.png"));
            run_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_1.png"));
            run_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_1.png"));
            run_anim_left[3]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_2.png"));
            run_anim_left[4]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_2.png"));
            run_anim_left[5]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_2.png"));
            run_anim_left[6]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_3.png"));
            run_anim_left[7]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_3.png"));
            run_anim_left[8]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_3.png"));
            run_anim_left[9]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_4.png"));
            run_anim_left[10]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_4.png"));
            run_anim_left[11]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_4.png"));

            run_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_1.png"));
            run_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_1.png"));
            run_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_1.png"));
            run_anim_right[3]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_2.png"));
            run_anim_right[4]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_2.png"));
            run_anim_right[5]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_2.png"));
            run_anim_right[6]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_3.png"));
            run_anim_right[7]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_3.png"));
            run_anim_right[8]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_3.png"));
            run_anim_right[9]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_4.png"));
            run_anim_right[10]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_4.png"));
            run_anim_right[11]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_4.png"));
        }
        else
        {
            run_anim_left = new Image[4];
            run_anim_right = new Image[4];

            run_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_1.png"));
            run_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_2.png"));
            run_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_3.png"));
            run_anim_left[3]=toolkit.getImage(Res.getMonster(type+"/"+"run_left_4.png"));

            run_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_1.png"));
            run_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_2.png"));
            run_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_3.png"));
            run_anim_right[3]=toolkit.getImage(Res.getMonster(type+"/"+"run_right_4.png"));
        }
    }

    private void LOAD_ATTACK()
    {
        if (0<=monsterID && monsterID<=5)
        {
            attack_anim_left = new Image[12];
            attack_anim_right = new Image[12];

            attack_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_1.png"));
            attack_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_1.png"));
            attack_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_1.png"));
            attack_anim_left[3]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_2.png"));
            attack_anim_left[4]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_2.png"));
            attack_anim_left[5]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_2.png"));
            attack_anim_left[6]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_3.png"));
            attack_anim_left[7]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_3.png"));
            attack_anim_left[8]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_3.png"));
            attack_anim_left[9]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_4.png"));
            attack_anim_left[10]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_4.png"));
            attack_anim_left[11]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_4.png"));

            attack_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_1.png"));
            attack_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_1.png"));
            attack_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_1.png"));
            attack_anim_right[3]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_2.png"));
            attack_anim_right[4]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_2.png"));
            attack_anim_right[5]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_2.png"));
            attack_anim_right[6]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_3.png"));
            attack_anim_right[7]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_3.png"));
            attack_anim_right[8]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_3.png"));
            attack_anim_right[9]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_4.png"));
            attack_anim_right[10]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_4.png"));
            attack_anim_right[11]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_4.png"));
        }
        else
        {
            attack_anim_left = new Image[4];
            attack_anim_right = new Image[4];

            attack_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_1.png"));
            attack_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_2.png"));
            attack_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_3.png"));
            attack_anim_left[3]=toolkit.getImage(Res.getMonster(type+"/"+"attack_left_4.png"));

            attack_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_1.png"));
            attack_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_2.png"));
            attack_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_3.png"));
            attack_anim_right[3]=toolkit.getImage(Res.getMonster(type+"/"+"attack_right_4.png"));
        }
    }

    public void LOAD_ATTACKED()
    {
        attacked_anim_left = new Image[3];
        attacked_anim_right = new Image[3];

        attacked_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_left_1.png"));
        attacked_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_left_2.png"));
        attacked_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_left_3.png"));

        attacked_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_right_1.png"));
        attacked_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_right_2.png"));
        attacked_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"attacked_right_3.png"));
    }

    private void LOAD_UPAIR()
    {
        upair_anim_left = new Image[3];
        upair_anim_right = new Image[3];

        upair_anim_left[0]=toolkit.getImage(Res.getMonster(type+"/"+"upair_left_1.png"));
        upair_anim_left[1]=toolkit.getImage(Res.getMonster(type+"/"+"upair_left_2.png"));
        upair_anim_left[2]=toolkit.getImage(Res.getMonster(type+"/"+"upair_left_3.png"));

        upair_anim_right[0]=toolkit.getImage(Res.getMonster(type+"/"+"upair_right_1.png"));
        upair_anim_right[1]=toolkit.getImage(Res.getMonster(type+"/"+"upair_right_2.png"));
        upair_anim_right[2]=toolkit.getImage(Res.getMonster(type+"/"+"upair_right_3.png"));
    }
}

class MonsterAI
{
    private Monster monster = null;
    private int monsterID = 0;
    private Player player = null;

    //测试使用的代码
    private Random random = new Random();

    public MonsterAI(Monster parent, int id, Player player)
    {
        this.monster = parent;
        this.monsterID = id;
        this.player = player;

        //【待完成】进行初始化

        //测试使用的代码
        Test();
    }

    public void attack(boolean toright)
    {
        player.player_attacked(toright);
        player.voiceManager.play("attacked");
        Global.hp_player-=monster.atk;
        if (Global.hp_player<0)
        {
            Global.hp_player =0;
        }
    }

    public void upair(boolean toright, int level)
    {
        player.player_upair(toright,level);
        player.voiceManager.play("attacked");
        Global.hp_player-=monster.atk;
        if (Global.hp_player<0)
        {
            Global.hp_player =0;
        }
    }

    public void atkANDup(boolean toright, int level)
    {
        player.player_atkANDup(toright,level);
        player.voiceManager.play("attacked");
        Global.hp_player-=monster.atk;
        if (Global.hp_player<0)
        {
            Global.hp_player =0;
        }
    }

    private void Test()
    {
        //测试功能
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (monster.state.isAlive())
                {
                    try
                    {
                        Thread.sleep(Global.frame_rate);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (monsterID==2 && monster.getHPDivMax()<0.25)
                    {
                        monster.resumehp();
                    }

                    if (!monster.is_bati && monster.anim.now_state!=10)
                    {
                        int num = random.nextInt(101);

                        switch (monsterID)
                        {
                            case 0:
                                if (num<1)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                            case 1:
                                if (num<5)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                            case 2:
                                if (num<5)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(2000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                            case 3:
                                if (num<5)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(1500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                            case 4:
                                if (num<5)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(800);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                            case 5:
                                if (num<5)
                                {
                                    monster.is_bati=true;
                                    if (!monster.is_wait_for_end_bati)
                                    {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                monster.is_wait_for_end_bati = true;
                                                int steady = monster.anim.steady;
                                                monster.anim.steady = 0;
                                                try {
                                                    Thread.sleep(2500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                monster.anim.steady = steady;
                                                monster.is_bati = false;
                                                monster.is_wait_for_end_bati = false;
                                            }
                                        }).start();
                                    }
                                }
                                break;
                        }
                    }

                    if (0<=monsterID && monsterID<=5)
                    {
                        if (player.x_pos+100<monster.x_pos)
                        {
                            if (monster.getHPDivMax()<0.15 && monster.is_bati==false)
                            {
                                monster.monster_run_right();
                            }
                            else
                            {
                                monster.monster_run_left();
                            }
                        }
                        else if (player.x_pos>monster.x_pos+100)
                        {
                            if (monster.getHPDivMax()<0.15 && monster.is_bati==false)
                            {
                                monster.monster_run_left();
                            }
                            else
                            {
                                monster.monster_run_right();
                            }
                        }
                        else
                        {
                            if (monster.getHPDivMax()<0.15)
                            {
                                monster.is_bati = true;
                                monster.monster_attack();
                            }
                            else
                            {
                                monster.monster_attack();
                            }
                        }
                    }
                }

                if (!monster.state.isAlive())
                {
                    monster.anim.action_upair();
                }
            }
        }).start();;
    }
}