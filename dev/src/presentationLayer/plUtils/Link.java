package presentationLayer.plUtils;


import java.util.Objects;

public class Link {

    private final String name;
    private final Runnable onClick;

    /**
     * @throws NullPointerException if name or onClick is null
     */
    public Link (String name, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
    }

    public void click(){
        onClick.run();
    }

    public String getName() {
        return name;
    }
}
