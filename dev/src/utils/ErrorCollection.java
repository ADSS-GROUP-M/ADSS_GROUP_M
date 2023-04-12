package utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class ErrorCollection {

    private final LinkedList<String> errors;
    private final LinkedList<String> causes;

    public ErrorCollection(){
        errors = new LinkedList<>();
        causes = new LinkedList<>();
    }

    /**
     * This constructor is used to create an ErrorCollection from a string of errors and a string of causes.
     * @param errors A string of errors, separated by newlines.
     * @param causes A string of causes, separated by commas.
     */
    public ErrorCollection(String errors, String causes){
        this();
        String[] errorsArray = errors.split("\n");
        String[] causesArray = causes.split(",");
        this.errors.addAll(Arrays.asList(errorsArray));
        this.causes.addAll(Arrays.asList(causesArray));
    }

    public void addError(String errorMessage, String cause){
        errors.add(errorMessage);
        causes.add(cause);
    }

    public String[] errorsAsArray(){
        return errors.toArray(new String[0]);
    }

    public String[] causesAsArray(){
        return causes.toArray(new String[0]);
    }

    public String message(){
        StringBuilder sb = new StringBuilder();
        ListIterator<String> it = errors.listIterator();
        while(it.hasNext()){
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public Throwable cause(){
        StringBuilder sb = new StringBuilder();
        ListIterator<String> it = causes.listIterator();
        while(it.hasNext()){
            sb.append(it.next());
            if(it.hasNext()) {
                sb.append(",");
            }
        }
        return new Throwable(sb.toString());
    }

    public boolean hasErrors(){
        return errors.size() > 0;
    }

    public String toJson(){
        return JSON.serialize(this);
    }

    public static ErrorCollection fromJson(String json){
        return JSON.deserialize(json, ErrorCollection.class);
    }
}
