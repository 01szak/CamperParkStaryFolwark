package CPSF.com.demo.model;

import CPSF.com.demo.model.dto.GuestDTO;
import CPSF.com.demo.model.dto.ReservationDTO;

public record EmailData(GuestDTO guest, Object additionalPayload) {}
