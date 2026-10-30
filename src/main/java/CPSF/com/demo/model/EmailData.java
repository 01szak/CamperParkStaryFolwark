package CPSF.com.demo.model;

import CPSF.com.demo.model.constant.SupportedLocale;
import CPSF.com.demo.model.dto.GuestDTO;

public record EmailData(SupportedLocale locale, GuestDTO guest, Object additionalPayload) {}
