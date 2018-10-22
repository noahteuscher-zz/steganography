// package application;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import javax.imageio.ImageIO;

// import com.sun.javafx.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application{

    private boolean encoder;
    private boolean picuploaded;
    private ImageView imagedisplay;
    
    
    
    
    public void start(Stage primaryStage) {
        
    	encoder = false;
    	picuploaded = false;
    	
    	TextField userTextField = new TextField();

        StackPane upperpane = new StackPane();
        
        imagedisplay = new ImageView();
        
        //button to upload an image
        Button upload = new Button();
        upload.setText("Upload Photo");
        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("User would like to upload a photo!");
                FileChooser chooser = new FileChooser();
                        new ExtensionFilter("Image Files", "*.png", "*.gif");        
                
                File selectedFile = chooser.showOpenDialog(primaryStage);
                if(selectedFile != null){      	
                        String file = selectedFile.getPath();
                        Image chosenimage = new Image("file:" + file, true);
                        upload.setVisible(false);
                        imagedisplay.setImage(chosenimage);
                        imagedisplay.setFitWidth(upperpane.getWidth()*.7);
                        imagedisplay.setFitHeight(upperpane.getHeight()*.7);
                        picuploaded = true;
                }
                else{
                	upload.setVisible(true);
                }
            }
        });
        
        upperpane.getChildren().addAll(imagedisplay, upload);

        upload.setAlignment(Pos.CENTER);
        
        BorderPane lowerpane = new BorderPane();
        
        //button to encode an uploaded image
        Button encode = new Button();
        encode.setText("Encode!");
        encode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(picuploaded){
                System.out.println("User would like to encode the following message: " + userTextField.getText());
                Encoder encoder = new Encoder(userTextField.getText(), imagedisplay.getImage());
               imagedisplay.setImage(encoder.encode());
               FileChooser saveImage = new FileChooser();
               saveImage.setTitle("Save Image");
               File file = saveImage.showSaveDialog(primaryStage);
               try {
                   FileWriter filewriter = new FileWriter(file);
                   ImageIO.write(SwingFXUtils.fromFXImage(imagedisplay.getImage(),
                           null), "png", file);
                   System.out.println("Image succesfully saved!");
                   imagedisplay.setImage(null);
                   picuploaded = false;
                   upload.setVisible(true);
                   filewriter.close();
               } catch (IOException ex) {
                   System.err.println("ERROR");
               }
            }
            	else{
            		Alert alert = new Alert(AlertType.ERROR);
                	alert.setContentText("PLEASE UPLOAD A PHOTO TO ENCODE");
                	alert.show();
            	}
                
            }
        });
        
        //button to decode an uploaded image
        Button decode = new Button();
        decode.setVisible(false);
        decode.setText("Decode");
        decode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(picuploaded){
            		Decoder decoder = new Decoder(imagedisplay.getImage());
                	Alert alert = new Alert(AlertType.INFORMATION);
                	alert.setContentText(decoder.decode());
                	alert.show();
            	}
            	else{
            		Alert alert = new Alert(AlertType.ERROR);
                	alert.setContentText("PLEASE UPLOAD A PHOTO TO DECODE");
                	alert.show();
            	}
            }
        });
        
        
        
        
        
        SplitPane mainpane = new SplitPane(upperpane, lowerpane);
        mainpane.setOrientation(Orientation.VERTICAL);
        mainpane.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());    
        mainpane.setDividerPositions(.8);
        

        
        HBox encodebox = new HBox();
        encodebox.getChildren().addAll(userTextField, encode);   
        encodebox.setAlignment(Pos.CENTER);
       
        StackPane sp = new StackPane();
        sp.getChildren().addAll(encodebox, decode);
        
        Button toggle = new Button();
    	toggle.setText("Go to Decode");
        toggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(encoder){
                    toggle.setText("Go to Decode");
                    decode.setVisible(false);
                    encodebox.setVisible(true);
            	}
            	else{
                    toggle.setText("Go to Encode");
                    encodebox.setVisible(false);
                    decode.setVisible(true);
            	}
            	
            	ObservableList<Node> workingCollection = FXCollections.observableArrayList(sp.getChildren());
                Collections.swap(workingCollection, 0, 1);
                sp.getChildren().setAll(workingCollection);
            	encoder = !encoder;
            }
        });

        
        lowerpane.setCenter(sp);
        lowerpane.setTop(toggle);
        BorderPane.setAlignment(toggle, Pos.TOP_RIGHT);
        
        Scene scene = new Scene(mainpane, 400, 500);   
        primaryStage.setTitle("Stegonography Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}