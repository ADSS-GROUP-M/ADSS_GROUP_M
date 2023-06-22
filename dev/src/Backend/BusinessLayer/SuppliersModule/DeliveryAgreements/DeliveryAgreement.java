package Backend.BusinessLayer.SuppliersModule.DeliveryAgreements;

import java.util.List;

public class DeliveryAgreement {
    private boolean havaTransport;
    /**
     * for json
     */
    private boolean deliveryByInvitation;
    protected List<Integer> daysOfTheWeek;
    protected int numberOfDays;

    public DeliveryAgreement(boolean havaTransport, boolean deliveryByInvitation, List<Integer> daysOfTheWeek, int numberOfDays){
        this.havaTransport = havaTransport;
        this.deliveryByInvitation = deliveryByInvitation;
        this.numberOfDays = numberOfDays;
        this.daysOfTheWeek = daysOfTheWeek;
    }
    public void setHavaTransport(boolean havaTransport){this.havaTransport = havaTransport;}

    public boolean getHavaTransport() {
        return havaTransport;
    }

    @Override
    public String toString() {
        return "DELIVERY AGREEMENT:\n\t\t\ttransport: " + havaTransport;
    }

    public String toString2(){
        return "DELIVERY AGREEMENT:\n\ttransport: " + havaTransport;
    }

    public String toString3(){
        String res = "transport: " + havaTransport;
        if(deliveryByInvitation){
            return res + ", number of days for order to arrive: " + numberOfDays;
        }
        else {
            return res + ", days of the week: " + daysOfTheWeek.toString().substring(1,daysOfTheWeek.toString().length() - 1);
        }
    }
}
