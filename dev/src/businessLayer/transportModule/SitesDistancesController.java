package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.*;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import javafx.util.Pair;
import objects.transportObjects.Site;
import utils.transportUtils.TransportException;

import java.io.IOException;
import java.util.*;

public class SitesDistancesController {
    public SitesDistancesController(){

    }

    public List<DistanceBetweenSites> createDistanceObjects(Site site ,List<Site> sites) throws TransportException {

        //TODO: Figure out how to test this

        //TODO: replace with real distances

        

        Random rand = new Random();
        List<DistanceBetweenSites> distances = new LinkedList<>();
        distances.add(new DistanceBetweenSites(site.address(),site.address(),0));

        for(Site other : sites){
            if(other.address().equals(site.address())) continue;
            int distance = rand.nextInt(1,50); //TODO: temporary random distance
            distances.add(new DistanceBetweenSites(other.address(),site.address(),distance));
            distances.add(new DistanceBetweenSites(site.address(),other.address(),distance));
        }


        return distances;
    }

    private Map<Pair<String,String>, Double> getDistanceMatrix(Site site, List<Site> otherSites){
        return  null;
    }

    private Point getPoint(Site site) throws TransportException {
        LocationByQueryResponse queryResponse;
        try {
            queryResponse = BingAPI.locationByQuery(site.address());
        } catch (IOException e) {
            throw new TransportException(e.getMessage(), e);
        }
        Resource[] resources = queryResponse.resourceSets()[0].resources();
        if (resources.length != 1) {
            throw new TransportException("Could not find site or found multiple sites");
        }
        return resources[0].point();
    }
}
