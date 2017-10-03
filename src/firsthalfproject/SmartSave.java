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
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Bill
 */
public class SmartSave {
    File tempSave = new File("temp");
    File lastSaveAsPoint;
    String lastSaveType = new String("png");
    
    
    void autoSave() {
        save(FirstHalfProject.canvasWrapper.getImageOnCanvas(), lastSaveType, tempSave);
    }
    
    void smartSave() {
        if (lastSaveAsPoint == null) {
            saveAs();
        } else {
            save(FirstHalfProject.canvasWrapper.getImageOnCanvas(), lastSaveType, lastSaveAsPoint);
        }
    }    
    void saveAs() {
        // launches the File Chooser to select a file to save to then stores that file in the file object
        File file = FirstHalfProject.fileChooser.showSaveDialog(FirstHalfProject.stage); 
        lastSaveAsPoint = file;
                 
        //stores the selected file formate in the variable fileType
        //gets the first of the posible extentions
        // then removes the first two charachters (*.) to get the name of the file type
        String fileType = new String(FirstHalfProject.fileChooser.getSelectedExtensionFilter().getExtensions().get(0).substring(2));
        
        save(FirstHalfProject.image, fileType, file);
    }
    
    void save(Image toSave, String fileType, File file) {             

        FirstHalfProject.image = FirstHalfProject.canvasWrapper.getCanvas().snapshot(null, null);
                    
        // writes image to the selected file 
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(FirstHalfProject.canvasWrapper.getCanvas().snapshot(null, null), null);
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null); 
                
        try {
            ImageIO.write(newBufferedImage, fileType, file);
        } catch (IOException e) {
            
        }
                
    }
    
    void recoverSmartSave() {
        try {
            FirstHalfProject.canvasWrapper.drawImageOnCanvas(new Image(new FileInputStream(tempSave)));
            
            // resizes the window to match the size of the image
            FirstHalfProject.stage.sizeToScene();
        } catch (IOException e) {
            
        }
    }
}


