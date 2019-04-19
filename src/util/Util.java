package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import res.Res;

public class Util
{
    public static boolean thread_running = true;
    public static int width = 1000;
    public static int height = 600;

    public static void log(String content)
    {
        log(0,"系统",content);
    }

    public static void log(String tag, String content)
    {
        log(0,"["+tag+"]",content);
    }

    public static void log(int priority,String tag, String content)
    {
        System.out.println("----------");
        System.out.println("优先级："+priority);
        System.out.println(tag+" "+content);
        System.out.println("----------");
    }

    public static JsonObject loadJson(String filename)
    {
        JsonParser parser = new JsonParser();
        String text = Res.readFile("config/"+filename);
        if (text==null || text.equals(""))
        {
            return null;
        }
        else
        {
            return (JsonObject)parser.parse(text);
        }
    }
}
