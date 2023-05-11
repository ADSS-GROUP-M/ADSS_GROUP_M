package serviceLayer.transportModule;

import businessLayer.transportModule.TransportsController;
import objects.transportObjects.Transport;
import utils.Response;
import exceptions.TransportException;

public class TransportsService {

    private final TransportsController tc;

    public TransportsService(TransportsController tc){
        this.tc = tc;
    }

    /**
     * @param json serialized {@link Transport} object with id -1
     * @throws UnsupportedOperationException if transport.id() != -1
     * @return if successful, a serialized {@link Response} object with the id of the added transport in the data field
     * <br/><br/>
     * if unsuccessful, a serialized {@link Response} object with all the collected errors
     * <br/>The following errors can occur:
     * <ul>
     * <li>The Truck's maximum weight has been exceeded</li>
     * <li>The assigned Driver does not have the required license for the Truck</li>
     * <li>The source name of the Transport does not exist in the System</li>
     * <li>Any of the destination addresses of the Transport do not exist in the System</li>
     * <li>Any of the assigned ItemLists for the destinations do not exist in the System</li>
     * </ul>
     * The Response's message will contain information detailing all the reasons for the failure,
     * separated by newlines, while the cause will contain a comma-separated list of the fields
     * that failed validation.
     * <br/><br/>
     * Example: <br/><br/> the message field will contain the following:<br/><br/>
     * The truck's maximum weight has been exceeded<br/>
     * A driver with license type A1 is not permitted to drive this truck<br/>
     * Site with name 123 Main St does not exist<br/>
     * Site with name 456 Oak St does not exist<br>
     * Item list with id 1234 does not exist<br/><br/>
     * the data field will contain the following:<br/><br/>
     * weight,license,source,destination:0,itemList:1
     * <br/><br/>
     * destination:0 means that the first destination failed validation
     * <br/>
     * itemList:1 means that the second item list failed validation
     */
    public String addTransport(String json){
        Transport transport = Transport.fromJson(json);
        try
        {
            Transport added = tc.addTransport(transport);
            return new Response("Transport added successfully with id "+added.id(),true, added).toJson();
        }
        catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateTransport(String json){
        Transport transport = Transport.fromJson(json);
        try
        {
            Transport updated = tc.updateTransport(transport.id(), transport);
            return new Response("Transport updated successfully",true,updated).toJson();
        }
        catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    /**
     * @param json serialized {@link Transport#getLookupObject(int)}
     */
    public String removeTransport(String json){
        Transport transport = Transport.fromJson(json);
        try
        {
            tc.removeTransport(transport.id());
            return new Response("Transport removed successfully",true).toJson();
        }
        catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    /**
     * @param json serialized {@link Transport#getLookupObject(int)}
     */
    public String getTransport(String json){
        Transport transport = Transport.fromJson(json);
        try
        {
            transport = tc.getTransport(transport.id());
            return new Response("Transport found successfully",true, transport).toJson();
        }
        catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getAllTransports(){
        try {
            return new Response("All Transports found successfully",true, tc.getAllTransports()).toJson();
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
    }
}
