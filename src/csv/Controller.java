package csv;

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
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TreeTableView<CsvData> csvTable;
    @FXML
    public AnchorPane root;
    @FXML
    public Button loadCSVBtn;
    @FXML
    public Button saveCSVBtn;
    @FXML
    public Button plusBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initUI();
        initHandler();
    }

    private void initHandler() {
        loadCSVBtn.setOnMouseClicked(e -> readCSV());
        saveCSVBtn.setOnMouseClicked(e -> writeCSV());
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

        Image saveImage = new Image(getClass().getResourceAsStream(Paths.get("image", "save.png").toString()));
        ImageView saveImageView = new ImageView(saveImage);
        saveImageView.setFitWidth(15);
        saveImageView.setFitHeight(15);
        saveCSVBtn.setGraphic(saveImageView);
        saveCSVBtn.setLayoutY(5);
        saveCSVBtn.setLayoutX(45);

        Image plusImage = new Image(getClass().getResourceAsStream(Paths.get("image", "plus.png").toString()));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(15);
        plusImageView.setFitHeight(15);
        plusBtn.setGraphic(plusImageView);
        plusBtn.setLayoutY(5);
        plusBtn.setLayoutX(85);

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

    private void error(String errorMassage) {
        try {
            Stage errorStage = new Stage();
            Parent errorPage = FXMLLoader.load(getClass().getResource("fxml/error.fxml"));
            errorStage.setTitle(errorMassage);
            errorStage.setScene(new Scene(errorPage, 200, 50));
            errorStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCSV(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input = br.readLine();
        String[] values;
        if (input == null) {
            error("CSV Can't Read!!");
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
        TreeItem<CsvData> lastItem = null;
        while ((input = br.readLine()) != null) {
            values = input.split(",");
            CsvData csv = new CsvData(values.length);
            if (!"".equals(values[0])) {
                for (int i = 0; i < values.length; ++i) {
                    csv.add(i, values[i]);
                }

                TreeItem<CsvData> treeItem = new TreeItem<>(csv);
                csvTable.getRoot().getChildren().addAll(treeItem);
                lastItem = treeItem;
            } else {
                for (int i = 1; i < values.length; ++i) {
                    csv.add(i-1, values[i]);
                }
                TreeItem<CsvData> treeItem = new TreeItem<>(csv);
                lastItem.getChildren().addAll(treeItem);
            }
        }
    }


    private void writeCSV() {
        String currentPath = Paths.get(".").toString();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File(currentPath));
        jFileChooser.showSaveDialog(new JFrame());
        File csvFile = jFileChooser.getSelectedFile();
        if (csvFile == null) {
            error("No file!!");
            return ;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));
            TreeItem<CsvData> rootItem = csvTable.getRoot();
            for (int i = 0; i < csvTable.getColumns().size() - 1; ++i) {
                bw.write(csvTable.getColumns().get(i).getText()+",");
            }
            bw.write(csvTable.getColumns().get(csvTable.getColumns().size() - 1).getText()+"\n");
            for (TreeItem<CsvData> csv : rootItem.getChildren()) {
                bw.write(csv.getValue().getCsvData());
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
