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

    public String getCsvData() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < csvData.length-1; i++) {
            stringBuilder.append(csvData[i].get()).append(",");
        }
        stringBuilder.append(csvData[csvData.length-1].get()).append("\n");
        return stringBuilder.toString();
    }
}
