package ch.hftm.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import ch.hftm.ClassPlannerFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FileItem extends VBox {
    @FXML
    Label lFileName;

    //SVGPath icon = new SVGPath();
    private File file;
    private FileType fileType;

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

        public final String resource;
        public final String extensions;

        private FileType(String resource, String extensions) {
            this.resource = resource;
            this.extensions = extensions;
        }

        public static FileType getByExtension(String extension) {
            return EXTENSION_LIST.stream()
                .filter(t -> t.extensions.matches(extension))
                .findFirst()
                .orElse(UNDEFINED);
        }
    }

    public FileItem(File file) throws IOException {
        super();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/FileItemView.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        loader.load();
        this.file = file;
        fileType = FileType.getByExtension(getExtensionByStringHandling(file.getAbsolutePath()));
        lFileName.setText(file.getName());
        SVGImage svgImage = SVGLoader.load(readFromInputStream(ClassPlannerFX.class.getResourceAsStream(fileType.resource)));
        svgImage.setScaleX(1.5);
        svgImage.setScaleY(1.5);
        getChildren().add(0, svgImage);
       /*SVGContent content = SVGLoader.load(url);
        icon.setContent(readFromInputStream(ClassPlannerFX.class.getResourceAsStream(fileType.ressource)));
        icon.setFill(Color.TRANSPARENT);
        this.getChildren().add(icon);*/
    }

    public final File getFile() {
        return file;
    }

    public final FileType getType() {
        return fileType;
    }

    private String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".")))
            .orElse("");
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
