package presentationLayer.plUtils;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuickAccess {
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
}
