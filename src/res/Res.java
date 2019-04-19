package res;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Res
{
    public static String readFile(String url)
    {
        String text = "";
        Scanner scanner = new Scanner(Res.class.getResourceAsStream(url),"UTF-8");
        while (scanner.hasNextLine())
        {
            text += scanner.nextLine();
        }
        return text;
    }

    public static URL getSplash(String filename)
    {
        return Res.class.getResource("splash/"+filename);
    }

    public static InputStream getBGM(String filename)
    {
        return new BufferedInputStream(Res.class.getResourceAsStream("music/bgm/"+filename));
    }

    public static InputStream getSound(String filename)
    {
        return new BufferedInputStream(Res.class.getResourceAsStream("music/sound/"+filename));
    }

    public static URL getScene(String url)
    {
        return Res.class.getResource("scene/"+url);
    }

    public static InputStream getVoice(String url)
    {
        return new BufferedInputStream(Res.class.getResourceAsStream(url));
    }

    public static URL getPlayer(String url)
    {
        return Res.class.getResource("charactor/player/"+url);
    }

    public static URL getMonster(String url)
    {
        return Res.class.getResource("charactor/monster/"+url);
    }

    public static URL getLogo(String filename)
    {
        return Res.class.getResource("logo/"+filename);
    }
}