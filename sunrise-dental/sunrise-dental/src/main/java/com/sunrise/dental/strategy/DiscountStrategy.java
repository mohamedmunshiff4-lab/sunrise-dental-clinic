package com.sunrise.dental.strategy;

/**
 * STRATEGY PATTERN: defines a family of interchangeable discount algorithms.
 * The billing service picks the strategy at runtime (normal vs loyalty patient).
 */
public interface DiscountStrategy {
    /** @return discount percentage to apply, e.g. 0.10 means 10% */
    double discountPercentage();
    String getName();
}
