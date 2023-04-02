package BusinessLayer.DeliveryAgreements;

import java.util.List;

public class DeliveryFixedDays extends DeliveryAgreement{
    /***
     * a List that contains all the days the supplier arrives
     */
    private List<Integer> daysOfTheWeek;
    public DeliveryFixedDays(boolean havaTransport) {
        super(havaTransport);
    }
}
