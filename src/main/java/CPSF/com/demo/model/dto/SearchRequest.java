package CPSF.com.demo.model.dto;

import CPSF.com.demo.service.core.SearchCriteria;
import jakarta.annotation.Nullable;

public record SearchRequest(@Nullable SearchCriteria[] searchCriteria) {}
