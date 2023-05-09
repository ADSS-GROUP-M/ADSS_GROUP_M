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

        Map<Pair<String,String>,Pair<Double,Double>> travelMatrix = getTravelMatrix(site,sites);

        List<DistanceBetweenSites> distances = new LinkedList<>();

        for(var entry : travelMatrix.entrySet()){
            distances.add(new DistanceBetweenSites(
                    entry.getKey().getKey(), //source
                    entry.getKey().getValue(), //destination
                    entry.getValue().getKey(), //distance
                    entry.getValue().getValue()) //duration
            );
        }
        return distances;
    }

    public Point getCoordinates(Site site) throws TransportException {
        return getPoint(site);
    }

    public Map<Pair<String,String>, Pair<Double,Double>> getTravelMatrix(Site site, List<Site> otherSites) throws TransportException {

        List<Pair<Point,Point>> points = new LinkedList<>();
        Point newSitePoint = new Point(site.address(), new double[]{site.latitude(),site.longitude()});
        for(Site other : otherSites){
            Point otherSitePoint = new Point(other.address(), new double[]{other.latitude(),other.longitude()});
            points.add(new Pair<>(newSitePoint,otherSitePoint));
            points.add(new Pair<>(otherSitePoint,newSitePoint));
        }
        DistanceMatrixResponse response;
        try {
            response = BingAPI.distanceMatrix(points);
        } catch (IOException e) {
            throw new TransportException(e.getMessage(), e);
        }
        Result[] results = Arrays.stream(response.resourceSets()[0].resources()[0].results())
                .filter(result -> result.originIndex() == result.destinationIndex())
                .toArray(Result[]::new);

        Map<Pair<String,String>, Pair<Double,Double>> distances = new HashMap<>();
        ListIterator<Pair<Point,Point>> pointsIterator = points.listIterator();
        while(pointsIterator.hasNext()){
            Pair<Point,Point> pair = pointsIterator.next();
            distances.put(new Pair<>(pair.getKey().address(),pair.getValue().address()),
                    new Pair<>(results[pointsIterator.previousIndex()].travelDuration(),results[pointsIterator.previousIndex()].travelDistance()));
        }

        distances.put(new Pair<>(site.address(),site.address()),new Pair<>(0.0,0.0));
        return distances;
    }

    private Point getPoint(Site site) throws TransportException {
        LocationByQueryResponse queryResponse;
        try {
            queryResponse = BingAPI.locationByQuery(site.address());
        } catch (IOException e) {
            throw new TransportException(e.getMessage(), e);
        }
        LocationResource[] locationResources = queryResponse.resourceSets()[0].resources();
        if (locationResources.length != 1) {
            throw new TransportException("Could not find site or found multiple sites");
        }
        return locationResources[0].point();
    }
}
