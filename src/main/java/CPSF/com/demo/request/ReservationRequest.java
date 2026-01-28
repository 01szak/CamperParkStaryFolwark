package CPSF.com.demo.request;

import CPSF.com.demo.entity.Guest;

public record ReservationRequest(
        String checkin,
        String checkout,
        Guest guest,
        String camperPlaceIndex,
        Boolean paid
) {
}
