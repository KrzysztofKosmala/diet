package com.kosmala.springbootapp.entity;

import lombok.*;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;

import javax.persistence.*;
import java.util.*;
import java.io.Serializable;


@Entity
    @Table(name = "daily_consumption_product_amount")
    @Getter
    @Setter
    @RequiredArgsConstructor
    @ToString
@EqualsAndHashCode
    public class DailyConsumptionProductAmount implements Serializable
    {
        @EmbeddedId
        private DailyConsumptionProductAmountId id = new DailyConsumptionProductAmountId();

        @ManyToOne(cascade = CascadeType.MERGE)
        @MapsId("dailyId")
        private DailyConsumption daily;


        @ManyToOne(cascade = CascadeType.PERSIST)
        @MapsId("productId")
        private Product product;


        @ElementCollection
        List<MealAmount> mealAmounts = new ArrayList<>();


/*        @Column(name = "amount")
        private double amount;

        @Column(name = "mealNumber")
        private int mealNumber;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DailyConsumptionProductAmount)) return false;
            DailyConsumptionProductAmount that = (DailyConsumptionProductAmount) o;
            return Objects.equals(daily.getId(), that.daily.getId()) &&
                    Objects.equals(product.getName(), that.product.getName()) &&
                    Objects.equals(mealNumber, that.mealNumber) &&
                    Objects.equals(amount, that.amount);
        }
*/

    }

