package presentationLayer.suppliersModule.DeliveryAgreements;

public abstract class DeliveryAgreement {
    private boolean havaTransport;

    public DeliveryAgreement(boolean havaTransport){
        this.havaTransport = havaTransport;
    }
    public void setHavaTransport(boolean havaTransport){this.havaTransport = havaTransport;}

    @Override
    public String toString() {
        return "DELIVERY AGREEMENT:\n\t\t\ttransport" + havaTransport;
    }

    public String toString2(){
        return "DELIVERY AGREEMENT:\n\ttransport" + havaTransport;
    }
}
