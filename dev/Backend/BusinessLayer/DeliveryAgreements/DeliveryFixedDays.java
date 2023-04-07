package Backend.BusinessLayer.DeliveryAgreements;

import java.util.List;

public class DeliveryFixedDays extends DeliveryAgreement{
    /***
     * a List that contains all the days the supplier arrives
     */
    private List<Integer> daysOfTheWeek;
    public DeliveryFixedDays(boolean havaTransport, List<Integer> daysOfTheWeek) {
        super(havaTransport);
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public void addDay(int day){
        if (!daysOfTheWeek.contains(day))
            daysOfTheWeek.add(day);
    }
    public void removeDay(int day){
        daysOfTheWeek.remove(day);
    }
}
