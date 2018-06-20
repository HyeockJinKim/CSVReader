package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CsvData {
    private final StringProperty[] csvData;

    public CsvData(int size) {
        csvData = new StringProperty[size];
    }

    public void add(int index, String str) {
        csvData[index] = new SimpleStringProperty(str);
    }

    public void setCsvData(int index, String string) {
        csvData[index].set(string);
    }

    public StringProperty getCsvData(int index) {
        return csvData[index];
    }
}
