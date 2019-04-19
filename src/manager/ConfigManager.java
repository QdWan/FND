package manager;

import com.google.gson.JsonObject;

public class ConfigManager
{
    private static JsonObject config = null;

    public static JsonObject getConfig()
    {
        return config;
    }

    public static void setConfig(JsonObject config)
    {
        ConfigManager.config = config;
    }
}
