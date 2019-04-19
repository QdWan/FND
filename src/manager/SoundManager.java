package manager;

import res.Res;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class SoundManager
{
    private HashMap<String,Sound> sounds = new HashMap<>();

    public void addSound(String name, String filename)
    {
        sounds.put(name,new Sound(Res.getSound(filename)));
    }
    public void addVoice(String name, String url)
    {
        sounds.put(name,new Sound(Res.getSound(url)));
    }

    public void play(String name)
    {
        Sound sound = sounds.get(name);
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
