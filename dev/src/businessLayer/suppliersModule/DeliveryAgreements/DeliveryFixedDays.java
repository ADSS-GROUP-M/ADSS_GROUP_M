package businessLayer.suppliersModule.DeliveryAgreements;

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

    @Override
    public String toString() {
        return super.toString() + "\n\t\t\tdays of the week: " + daysOfTheWeek.toString().substring(1,daysOfTheWeek.toString().length() - 1);
    }
    public String toString2() {
        return super.toString2() + "\n\tdays of the week: " + daysOfTheWeek.toString().substring(1,daysOfTheWeek.toString().length() - 1);
    }

    public List<Integer> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }
}
