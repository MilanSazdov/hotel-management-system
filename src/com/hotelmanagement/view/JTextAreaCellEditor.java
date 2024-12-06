package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.Component;

public class JTextAreaCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextArea textArea;

    public JTextAreaCellEditor() {
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textArea.setText((String) value);
        return new JScrollPane(textArea);
    }

    @Override
    public Object getCellEditorValue() {
        return textArea.getText();
    }
}
