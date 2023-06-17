package presentationLayer.gui.plAbstracts;

public interface ObjectObserver<T> {
    void update(ObservableObject<T> observable);
}
