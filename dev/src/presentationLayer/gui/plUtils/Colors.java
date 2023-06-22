package presentationLayer.gui.plUtils;

import utils.FileUtils;

import javax.swing.*;
import java.awt.*;

public class Colors {

    public static ColorPalette colorPalette = ColorPalette.blue;

    public enum ColorPalette{
        orange,
        blue,
        hr
    }


    // ================================ ORANGE ================================ |
    private static final Color orangeForeground = new Color(236, 119, 78, 255);
    private static final Color orangeTransparentForeground = new Color(236, 119, 78,128);
    private static final Color orangeBackground = new Color(255, 245, 244, 255);
    private static final Color orangeContentPanel = new Color(255, 255, 255, 230);
    private static final String orangeImage = "orange_truck.jpg";
    // ================================= BLUE ================================== |
    private static final Color blueForeground = new Color(39, 103, 166, 255);
    private static final Color blueTransparentForeground = new Color(39, 103, 166, 128);
    private static final Color blueBackground = new Color(244, 250, 255, 255);
    private static final Color blueContentPanel = new Color(255, 255, 255, 230);
    private static final String blueImage = "blue_truck.jpg";
    // ================================= HR ================================== |
    private static final Color hrForeground = new Color(39, 103, 166, 255);
    private static final Color hrTransparentForeground = new Color(39, 103, 166, 128);
    private static final Color hrBackground = new Color(244, 250, 255, 255);
    private static final Color hrContentPanel = new Color(255, 255, 255, 230);
    private static final String hrImage = "hr_background.jpg";

    public static Color getForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeForeground;
            case blue -> blueForeground;
            case hr -> hrForeground;
        };
    }

    public static Color getTrasparentForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeTransparentForeground;
            case blue -> blueTransparentForeground;
            case hr -> hrTransparentForeground;
        };
    }

    public static Color getBackgroundColor(){
        return switch(colorPalette){
            case orange -> orangeBackground;
            case blue -> blueBackground;
            case hr -> hrBackground;
        };
    }

    public static Image getBackgroundImage(){

        Image image = null;
        try{
            image = FileUtils.getImage(switch(colorPalette){
                case orange -> orangeImage;
                case blue -> blueImage;
                case hr -> hrImage;
            });
        } catch(Exception ignored){}
        return image;
    }

    public static Color getContentPanelColor(){
        return switch(colorPalette){
            case orange -> orangeContentPanel;
            case blue -> blueContentPanel;
            case hr -> hrContentPanel;
        };
    }
}
