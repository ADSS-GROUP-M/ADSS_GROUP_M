package presentationLayer.suppliersModule;

public class Response <T>{
    private T returnValue;
    private String msg;

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
}
