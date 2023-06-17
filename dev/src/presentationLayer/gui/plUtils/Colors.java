package presentationLayer.gui.plUtils;

import javax.swing.*;
import java.awt.*;

public class Colors {

    public static ColorPalette colorPalette = ColorPalette.orange;

    enum ColorPalette{
        orange,
        blue
    }

    private static final Color orangeForeground = new Color(236, 119, 78);
    private static final Color orangeTransparentForeground = new Color(236, 119, 78,128);
    private static final Color orangeBackground = new Color(255, 245, 244, 255);
    private static final Image orangeImage = new ImageIcon("src/resources/orange_truck.jpg").getImage();

    public static Color getForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeForeground;
            case blue -> null;
        };
    }

    public static Color getTrasparentForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeTransparentForeground;
            case blue -> null;
        };
    }

    public static Color getBackgroundColor(){
        return switch(colorPalette){
            case orange -> orangeBackground;
            case blue -> null;
        };
    }

    public static Image getBackgroundImage(){
        return switch(colorPalette){
            case orange -> orangeImage;
            case blue -> null;
        };
    }
}
