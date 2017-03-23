package br.cin.ufpe.dass.matchers.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by diego on 22/03/17.
 */
public class MatcherProfile {

    @Id
    private String id;

    @DBRef
    @Indexed(unique = true)
    private Matcher matcher;

    private int generatedAlignments;

    private AlignmentMetrics complianceMetrics = new AlignmentMetrics();

    private PerformanceMetrics performanceMetrics = new PerformanceMetrics();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    public AlignmentMetrics getComplianceMetrics() {
        return complianceMetrics;
    }

    public void setComplianceMetrics(AlignmentMetrics complianceMetrics) {
        this.complianceMetrics = complianceMetrics;
    }

    public int getGeneratedAlignments() {
        return generatedAlignments;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public void setGeneratedAlignments(int generatedAlignments) {
        this.generatedAlignments = generatedAlignments;
    }
}
