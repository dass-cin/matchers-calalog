package br.cin.ufpe.dass.matchers.core;

/**
 * Created by diego on 12/03/17.
 */
public class OntologyMetrics {


    /** Avarage depth of elements considering all ontology's classes **/
    float avgDepth;

    /** Rate of elements described as subclassesOf and not semantically described **/
    float relationshipRichness;

    float attributeRichness;
    float inheritanceRichness;
    float labelWordnet;
    float localWordnet;
    float classRichness;
    float avgPopulation;
    float nullCommentPerc;
    float nullLabelPerc;
    float diffLabelLocPerc;

    public float getRelationshipRichness() {
        return relationshipRichness;
    }

    public void setRelationshipRichness(float relationshipRichness) {
        this.relationshipRichness = relationshipRichness;
    }

    public float getAttributeRichness() {
        return attributeRichness;
    }

    public void setAttributeRichness(float attributeRichness) {
        this.attributeRichness = attributeRichness;
    }

    public float getInheritanceRichness() {
        return inheritanceRichness;
    }

    public void setInheritanceRichness(float inheritanceRichness) {
        this.inheritanceRichness = inheritanceRichness;
    }

    public float getLabelWordnet() {
        return labelWordnet;
    }

    public void setLabelWordnet(float labelWordnet) {
        this.labelWordnet = labelWordnet;
    }

    public float getLocalWordnet() {
        return localWordnet;
    }

    public void setLocalWordnet(float localWordnet) {
        this.localWordnet = localWordnet;
    }

    public float getClassRichness() {
        return classRichness;
    }

    public void setClassRichness(float classRichness) {
        this.classRichness = classRichness;
    }

    public float getAvgPopulation() {
        return avgPopulation;
    }

    public void setAvgPopulation(float avgPopulation) {
        this.avgPopulation = avgPopulation;
    }

    public float getNullCommentPerc() {
        return nullCommentPerc;
    }

    public void setNullCommentPerc(float nullCommentPerc) {
        this.nullCommentPerc = nullCommentPerc;
    }

    public float getNullLabelPerc() {
        return nullLabelPerc;
    }

    public void setNullLabelPerc(float nullLabelPerc) {
        this.nullLabelPerc = nullLabelPerc;
    }

    public float getDiffLabelLocPerc() {
        return diffLabelLocPerc;
    }

    public void setDiffLabelLocPerc(float diffLabelLocPerc) {
        this.diffLabelLocPerc = diffLabelLocPerc;
    }

    public float getAvgDepth() {
        return avgDepth;
    }

    public void setAvgDepth(float avgDepth) {
        this.avgDepth = avgDepth;
    }
}
