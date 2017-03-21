package br.cin.ufpe.dass.matchers.core;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by diego on 08/03/17.
 */
public class AlignmentEvaluation {

    private Alignment evaluatedAlignment;

    private Alignment referenceAlignment; //@TODO implementar carregamento do arquivo para o objeto Alignment

    private double precision;

    private double recall;

    private double fmeasure;

    @DBRef
    private Matcher matcher;

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

    public Alignment getEvaluatedAlignment() {
        return evaluatedAlignment;
    }

    public void setEvaluatedAlignment(Alignment evaluatedAlignment) {
        this.evaluatedAlignment = evaluatedAlignment;
    }

    public Alignment getReferenceAlignment() {
        return referenceAlignment;
    }

    public void setReferenceAlignment(Alignment referenceAlignment) {
        this.referenceAlignment = referenceAlignment;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }
}
