/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firsthalfproject;
// Testing Git
// This is the currrent version

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Optional;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javafx.scene.image.WritableImage;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javax.imageio.ImageIO;


/**
 *
 * @author Bill
 */
public class FirstHalfProject extends Application {
    
    // variable that containes the active image
    private Image image;
    private String curserMode = new String ("Default");
    
    
    private double startDragClickX;
    private double startDragClickY;
    
    private boolean dragStarted = new Boolean(false);
    
    @Override
    public void start(Stage primaryStage) {        
        
        // Container that arranges the items contained in it in a grid for display on screen
        // grid is arranged as (column, row)        
        GridPane root = new GridPane();
        

        CanvasWrapper canvasWrapper = new CanvasWrapper();
        

        Scene scene = new Scene(root);              
        
        // menuBar serves two purposes
        // 1 create the menu bar displaied on the screen
        // 2 act as a container for the Menu objects
        MenuBar menuBar = new MenuBar();        
 
        // --- Menu File
        Menu menuFile = new Menu("File"); // creates the File menu
        
        
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {   
                image = canvasWrapper.drawBlankCanvas(image);                
                root.add(canvasWrapper.getCanvas(), 0, 1);
                image = canvasWrapper.getCanvas().snapshot(null, null);
                
                // resizes the window to match the size of the image
                primaryStage.sizeToScene();
            }
        });
        
        
        MenuItem open = new MenuItem("Open"); // creates the open menu item to be placed in the file menu
        
        // provides access to the File Manager's File Chooser in a file manager and os agnostic way
        // The File Chooser opens a window that allows the user to select a file 
        // this file is then retured to the program to use as it will
        final FileChooser fileChooser = new FileChooser();
        
        // sets the file formates that the File Chooser will show in its window
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG(*.png)", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG(*.jpg)", "*.jpg"));
        
        
        // this function is called when the open MenuItem is clicked on
        open.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {                
                try {
                    // launches the File Chooser to open a file then stores that file in the file object
                    File file = fileChooser.showOpenDialog(primaryStage);
                    
                    // converts the file to an image object for further use
                    image = new Image(new FileInputStream(file));
                    canvasWrapper.drawImageOnCanvas(image);
                    root.add(canvasWrapper.getCanvas(), 0, 1);
                    

                    // resizes the window to match the size of the image
                    primaryStage.sizeToScene();    
                    
                } catch (FileNotFoundException e) {
                   
                }                     
            }
        });

                
        // creates a new menu item Save to be placed in the file menu
        // when pressed this item saves the image currently in use 
        MenuItem save = new MenuItem("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                 try {
                 // launches the File Chooser to select a file to save to then stores that file in the file object
                 File file = fileChooser.showSaveDialog(primaryStage); 
                 
                 //stores the selected file formate in the variable fileType
                 //gets the first of the posible extentions
                 // then removes the first two charachters (*.) to get the name of the file type

                 String fileType = new String(fileChooser.getSelectedExtensionFilter().getExtensions().get(0).substring(2));                 
                 
                 // writes image to the selected file 
                 WritableImage writableImage = new WritableImage((int)canvasWrapper.getCanvas().getWidth(), (int)canvasWrapper.getCanvas().getHeight());
                 canvasWrapper.getCanvas().snapshot(null, writableImage);
                 RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                 
                 ImageIO.write(renderedImage, fileType, file);

                 
                 
                } catch (IOException e) {
                    
                } 
            }
        });
        

        menuFile.getItems().addAll(newItem, open, save);

        
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
 
        // --- Menu View
        Menu menuView = new Menu("View"); 

        menuView.getItems().addAll();
        
        
        Menu menuDraw = new Menu("Draw");
        
        MenuItem drawLine = new MenuItem("Line");
        
        drawLine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                canvasWrapper.setCurserMode("Line");
            }
        });
        menuDraw.getItems().addAll(drawLine);
        
        Menu menuDraw = new Menu("Draw");
        
        MenuItem drawLine = new MenuItem("Line");
        
        drawLine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                curserMode = "Line";
                
                //gc.setStroke(colorPicker.getValue());
                //gc.strokeLine(0, 0, image.getHeight(), image.getWidth());
            }
        });
        menuDraw.getItems().addAll(drawLine);
        
        // adds the File Edit, and View objects to the menuBar container
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuDraw);

        
        // adds the menu bar to the top of the view 
        root.add(menuBar, 0, 0);
        // Aligns the grid so that (0, 0) is in the top write
        root.setAlignment(Pos.TOP_RIGHT);
        

        root.add(canvasWrapper.getColorPicker(), 1, 0);

        
        
        primaryStage.setTitle("Image Viewer");
        primaryStage.setScene(scene);        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}


/*
        // remove from Rososcos Version
        MenuItem resizeCanvas = new MenuItem("Resize Canvas");
        resizeCanvas.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                
                ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
                
                TextField from = new TextField();
                from.setPromptText("From");
                TextField to = new TextField();
                to.setPromptText("To");
                
                GridPane gridPane = new GridPane();
                
                gridPane.add(from, 0, 0);
                gridPane.add(new Label("To:"), 1, 0);
                gridPane.add(to, 2, 0);

                dialog.getDialogPane().setContent(gridPane);
                
                Optional<Pair<String, String>> data = dialog.showAndWait();
                
                data.ifPresent(pair -> {  
                    System.out.println("From=" + pair.getKey() + ", To=" + pair.getValue());                    
                    canvasWrapper.resizeCanvas(Double.parseDouble(pair.getKey()), Double.parseDouble(pair.getValue()));
                });
                
                primaryStage.sizeToScene();
                
            }
        });
        // remove from Rososcos Version
        */