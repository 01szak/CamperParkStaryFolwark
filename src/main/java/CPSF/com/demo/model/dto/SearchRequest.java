package CPSF.com.demo.model.dto;

import CPSF.com.demo.service.core.SearchCriteria;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

public record SearchRequest(@Nullable @Valid SearchCriteria[] searchCriteria) {}
