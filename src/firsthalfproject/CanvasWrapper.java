/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firsthalfproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import static java.lang.Math.abs;
import static java.lang.Math.min;


/**
 *
 * @author Bill
 */
public class CanvasWrapper {
        
    final Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    
    
    private String curserMode = new String ("Default");
    
    
    private double startDragClickX;
    private double startDragClickY;
    
    void setCurserMode(String mode) {
        curserMode = mode;
    }
    
    Image dragImage;
    double dragWidth;
    double dragHeight;
    Point2D dragTopCorner;
    
    Boolean dragStarted = false;
    
    double xDistanceBetweenMouseAndCorner;
    double yDistanceBetweenMouseAndCorner;
    
    
    CanvasWrapper() {
        //FirstHalfProject.root.add(canvas, 0, 1);
       /*
       canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
           public void handle(MouseEvent event) {  
               if (curserMode == "drop") {                  
                    gc.drawImage(dragImage, event.getX(), event.getY(), dragWidth, dragHeight);
                    setCurserMode("default");
                }
           }
       }); */
       canvas.setOnDragDetected(new EventHandler<MouseEvent>() {            
            public void handle(MouseEvent event) {  
                System.out.println(curserMode);
                if (curserMode == "Line" || curserMode == "Rectangle" || curserMode == "Square" || curserMode == "drag") {
                    System.out.println("Drag Detected");
                    FirstHalfProject.undoWrapper.updateUndoStack();
                    startDragClickX = event.getX();
                    startDragClickY = event.getY();
                    dragStarted = true;
                    
                } 
                if (curserMode == ("FreeDraw")) {
                    gc.beginPath();
                    gc.moveTo(event.getX(), event.getY());   
                    dragStarted = true;
                }
                if (curserMode == "drop") {
                    FirstHalfProject.undoWrapper.undo();
                    xDistanceBetweenMouseAndCorner = event.getX() - dragTopCorner.getX();
                    yDistanceBetweenMouseAndCorner = event.getY() - dragTopCorner.getY();
                    gc.clearRect(dragTopCorner.getX(), dragTopCorner.getY(), dragWidth, dragHeight);
                    FirstHalfProject.undoWrapper.updateUndoStack();
                    dragStarted = true;
                }
                
            }
        });
    canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {        
            public void handle(MouseEvent event) {
                if (dragStarted == true) {
                    if (curserMode == "Line") {
                        FirstHalfProject.undoWrapper.undo();
                        updateEnviormentalVariables();                    
                        gc.strokeLine(startDragClickX, startDragClickY, event.getX(), event.getY());
                        FirstHalfProject.undoWrapper.updateUndoStack();
                    }
                    if (curserMode == "Rectangle" || curserMode == "drag") { 
                        FirstHalfProject.undoWrapper.undo();
                        updateEnviormentalVariables();                    
                        drawRectangle(startDragClickX, startDragClickY, event.getX(), event.getY());
                        FirstHalfProject.undoWrapper.updateUndoStack();
                    }
                    if (curserMode == "Square") { 
                        FirstHalfProject.undoWrapper.undo();
                        updateEnviormentalVariables();                    
                        drawSquare(startDragClickX, startDragClickY, event.getX(), event.getY());
                        FirstHalfProject.undoWrapper.updateUndoStack();
                    }
                    if (curserMode == "FreeDraw") {
                        gc.lineTo(event.getX(), event.getY());
                        gc.moveTo(event.getX(), event.getY());
                        updateEnviormentalVariables();
                        gc.stroke();
                    }
                    if (curserMode == "drop") {                  
                        FirstHalfProject.undoWrapper.undo();
                        gc.clearRect(dragTopCorner.getX(), dragTopCorner.getY(), dragWidth, dragHeight);
                        gc.drawImage(dragImage, event.getX()-xDistanceBetweenMouseAndCorner, event.getY()-yDistanceBetweenMouseAndCorner, dragWidth, dragHeight);   
                        FirstHalfProject.undoWrapper.updateUndoStack();
                    } 
                }
            }
    });
        
    canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {    
        public void handle(MouseEvent event) {
            if (curserMode == "Line") {                
                FirstHalfProject.undoWrapper.undo();
                updateEnviormentalVariables();
                gc.strokeLine(startDragClickX, startDragClickY, event.getX(), event.getY());
                doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas();
                
                dragStarted = false;
            } else if (curserMode == "Rectangle") {
                FirstHalfProject.undoWrapper.undo();
                drawRectangle(startDragClickX, startDragClickY, event.getX(), event.getY());
                doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas();
                dragStarted = false;
            } else if (curserMode == "Square") {
                FirstHalfProject.undoWrapper.undo();
                drawSquare(startDragClickX, startDragClickY, event.getX(), event.getY());
                doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas();
                dragStarted = false;
        
                //FirstHalfProject.smartSaveWrapper.autoSave();
            } else if (curserMode == "FreeDraw") {              
                gc.lineTo(event.getX(), event.getY());   
                gc.closePath();
                updateEnviormentalVariables();
                gc.stroke();
                gc.beginPath();
                doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas();
                dragStarted = false;
            } else if (curserMode == "drag") { 
                FirstHalfProject.undoWrapper.undo();
                dragTopCorner = findTopCorner(startDragClickX, startDragClickY, event.getX(), event.getY());
                dragWidth= findWidth(startDragClickX, startDragClickY, event.getX(), event.getY());
                dragHeight = findHeight(startDragClickX, startDragClickY, event.getX(), event.getY());        
                dragImage = getImageOnCanvas(dragTopCorner.getX(), dragTopCorner.getY(), dragWidth, dragHeight);

                drawRectangle(startDragClickX, startDragClickY, event.getX(), event.getY());
                FirstHalfProject.undoWrapper.updateUndoStack();
                dragStarted = false;
                                
                setCurserMode("drop");
            } else if (curserMode == "drop") {                  
                FirstHalfProject.undoWrapper.undo();
                gc.clearRect(dragTopCorner.getX(), dragTopCorner.getY(), dragWidth, dragHeight);
                gc.drawImage(dragImage, event.getX()-xDistanceBetweenMouseAndCorner, event.getY()-yDistanceBetweenMouseAndCorner, dragWidth, dragHeight);
                setCurserMode("default");
                doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas();                
                dragStarted = false;
            }
        }
    });   
    }
    
    void doTheThingsThatShouldBeDoneAfterDrawingOnTheCanvas() {
        FirstHalfProject.undoWrapper.updateUndoStack();
        FirstHalfProject.undoWrapper.clearRedoStack();
        FirstHalfProject.smartSaveWrapper.autoSave();
    }
    
    void drawTestPath () {
        gc.beginPath();
        gc.moveTo(0, 0);
        gc.lineTo(100, 100);
        gc.stroke();
    }
    
    void drawBlankCanvas() {
        File blankImage = new File("Empty.png");
        try {
            //FirstHalfProject.image = new Image(new FileInputStream(blankImage));
            drawImageOnCanvas(new Image(new FileInputStream(blankImage)));          
        } catch (FileNotFoundException e) {
            
        }
       
    }
    
    void drawImageOnCanvas(Image image) {
        // sets the Image that will be shown in the Viewer 
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(image, 0, 0);
        FirstHalfProject.image= canvas.snapshot(null, null);
        FirstHalfProject.stage.sizeToScene();
        //FirstHalfProject.undoWrapper.updateUndoStack();
    }
    
    void resizeCanvas (double width, double height) {
        canvas.resize(width, height);
    }
   
    
    Canvas getCanvas() {
        return canvas;
    }
    
    Image getImageOnCanvas() {
        return canvas.snapshot(null, null);
    }
    
    Image getImageOnCanvas(double topCornerX, double topCornerY, double width,double height) {
        Image oldImage = getImageOnCanvas();
        PixelReader reader = oldImage.getPixelReader();
        WritableImage newImage = new WritableImage(reader, (int)topCornerX, (int)topCornerY, (int)width, (int)height);
        return newImage;
        
    }
    
    
    
    void drawRectangle(double CornerOneX, double CornerOneY, double CornerTwoX, double CornerTwoY) {
        double x = findTopCorner(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY).getX();
        double y = findTopCorner(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY).getY();
        
        double width = findWidth(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY);
        double height = findHeight(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY);    
        
        updateEnviormentalVariables();
        gc.strokeRect(x, y, width, height);
        
    }
    
    void drawSquare (double CornerOneX, double CornerOneY, double CornerTwoX, double CornerTwoY) {                       
        double x = findTopCorner(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY).getX();
        double y = findTopCorner(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY).getY();
        
        double width = findWidth(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY);
        double height = findHeight(CornerOneX,  CornerOneY,  CornerTwoX,  CornerTwoY);
        
        width = min (width, height);
        height = min (width, height);
        
        
        updateEnviormentalVariables();
        gc.strokeRect(x, y, width, height);
        
    }
    
    Point2D findTopCorner (double CornerOneX, double CornerOneY, double CornerTwoX, double CornerTwoY) {
        // finds the top corner of a Rectangle and returns its coordinates
        
        double x;
        double y;
        
        x = min(CornerOneX, CornerTwoX);
        y = min(CornerOneY, CornerTwoY);
        
        Point2D toReturn = new Point2D(x, y);
        
        return toReturn;
    }
    
    double findWidth (double CornerOneX, double CornerOneY, double CornerTwoX, double CornerTwoY) {
        return abs(CornerOneX - CornerTwoX);
    }
    
    double findHeight (double CornerOneX, double CornerOneY, double CornerTwoX, double CornerTwoY) {
        return abs(CornerOneY - CornerTwoY);
    }
    
    double getDistance (double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }
    
    void updateEnviormentalVariables() {
        gc.setStroke(FirstHalfProject.colorPickerWrapper.getValue());
        gc.setLineWidth(FirstHalfProject.comboBoxWrapper.getValue());
    }
}