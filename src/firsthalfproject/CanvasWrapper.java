/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firsthalfproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;


/**
 *
 * @author Bill
 */
public class CanvasWrapper {
        
    final Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    private String curserMode = new String ("Default");
    
    Image imageBeforeDraw;
    
    private double startDragClickX;
    private double startDragClickY;
    
    void setCurserMode(String mode) {
        curserMode = mode;
    }
    
    CanvasWrapper() {
        //FirstHalfProject.root.add(canvas, 0, 1);
        
       canvas.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                imageBeforeDraw = getImageOnCanvas();
                if (curserMode == "Line" || curserMode == "Rectangle" || curserMode == "Square") {
                    startDragClickX = event.getX();
                    startDragClickY = event.getY();
                } 
                if (curserMode == ("FreeDraw")) {
                        gc.beginPath();
                        gc.moveTo(event.getX(), event.getY());   
                }
                
            }
        });
    canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (curserMode == "Rectangle") {
                    gc.drawImage(imageBeforeDraw, 0, 0);
                    drawRectangle(startDragClickX, startDragClickY, event.getX(), event.getY());
                }
                if (curserMode == "Square") {
                    gc.drawImage(imageBeforeDraw, 0, 0);
                    drawSquare(startDragClickX, startDragClickY, event.getX(), event.getY());
                }
                
                
                if (curserMode == "FreeDraw") {
                    gc.lineTo(event.getX(), event.getY());
                    gc.moveTo(event.getX(), event.getY());
                    gc.stroke();
                }
            }
    });
        
    canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {    
        public void handle(MouseEvent event) {
            if (curserMode == "Line") {
                updateEnviormentalVariables();
                gc.strokeLine(startDragClickX, startDragClickY, event.getX(), event.getY());
                FirstHalfProject.undoWrapper.updateUndoStack();
                FirstHalfProject.smartSaveWrapper.autoSave();
            } else if (curserMode == "Rectangle") {
                drawRectangle(startDragClickX, startDragClickY, event.getX(), event.getY());
                FirstHalfProject.undoWrapper.updateUndoStack();
                FirstHalfProject.smartSaveWrapper.autoSave();
            } else if (curserMode == "Square") {
                drawSquare(startDragClickX, startDragClickY, event.getX(), event.getY());
                FirstHalfProject.undoWrapper.updateUndoStack();
                FirstHalfProject.smartSaveWrapper.autoSave();
            } else if (curserMode == "FreeDraw") {              
                gc.lineTo(event.getX(), event.getY());   
                gc.closePath();
                updateEnviormentalVariables();
                gc.stroke();
                gc.beginPath();
                FirstHalfProject.undoWrapper.updateUndoStack();
                FirstHalfProject.smartSaveWrapper.autoSave();
            }
        }
    });   
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
                    
        gc.drawImage(image, 0, 0);
        FirstHalfProject.image= canvas.snapshot(null, null);
        FirstHalfProject.undoWrapper.updateUndoStack();
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
