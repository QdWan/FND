package manager;

import util.Util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class MusicManager
{
    private static AudioInputStream stream_bgm = null;
    private static AudioFormat format_bgm = null;
    private static DataLine.Info info_bgm = null;
    private static Clip clip_bgm = null;
    private static FloatControl gainControl = null;

    public static void loadBGM(InputStream stream)
    {
        try
        {
            stream_bgm = AudioSystem.getAudioInputStream(stream);
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

        format_bgm = stream_bgm.getFormat();

        info_bgm = new DataLine.Info(Clip.class,format_bgm);

        try
        {
            clip_bgm = (Clip)AudioSystem.getLine(info_bgm);
        }
        catch
        (LineUnavailableException e)
        {
            e.printStackTrace();
        }

        try
        {
            clip_bgm.open(stream_bgm);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        gainControl = (FloatControl)clip_bgm.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public static void playBGM()
    {
        clip_bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stopBGM()
    {
        if (clip_bgm!=null && clip_bgm.isRunning())
        {
            clip_bgm.stop();
        }
    }

    public static void setVol(float value)
    {
//        Util.log(gainControl.getValue()+"");
        gainControl.setValue(value);
    }

    public static void release()
    {
        stream_bgm = null;
        format_bgm = null;
        info_bgm = null;
        clip_bgm = null;
    }

    public static void stopAll()
    {
        stopBGM();
    }
}
