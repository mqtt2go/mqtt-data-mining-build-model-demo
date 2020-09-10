package com.vutbr.feec.utko.demo.service;

import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FindRangePercentilesService {

    public double[] findValueWithPercentileOne(List<BigDecimal> data, Integer lowerPercentile, Integer upperPercentile) {
        // Statistics, other attributes
        double[] flattenDataArray = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            flattenDataArray[i] = data.get(i).doubleValue();
        }
        return findPercentile(flattenDataArray, lowerPercentile, upperPercentile);
    }

    public double[] findValueWithPercentileOne(Integer lowerPercentile, Integer upperPercentile, List<Double> data) {
        // Statistics, other attributes
        double[] flattenDataArray = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            flattenDataArray[i] = data.get(i);
        }
        return findPercentile(flattenDataArray, lowerPercentile, upperPercentile);
    }

    private double[] findPercentile(double[] flattenDataArray, Integer lowerPercentile, Integer upperPercentile) {
        // Stats
        double lowerValue = StatUtils.percentile(flattenDataArray, lowerPercentile);
        double upperValue = StatUtils.percentile(flattenDataArray, upperPercentile);

        double[] lowerAndUpperValues = new double[2];
        lowerAndUpperValues[0] = lowerValue;
        lowerAndUpperValues[1] = upperValue;

        return lowerAndUpperValues;
    }

    public static void main(String[] args) {
        List<BigDecimal> decimals = new ArrayList<>();
        for (int i = 0; i < 10203; i++) {
            decimals.add(new BigDecimal(i));
        }

        FindRangePercentilesService p = new FindRangePercentilesService();
        double[] rangeValues = p.findValueWithPercentileOne(decimals, 1, 99);
        System.out.println("Lower value: " + rangeValues[0]);
        System.out.println("Power value: " + rangeValues[1]);

        List<BigDecimal> states = new ArrayList<>();
        states.add(new BigDecimal(1));
        states.add(new BigDecimal(1));
        double[] rangeValues2 = p.findValueWithPercentileOne(states, 1, 99);
        System.out.println("Lower value: " + rangeValues2[0]);
        System.out.println("Power value: " + rangeValues2[1]);

    }

}
