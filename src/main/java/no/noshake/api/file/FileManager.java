package no.noshake.api.file;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileManager {
    private static final String filedir = ".f1natory";
    private static final String filename = "autosave.shake";

    private FileManager() {
    }

    public static Path getDataDirectory() {
        return FabricLoader.getInstance().getGameDir().resolve(filedir);
    }

    public static Path getConfigFile() {
        return getDataDirectory().resolve(filename);
    }

    public static boolean configFileExists() {
        return Files.isRegularFile(getConfigFile());
    }

    public static String readConfigFile() throws IOException {
        return Files.readString(getConfigFile(), StandardCharsets.UTF_8);
    }

    public static void writeConfigFile(String content) throws IOException {
        Files.createDirectories(getDataDirectory());
        Files.writeString(getConfigFile(), content, StandardCharsets.UTF_8);
    }
}
