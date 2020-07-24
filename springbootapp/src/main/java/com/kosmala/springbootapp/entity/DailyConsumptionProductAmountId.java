package com.kosmala.springbootapp.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.awt.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@SuppressWarnings("serial")
@Embeddable
public class DailyConsumptionProductAmountId implements Serializable
{
    private Long dailyId;
    private Long productId;
}
