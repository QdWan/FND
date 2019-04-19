import com.google.gson.JsonObject;
import manager.ConfigManager;
import manager.SceneManager;
import util.Util;

public class Main
{
    public static void main(String[] args)
    {
        if (check())
        {
            Util.log("文件检查完成");

            //下面开始载入窗口和场景资源管理器
            new SceneManager();
        }
        else
        {
            Util.log(2,"错误","文件检查出错");
            System.exit(-1);
        }
    }

    private static boolean check()
    {
        //此处进行外部文件检查

        //检查Config.json文件
        JsonObject config = Util.loadJson("config.json");
        if (config==null)
        {
            return false;
        }
        else
        {
            ConfigManager.setConfig(config);
        }

        //检查Data.json文件


        return true;
    }
}
