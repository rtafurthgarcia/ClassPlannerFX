package ch.hftm.util;

import java.util.Arrays;
import java.util.Collection;

import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import ch.hftm.ClassPlannerFX;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class CustomValidationDecoration extends GraphicValidationDecoration {

    private static final SVGImage ERROR_IMAGE = SVGLoader.load(ClassPlannerFX.class.getResource("resources/alert-circle.svg"));

    @Override
    protected Node createErrorNode() {
       return ERROR_IMAGE;
    }
    
    protected Node createDecorationNode(ValidationMessage message) {
        Node graphic = getGraphicBySeverity(message.getSeverity());
        Label label = new Label();
        label.setGraphic(graphic);
        label.setTooltip(createTooltip(message));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
    	return Arrays.asList(new GraphicDecoration(createDecorationNode(message),Pos.CENTER_RIGHT));
	}

}
