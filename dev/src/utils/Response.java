package utils;

import transportModule.frontend.cmdApp.AppData;
import transportModule.records.Truck;

import java.lang.reflect.Type;

public class Response {

    private final String message;
    private final boolean success;
    private final String data;

    /**
     * The data parameter will be serialized using {@link JSON#serialize(Object)}
     */
    public <T> Response(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = JSON.serialize(data);
    }

    /**
     * this constructor takes a string as data and does not serialize it
     */
    public Response(String message, boolean success, String data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    /**
     * This constructor is used when there is no data to be sent. The data field will be null.
     */
    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
        data = null;
    }

    public String message() {
        return message;
    }

    public boolean success() {
        return success;
    }

    /**
     * @param typeOfT Type of the object for deserialization
     * @return Deserialized object of type T
     * @apiNote If you want to get the raw data as a string, use {@link #data()} instead
     * <br/><br/><br/>example for a type definition:<br/><br/><code>1) Type type = new TypeToken&lt;LinkedList&lt;SomeClass&gt;&gt;(){}.getType();</code>
     * <br/><br/><code>2) Type type = SomeClass.class;</code>
     */
    public <T> T data(Type typeOfT) {
        if(data == null)
            return null;
        return JSON.deserialize(data,typeOfT);
    }

    /**
     * @return Raw data as a string. Object must be deserialized manually
     * @apiNote If you want to get a deserialized object Use {@link #data(Type)} instead
     */
    public String data(){
        return data;
    }

    /**
     * This method will serialize the response object using {@link JSON#serialize(Object)}
     */
    public String toJson(){
        return JSON.serialize(this);
    }

    /**
     * This method will deserialize the json string using {@link JSON#deserialize(String, Type)}
     */
    public static Response fromJson(String json){
        return JSON.deserialize(json, Response.class);
    }

    /**
     * This method will return a response object with the message as the exception message and success as false.
     * @apiNote if the exception has a cause, the cause message will be added to the response object in the data field as a string.
     * Otherwise, the data field will be an empty string
     */
    public static Response getErrorResponse(Exception e){

        String cause = "";
        if(e.getCause() != null){
            cause = e.getCause().getMessage();
        }
        return new Response(e.getMessage(), false, cause);
    }
}
