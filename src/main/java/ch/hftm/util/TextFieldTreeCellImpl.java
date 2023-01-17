package ch.hftm.util;

import ch.hftm.model.CoreCompetency;
import ch.hftm.model.ThematicAxis;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextFieldTreeCellImpl<T> extends TreeCell<T> {
    private TextField textField;

    /**
     * On editing, create new text field and set it using setGraphic method.
     */
    @Override
    public void startEdit() {
        if (getItem() instanceof ThematicAxis || getItem() instanceof CoreCompetency) {
            super.startEdit();
    
            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    /**
     * On cancel edit, set the original content back
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        if (!isEditing()) {
            setText(getString());
            setGraphic(getTreeItem().getGraphic());
            return;
        }

        if (textField != null) {
            textField.setText(getString());
        }
        setText(null);
        setGraphic(textField);
    }
    
    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased((KeyEvent t) -> {
            if (t.getCode() == KeyCode.ENTER) {
                if (getItem() instanceof ThematicAxis) {
                    ThematicAxis toUpdateObject = (ThematicAxis) getItem();
                    toUpdateObject.setName(textField.getText());
                }

                if (getItem() instanceof CoreCompetency) {
                    CoreCompetency toUpdateObject = (CoreCompetency) getItem();
                    toUpdateObject.setName(textField.getText());
                }
                
                commitEdit(super.getItem());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
    
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}