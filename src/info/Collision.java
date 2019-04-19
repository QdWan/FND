package info;

import util.Util;

public class Collision
{
    private Player parent = null;
    private Monster[] monsters = null;

    public Collision(Player parent)
    {
        this.parent = parent;
    }

    public void setMonsters(Monster[] out_monsters)
    {
        monsters = out_monsters;
    }

    public void big_upair(int value)
    {
        for (Monster monster : monsters)
        {
            monster.monster_upaired(value);
        }
    }

    public void tkANDupair(int value)
    {
        if (parent.face_to_right)
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos >parent.x_pos-50 && monster.x_pos<parent.x_pos+150)
                {
                    monster.is_face_right = false;
                    monster.monster_atkANDupair(value);
                }
            }
        }
        else
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos <parent.x_pos+50 && monster.x_pos>parent.x_pos-150)
                {
                    monster.is_face_right = true;
                    monster.monster_atkANDupair(value);
                }
            }
        }
    }

    public void attack(int value)
    {
        if (parent.face_to_right)
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos >parent.x_pos-50 && monster.x_pos<parent.x_pos+150)
                {
                    monster.is_face_right = false;
                    monster.monster_attacked(value);
                }
            }
        }
        else
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos <parent.x_pos+50 && monster.x_pos>parent.x_pos-150)
                {
                    monster.is_face_right = true;
                    monster.monster_attacked(value);
                }
            }
        }
    }

    public void upair(int value)
    {
        if (parent.face_to_right)
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos >parent.x_pos-50 && monster.x_pos<parent.x_pos+150)
                {
                    monster.is_face_right = false;
                    monster.monster_upaired(value);
                }
            }
        }
        else
        {
            for (Monster monster : monsters)
            {
                if (monster.x_pos <parent.x_pos+50 && monster.x_pos>parent.x_pos-150)
                {
                    monster.is_face_right = true;
                    monster.monster_upaired(value);
                }
            }
        }
    }
}
