package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.*;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import javafx.util.Pair;
import objects.transportObjects.Site;
import utils.transportUtils.TransportException;

import java.io.IOException;
import java.util.*;

public class SitesDistancesController {

    private final BingAPI bingAPI;

    public SitesDistancesController(BingAPI bingAPI) {
        this.bingAPI = bingAPI;
    }

    public Point getCoordinates(Site site) throws TransportException {
        LocationByQueryResponse queryResponse;
        try {
            queryResponse = bingAPI.locationByQuery(site.address());
        } catch (IOException e) {
            throw new TransportException(e.getMessage(), e);
        }
        LocationResource[] locationResources = queryResponse.resourceSets()[0].resources();
        if (locationResources.length != 1) {
            throw new TransportException("Could not find site or found multiple sites");
        }
        return locationResources[0].point();
    }

    public List<DistanceBetweenSites> createDistanceObjects(Site site ,List<Site> sites) throws TransportException {

        Map<Pair<String,String>,Pair<Double,Double>> travelMatrix = getTravelData(site,sites);

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

    /**
     * Returns a map of all the distances between the given site and the other sites.
     * @implNote this implementation calculates efficiently but requires a lot of different API calls.
     * this is the method that should be used when adding a single new site.
     */
    public Map<Pair<String,String>, Pair<Double,Double>> getTravelData(Site site, List<Site> otherSites) throws TransportException {

        Map<Pair<String,String>, Pair<Double,Double>> distances = new HashMap<>();

        Point newSitePoint = new Point(site.address(), new double[]{site.latitude(),site.longitude()});

        for(Site other : otherSites){
            Point otherSitePoint = new Point(other.address(), new double[]{other.latitude(),other.longitude()});

            DistanceMatrixResponse response;
            try {
                response = bingAPI.distanceMatrix(List.of(newSitePoint,otherSitePoint));
            } catch (IOException e) {
                throw new TransportException(e.getMessage(), e);
            }
            Result[] results = Arrays.stream(response.resourceSets()[0].resources()[0].results())
                    .filter(result -> result.originIndex() == result.destinationIndex())
                    .toArray(Result[]::new);

            distances.put(new Pair<>(site.address(),other.address()),
                    new Pair<>(results[0].travelDistance(),results[0].travelDuration()));
            distances.put(new Pair<>(other.address(),site.address()),
                    new Pair<>(results[1].travelDistance(),results[1].travelDuration()));
        }

        distances.put(new Pair<>(site.address(),site.address()),new Pair<>(0.0,0.0));
        return distances;
    }

    public List<DistanceBetweenSites> createAllDistanceObjectsFirstTimeLoad(List<Site> sites) throws IOException {
        Map<Pair<String,String>, Pair<Double,Double>> data = new HashMap<>();

        Point[] points = sites.stream()
                .map(site -> new Point(site.address(), new double[]{site.latitude(),site.longitude()}))
                .toArray(Point[]::new);

        DistanceMatrixResponse response = bingAPI.distanceMatrix(Arrays.stream(points).toList());
        Result[] results = response.resourceSets()[0].resources()[0].results();
        Arrays.stream(results).forEach(result -> {
            int originIndex = result.originIndex();
            int destinationIndex = result.destinationIndex();
            data.put(new Pair<>(points[originIndex].address(),points[destinationIndex].address()),
                    new Pair<>(result.travelDistance(),result.travelDuration()));
        });

        List<DistanceBetweenSites> distances = new LinkedList<>();

        for(var entry : data.entrySet()){
            distances.add(new DistanceBetweenSites(
                    entry.getKey().getKey(), //source
                    entry.getKey().getValue(), //destination
                    entry.getValue().getKey(), //distance
                    entry.getValue().getValue()) //duration
            );
        }
        return distances;
    }
}
