package ch.hftm.util;

import java.io.File;
import java.util.logging.Level;

import ch.hftm.model.Context;

public class OSHelper {
    private static Context sharedContext = Context.getInstance();

    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS, UNKNOWN
    };

    private static OS os = null;

    public static OS getOS() {
        if (os == null) {
            String operatingSystem = System.getProperty("os.name").toLowerCase();
            if (operatingSystem.contains("win")) {
                os = OS.WINDOWS;
            } else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
                    || operatingSystem.contains("aix")) {
                os = OS.LINUX;
            } else if (operatingSystem.contains("mac")) {
                os = OS.MAC;
            } else if (operatingSystem.contains("sunos")) {
                os = OS.SOLARIS;
            } else {
                os = OS.UNKNOWN;
            }
        }

        return os;
    }

    public static void run(File file) {
        try {
            String command = "";
            OS os = getOS();
            switch (os) {
                case WINDOWS:
                    command = "cmd /c start ";
                break;
                case LINUX:
                    command = "xdg-open";
                break;
                case MAC:
                    command = "open";
                break;
                default:
                break;
            }

            new ProcessBuilder(command, file.getAbsolutePath()).start();
        } catch (Exception exception) {
            sharedContext.getLogger().log(Level.INFO, exception.getLocalizedMessage());
        }
    }
}