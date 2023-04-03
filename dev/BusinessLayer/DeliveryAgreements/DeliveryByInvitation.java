package BusinessLayer.DeliveryAgreements;

public class DeliveryByInvitation extends DeliveryAgreement{
    /***
     * number of days for the order to arrive
     */
    private int numberOfDays;
    public DeliveryByInvitation(boolean havaTransport, int numberOfDays) {
        super(havaTransport);
        this.numberOfDays = numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
