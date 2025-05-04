package cz.uhk.GUI;

import com.google.gson.Gson;
import cz.uhk.tables.JsonWrapper;
import cz.uhk.tables.RozvrhovaAkce;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


public class MainWindow extends JFrame {

    private TableModel MyTableModel = new AbstractTableModel() {
        private String[] columnNames = {"1", "2", "3", "4", "5", "6"};
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
    private JsonWrapper wrapper;

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
        String url = "https://stag-demo.uhk.cz/ws/services/rest2/rozvrhy/getRozvrhByMistnost?semestr=%25&budova=J&mistnost=J23&outputFormat=JSON";

        try {
            URL obj = new URL(url);  //making the url
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();   //opening the connection

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  //reading data from the server

            //below we build the response into something workable
            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null){
                response.append(input);
            }
            in.close(); //closing the stream, don't know if needed but might as well

            //using Gson to create a usable Json
            Gson gson = new Gson();
            wrapper = gson.fromJson(response.toString(), JsonWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<RozvrhovaAkce> items = wrapper.rozvrhovaAkce;
        Object[][] data = new Object[items.size()][7];

        for (int i = 0; i < items.size(); i++){
            RozvrhovaAkce item = items.get(i);

            if (item.ucitel != null) {
                String teacherName = (item.ucitel.jmeno != null ? item.ucitel.jmeno + " " : "") +
                        (item.ucitel.prijmeni != null ? item.ucitel.prijmeni + " " : "");

                String timeFrom = (item.hodinaSkutOd != null && item.hodinaSkutOd.value != null) ? item.hodinaSkutOd.value + " " : "";
                String timeTo = (item.hodinaSkutDo != null && item.hodinaSkutDo.value != null) ? item.hodinaSkutDo.value + " " : "";

                String subId;
                if (item.roakIdno != null){subId = item.roakIdno;}
                else {subId = null;}

                data[i][0] = subId;
                data[i][1] = item.predmet;
                data[i][2] = item.nazev;
                data[i][3] = item.den;
                data[i][4] = timeFrom;
                data[i][5] = timeTo;
                data[i][6] = teacherName;
            }
        }

        for (Object[] row : data) {
            System.out.println(java.util.Arrays.toString(row));
        }
    }
}
