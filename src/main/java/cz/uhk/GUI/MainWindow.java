package cz.uhk.GUI;

import com.google.gson.Gson;
import cz.uhk.tables.JsonWrapper;
import cz.uhk.tables.MyCustomTableModel;
import cz.uhk.tables.RozvrhovaAkce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


public class MainWindow extends JFrame {

    private JToolBar toolBar;
    private JComboBox<Object> buildingSelect;
    private JComboBox<Object> classSelect;
    private JButton doSomething;
    private JsonWrapper wrapper;
    private String roomChoice;
    private MyCustomTableModel MyTableModel;

    public MainWindow() {
        super("Timetables");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createToolBar();

        MyTableModel = new MyCustomTableModel(getData(parseData()));
        JTable table = new JTable(MyTableModel);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        doSomething.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getting the selected room and changing it globally
                String room = (String) classSelect.getSelectedItem();
                roomChoice = room;

                //getting new data and passing them to the table model
                Object[][] newData = getData(parseData());
                MyTableModel.updateData(newData);
            }
        });

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void createToolBar() {
        String[] buildings = {"J"};
        String[] classes = {"J23", "J1", "J30", "J15", "J6", "J8"};
        toolBar = new JToolBar(JToolBar.HORIZONTAL);
        add(toolBar, BorderLayout.NORTH);

        buildingSelect = new JComboBox<>(buildings);
        classSelect = new JComboBox<>(classes);
        doSomething = new JButton("Update");
        toolBar.add(buildingSelect);
        toolBar.add(classSelect);
        toolBar.add(doSomething);
    }

    public Object[][] getData(Object[][] origData){
        //getting a size for a final Object
        int count = 0;
        for (int i = 0; i < origData.length; i++){
            if (origData[i][0] != null){count++;}
        }

        Object[][] ret = new Object[count][6];

        //filling up the new object
        int retIndex = 0;
        for (int i = 0; i < origData.length; i++){
            if (origData[i][0] != null){
                for (int j = 1; j < 7; j++){
                    ret[retIndex][j-1] = origData[i][j];
                }
                retIndex++;
            }
        }

        return ret;
    }

    public void tryData(){
        //building URL
        String room;
        if (roomChoice == null){room = "J23";} else {room = roomChoice;}
        String url = "https://stag-demo.uhk.cz/ws/services/rest2/rozvrhy/getRozvrhByMistnost?semestr=%25&budova=J&mistnost=" + room + "&outputFormat=JSON";

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
    }

    public Object[][] parseData(){
        //calls the data and prepares it for work
        tryData();
        List<RozvrhovaAkce> items = wrapper.rozvrhovaAkce;
        Object[][] data = new Object[items.size()][7];

        //getting all the info we need from the Json
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

        return data;
    }
}
