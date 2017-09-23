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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Bill
 */
public class CanvasWrapper {
    ColorPicker colorPicker = new ColorPicker();
        
    final Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    private String curserMode = new String ("Default");
    
    
    private double startDragClickX;
    private double startDragClickY;
    
    void setCurserMode(String mode) {
        curserMode = mode;
    }
    
    CanvasWrapper() {
       canvas.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            if (curserMode == "Line") {
                startDragClickX = event.getX();
                startDragClickY = event.getY();
            }
        }
    });
        
    canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (curserMode == "Line") {
                gc.setStroke(colorPicker.getValue());
                gc.strokeLine(startDragClickX, startDragClickY, event.getX(), event.getY());
            }
        }
    }); 
    }
    
    Image drawBlankCanvas(Image toWrite) {
        File blankImage = new File("Empty.png");
        try {
            Image blank = new Image(new FileInputStream(blankImage));
            drawImageOnCanvas(blank);
            return blank;            
        } catch (FileNotFoundException e) {
            return toWrite;
        }
       
    }
    
    void drawImageOnCanvas(Image image) {
        // sets the Image that will be shown in the Viewer 
                    //imageView.setImage(image);
                    canvas.setHeight(image.getHeight());
                    canvas.setWidth(image.getWidth());
                    
                    gc.drawImage(image, 0, 0);
    }
    
    void resizeCanvas (double width, double height) {
        canvas.resize(width, height);
    }
    
    ColorPicker getColorPicker () {
        return colorPicker;
    }
    
    Canvas getCanvas() {
        return canvas;
    }
}
