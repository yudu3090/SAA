package com.yuliya;

/**
 * Metrikos
 */
@FunctionalInterface
public interface LenCalculationMethod {

    double calc(double[] differences);

    /**
     * Euklidas
     */
    final class EuclidCalculationMethod implements LenCalculationMethod {
        @Override
        public double calc(double[] differences) {
            double squareLen = 0;
            for (double difference : differences) {
                squareLen += Math.pow(difference, 2);
            }
            return Math.sqrt(squareLen);
        }
    }

    /**
     * Manhetenas
     */
    final class TaxicabCalculationMethod implements LenCalculationMethod {
        @Override
        public double calc(double[] differences) {
            double len = 0;
            for (double difference : differences) {
                len += Math.abs(difference);
            }
            return len;
        }
    }

    /**
     * Maksimumas
     */
    final class InfinityCalculationMethod implements LenCalculationMethod {
        @Override
        public double calc(double[] differences) {
            double len = 0;
            for (double difference : differences) {
                len = Math.max(Math.abs(difference), len);
            }
            return len;
        }
    }
}
