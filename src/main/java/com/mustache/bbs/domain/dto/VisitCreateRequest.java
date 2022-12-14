package com.mustache.bbs.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VisitCreateRequest {
    private Integer hospitalId;
    private String disease;
    private float amount;
}
