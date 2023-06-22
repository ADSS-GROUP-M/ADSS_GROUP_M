package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FileUtils {

    public static String getRootPath() {
        return new File(FileUtils.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getPath() + File.separator;
    }

    /**
     * This is relative to getRootPath(), it goes one folder up
     */
    public static String getParentFolderPath(){
        String rootPath = getRootPath();
        rootPath = rootPath.substring(0,rootPath.length()-1);
        return  rootPath.substring(0,rootPath.lastIndexOf(File.separator)+1);
    }

    public static String getResource(String fileName) throws IOException {
        try (InputStream stream = FileUtils.class.getResourceAsStream("/"+fileName)) {
            if(stream == null) {
                throw new IOException("Resource %s not found".formatted(fileName));
            }
            Scanner scanner = new Scanner(stream);
            StringBuilder builder = new StringBuilder();
            while(scanner.hasNext()) {
                builder.append(scanner.next());
            }
            return builder.toString();
        }
    }

    public static Image getImage(String fileName) throws IOException {
        try (InputStream stream = FileUtils.class.getResourceAsStream("/"+fileName)) {
            if(stream == null) {
                throw new IOException("Resource %s not found".formatted(fileName));
            }
            return ImageIO.read(stream);
        }
    }
}
