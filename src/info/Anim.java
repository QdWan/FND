package info;

import res.Res;
import util.Util;

import java.awt.*;

public class Anim
{
    public static final int STOP_STATE_RIGHT = 0;
    public static final int STOP_STATE_LEFT = 1;
    public static final int RUN_STATE_RIGHT = 2;
    public static final int RUN_STATE_LEFT = 3;
    public static final int ATTACK_A_STAGE_RIGHT = 4;
    public static final int ATTACK_A_STAGE_LEFT = 5;
    public static final int ATTACK_B_STAGE_RIGHT = 6;
    public static final int ATTACK_B_STAGE_LEFT = 7;
    public static final int ATTACK_C_STAGE_RIGHT = 8;
    public static final int ATTACK_C_STAGE_LEFT = 9;
    public static final int JUMPING_STATE = 10;
    public static final int ATTACKED_STATE = 11;
    public static final int ATTACKED_STATE_OVER = 12;
    public static final int UPAIR_STATE = 13;

    public static final int SKILL_GUIZHAN_LEFT_STATE = 14;
    public static final int SKILL_GUIZHAN_RIGHT_STATE = 15;

    public static final int SKILL_LIEBOZHAN_LEFT_STATE = 16;
    public static final int SKILL_LIEBOZHAN_RIGHT_STATE = 17;

    private int playerID = 0;
    private Toolkit toolkit = null;
    private Player parent = null;
    private String player_name = null;

    private int now_frame = -1;
    public boolean is_face_right = true;
    private int now_state = STOP_STATE_LEFT;

    private int attacked_over_time = 0;
    private boolean is_attaceding = false;

    private boolean attack_a_waitforend = false;
    private boolean attack_b_waitforend = false;
    private boolean attack_c_waitforend = false;

    private Image[] upair = null;
    private Image[] stop_anim_left  = null;
    private Image[] stop_anim_right  = null;
    private Image[] run_anim_left  = null;
    private Image[] run_anim_right  = null;
    private Image[] attack_a_anim_right  = null;
    private Image[] attack_a_anim_left  = null;
    private Image[] attack_b_anim_right  = null;
    private Image[] attack_b_anim_left  = null;
    private Image[] attack_c_anim_right  = null;
    private Image[] attack_c_anim_left  = null;
    private Image[] attacked_anim_left = null;
    private Image[] attacked_anim_right = null;

    private Image[] skill_guizhan_left = null;
    private Image[] skill_guizhan_right = null;
    private boolean is_thread_guizhan = false;

    private Image[] skill_liebozhan_left = null;
    private Image[] skill_liebozhan_right = null;
    private boolean is_thread_liebozhan = false;

    private Image[] skill_badaozhan = null;
    private boolean is_thread_badaozhan = false;
    private boolean is_badaozhan = false;

    private int big_skill_frame = 0;

    public Anim(int ID, Player player)
    {
        this.playerID = ID;
        this.parent = player;
        toolkit = Toolkit.getDefaultToolkit();

        checkName();
        load_STOP_ANIM();
        load_RUN_ANIM();
        load_ATTACK_ANIM();
        load_ATTACKED_ANIM();
        load_UPAIR();
        load_SKILL();
    }

    private void load_SKILL()
    {
        load_GUIZHAN();
        load_LIEBOZHAN();
        load_BADAOZHAN();
    }

    private void load_BADAOZHAN()
    {
        skill_badaozhan = new Image[69];

        for (int i=0;i<skill_badaozhan.length;i++)
        {
            skill_badaozhan[i]=toolkit.getImage(Res.getPlayer(player_name+"/big/badaozhan_"+((i+1)%23)+".png"));
        }
    }

