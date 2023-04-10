package Backend.ServiceLayer;

public class Response <T>{
    private T returnValue;
    private String msg;
    private boolean error;

    public Response(T returnValue){
        this.returnValue = returnValue;
    }

    public Response(String msg, boolean error){
        this.msg = msg;
        this.error = error;
    }

    public T getReturnValue() {
        return returnValue;
    }

    public String getMsg() {
        return msg;
    }

    public boolean errorOccurred(){
        return error;
    }
}
