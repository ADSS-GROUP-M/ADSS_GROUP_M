package Backend.BusinessLayer.SuppliersModule.DeliveryAgreements;

public class DeliveryByInvitation extends DeliveryAgreement{
    /***
     * number of days for the order to arrive
     */
    public DeliveryByInvitation(boolean havaTransport, int numberOfDays) {
        super(havaTransport, true, null, numberOfDays);
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\t\t\tnumber of days for order to arrive: " + numberOfDays;
    }

    public String toString2() {
        return super.toString2() + "\n\tnumber of days for order to arrive: " + numberOfDays;
    }
}
