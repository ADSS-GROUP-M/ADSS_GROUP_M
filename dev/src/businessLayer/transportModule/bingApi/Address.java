package businessLayer.transportModule.bingApi;

public record Address(
        String addressLine,
        String adminDistrict,
        String adminDistrict2,
        String countryRegion,
        String formattedAddress,
        String locality,
        String postalCode
) {
}
