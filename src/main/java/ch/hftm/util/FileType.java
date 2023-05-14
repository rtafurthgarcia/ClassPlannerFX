package ch.hftm.util;

import java.util.Arrays;
import java.util.List;

public enum FileType {
    CALC("resources/Papirus-Team-Papirus-Apps-Libreoffice-calc.svg", ".xls|.xlsx|.ods|.csv"),
    IMPRESS("resources/Papirus-Team-Papirus-Apps-Libreoffice-impress.svg", ".ppt|.pptx|.odp"),
    WRITER("resources/Papirus-Team-Papirus-Apps-Libreoffice-writer.svg", ".doc|.docx|.odt"),
    PDF("resources/Papirus-Team-Papirus-Mimetypes-App-pdf.svg", ".pdf|.xps"),
    PICTURE("resources/Papirus-Team-Papirus-Mimetypes-App-vnd.insync.link.drive.draw.svg",
            ".svg|.jpg|.jpeg|.png|.bmp|.webp|.gif"),
    ARCHIVE("resources/Papirus-Team-Papirus-Mimetypes-App-x-compress.svg",
            ".zip|.tar|.7z|.tar.gz|.tar.bz2|.tar.xz|.rar"),
    AUDIO("resources/Papirus-Team-Papirus-Mimetypes-App-x-generic.svg", ".mp3|.flac|.m4p|.wav"),
    VIDEO("resources/Papirus-Team-Papirus-Mimetypes-App-x-compress.svg",
            ".mp4|.avi|.flv|.mov|.mkv|.mpv|.mpg|.mpeg"),
    TEXT("resources/Papirus-Team-Papirus-Mimetypes-Text-x-generic.svg", ".txt|.md"),
    UNDEFINED("resources/Papirus-Team-Papirus-Mimetypes-Unknown.svg", "");

    private static final List<FileType> EXTENSION_LIST = Arrays.asList(values());
    private final String resource;
    private final String extensions;
    
    public String getResource() {
        return resource;
    }

    public String getExtensions() {
        return extensions;
    }
    
    private FileType(String resource, String extensions) {
        this.resource = resource;
        this.extensions = extensions;
    }

    public static FileType getByExtension(String extension) {
        return EXTENSION_LIST.stream()
            .filter(t -> t.extensions.contains(extension))
            .findFirst()
            .orElse(UNDEFINED);
    }
}