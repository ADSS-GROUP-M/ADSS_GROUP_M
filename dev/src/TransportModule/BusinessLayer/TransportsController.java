package TransportModule.BusinessLayer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;
public class TransportsController {
    TreeSet <Transport> transports;
    public TransportsController(){
        transports = new TreeSet<>();
    }
    public Transport createTransport(Site source, LinkedList<Site> destinations, HashMap<Site,ItemList> itemList, int truckID, int DriverID, LocalDateTime date/* int Weight*/){
        int id = 0;
        Transport transport = new Transport(id, source, destinations, itemList, truckID, DriverID, date/*Weight*/);
        transports.add(transport);
        id++;
        return transport;
    }
    public Transport getTransport(int id){
        for (Transport transport : transports) {
            if (transport.getId() == id) {
                return transport;
            }
        }
        return null;
    }
    public Transport removeTransport(int id) throws Exception {
        Transport transport = getTransport(id);
        if (transport != null) {
            Transport removedTransport = transport;
            transports.remove(transport);
            return removedTransport;
        }
        throw new Exception("Transport not found");
    }
    public boolean updateTransport(int id, Transport newTransport){
        Transport transport = getTransport(id);
        if (transport != null) {
            transports.remove(transport);
            transports.add(newTransport);
            return true;
        }
        return false;
    }

}
