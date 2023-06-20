package Backend.ServiceLayer.SuppliersModule;

public class Response <T>{
    private T returnValue;
    private String msg;
    private boolean error;

    public Response(T returnValue){
        this.returnValue = returnValue;
        error = false;
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

    public String getError(){
        return msg;
    }

    public T getData() {
        return returnValue;
    }
    public boolean errorOccurred(){
        return error;
    }

    public boolean isSuccess(){
        return !error;
    }
}
