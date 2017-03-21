package br.cin.ufpe.dass.matchers.core;

import java.time.ZonedDateTime;

/**
 * Created by diego on 12/03/17.
 */
public class OntologyProfile {

    private OntologyMetrics metrics = new OntologyMetrics();

    private int attributes = 0;
    private int classes = 0;
    private float avgDepth = 0;
    private int dataProperties = 0;
    private int objectProperties = 0;
    private int instancesNumber = 0;
    private int classesWithInstances = 0;
    private int nullLabels = 0;
    private int nullComments = 0;
    private int diffLabelLocalName = 0;
    private int totalElements = 0;
    private float nullCommentsPercent = 0;
    private float nullLabelsPercent = 0;
    private float diffLabelLocPercent = 0;
    private int totalSubClassesOf = 0;
    private int otherRelations = 0;

    public int getClassesWithInstances() {
        return classesWithInstances;
    }

    public void setClassesWithInstances(int classesWithInstances) {
        this.classesWithInstances = classesWithInstances;
    }

    public OntologyMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(OntologyMetrics metrics) {
        this.metrics = metrics;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public float getAvgDepth() {
        return avgDepth;
    }

    public void setAvgDepth(float avgDepth) {
        this.avgDepth = avgDepth;
    }

    public int getDataProperties() {
        return dataProperties;
    }

    public void setDataProperties(int dataProperties) {
        this.dataProperties = dataProperties;
    }

    public int getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(int objectProperties) {
        this.objectProperties = objectProperties;
    }

    public int getInstancesNumber() {
        return instancesNumber;
    }

    public void setInstancesNumber(int instancesNumber) {
        this.instancesNumber = instancesNumber;
    }

    public int getNullLabels() {
        return nullLabels;
    }

    public void setNullLabels(int nullLabels) {
        this.nullLabels = nullLabels;
    }

    public int getNullComments() {
        return nullComments;
    }

    public void setNullComments(int nullComments) {
        this.nullComments = nullComments;
    }

    public int getDiffLabelLocalName() {
        return diffLabelLocalName;
    }

    public void setDiffLabelLocalName(int diffLabelLocalName) {
        this.diffLabelLocalName = diffLabelLocalName;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public float getNullCommentsPercent() {
        return nullCommentsPercent;
    }

    public void setNullCommentsPercent(float nullCommentsPercent) {
        this.nullCommentsPercent = nullCommentsPercent;
    }

    public float getNullLabelsPercent() {
        return nullLabelsPercent;
    }

    public void setNullLabelsPercent(float nullLabelsPercent) {
        this.nullLabelsPercent = nullLabelsPercent;
    }

    public float getDiffLabelLocPercent() {
        return diffLabelLocPercent;
    }

    public void setDiffLabelLocPercent(float diffLabelLocPercent) {
        this.diffLabelLocPercent = diffLabelLocPercent;
    }

    public int getTotalSubClassesOf() {
        return totalSubClassesOf;
    }

    public void setTotalSubClassesOf(int totalSubClassesOf) {
        this.totalSubClassesOf = totalSubClassesOf;
    }

    public int getOtherRelations() {
        return otherRelations;
    }

    public void setOtherRelations(int otherRelations) {
        this.otherRelations = otherRelations;
    }
}
