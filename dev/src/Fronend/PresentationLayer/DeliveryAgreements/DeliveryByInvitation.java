package Fronend.PresentationLayer.DeliveryAgreements;

public class DeliveryByInvitation extends DeliveryAgreement {
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

    @Override
    public String toString() {
        return super.toString() + "\n\t\t\tnumber of days for order to arrive: " + numberOfDays;
    }

    public String toString2() {
        return super.toString() + "\n\tnumber of days for order to arrive: " + numberOfDays;
    }
}
