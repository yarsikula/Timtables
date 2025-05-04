package cz.uhk.tables;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private List<Component> rowData = new ArrayList<>();

    public void addData(Component thing){
        rowData.add(thing);
    }

    public Component getData(int index) {
        return rowData.get(index);
    }
}
