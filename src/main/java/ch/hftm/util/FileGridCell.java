package ch.hftm.util;

import java.io.File;
import java.util.logging.Level;

import org.controlsfx.control.GridCell;
import ch.hftm.component.FileItem;
import ch.hftm.model.Context;

public class FileGridCell extends GridCell<File> {
    private Context sharedContext = Context.getInstance();

    public FileGridCell() {
        super();

        itemProperty().addListener(observable -> {
            onItemChanged();
        });
    }

    private void onItemChanged() {
        try {
            setGraphic(new FileItem(getItem().getName()));
        } catch (Exception exception) {
            sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());
        }
    }
}
