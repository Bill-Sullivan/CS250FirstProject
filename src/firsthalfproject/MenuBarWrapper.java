/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firsthalfproject;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Bill
 */
public class MenuBarWrapper {
    MenuBar menuBar;
    MenuBarWrapper() {
        // menuBar serves two purposes
        // 1 create the menu bar displaied on the screen
        // 2 act as a container for the Menu objects
        menuBar = new MenuBar();        
 
        // --- Menu File
        Menu menuFile = new Menu("File"); // creates the File menu
        
        
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {   
                FirstHalfProject.canvasWrapper.drawBlankCanvas();
                FirstHalfProject.image = FirstHalfProject.canvasWrapper.getCanvas().snapshot(null, null);
                
                // resizes the window to match the size of the image
                FirstHalfProject.stage.sizeToScene();
            }
        });
        
        
        MenuItem open = new MenuItem("Open"); // creates the open menu item to be placed in the file menu
        
        // this function is called when the open MenuItem is clicked on
        open.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {                
                try {
                    // launches the File Chooser to open a file then stores that file in the file object
                    File file = FirstHalfProject.fileChooser.showOpenDialog(FirstHalfProject.stage);
                    
                    // converts the file to an image object for further use
                    FirstHalfProject.image = new Image(new FileInputStream(file));
                    FirstHalfProject.canvasWrapper.drawImageOnCanvas(FirstHalfProject.image);                    
                    
                    // resizes the window to match the size of the image
                    FirstHalfProject.stage.sizeToScene();    
                    
                } catch (FileNotFoundException e) {
                   
                }                     
            }
        });

        
        MenuItem save = new MenuItem("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.smartSaveWrapper.smartSave();
            }
        });
        
        // creates a new menu item Save to be placed in the file menu
        // when pressed this item saves the image currently in use 
        MenuItem saveAs = new MenuItem("Save As");
        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.smartSaveWrapper.saveAs();
            }
        });
        
        MenuItem recover = new MenuItem("Recover");
        recover.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.smartSaveWrapper.recoverSmartSave();
            }
        });
        
        menuFile.getItems().addAll(newItem, open, save, saveAs, recover);
        
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        
        MenuItem undo = new MenuItem("Undo");

        undo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.undoWrapper.undo();
            }
        });
        
        MenuItem redo = new MenuItem("Redo");

        redo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.undoWrapper.redo();
            }
        });
 
        menuEdit.getItems().addAll(undo, redo);
        
        // --- Menu View
        Menu menuView = new Menu("View"); 

        menuView.getItems().addAll();
        
        
        Menu menuDraw = new Menu("Draw");
        
        MenuItem drawLine = new MenuItem("Line");
        
        drawLine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.setCurserMode("Line");
            }
        });
        
        MenuItem freeDraw = new MenuItem("Free Draw");
        
        freeDraw.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.setCurserMode("FreeDraw");
            }
        });
        
        MenuItem drawRect = new MenuItem("Rectangle");
        
        drawRect.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.setCurserMode("Rectangle");
            }
        });
        
        MenuItem drawCircle = new MenuItem("Circle");
        
        drawCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.setCurserMode("Circle");
            }
        });
        
        MenuItem drawSquare = new MenuItem("Square");
        
        drawSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.setCurserMode("Square");
            }
        });
        
        
        MenuItem testDraw = new MenuItem("testDraw");
        
        testDraw.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FirstHalfProject.canvasWrapper.drawTestPath();
            }
        });
        
        menuDraw.getItems().addAll(drawLine, drawRect, drawCircle, drawSquare, freeDraw, testDraw);
        
        // adds the File Edit, and View objects to the menuBar container
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuDraw);
    }
    
    MenuBar getMenuBar() {
        return menuBar;
    }
}
