package com.alok.home.email.parser.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MonthInvestmentRecord {

    private String head;
    private Integer asOnInvestment;
    private Integer asOnValue;
    private Integer monthInvestment;
    private Integer monthReturn;
}
