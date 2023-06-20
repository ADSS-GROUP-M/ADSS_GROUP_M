package Fronend.PresentationLayer.SuppliersModule.CLI;

public class Response <T>{
    private T returnValue;
    private String msg;
    private boolean error;


    public Response(T returnValue){
        this.returnValue = returnValue;
    }

    public Response(String msg){
        this.msg = msg;
    }

    public T getReturnValue() {
        return returnValue;
    }

    public String getMsg() {
        return msg;
    }

    public String getError() {
        return msg;
    }

    public T getData() {
        return returnValue;
    }

    public boolean isSuccess() {
        return !error;
    }
}
