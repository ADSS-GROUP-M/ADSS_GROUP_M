package dev.ServiceLayer.Objects;

public class Response<T> {
    private T returnValue;
    private String errorMessage;

    public Response() {
        this.returnValue = null;
        this.errorMessage = null;
    }

    public Response(T returnValue) {
        this.returnValue = returnValue;
        this.errorMessage = null;
    }

    public Response(T returnValue, String errorMessage) {
        this.returnValue = returnValue;
        this.errorMessage = errorMessage;
    }

    public static <T> Response<T> createErrorResponse(String errorMessage) {
        return new Response<>(null, errorMessage);
    }

    public T getReturnValue() {
        return this.returnValue;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean errorOccurred() {
        return this.errorMessage != null;
    }
}
