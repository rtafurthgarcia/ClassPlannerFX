package ch.hftm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import org.girod.javafx.svgimage.SVGLoader;
import org.girod.javafx.svgimage.xml.parsers.SVGParsingException;

import ch.hftm.ClassPlannerFX;
import javafx.scene.control.ListCell;

public class FileCell extends ListCell<File> {

    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            setText(item.getName());
            try {
                setGraphic(SVGLoader.load(readFromInputStream(ClassPlannerFX.class.getResourceAsStream(
                    FileType.getByExtension(getExtensionByStringHandling(item.getAbsolutePath())).getResource()
                ))));
            } catch (SVGParsingException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".")))
            .orElse("");
    }

    public static String readFromInputStream(InputStream inputStream)
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

