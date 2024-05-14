package com.empresa.javafx_mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

public class HelloController {
    @FXML
    private TableView<Data> tableView;

    @FXML
    private TableColumn<Data, String> keyColumn;

    @FXML
    private TableColumn<Data, String> valueColumn;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField edadField;

    @FXML
    private TextField ciudadField;

    private MongoClient client;
    private MongoDatabase database;

    public HelloController() {
        client = MongoClients.create("mongodb+srv://admin:admin@cluster2.unsikqk.mongodb.net/");
        database = client.getDatabase("DB_Javafx");
    }

    @FXML
    protected void onAddButtonClick() {
        MongoCollection<Document> collection = database.getCollection("Javafx");

        Document newDoc = new Document("nombre", nombreField.getText())
                .append("edad", edadField.getText())
                .append("ciudad", ciudadField.getText());

        collection.insertOne(newDoc);

        nombreField.clear();
        edadField.clear();
        ciudadField.clear();
    }

    @FXML
    protected void onHelloButtonClick() {
        MongoCollection<Document> collection = database.getCollection("Javafx");

        ObservableList<Data> data = FXCollections.observableArrayList();
        for (Document doc : collection.find()) {
            doc.forEach((key, value) -> data.add(new Data(key, value.toString())));
        }

        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.setItems(data);
    }

    public static class Data {
        private SimpleStringProperty key;
        private SimpleStringProperty value;

        public Data(String key, String value) {
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
        }

        public String getKey() {
            return key.get();
        }

        public String getValue() {
            return value.get();
        }
    }
}