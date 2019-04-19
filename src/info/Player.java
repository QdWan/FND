package info;

import manager.VoiceManager;
import util.Util;

import java.awt.*;

public class Player
{
    public static final int STOP_STATE = 0;
    public static final int RUN_STATE_RIGHT = 1;
    public static final int RUN_STATE_LEFT = 2;
    public static final int ATTACK_A_STATE = 3;
    public static final int ATTACK_B_STATE = 4;
    public static final int ATTACK_C_STATE = 5;
    public static final int ATTACK_A_OVER_STATE = 6;
    public static final int ATTACK_B_OVER_STATE = 7;
    public static final int ATTACK_C_OVER_STATE = 8;
    public static final int JUMPING_STATE = 9;
    public static final int DOWNING_STATE = 10;
    public static final int BACK_JUMP_STATE = 11;
    public static final int BACK_DOWN_STATE = 12;
    public static final int ATTACKED_STATE = 13;
    public static final int UPAIR_STATE = 14;
    public static final int DOWNED_STATE = 15;
    public static final int UPAIR_DOWN_STATE = 16;

    public static final int SKILL_GUIZHAN_STATE = 17;
    public static final int SKILL_LIEBOZHAN_STATE = 18;
    public static final int SKILL_BADAOZHAN_STATE = 19;

    private int playerID = 0;
    public int x_pos = 0;
    public int y_pos = 0;
    public int v = 0;
    public int vx = 0;
    public int vy = 0;

    public int now_state = STOP_STATE;
    public boolean face_to_right = true;
    private boolean in_city = false;
    public boolean alive = true;

    private Anim anim = null;
    public VoiceManager voiceManager = null;
    public Collision collision = null;

    private int[] cd = null;
    /*
    0-后跳
     */

    public Player(int playerID)
    {
        this.playerID = playerID;

        //初始化动画类
        initAnim();

        //初始化音效
        initSound();

        //初始化碰撞类
        collision = new Collision(this);

        //初始化冷却时间
        initCD();
    }

    private void initCD()
    {
        cd = new int[5];

        //基础CD【静态的】
        cd[0]=0;
        cd[1]=0;

        //技能CD【根据配置文件变化】【不了】
        cd[2]=0;
        cd[3]=0;
        cd[4]=0;
    }

    public void setMonsters(Monster[] monsters)
    {
        collision.setMonsters(monsters);
    }

    private void initAnim()
    {
        anim = new Anim(playerID,this);
    }
    private void initSound()
    {
        voiceManager = new VoiceManager(playerID,0);
    }

