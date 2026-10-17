package no.noshake.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.noshake.api.file.FileManager;

public final class ConfigManager {

    public static final float maxdownstrength = 2.0f;

    private static final float defaultstrength = 1.0f;
    private static final long autosave = 500L;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final ConfigManager INSTANCE = new ConfigManager();

    private boolean loaded;
    private long lastSave;

    private boolean customBob;
    private float downStrength = defaultstrength;

    private ConfigManager() {
    }

    public static ConfigManager get() {
        return INSTANCE;
    }

    public boolean isCustomBob() {
        load();
        return this.customBob;
    }

    public void setCustomBob(boolean value) {
        load();
        if (this.customBob != value) {
            this.customBob = value;
            autosave();
        }
    }

    public float getDownStrength() {
        load();
        return this.downStrength;
    }

    public void setDownStrength(float value) {
        load();
        float clamped = clamp(value);
        if (this.downStrength != clamped) {
            this.downStrength = clamped;
            autosave();
        }
    }

    public synchronized void load() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;

        if (!FileManager.configFileExists()) {
            writeToDisk();
            return;
        }

        try {
            JsonObject root = JsonParser.parseString(FileManager.readConfigFile()).getAsJsonObject();
            if (root.has("customBob")) {
                this.customBob = root.get("customBob").getAsBoolean();
            }
            if (root.has("downStrength")) {
                this.downStrength = clamp(root.get("downStrength").getAsFloat());
            }
        } catch (Exception e) {
        }
    }

    public synchronized void save() {
        load();
        writeToDisk();
    }

    private void autosave() {
        if (System.currentTimeMillis() - this.lastSave >= autosave) {
            writeToDisk();
        }
    }

    private void writeToDisk() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("customBob", this.customBob);
            root.addProperty("downStrength", this.downStrength);
            FileManager.writeConfigFile(GSON.toJson(root));
            this.lastSave = System.currentTimeMillis();
        } catch (Exception e) {
        }
    }

    private static float clamp(float value) {
        return Math.max(0.0f, Math.min(maxdownstrength, value));
    }
}
