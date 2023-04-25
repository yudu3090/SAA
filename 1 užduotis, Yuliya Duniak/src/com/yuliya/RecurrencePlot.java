package com.yuliya;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RecurrencePlot {
    /**
     * skaitome signalo reiksmiu masyva is failo
     *
     * @param timeSeriesFileIndex failo indexas
     * @return signalo reiksmiu masyvas
     * @throws IOException klaida
     */
    public static Double[] readTimeSeries(Integer timeSeriesFileIndex) throws IOException {
        ArrayList<Double> timeSeries = new ArrayList<>();
        BufferedReader timeSeriesBufferedReader = new BufferedReader(new FileReader("resources/" + timeSeriesFileIndex));
        String line;
        while ((line = timeSeriesBufferedReader.readLine()) != null) {
            timeSeries.add(Double.parseDouble(line));
        }
        return timeSeries.toArray(new Double[0]);
    }

    /**
     * formuoja vektorius is signalo reiksmiu
     *
     * @param timeSeries signalo reiksmiu masyvas
     * @param D          D
     * @param d          d
     * @return kortezu masyvas
     */
    public static Double[][] collectTimeSeriesCorteges(Double[] timeSeries, Integer D, Integer d) {
        Double[][] timerSeriesVectors = new Double[timeSeries.length - (D - 1) * d][D];
        for (int i = 0; i < timerSeriesVectors.length; i++) {
            for (int j = 0; j < D; j++) {
                timerSeriesVectors[i][j] = timeSeries[i + j * d];
            }
        }
        return timerSeriesVectors;
    }

    /**
     * skaiciuoja tarpus "kiekvienas su kiekvienu"
     *
     * @param timeSeriesVectors signalo reiksmiu kortezo masayvas
     * @return tarpas
     */
    public static Double[][] calcTimeSeriesCortegesDifferences(Double[][] timeSeriesVectors, LenCalculationMethod lenCalculationMethod) {
        Double[][] timeSeriesDifferences = new Double[timeSeriesVectors.length][timeSeriesVectors.length];
        for (int i = 0; i < timeSeriesDifferences.length; i++) {
            for (int j = 0; j < timeSeriesDifferences[i].length; j++) {
                double[] differences = new double[timeSeriesVectors[i].length];
                for (int k = 0; k < differences.length; k++) {
                    differences[k] = timeSeriesVectors[j][k] - timeSeriesVectors[i][k];
                }
                timeSeriesDifferences[i][j] = lenCalculationMethod.calc(differences);
            }
        }
        return timeSeriesDifferences;
    }

    /**
     * isskaiciuoja toleriacijos parametra
     *
     * @param timeSeriesCortegesDifferences skirtumu tarp kortezu matrica
     * @param blackPointsPercent            juodu tasku procentas
     * @param blackPointsPercentEps         paklaida
     * @return isskaiciuoja toleriacijos parametra
     */
    public static Double calcR(Double[][] timeSeriesCortegesDifferences, Double blackPointsPercent, Double blackPointsPercentEps) {
        double fx0 = timeSeriesCortegesDifferences[0][0];
        double fx1 = timeSeriesCortegesDifferences[0][0];
        double fx;
        for (Double[] timeSeriesCortegesDifference : timeSeriesCortegesDifferences) {
            for (Double jDouble : timeSeriesCortegesDifference) {
                fx0 = Math.min(jDouble, fx0);
                fx1 = Math.max(jDouble, fx1);
            }
        }
        double x0 = 0;
        double x1 = 1;
        double x;
        do {
            fx = linearInterpolation(x0, x1, fx0, fx1, blackPointsPercent);
            double blackPointsCount = 0;
            for (Double[] timeSeriesCortegesDifference : timeSeriesCortegesDifferences) {
                for (Double jDouble : timeSeriesCortegesDifference) {
                    blackPointsCount += jDouble <= fx ? 1 : 0;
                }
            }
            x = blackPointsCount / (timeSeriesCortegesDifferences.length * timeSeriesCortegesDifferences.length);
            if (x0 == x) {
                JOptionPane.showMessageDialog(null, "Nerasta !");
                return null;
            }
            fx1 = fx0;
            x1 = x0;
            fx0 = fx;
            x0 = x;
        } while (Math.abs(blackPointsPercent - x) > blackPointsPercentEps);
        return fx;
    }

    /**
     *
     * @param x0  x0
     * @param x1  x1
     * @param fx0 fx0
     * @param fx1 fx1
     * @param x   x
     * @return apitiklis ieskomas taskas
     */
    private static double linearInterpolation(double x0, double x1, double fx0, double fx1, double x) {
        return fx0 + (fx1 - fx0) / (x1 - x0) * (x - x0);
    }

    /**
     * formuoja paveiksliuka
     *
     * @param timeSeriesDifferences time Series Differences matrica
     * @param r                     r parametras (toleracija)
     * @return paveikliukas
     */
    public static BufferedImage buildRecurrencePlotImage(Double[][] timeSeriesDifferences, Double r) {
        BufferedImage recurrencePlotImage = new BufferedImage(timeSeriesDifferences.length, timeSeriesDifferences[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < recurrencePlotImage.getWidth(); i++) {
            for (int j = 0; j < recurrencePlotImage.getHeight(); j++) {
                recurrencePlotImage.setRGB(i, j, timeSeriesDifferences[i][j] <= r ? 0 : 0xFFFFFF);
            }
        }
        return recurrencePlotImage;
    }
}
