package com.sunrise.dental.strategy;

import org.springframework.stereotype.Component;

@Component
public class LoyaltyDiscountStrategy implements DiscountStrategy {
    @Override
    public double discountPercentage() { return 0.10; } // 10% for regular patients

    @Override
    public String getName() { return "Loyalty Discount (10%)"; }
}
