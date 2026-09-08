package com.sunrise.dental.strategy;

import org.springframework.stereotype.Component;

@Component
public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double discountPercentage() { return 0.0; }

    @Override
    public String getName() { return "No Discount"; }
}