    public void player_stop()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state == RUN_STATE_RIGHT || now_state == RUN_STATE_LEFT)
        {
            anim.action_stop();
            vx = 0;
            now_state = STOP_STATE;
        }
    }

    public void player_jump()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (now_state!=BACK_JUMP_STATE && now_state!=BACK_DOWN_STATE && now_state!=DOWNING_STATE && now_state!=JUMPING_STATE && now_state!=ATTACK_A_STATE && now_state!=ATTACK_B_STATE && now_state!=ATTACK_C_STATE)
        {
            anim.action_jumping();
            vy = -5*v;
            now_state = JUMPING_STATE;
        }
    }

    public void player_skill_badaozhan()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (cd[4]>0)
        {
            return;
        }

        if (Global.mp_player<350)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            return;
        }

        if (in_city)
        {
            return;
        }
        vx = 0;
        vy = 0;
        anim.action_skill_badaozhan();
        now_state = SKILL_BADAOZHAN_STATE;
        cd[4]=9000;
        Global.mp_player-=350;
        if (Global.mp_player<0)
        {
            Global.mp_player = 0;
        }
    }

    public void player_attacked(boolean towhere)
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE || now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            vy=0;
            y_pos=350;
        }

        now_state=ATTACKED_STATE;

        if (towhere)
        {
            face_to_right = false;
            anim.is_face_right = false;
            if (vx<0)
            {
                vx=0;
            }
        }
        else
        {
            face_to_right = true;
            anim.is_face_right = true;
            if (vx>0)
            {
                vx=0;
            }
        }
        anim.action_attacked();
    }

    public void player_skill_guizhan()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (cd[2]>0)
        {
            return;
        }

        if (Global.mp_player<100)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            return;
        }

        if (in_city)
        {
            return;
        }
        vx = 0;
        vy = 0;
        anim.action_skill_guizhan();
        now_state = SKILL_GUIZHAN_STATE;
        cd[2]=3500;
        Global.mp_player-=100;
        if (Global.mp_player<0)
        {
            Global.mp_player = 0;
        }
    }

    public void player_skill_liebozhan()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (cd[3]>0)
        {
            return;
        }

        if (Global.mp_player<250)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            return;
        }

        if (in_city)
        {
            return;
        }
        vx = 0;
        vy = 0;
        anim.action_skill_liebozhan();
        now_state = SKILL_LIEBOZHAN_STATE;
        cd[3]=5000;
        Global.mp_player-=250;
        if (Global.mp_player<0)
        {
            Global.mp_player = 0;
        }
    }

    public void resumeMP()
    {
        if (alive)
        {
            Global.mp_player+=1;
            if (Global.mp_player>Global.mp_player_max)
            {
                Global.mp_player = Global.mp_player_max;
            }
        }
    }

    public void player_upair(boolean towhere,int level)
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE || now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            vy=0;
            y_pos=350;
        }

        now_state=UPAIR_STATE;

        if (towhere)
        {
            face_to_right = false;
            anim.is_face_right = false;
            if (vx<0)
            {
                vx=0;
            }
        }
        else
        {
            face_to_right = true;
            anim.is_face_right = true;
            if (vx>0)
            {
                vx=0;
            }
        }

        anim.action_upair();
        vy=-20*level;
    }

    public void player_atkANDup(boolean towhere,int level)
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE || now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            vy=0;
            y_pos=350;
        }

        now_state=UPAIR_STATE;

        if (towhere)
        {
            face_to_right = false;
            anim.is_face_right = false;
            vx=level*30;
        }
        else
        {
            face_to_right = true;
            anim.is_face_right = true;
            vx=level*-30;
        }

        anim.action_upair();
        vy=-20*level;
    }

    public void controlUpAir()
    {
        if (in_city)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE)
        {
            vy=(int)(vy*0.95);
            if (y_pos<=250)
            {
                vy = 0;
                now_state=UPAIR_DOWN_STATE;
            }
        }
        else if (now_state==UPAIR_DOWN_STATE)
        {
            vy+=v;
            if (y_pos>=350)
            {
                vy = 0;
                vx=0;
                y_pos=350;
                now_state = DOWNED_STATE;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        anim.action_stop();
                        now_state = STOP_STATE;
                    }
                }).start();
            }
        }
    }

    public void controlJump()
    {
        if (in_city)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE)
        {
            vy=(int)(vy*0.95);
            if (y_pos<=250)
            {
                vy = 0;
                now_state=DOWNING_STATE;
            }
        }
        else if (now_state==DOWNING_STATE)
        {
            vy+=v;
            if (y_pos>=350)
            {
                vy = 0;
                vx=0;
                y_pos=350;
                anim.action_stop();
                now_state = STOP_STATE;
            }
        }
    }

    public void computCD()
    {
        for (int i=0;i<cd.length;i++)
        {
            if (cd[i]>0)
            {
                cd[i]-=Global.frame_rate;
                if (cd[i]<0)
                {
                    cd[i]=0;
                }
            }
        }
    }

    public void controlBackJump()
    {
        if (in_city)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE)
        {
            vy=(int)(vy*0.95);
            if (y_pos<=310)
            {
                vy = 0;
                now_state=BACK_DOWN_STATE;
            }
        }
        else if (now_state==BACK_DOWN_STATE)
        {
            vy+=v;
            if (y_pos>=350)
            {
                vy = 0;
                vx=0;
                y_pos=350;
                anim.action_stop();
                now_state = STOP_STATE;
            }
        }
    }

    public void set_is_in_city(boolean is_city)
    {
        in_city = is_city;
    }

    public void player_backjump()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (cd[0]>0)
        {
            return;
        }

        cd[0] = 200;

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state!=JUMPING_STATE && now_state!=DOWNING_STATE)
        {
            if (face_to_right)
            {
                anim.action_stop();
                vy = -5*v;
                vx = -2*v;
            }
            else
            {
                anim.action_stop();
                vy = -5*v;
                vx = 2*v;
            }
            now_state = BACK_JUMP_STATE;
        }
    }

    public void player_run_left()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            vx = -2*v;
            face_to_right = false;
            anim.is_face_right = false;
            return;
        }

        if (now_state!=RUN_STATE_LEFT && now_state!=RUN_STATE_RIGHT && now_state!=ATTACK_A_STATE && now_state!=ATTACK_B_STATE)
        {
            anim.action_run_left();
            vx = -v;
            now_state = RUN_STATE_LEFT;
            face_to_right = false;
        }
    }

    public Image getSkillImage()
    {
        return anim.getSkillAnim();
    }

    public void player_run_right()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            vx = 2*v;
            face_to_right = true;
            anim.is_face_right = true;
            return;
        }

        if (now_state!=RUN_STATE_LEFT && now_state!=RUN_STATE_RIGHT && now_state!=ATTACK_A_STATE && now_state!=ATTACK_B_STATE)
        {
            anim.action_run_right();
            vx = v;
            now_state = RUN_STATE_RIGHT;
            face_to_right = true;
        }
    }

    public void player_skill_upattack()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (cd[1]>0)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            //进行下刺攻击

            return;
        }

        if (in_city)
        {
            return;
        }
        vx = 0;
        anim.action_stop();
        now_state = ATTACK_C_STATE;
        anim.action_attack_c();
        voiceManager.play("attack_c");
        cd[1]=1000;
    }

    public void player_attack()
    {
        if (!alive)
        {
            return;
        }

        if (SKILL_GUIZHAN_STATE<=now_state&&now_state<=SKILL_BADAOZHAN_STATE)
        {
            return;
        }

        if (now_state==UPAIR_STATE || now_state==DOWNED_STATE || now_state==UPAIR_DOWN_STATE)
        {
            return;
        }

        if (now_state==ATTACKED_STATE)
        {
            return;
        }

        if (now_state==BACK_JUMP_STATE || now_state==BACK_DOWN_STATE)
        {
            return;
        }

        if (now_state==JUMPING_STATE || now_state==DOWNING_STATE)
        {
            return;
        }

        if (in_city || now_state==ATTACK_A_STATE || now_state==ATTACK_B_STATE || now_state==ATTACK_C_STATE)
        {
            return;
        }
        else
        {
            if (now_state==ATTACK_A_OVER_STATE)
            {
                now_state = ATTACK_B_STATE;
                anim.action_attack_b();
                vx = face_to_right ? v/10 : -(v/10);
                voiceManager.play("attack_b");
            }
            else if (now_state==ATTACK_B_OVER_STATE)
            {
                now_state = ATTACK_C_STATE;
                anim.action_attack_c();
                vx = face_to_right ? v/10 : -(v/10);
                voiceManager.play("attack_c");
            }
            else if (now_state==ATTACK_C_OVER_STATE)
            {
                return;
            }
            else
            {
                now_state = ATTACK_A_STATE;
                anim.action_attack_a();
                vx = face_to_right ? v/10 : -(v/10);
                voiceManager.play("attack_a");

            }
        }
    }

    public Image getImage()
    {
        return anim.getAnim();
    }

    public int getX_pos()
    {
        return x_pos;
    }

    public void setX_pos(int x_pos)
    {
        this.x_pos = x_pos;
    }

    public int getY_pos()
    {
        return y_pos;
    }

    public void setY_pos(int y_pos)
    {
        this.y_pos = y_pos;
    }

    public void initFaceTo(boolean face)
    {
        anim.is_face_right = face;
        face_to_right = face;
        anim.action_stop();
        now_state = STOP_STATE;
    }
}
