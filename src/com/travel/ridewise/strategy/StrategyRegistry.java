package com.travel.ridewise.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StrategyRegistry {
    private static final Map<String, RideMatchingStrategy> matchingStrategies = new ConcurrentHashMap<>();
    private static final Map<String, FareStrategy> fareStrategies = new ConcurrentHashMap<>();

    static {
        matchingStrategies.put("NEAREST", new NearestDriverStrategy());
        matchingStrategies.put("LEAST_ACTIVE", new LeastActiveDriverStrategy());

        fareStrategies.put("DEFAULT", new DefaultFareStrategy());
        fareStrategies.put("PEAK_HOUR", new PeakHourFareStrategy());
    }

    public static RideMatchingStrategy getMatchingStrategy(String name) {
        RideMatchingStrategy strategy = matchingStrategies.get(name.toUpperCase().replace(" ", "_"));
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown matching strategy: " + name);
        }
        return strategy;
    }

    public static FareStrategy getFareStrategy(String name) {
        FareStrategy strategy = fareStrategies.get(name.toUpperCase().replace(" ", "_"));
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown fare strategy: " + name);
        }
        return strategy;
    }
}
