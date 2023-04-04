package TransportModule.BusinessLayer;

import TransportModule.BusinessLayer.Records.Driver;
import TransportModule.BusinessLayer.Records.Transport;
import TransportModule.BusinessLayer.Records.Truck;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class TransportsController {

    private TrucksController tc;
    private DriversController dc;
    private TreeMap<Integer, Transport> transports;
    private int idCounter;

    public TransportsController(TrucksController tc, DriversController dc){
        transports = new TreeMap<>();
        idCounter = 0; // currently not in use. this will have to be restored from the DB in the future
        this.tc = tc;
        this.dc = dc;
    }

    public void addTransport(Transport transport)throws IOException{
        validateTransport(transport);

        transports.put(transport.id(), transport);
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
        Truck truck = tc.getTruck(transport.truckId());
        Driver driver = dc.getDriver(transport.driverId());

        // used to return information about all the errors
        String toThrow = "";
        boolean throwException = false;
        Throwable cause = null;
        //==================================================

        // weight validation
        int weight = transport.weight();
        if (truck.maxWeight() < weight) {
            toThrow += "The truck's maximum weight has been exceeded";
            throwException = true;
            cause = new Throwable("weight");
        }

        // truck - driver validation
        String[] requiredLicense = truck.requiredLicense().split("");
        String[] driverLicense = driver.licenseType().name().split("");

        if(driverLicense[0].compareToIgnoreCase(requiredLicense[0]) < 0 ||  driverLicense[1].compareToIgnoreCase(requiredLicense[1]) < 0) {
            if(throwException){
                toThrow += "\n";
                cause = new Throwable(cause.getMessage()+",license");
            }
            else{
                cause = new Throwable("license");
            }
            toThrow += "A driver with license type "+driver.licenseType()+
                            " is not permitted to drive this truck";
            throwException = true;

        }
        if(throwException) throw new IOException(toThrow,cause);
    }
}
