package utils;

import java.util.LinkedList;
import java.util.ListIterator;

public class ErrorCollection {

    //TODO: implement this class
    private final LinkedList<String> errors;
    private final LinkedList<String> causes;

    public ErrorCollection(){
        errors = new LinkedList<>();
        causes = new LinkedList<>();
    }

    public void addError(String errorMessage, String cause){
        errors.add(errorMessage);
        causes.add(cause);
    }

    public String[] errors(){
        return errors.toArray(new String[0]);
    }

    public String[] causes(){
        return causes.toArray(new String[0]);
    }

    public String message(){
        StringBuilder sb = new StringBuilder();
        ListIterator<String> it = errors.listIterator();
        while(it.hasNext()){
            sb.append(it.next());
            if(it.hasNext()) sb.append("\n");
        }
        return sb.toString();
    }
}
