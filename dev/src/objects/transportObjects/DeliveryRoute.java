package objects.transportObjects;

import javafx.util.Pair;

import java.time.LocalTime;
import java.util.*;

public class DeliveryRoute {

    private static final int AVERAGE_SPEED = 80;
    private static final long AVERAGE_TIME_PER_VISIT = 30;

    private final String source;
    private final LocalTime departureHour;

    private final List<String> destinations;

    private final Set<String> destinationsSet;
    private final Map<String,Integer> destinations_itemListIds;

    private Map<Pair<String,String>,Integer> distancesBetweenDestinations;

    public DeliveryRoute(String source,
                         LocalTime departureHour,
                         List<String> destinations,
                         Map<String, Integer> destinations_itemListIds){
        this.source = source;
        this.departureHour = departureHour;
        this.destinations = destinations;
        this.destinationsSet = new HashSet<>(destinations);
        this.destinations_itemListIds = destinations_itemListIds;
    }

    public LocalTime getEstimatedTimeOfArrival(String destination) {

        if(distancesBetweenDestinations == null){
            throw new RuntimeException("distances between destinations not initialized");
        }

        if(destinationsSet.contains(destination) == false){
            throw new NoSuchElementException("destination not in route");
        }

        List<String> destinationsInRoute = destinations.stream()
                .takeWhile(dest -> dest.equals(destination) == false)
                .toList();

        ListIterator<String> destinationsIterator = destinationsInRoute.listIterator();

        long minutes = 0;

        String curr = source;
        String next;
        if(destinationsIterator.hasNext()){
            next = destinationsIterator.next();
            minutes += (long)distancesBetweenDestinations.get(new Pair<>(curr, next)) / AVERAGE_SPEED;
        }

        while(destinationsIterator.hasNext()){
            minutes += AVERAGE_TIME_PER_VISIT;
            next = destinationsIterator.next();
            minutes += (long)distancesBetweenDestinations.get(new Pair<>(curr, next)) / AVERAGE_SPEED;
            curr = next;
        }

        return departureHour.plusMinutes(minutes);
    }

    public List<Pair<String,LocalTime>> getAllEstimatedArrivalTimes(){

        if(distancesBetweenDestinations == null){
            throw new RuntimeException("distances between destinations not initialized");
        }

        List<Pair<String,LocalTime>> route = new ArrayList<>();
        route.add(new Pair<>(source, departureHour));

        ListIterator<String> destinationsIterator = destinations.listIterator();
        LocalTime time = departureHour;
        String curr = source;
        String next;

        if(destinationsIterator.hasNext()){
            next = destinationsIterator.next();
            time = addTravelTime(time, curr, next);
        }

        while(destinationsIterator.hasNext()){
            time = time.plusMinutes(AVERAGE_TIME_PER_VISIT);
            next = destinationsIterator.next();
            time = addTravelTime(time, curr, next);
            curr = next;
        }

        return route;
    }

    public void initializeDistances(Map<Pair<String,String>,Integer> distancesBetweenDestinations){

        if(distancesBetweenDestinations == null){
            throw new NullPointerException("distancesBetweenDestinations is null");
        }

        // check there is a distance between each following pair of destinations
        for(int i = 0; i < destinations.size() - 1; i++){
            String curr = destinations.get(i);
            String next = destinations.get(i + 1);
            if(distancesBetweenDestinations.containsKey(new Pair<>(curr, next)) == false){
                throw new RuntimeException("missing distance between " + curr + " and " + next);
            }
        }

        this.distancesBetweenDestinations = new HashMap<>();
        this.distancesBetweenDestinations.putAll(distancesBetweenDestinations);
    }

    private LocalTime addTravelTime(LocalTime time, String curr, String next) {
        time = time.plusMinutes((long)distancesBetweenDestinations.get(new Pair<>(curr, next)) / AVERAGE_SPEED);
        return time;
    }

    public String source() {
        return source;
    }

    public List<String> destinations() {
        return destinations;
    }

    public Map<String, Integer> itemLists() {
        return destinations_itemListIds;
    }

    public Map<Pair<String, String>, Integer> distancesBetweenDestinations() {
        return distancesBetweenDestinations;
    }
}
