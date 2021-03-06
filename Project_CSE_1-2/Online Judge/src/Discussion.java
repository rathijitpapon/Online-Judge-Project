import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sun.nio.ch.Net;

public class Discussion {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button home;

    @FXML
    private Button contest;

    @FXML
    private Button problemset;

    @FXML
    private Button discussion;

    @FXML
    private Button profile;

    @FXML
    private Button signOut;

    @FXML
    private ListView<String> blogs;

    ArrayList<String>allttl = new ArrayList<>();
    ArrayList<String>alldtl = new ArrayList<>();

    @FXML
    void BlogView(MouseEvent event) throws IOException {
        String s1 = blogs.getSelectionModel().getSelectedItem();
        for (int i=0; i< allttl.size(); i++){
            if (allttl.get(i).equals(s1)){
                DiscussionDetails.ttl = allttl.get(i);
                DiscussionDetails.dtls = alldtl.get(i);
                break;
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("DiscussionDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Contest(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Contest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void DiscussionItself(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Discussion.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Home(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Problemset(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Problemset.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Profile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SignOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUpIn.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        assert home != null : "fx:id=\"home\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert contest != null : "fx:id=\"contest\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert problemset != null : "fx:id=\"problemset\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert discussion != null : "fx:id=\"discussion\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert profile != null : "fx:id=\"profile\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert signOut != null : "fx:id=\"signOut\" was not injected: check your FXML file 'Discussion.fxml'.";
        assert blogs != null : "fx:id=\"blogs\" was not injected: check your FXML file 'Discussion.fxml'.";

        NetworkUtil nc = Communication.get();
        nc.write("Blogs");
        BlogObject bo = (BlogObject) nc.read();

        ObservableList<String>list = FXCollections.observableArrayList();
        for (int i=0; i<bo.title.size(); i++) {
            list.add(bo.title.get(i));
            allttl.add(bo.title.get(i));
            alldtl.add(bo.dis.get(i));
        }
        System.out.println(allttl.size());
        blogs.setItems(list);
    }
}