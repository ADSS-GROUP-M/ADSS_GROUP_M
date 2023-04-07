package Backend.BusinessLayer.DeliveryAgreements;

public abstract class DeliveryAgreement {
    private boolean havaTransport;

    public DeliveryAgreement(boolean havaTransport){
        this.havaTransport = havaTransport;
    }
    public void setHavaTransport(boolean havaTransport){this.havaTransport = havaTransport;}
}