    private void load_LIEBOZHAN()
    {
        skill_liebozhan_left = new Image[25];
        skill_liebozhan_right = new Image[25];

        for (int i=0;i<skill_liebozhan_left.length;i++)
        {
            if (0<=i&&i<=2)
            {
                skill_liebozhan_left[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_left_1.png"));
                skill_liebozhan_right[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_right_1.png"));
            }
            else if (3<=i && i<=5)
            {
                skill_liebozhan_left[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_left_2.png"));
                skill_liebozhan_right[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_right_2.png"));
            }
            else if (22<=i&&i<=24)
            {
                skill_liebozhan_left[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_left_5.png"));
                skill_liebozhan_right[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_right_5.png"));
            }
            else
            {
                if (i%2==0)
                {
                    skill_liebozhan_left[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_left_3.png"));
                    skill_liebozhan_right[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_right_3.png"));
                }
                else
                {
                    skill_liebozhan_left[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_left_4.png"));
                    skill_liebozhan_right[i]=toolkit.getImage(Res.getPlayer(player_name+"/liebozhan_right_4.png"));
                }
            }
        }
    }

    private void load_GUIZHAN()
    {
        skill_guizhan_left = new Image[12];
        skill_guizhan_right = new Image[12];

        skill_guizhan_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_1.png"));
        skill_guizhan_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_1.png"));
        skill_guizhan_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_1.png"));
        skill_guizhan_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_2.png"));
        skill_guizhan_left[4] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_2.png"));
        skill_guizhan_left[5] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_2.png"));
        skill_guizhan_left[6] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_3.png"));
        skill_guizhan_left[7] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_3.png"));
        skill_guizhan_left[8] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_3.png"));
        skill_guizhan_left[9] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_4.png"));
        skill_guizhan_left[10] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_4.png"));
        skill_guizhan_left[11] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_left_4.png"));

        skill_guizhan_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_1.png"));
        skill_guizhan_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_1.png"));
        skill_guizhan_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_1.png"));
        skill_guizhan_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_2.png"));
        skill_guizhan_right[4] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_2.png"));
        skill_guizhan_right[5] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_2.png"));
        skill_guizhan_right[6] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_3.png"));
        skill_guizhan_right[7] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_3.png"));
        skill_guizhan_right[8] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_3.png"));
        skill_guizhan_right[9] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_4.png"));
        skill_guizhan_right[10] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_4.png"));
        skill_guizhan_right[11] = toolkit.getImage(Res.getPlayer(player_name+"/guizhan_right_4.png"));
    }

    private void load_UPAIR()
    {
        upair = new Image[2];
        upair[0] = toolkit.getImage(Res.getPlayer(player_name+"/upair_left.png"));
        upair[1] = toolkit.getImage(Res.getPlayer(player_name+"/upair_right.png"));
    }

    private void checkName()
    {
        player_name = "blademaster";
        switch (playerID)
        {
            case 0:
                player_name = "blademaster";
                break;
        }
    }

    private void load_STOP_ANIM()
    {
        stop_anim_right = new Image[4];
        stop_anim_left = new Image[4];

        stop_anim_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/normal_left_2.png"));
        stop_anim_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/normal_left_1.png"));
        stop_anim_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/normal_left_2.png"));
        stop_anim_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/normal_left_3.png"));

        stop_anim_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/normal_right_2.png"));
        stop_anim_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/normal_right_1.png"));
        stop_anim_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/normal_right_2.png"));
        stop_anim_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/normal_right_3.png"));
    }

    public void action_skill_badaozhan()
    {
        is_badaozhan = true;
        big_skill_frame = -1;
    }

    public Image getSkillAnim()
    {
        if (!is_badaozhan)
        {
            return null;
        }
        else
        {
            if (is_face_right)
            {
                now_state = ATTACK_B_STAGE_RIGHT;
            }
            else
            {
                now_state = ATTACK_B_STAGE_LEFT;
            }
            big_skill_frame++;
            if (big_skill_frame>=skill_badaozhan.length)
            {
                big_skill_frame = skill_badaozhan.length-1;
                if (!is_thread_badaozhan)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            is_thread_badaozhan = true;
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            is_thread_badaozhan = false;
                            is_badaozhan = false;
                            action_stop();
                            parent.now_state = Player.STOP_STATE;
                        }
                    }).start();
                }
            }
            if (big_skill_frame==21 || big_skill_frame==42 || big_skill_frame == 63)
            {
                parent.collision.big_upair(20*Global.atk_player);
            }
            return skill_badaozhan[big_skill_frame];
        }
    }

    public void action_skill_guizhan()
    {
        if (is_face_right)
        {
            now_state = SKILL_GUIZHAN_RIGHT_STATE;
        }
        else
        {
            now_state = SKILL_GUIZHAN_LEFT_STATE;
        }

        now_frame = -1;
    }

    public void action_skill_liebozhan()
    {
        if (is_face_right)
        {
            now_state = SKILL_LIEBOZHAN_RIGHT_STATE;
        }
        else
        {
            now_state = SKILL_LIEBOZHAN_LEFT_STATE;
        }

        parent.voiceManager.play("attack_c");

        now_frame = -1;
    }

    private void load_RUN_ANIM()
    {
        run_anim_left = new Image[4];
        run_anim_right = new Image[4];

        run_anim_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/run_left_1.png"));
        run_anim_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/run_left_2.png"));
        run_anim_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/run_left_3.png"));
        run_anim_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/run_left_4.png"));

        run_anim_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/run_right_1.png"));
        run_anim_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/run_right_2.png"));
        run_anim_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/run_right_3.png"));
        run_anim_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/run_right_4.png"));
    }

    private void load_ATTACK_ANIM()
    {
        attack_a_anim_left = new Image[4];
        attack_a_anim_right = new Image[4];
        attack_b_anim_left = new Image[4];
        attack_b_anim_right = new Image[4];
        attack_c_anim_left = new Image[4];
        attack_c_anim_right = new Image[4];

        attack_a_anim_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_a_1.png"));
        attack_a_anim_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_a_2.png"));
        attack_a_anim_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_a_3.png"));
        attack_a_anim_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_a_4.png"));

        attack_a_anim_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_a_1.png"));
        attack_a_anim_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_a_2.png"));
        attack_a_anim_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_a_3.png"));
        attack_a_anim_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_a_4.png"));

        attack_b_anim_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_b_1.png"));
        attack_b_anim_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_b_2.png"));
        attack_b_anim_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_b_3.png"));
        attack_b_anim_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_b_4.png"));

        attack_b_anim_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_b_1.png"));
        attack_b_anim_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_b_2.png"));
        attack_b_anim_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_b_3.png"));
        attack_b_anim_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_b_4.png"));

        attack_c_anim_left[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_c_1.png"));
        attack_c_anim_left[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_c_2.png"));
        attack_c_anim_left[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_c_3.png"));
        attack_c_anim_left[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_left_c_4.png"));

        attack_c_anim_right[0] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_c_1.png"));
        attack_c_anim_right[1] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_c_2.png"));
        attack_c_anim_right[2] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_c_3.png"));
        attack_c_anim_right[3] = toolkit.getImage(Res.getPlayer(player_name+"/attack_right_c_4.png"));
    }

    private void load_ATTACKED_ANIM()
    {
        attacked_anim_left = new Image[3];
        attacked_anim_right = new Image[3];

        attacked_anim_left[0]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_left_1.png"));
        attacked_anim_left[1]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_left_2.png"));
        attacked_anim_left[2]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_left_3.png"));

        attacked_anim_right[0]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_right_1.png"));
        attacked_anim_right[1]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_right_2.png"));
        attacked_anim_right[2]=toolkit.getImage(Res.getPlayer(player_name+"/attacked_right_3.png"));
    }

    public Image getAnim()
    {
        Image result = null;
        if(now_state==JUMPING_STATE)
        {
            if (is_face_right)
            {
                return stop_anim_right[3];
            }
            else
            {
                return stop_anim_left[3];
            }
        }
        switch (now_state)
        {
            case SKILL_GUIZHAN_LEFT_STATE:
                now_frame++;
                if (now_frame>=skill_guizhan_left.length)
                {
                    now_frame = skill_guizhan_left.length-1;
                    if (!is_thread_guizhan)
                    {
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.voiceManager.play("attack_b");
                                parent.collision.tkANDupair(10*Global.atk_player);
                                is_thread_guizhan = true;
                                try
                                {
                                    Thread.sleep(500);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                action_stop();
                                parent.now_state = Player.STOP_STATE;
                                is_thread_guizhan = false;
                            }
                        }).start();
                    }
                }
                result = skill_guizhan_left[now_frame];
                break;
            case SKILL_GUIZHAN_RIGHT_STATE:
                now_frame++;
                if (now_frame>=skill_guizhan_right.length)
                {
                    now_frame = skill_guizhan_right.length-1;
                    if (!is_thread_guizhan)
                    {
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.voiceManager.play("attack_b");
                                parent.collision.tkANDupair(10*Global.atk_player);
                                is_thread_guizhan = true;
                                try
                                {
                                    Thread.sleep(500);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                action_stop();
                                parent.now_state = Player.STOP_STATE;
                                is_thread_guizhan = false;
                            }
                        }).start();
                    }
                }
                result = skill_guizhan_right[now_frame];
                break;
            case SKILL_LIEBOZHAN_LEFT_STATE:
                now_frame++;
                if (now_frame>=skill_liebozhan_left.length)
                {
                    now_frame = skill_liebozhan_left.length-1;
                    if (!is_thread_liebozhan)
                    {
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                is_thread_liebozhan = true;
                                try
                                {
                                    Thread.sleep(0);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                action_stop();
                                parent.now_state = Player.STOP_STATE;
                                is_thread_liebozhan = false;
                            }
                        }).start();
                    }
                }
                else
                {
                    parent.collision.attack(Global.atk_player);
                }
                result = skill_liebozhan_left[now_frame];
                break;
            case SKILL_LIEBOZHAN_RIGHT_STATE:
                now_frame++;
                if (now_frame>=skill_liebozhan_right.length)
                {
                    now_frame = skill_liebozhan_right.length-1;
                    if (!is_thread_liebozhan)
                    {
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                is_thread_liebozhan = true;
                                try
                                {
                                    Thread.sleep(0);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                action_stop();
                                parent.now_state = Player.STOP_STATE;
                                is_thread_liebozhan = false;
                            }
                        }).start();
                    }
                }
                else
                {
                    parent.collision.attack(Global.atk_player);
                }
                result = skill_liebozhan_right[now_frame];
                break;
            case STOP_STATE_RIGHT:
                result =  getFrame(stop_anim_right);
                break;
            case STOP_STATE_LEFT:
                result =  getFrame(stop_anim_left);
                break;
            case RUN_STATE_RIGHT:
                result = getFrame(run_anim_right);
                break;
            case RUN_STATE_LEFT:
                result = getFrame(run_anim_left);
                break;
            case ATTACK_A_STAGE_LEFT:
                now_frame++;
                if (now_frame>=attack_a_anim_left.length)
                {
                    now_frame--;
                    result = attack_a_anim_left[attack_a_anim_left.length-1];
                    if (!attack_a_waitforend)
                    {
                        attack_a_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.attack(3*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_A_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_A_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_a_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_a_anim_left[now_frame];
                break;
            case ATTACK_A_STAGE_RIGHT:
                now_frame++;
                if (now_frame>=attack_a_anim_right.length)
                {
                    now_frame--;
                    result = attack_a_anim_right[attack_a_anim_right.length-1];
                    if (!attack_a_waitforend)
                    {
                        attack_a_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.attack(3*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_A_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_A_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_a_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_a_anim_right[now_frame];
                break;
            case ATTACK_B_STAGE_LEFT:
                now_frame++;
                if (now_frame>=attack_b_anim_left.length)
                {
                    if (is_badaozhan)
                    {
                        now_frame = 0;
                        result = attack_b_anim_left[now_frame];
                        break;
                    }

                    now_frame--;
                    result = attack_b_anim_left[attack_b_anim_left.length-1];
                    if (!attack_b_waitforend)
                    {
                        attack_b_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.attack(3*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_B_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_B_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_b_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_b_anim_left[now_frame];
                break;
            case ATTACK_B_STAGE_RIGHT:
                now_frame++;
                if (now_frame>=attack_b_anim_right.length)
                {
                    if (is_badaozhan)
                    {
                        now_frame = 0;
                        result = attack_b_anim_right[now_frame];
                        break;
                    }

                    now_frame--;
                    result = attack_b_anim_right[attack_b_anim_right.length-1];
                    if (!attack_b_waitforend)
                    {
                        attack_b_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.attack(3*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_B_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_B_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_b_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_b_anim_right[now_frame];
                break;
            case ATTACK_C_STAGE_LEFT:
                now_frame++;
                if (now_frame>=attack_c_anim_left.length)
                {
                    now_frame--;
                    result = attack_c_anim_left[attack_c_anim_left.length-1];
                    if (!attack_c_waitforend)
                    {
                        attack_c_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.upair(4*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_C_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_C_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_c_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_c_anim_left[now_frame];
                break;
            case ATTACK_C_STAGE_RIGHT:
                now_frame++;
                if (now_frame>=attack_c_anim_right.length)
                {
                    now_frame--;
                    result = attack_c_anim_right[attack_c_anim_right.length-1];
                    if (!attack_c_waitforend)
                    {
                        attack_c_waitforend = true;
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                parent.collision.upair(4*Global.atk_player);
                                try
                                {
                                    Thread.sleep(Global.gap_attack);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                parent.now_state = Player.ATTACK_C_OVER_STATE;
                                try
                                {
                                    Thread.sleep(Global.steady);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if (parent.now_state==Player.ATTACK_C_OVER_STATE)
                                {
                                    parent.now_state = Player.STOP_STATE;
                                    action_stop();
                                }
                                attack_c_waitforend = false;
                            }
                        }).start();
                    }
                    break;
                }
                result = attack_c_anim_right[now_frame];
                break;
            case ATTACKED_STATE:
                now_frame++;
                if (now_frame>=attacked_anim_left.length)
                {
                    now_state = ATTACKED_STATE_OVER;
                    now_frame = attacked_anim_left.length-1;
                }

                if (is_face_right)
                {
                    result = attacked_anim_right[now_frame];
                }
                else
                {
                    result = attacked_anim_left[now_frame];
                }
                break;
            case ATTACKED_STATE_OVER:
                if (is_face_right)
                {
                    result = attacked_anim_right[attacked_anim_right.length-1];
                }
                else
                {
                    result = attacked_anim_left[attacked_anim_left.length-1];
                }
                if (!is_attaceding)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            is_attaceding=true;

                            while (attacked_over_time>0)
                            {
                                try
                                {
                                    Thread.sleep(50);
                                    attacked_over_time-=50;
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            is_attaceding = false;
                            if (now_state==ATTACKED_STATE_OVER)
                            {
                                parent.now_state=Player.STOP_STATE;
                                action_stop();
                            }
                        }
                    }).start();
                }
                break;
            case UPAIR_STATE:
                if (is_face_right)
                {
                    result = upair[1];
                }
                else
                {
                    result = upair[0];
                }
                break;
        }
        return result;
    }

    public void action_attacked()
    {
        attacked_over_time = 300;

        now_frame = -1;
        now_state = ATTACKED_STATE;
    }

    public void action_upair()
    {
        now_frame = -1;
        now_state = UPAIR_STATE;
    }

    private Image getFrame(Image[] action_anim)
    {
        now_frame++;
        if (now_frame>=action_anim.length)
        {
            now_frame=0;
        }
        return action_anim[now_frame];
    }

    public void action_stop()
    {
        if (is_face_right)
        {
            now_state = STOP_STATE_RIGHT;
        }
        else
        {
            now_state = STOP_STATE_LEFT;
        }

        now_frame = -1;
    }

    public void action_attack_a()
    {
        if (is_face_right)
        {
            now_state = ATTACK_A_STAGE_RIGHT;
        }
        else
        {
            now_state = ATTACK_A_STAGE_LEFT;
        }

        now_frame = -1;
    }

    public void action_attack_b()
    {
        if (is_face_right)
        {
            now_state = ATTACK_B_STAGE_RIGHT;
        }
        else
        {
            now_state = ATTACK_B_STAGE_LEFT;
        }

        now_frame = -1;
    }

    public void action_attack_c()
    {
        if (is_face_right)
        {
            now_state = ATTACK_C_STAGE_RIGHT;
        }
        else
        {
            now_state = ATTACK_C_STAGE_LEFT;
        }

        now_frame = -1;
    }

    public void action_run_right()
    {
        now_state = RUN_STATE_RIGHT;
        is_face_right = true;

        now_frame = -1;
    }

    public void action_jumping()
    {
        now_state = JUMPING_STATE;
    }

    public void action_run_left()
    {
        now_state = RUN_STATE_LEFT;
        is_face_right = false;

        now_frame = -1;
    }
}
