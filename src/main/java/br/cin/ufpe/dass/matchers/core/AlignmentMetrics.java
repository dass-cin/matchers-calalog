package br.cin.ufpe.dass.matchers.core;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.net.URI;

/**
 * Created by diego on 08/03/17.
 */
public class AlignmentMetrics {

    private double precision;

    private double recall;

    private double fmeasure;

    //Accuracy
    private double overall;

    private long correspondencesFound;

    private long correspondencesExpected;

    private long truePositives;

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getFmeasure() {
        return fmeasure;
    }

    public void setFmeasure(double fmeasure) {
        this.fmeasure = fmeasure;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }

    public long getCorrespondencesFound() {
        return correspondencesFound;
    }

    public void setCorrespondencesFound(long correspondencesFound) {
        this.correspondencesFound = correspondencesFound;
    }

    public long getCorrespondencesExpected() {
        return correspondencesExpected;
    }

    public void setCorrespondencesExpected(long correspondencesExpected) {
        this.correspondencesExpected = correspondencesExpected;
    }

    public long getTruePositives() {
        return truePositives;
    }

    public void setTruePositives(long truePositives) {
        this.truePositives = truePositives;
    }
}
