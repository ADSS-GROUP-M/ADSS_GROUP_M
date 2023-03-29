package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.Transport;
import TransportModule.BusinessLayer.TransportsController;

import java.io.IOException;
import java.util.LinkedList;

public class TransportsService {

    private TransportsController tc;

    public TransportsService(){
        tc = new TransportsController();
    }

    public String createTransport(String json){
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
        try
        {
            tc.addTransport(transport);
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport created successfully",true, "").getJson();
    }

    public String updateTransport(String json){
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
        try
        {
            tc.updateTransport(transport.getId(), transport);
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport updated successfully",true, "").getJson();
    }

    public String removeTransport(String json){
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
        try
        {
            tc.removeTransport(transport.getId());
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport removed successfully",true, "").getJson();
    }

    public String getTransport(String json){
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
        try
        {
            transport = tc.getTransport(transport.getId());
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<Transport>("Transport found successfully",true, transport).getJson();
    }

    public String getAllTransports(){
        return new Response<LinkedList<Transport>>("All Transports found successfully",true, tc.getAllTransports()).getJson();
    }
}
