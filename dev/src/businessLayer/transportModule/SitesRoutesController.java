package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.*;
import dataAccessLayer.dalAssociationClasses.transportModule.SiteRoute;
import dataAccessLayer.transportModule.SitesRoutesDAO;
import exceptions.DalException;
import exceptions.TransportException;
import javafx.util.Pair;
import objects.transportObjects.Site;

import java.io.IOException;
import java.util.*;

public class SitesRoutesController {

    private final BingAPI bingAPI;

    private final SitesRoutesDAO dao;

    public SitesRoutesController(BingAPI bingAPI, SitesRoutesDAO dao) {
        this.bingAPI = bingAPI;
        this.dao = dao;
    }

    public Point getCoordinates(Site site) throws TransportException {
        LocationByQueryResponse queryResponse;
        queryResponse = bingAPI.locationByQuery(site.address());
        LocationResource[] locationResources = queryResponse.resourceSets()[0].resources();
        if (locationResources.length != 1) {
            throw new TransportException("Could not find site or found multiple sites");
        }
        return locationResources[0].point();
    }

    public void addRoutes(Site site , List<Site> otherSites) throws TransportException {

        Map<Pair<String,String>,Pair<Double,Double>> travelMatrix = getTravelData(site,otherSites);

        List<SiteRoute> routes = new LinkedList<>();

        for(var entry : travelMatrix.entrySet()){
            routes.add(new SiteRoute(
                    entry.getKey().getKey(), //source
                    entry.getKey().getValue(), //destination
                    entry.getValue().getKey(), //distance
                    entry.getValue().getValue()) //duration
            );
        }
        try {
            dao.insertAll(routes);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(), e);
        }
    }

    /**
     * @return  a map of all the routes between the given site and the other sites.
     * The map is of the form <br/> &lt;source,destination&gt; -> &lt;distance,duration&gt;.
     * @implNote this implementation calculates efficiently but requires a lot of different API calls.
     * this is the method that should be used when adding a single new site.
     */
    public Map<Pair<String,String>, Pair<Double,Double>> getTravelData(Site site, List<Site> otherSites) throws TransportException {

        Map<Pair<String,String>, Pair<Double,Double>> routes = new HashMap<>();

        Point newSitePoint = new Point(site.address(), new double[]{site.latitude(),site.longitude()});

        for(Site other : otherSites){
            Point otherSitePoint = new Point(other.address(), new double[]{other.latitude(),other.longitude()});

            DistanceMatrixResponse response;
            response = bingAPI.distanceMatrix(List.of(newSitePoint,otherSitePoint));
            Result[] results = Arrays.stream(response.resourceSets()[0].resources()[0].results())
                    .filter(result -> result.originIndex() != result.destinationIndex())
                    .toArray(Result[]::new);

            routes.put(new Pair<>(site.address(),other.address()),
                    new Pair<>(results[0].travelDistance(),results[0].travelDuration()));
            routes.put(new Pair<>(other.address(),site.address()),
                    new Pair<>(results[1].travelDistance(),results[1].travelDuration()));
        }

        routes.put(new Pair<>(site.address(),site.address()),new Pair<>(0.0,0.0));
        return routes;
    }

    /**
     * This method is used to create all the route objects for the first time the program is run.
     */
    public void addAllRouteObjectsFirstTimeLoad(List<Site> sites) throws TransportException {
        Map<Pair<String,String>, Pair<Double,Double>> data = new HashMap<>();

        Point[] points = sites.stream()
                .map(site -> new Point(site.address(), new double[]{site.latitude(),site.longitude()}))
                .toArray(Point[]::new);

        DistanceMatrixResponse response = null;
        response = bingAPI.distanceMatrix(Arrays.stream(points).toList());
        Result[] results = response.resourceSets()[0].resources()[0].results();
        Arrays.stream(results).forEach(result -> {
            int originIndex = result.originIndex();
            int destinationIndex = result.destinationIndex();
            data.put(new Pair<>(points[originIndex].address(),points[destinationIndex].address()),
                    new Pair<>(result.travelDistance(),result.travelDuration()));
        });

        List<SiteRoute> routes = new LinkedList<>();

        for(var entry : data.entrySet()){
            routes.add(new SiteRoute(
                    entry.getKey().getKey(), //source
                    entry.getKey().getValue(), //destination
                    entry.getValue().getKey(), //distance
                    entry.getValue().getValue()) //duration
            );
        }
        try {
            dao.insertAll(routes);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(), e);
        }
    }

    public Map<Pair<String,String>,Double> buildSitesTravelTimes(List<Site> route) throws TransportException {

        HashMap<Pair<String,String>,Double> distances = new HashMap<>();
        ListIterator<Site> destinationsIterator = route.listIterator();
        Site curr = destinationsIterator.next();
        Site next;

        // map distances between following sites
        while (destinationsIterator.hasNext()) {
            next = destinationsIterator.next();

            String currAddress = curr.address();
            String nextAddress = next.address();

            SiteRoute lookUpObject = SiteRoute.getLookupObject(currAddress,nextAddress);
            double distance;
            try {
                distance = dao.select(lookUpObject).duration();
            } catch (DalException e) {
                throw new TransportException(e.getMessage(),e);
            }
            distances.put(new Pair<>(curr.name(),next.name()),distance);
            curr = next;
        }
        return distances;
    }
}
