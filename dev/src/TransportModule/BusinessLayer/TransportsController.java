package TransportModule.BusinessLayer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class TransportsController {

    private TreeMap<Integer,Transport> transports;
    private int idCounter;

    public TransportsController(){
        transports = new TreeMap<>();
        idCounter = 0; // this will have to be restored from the DB in the future
    }

    public Transport createTransport(Site source, LinkedList<Site> destinations, HashMap<Site,ItemList> itemList, int truckID, int DriverID, LocalDateTime date/* int Weight*/){
        Transport transport = new Transport(idCounter++, source, destinations, itemList, truckID, DriverID, date/*, Weight*/);
        transports.put(transport.getId(), transport);
        return transport;
    }

    public Transport getTransport(int id) throws IOException {
        if (transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        return transports.get(id);
    }

    public Transport removeTransport(int id) throws IOException {
        if (transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        return transports.remove(id);
    }
    public void updateTransport(int id, Transport newTransport) throws IOException{
        if(transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        transports.put(id, newTransport);
    }

}
