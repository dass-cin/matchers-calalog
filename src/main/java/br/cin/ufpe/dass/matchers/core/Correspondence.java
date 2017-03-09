package br.cin.ufpe.dass.matchers.core;

import fr.inrialpes.exmo.align.impl.BasicCell;
import fr.inrialpes.exmo.align.impl.BasicRelation;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URI;

/**
 * Created by diego on 08/03/17.
 */
@Document
public class Correspondence {

    @Id
    private String id;

    private URI sourceElement;

    private URI targetElement;

    private String relation = "Equivalence";

    private float similarityValue;

    public Correspondence() {
    }

    public Correspondence(URI sourceElement, URI targetElement, String relation, float similarityValue) {
        this.sourceElement = sourceElement;
        this.targetElement = targetElement;
        this.relation = relation;
        this.similarityValue = similarityValue;
    }

    public BasicCell toBasicCell() throws AlignmentException {
        BasicRelation relation = new BasicRelation(this.relation);
        BasicCell basicCell = new BasicCell(id, sourceElement, targetElement, relation, similarityValue);
        return basicCell;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URI getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(URI sourceElement) {
        this.sourceElement = sourceElement;
    }

    public URI getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(URI targetElement) {
        this.targetElement = targetElement;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public float getSimilarityValue() {
        return similarityValue;
    }

    public void setSimilarityValue(float similarityValue) {
        this.similarityValue = similarityValue;
    }
}
