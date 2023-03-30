package TransportModule.BusinessLayer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class TransportsController {

    private TrucksController tc;
    private TreeMap<Integer,Transport> transports;
    private int idCounter;

    public TransportsController(TrucksController tc){
        transports = new TreeMap<>();
        idCounter = 0; // this will have to be restored from the DB in the future
    }

    public void addTransport(Transport transport)throws IOException{
        validateTransport(transport);

        transports.put(idCounter++, transport);
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

        validateTransport(newTransport);

        transports.put(id, newTransport);
    }

    public LinkedList<Transport> getAllTransports(){
        LinkedList<Transport> list = new LinkedList<>();
        for (Transport t : transports.values())
            list.add(t);
        return list;
    }

    private void validateTransport(Transport transport) throws IOException{
        int weight = transport.weight();
        Truck truck = tc.getTruck(transport.id());
        if (truck.maxWeight() < weight)
            throw new IOException("The truck's maximum weight has been exceeded");
    }
}
