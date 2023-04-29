package objects.transportObjects;

import javafx.util.Pair;

import java.time.LocalTime;
import java.util.*;

public class DeliveryRoute {

    private static final int AVERAGE_SPEED = 80;
    private static final int AVERAGE_TIME_PER_VISIT = 30;

    private final String source;
    private final LocalTime departureTime;
    private final List<String> destinations;
    private final Set<String> destinationsSet;
    private final Map<Pair<String,String>,Integer> distancesBetweenDestinations;

    public DeliveryRoute(String source,
                         LocalTime departureTime,
                         List<String> destinations,
                         Map<Pair<String,String>,Integer> distancesBetweenDestinations){
        this.source = source;
        this.departureTime = departureTime;
        this.destinations = destinations;
        this.destinationsSet = new HashSet<>(destinations);
        this.distancesBetweenDestinations = distancesBetweenDestinations;
    }

    public LocalTime getEstimatedTimeOfArrival(String destination) {
        if(destinationsSet.contains(destination) == false){
            return null;
        }

        List<String> destinationsInRoute = destinations.stream()
                .takeWhile(dest -> dest.equals(destination) == false)
                .toList();

        ListIterator<String> destinationsIterator = destinationsInRoute.listIterator();

        double minutes = 0;

        String curr = source;
        String next;
        if(destinationsIterator.hasNext()){
            next = destinationsIterator.next();
            minutes += (double)distancesBetweenDestinations.get(new Pair<>(curr, next)) / AVERAGE_SPEED;
        }

        while(destinationsIterator.hasNext()){
            minutes += AVERAGE_TIME_PER_VISIT;
            next = destinationsIterator.next();
            minutes += (double)distancesBetweenDestinations.get(new Pair<>(curr, next)) / AVERAGE_SPEED;
            curr = next;
        }

        return departureTime.plusMinutes((long)minutes);
    }





}
