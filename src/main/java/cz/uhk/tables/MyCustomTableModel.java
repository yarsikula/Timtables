package cz.uhk.tables;

import javax.swing.table.AbstractTableModel;
import java.io.ObjectStreamException;

public class MyCustomTableModel extends AbstractTableModel {
    private String[] columnNames = {"Předmět", "Název", "Den", "Start", "Konec", "Učitel"};
    private Object[][] data;

    public MyCustomTableModel(Object[][] inData){
        this.data = inData;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void updateData(Object[][] newData){
        this.data = newData;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
}
