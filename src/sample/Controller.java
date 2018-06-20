package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TreeTableView csvTable;
    @FXML
    public AnchorPane root;
    @FXML
    public Button loadCSVBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initUI();
        initHandler();
    }

    private void initHandler() {
        loadCSVBtn.setOnMouseClicked(e -> readCSV());
    }

    private void initUI() {
        csvTable.prefHeightProperty().bind(root.heightProperty().subtract(40));
        csvTable.prefWidthProperty().bind(root.widthProperty().subtract(10));
        csvTable.setRoot(new TreeItem<CsvData>());
        csvTable.setShowRoot(false);
        csvTable.setLayoutX(5);
        csvTable.setLayoutY(35);
        Image csvImage = new Image(getClass().getResourceAsStream(Paths.get("image", "csv.png").toString()));
        ImageView csvImageView = new ImageView(csvImage);
        csvImageView.setFitWidth(15);
        csvImageView.setFitHeight(15);
        loadCSVBtn.setGraphic(csvImageView);
        loadCSVBtn.setLayoutX(5);
        loadCSVBtn.setLayoutY(5);
    }

    private void readCSV() {
        String currentPath = Paths.get(".").toString();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File(currentPath));
        jFileChooser.showOpenDialog(new JFrame());
        File csvFile = jFileChooser.getSelectedFile();
        try {
            parseCSV(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCSV(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input = br.readLine();
        String[] values;
        if (input == null) {
            Stage errorStage = new Stage();
            Parent errorPage = FXMLLoader.load(getClass().getResource("error.fxml"));
            errorStage.setTitle("CSV Can't Read!!");
            errorStage.setScene(new Scene(errorPage, 200, 50));
            errorStage.show();
            return ;
        }
        csvTable.getColumns().clear();


        values = input.split(",");
        for (int i = 0; i < values.length; ++i) {
            final int index = i;
            TreeTableColumn<CsvData, String> col = new TreeTableColumn<>(values[i]);
            col.setCellValueFactory(x-> x.getValue().getValue().getCsvData(index));
            csvTable.getColumns().addAll(col);
        }

        while ((input = br.readLine()) != null) {
            values = input.split(",");
            CsvData csv = new CsvData(values.length);
            for (int i = 0; i < values.length; ++i) {
                csv.add(i, values[i]);
                System.out.println(values[i]);
                System.out.println(csv.getCsvData(i));
            }
            TreeItem<CsvData> treeItem = new TreeItem<>(csv);
            csvTable.getRoot().getChildren().addAll(treeItem);
        }
    }


}
