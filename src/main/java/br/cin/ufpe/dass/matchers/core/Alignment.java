package br.cin.ufpe.dass.matchers.core;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.inrialpes.exmo.align.impl.*;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owl.align.Relation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
public class Alignment {

    @Id
    private String id;

    private Set<Correspondence> correspondences = new HashSet<Correspondence>();

    @DBRef
    private Matcher matcher;

    private long executionTimeInMillis;

    private long totalMemoryUsed;

    private long totalDiskUsed;

    @DBRef
    private Ontology ontology1;

    @DBRef
    private Ontology ontology2;

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

    public BasicAlignment toBasicAlignment() throws AlignmentException {
        BasicAlignment basicAlignment = new BasicAlignment().createNewAlignment(ontology1, ontology2, BasicRelation.class, BasicConfidence.class);
        for (Correspondence correspondence : this.correspondences) {
            BasicCell basicCell = correspondence.toBasicCell();
            basicAlignment.addAlignCell(basicCell.getId(), basicCell.getObject1(), basicCell.getObject2(), basicCell.getRelation(), basicCell.getStrength());
        }
        basicAlignment.setOntology1(ontology1);
        basicAlignment.setOntology2(ontology2);
        return basicAlignment;
    }

    public URIAlignment toURIAlignment() throws AlignmentException {
        URIAlignment uriAlignment = new URIAlignment();
        for (Correspondence correspondence : this.correspondences) {
            BasicCell basicCell = correspondence.toBasicCell();
            uriAlignment.addAlignCell(basicCell.getId(), basicCell.getObject2(), basicCell.getObject1(), basicCell.getRelation(), basicCell.getStrength());
        }
        uriAlignment.setOntology1(ontology1.getFile());
        uriAlignment.setOntology2(ontology2.getFile());
        return uriAlignment;
    }

    public boolean addCorrespodence(Correspondence correspondence) {
        return correspondences.add(correspondence);
    }

    public boolean removeCorrespondence(Correspondence correspondence) {
        return correspondences.remove(correspondence);
    }

    public Set<Correspondence> getCorrespondences() {
        return correspondences;
    }

    public void setCorrespondences(Set<Correspondence> correspondences) {
        this.correspondences = correspondences;
    }

    public long getExecutionTimeInMillis() {
        return executionTimeInMillis;
    }

    public void setExecutionTimeInMillis(long executionTimeInMillis) {
        this.executionTimeInMillis = executionTimeInMillis;
    }

    public long getTotalMemoryUsed() {
        return totalMemoryUsed;
    }

    public void setTotalMemoryUsed(long totalMemoryUsed) {
        this.totalMemoryUsed = totalMemoryUsed;
    }

    public long getTotalDiskUsed() {
        return totalDiskUsed;
    }

    public void setTotalDiskUsed(long totalDiskUsed) {
        this.totalDiskUsed = totalDiskUsed;
    }

    public Ontology getOntology1() {
        return ontology1;
    }

    public void setOntology1(Ontology ontology1) {
        this.ontology1 = ontology1;
    }

    public Ontology getOntology2() {
        return ontology2;
    }

    public void setOntology2(Ontology ontology2) {
        this.ontology2 = ontology2;
    }
}
