package ch.hftm.component;

import ch.hftm.util.OSHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import ch.hftm.ClassPlannerFX;
import ch.hftm.util.FileType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class FileItem extends VBox {
    @FXML
    Label lFileName;

    private File file;
    private FileType fileType;

    private String CSS_CLASS = "file-item";
    private String CSS_CLASS_SELECTION = "file-item-selected";

    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean isSelected() {
        return selected.get();
    }

    public FileItem setSelected(Boolean selected) {
        this.selected.set(selected);

        return this;
    }

    public BooleanProperty selectedProperty() {
        return selected;
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

        getStyleClass().add(CSS_CLASS);

        setOnMouseClicked(event -> onMouseClicked(event));

        selected.addListener(observable -> {
            if (isSelected()) {
                getStyleClass().remove(CSS_CLASS);
                getStyleClass().add(CSS_CLASS_SELECTION);
            } else {
                getStyleClass().remove(CSS_CLASS_SELECTION);
                getStyleClass().add(CSS_CLASS);
            }
        });
    }

    public final File getFile() {
        return file;
    }

    public final FileType getType() {
        return fileType;
    }

    private void onMouseClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            selected.set(! selected.get());

            if (event.getClickCount() == 2) {
                OSHelper.run(getFile());
            }
        }
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
