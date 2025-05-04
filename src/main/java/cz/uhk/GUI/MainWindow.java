package cz.uhk.GUI;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;


public class MainWindow extends JFrame {

    private TableModel MyTableModel = new AbstractTableModel() {
        private String[] columnNames = {"1", "2", "3"};
        private Object[][] data = getData();

        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
    };

    private JToolBar toolBar;
    private JComboBox<Object> buildingSelect;
    private JComboBox<Object> classSelect;
    private JButton doSomething;

    public MainWindow() {
        super("Timetables");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createToolBar();

        JTable table = new JTable(MyTableModel);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        tryData();

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void createToolBar() {
        String[] buildings = {"J"};
        String[] classes = {"J23"};
        toolBar = new JToolBar(JToolBar.HORIZONTAL);
        add(toolBar, BorderLayout.NORTH);

        buildingSelect = new JComboBox<>(buildings);
        classSelect = new JComboBox<>(classes);
        doSomething = new JButton("View");
        toolBar.add(buildingSelect);
        toolBar.add(classSelect);
        toolBar.add(doSomething);
    }

    public Object[][] getData(){
        Object[][] ret = {{"test1", "test2", "test3"}};
        return ret;
    }

    public void tryData(){
        String url = "https://stag-demo.uhk.cz/ws/services/rest2/rozvrhy/getRozvrhByMistnost?semestr=%25&budova=J&mistnost=J1&outputFormat=JSON";

    }
}
