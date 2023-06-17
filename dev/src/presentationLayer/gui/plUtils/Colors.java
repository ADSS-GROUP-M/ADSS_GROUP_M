package presentationLayer.gui.plUtils;

import javax.swing.*;
import java.awt.*;

public class Colors {

    public static ColorPalette colorPalette = ColorPalette.blue;

    enum ColorPalette{
        orange,
        blue
    }


    // ================================ ORANGE ================================ |
    private static final Color orangeForeground = new Color(236, 119, 78, 255);
    private static final Color orangeTransparentForeground = new Color(236, 119, 78,128);
    private static final Color orangeBackground = new Color(255, 245, 244, 255);
    private static final Image orangeImage = new ImageIcon("src/resources/orange_truck.jpg").getImage();
    // ================================= BLUE ================================== |

//    private static final Color blueForeground = new Color(78, 118, 236, 255);
//    private static final Color blueTransparentForeground = new Color(78, 118, 236, 128);
//    private static final Color blueBackground = new Color(244, 250, 255, 255);
    private static final Color blueForeground = new Color(39, 103, 166, 255);
    private static final Color blueTransparentForeground = new Color(39, 103, 166, 128);
    private static final Color blueBackground = new Color(244, 250, 255, 255);
    private static final Image blueImage = new ImageIcon("src/resources/blue_truck.jpg").getImage();


    public static Color getForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeForeground;
            case blue -> blueForeground;
        };
    }

    public static Color getTrasparentForegroundColor(){
        return switch(colorPalette){
            case orange -> orangeTransparentForeground;
            case blue -> blueTransparentForeground;
        };
    }

    public static Color getBackgroundColor(){
        return switch(colorPalette){
            case orange -> orangeBackground;
            case blue -> blueBackground;
        };
    }

    public static Image getBackgroundImage(){
        return switch(colorPalette){
            case orange -> orangeImage;
            case blue -> blueImage;
        };
    }
}
