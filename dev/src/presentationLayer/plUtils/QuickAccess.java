package presentationLayer.plUtils;

import javafx.util.Pair;
import presentationLayer.plAbstracts.UIComponent;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuickAccess implements UIComponent {
    private final List<Pair<String, List<Link>>> links;

    public QuickAccess(){
        links = new LinkedList<>();
    }

    public QuickAccess addCategory(String name, Link ... links){
        this.links.add(new Pair<>(name, Arrays.asList(links)));
        return this;
    }

    public List<Pair<String, List<Link>>> getLinks(){
        return links;
    }

    @Override
    public Component getComponent() {
        return null;
    }
}
