package manager;

import res.Res;
import util.Util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class VoiceManager
{
    private int playerID = 0;
    private int playerType = 0;

    private HashMap<String, VoiceManager.Sound> sounds = new HashMap<>();

    public VoiceManager(int ID, int type)
    {
        playerID = ID;
        playerType = type;
        Init();
    }

    private void Init()
    {
        String url = "charactor/";
        switch (playerType)
        {
            case 0:
                url+="player/";
                if (playerID==0)
                {
                    url+="blademaster/";
                    url+="voice/";
                    addVoice("attack_a",url+"attack_a.wav");
                    addVoice("attack_b",url+"attack_b.wav");
                    addVoice("attack_c",url+"attack_c.wav");
                    addVoice("attacked",url+"attacked.wav");
                }
                break;
            case 1:
                url+="monster/";
                if (playerID==0)
                {
                    url+="goblin/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                else if (playerID==1)
                {
                    url+="gbl/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                else if (playerID==2)
                {
                    url+="hadis/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                else if (playerID==3)
                {
                    url+="bufx/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                else if (playerID==4)
                {
                    url+="cat/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                else if (playerID==5)
                {
                    url+="zombie/";
                    url+="voice/";
                    addVoice("attacked",url+"attacked.wav");
                    addVoice("dead",url+"dead.wav");
                }
                break;
            case 2:
                url+="human/";
                break;
        }
    }

    public void addVoice(String name, String url)
    {
        sounds.put(name,new VoiceManager.Sound(Res.getVoice(url)));
    }

    public void play(String name)
    {
        VoiceManager.Sound sound = sounds.get(name);
        sound.play();
    }

    public class Sound
    {
        private  AudioInputStream stream = null;
        private  AudioFormat format = null;
        private  DataLine.Info info = null;
        private  Clip clip = null;

        public Sound(InputStream s)
        {
            try
            {
                stream = AudioSystem.getAudioInputStream(s);
            }
            catch
            (UnsupportedAudioFileException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            format = stream.getFormat();

            info = new DataLine.Info(Clip.class,format);

            try
            {
                clip = (Clip)AudioSystem.getLine(info);
            }
            catch
            (LineUnavailableException e)
            {
                e.printStackTrace();
            }

            try
            {
                clip.open(stream);
            }
            catch (LineUnavailableException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void play()
        {
            clip.loop(1);
        }
    }
}