/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firsthalfproject;
// Testing Git
// This is to verify that I am committing directly to github
//current time 15:20

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;


/**
 *
 * @author Bill
 */
public class FirstHalfProject extends Application {
    
    // variable that containes the active image
    public static Image image;
    public static GridPane root = new GridPane();
   
    
    public static Stage stage;
    
    
    static ComboBoxWrapper comboBoxWrapper = new ComboBoxWrapper();
    static CanvasWrapper canvasWrapper = new CanvasWrapper();
    static ColorPickerWrapper colorPickerWrapper = new ColorPickerWrapper();
    static ToolBarWrapper toolBarWrapper = new ToolBarWrapper();
    static MenuBarWrapper menuBarWrapper = new MenuBarWrapper();
    static UndoWrapper undoWrapper = new UndoWrapper();
    static SmartSave smartSaveWrapper = new SmartSave();
    
    
    
    // provides access to the File Manager's File Chooser in a file manager and os agnostic way
    // The File Chooser opens a window that allows the user to select a file 
    // this file is then retured to the program to use as it will
    static FileChooser fileChooser = new FileChooser();
    
    @Override
    public void start(Stage primaryStage) {        
        
        
        
        // sets the file formates that the File Chooser will show in its window
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG(*.png)", "*.png"));        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG(*.jpg)", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP(*.bmp)", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF(*.gif)", "*.gif"));
        
        
        stage = primaryStage;
        // Container that arranges the items contained in it in a grid for display on screen
        // grid is arranged as (column, row)    
                
        Scene scene = new Scene(root);
        
        // adds the menu bar to the top of the view 
        root.add(canvasWrapper.getCanvas(), 0, 3);
        
        root.add(menuBarWrapper.getMenuBar(), 0, 0);
        
        root.add(comboBoxWrapper.getComboBox(), 2, 0);
        
        root.add(toolBarWrapper.getToolBar(), 0, 2);
        
        root.add(colorPickerWrapper.getColorPicker(), 1, 0);
        
        // Aligns the grid so that (0, 0) is in the top right
        root.setAlignment(Pos.TOP_RIGHT);
        
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