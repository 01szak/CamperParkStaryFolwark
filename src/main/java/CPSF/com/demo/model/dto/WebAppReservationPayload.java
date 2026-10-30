package CPSF.com.demo.model.dto;

import CPSF.com.demo.model.constant.SupportedLocale;

public record WebAppReservationPayload(ReservationDTO reservationDTO, SupportedLocale locale){}
