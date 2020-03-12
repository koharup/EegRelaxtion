package com.sano.picchi.muse.model;

import java.util.Arrays;

import static com.sano.picchi.muse.constants.AppConstants.defaultPredictValue;
import static com.sano.picchi.muse.constants.AppConstants.smoothPredict;

public class MuseResults {

    double[] predict;
    int pointer;

    public MuseResults() {
        pointer = 0;
        predict = new double[smoothPredict];
        Arrays.fill(predict, defaultPredictValue);
    }

    public void add(double prob) {
        predict[pointer] = prob;
        pointer = (pointer + 1) % smoothPredict;
    }

    public double getResult() {
        double sum = 0;
        for (double prob : predict) {
            sum += (prob / (double) smoothPredict);
        }

        return sum;
    }

}
