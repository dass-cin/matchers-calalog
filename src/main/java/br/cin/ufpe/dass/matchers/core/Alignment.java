package br.cin.ufpe.dass.matchers.core;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.inrialpes.exmo.align.impl.BasicAlignment;
import fr.inrialpes.exmo.align.impl.BasicCell;
import fr.inrialpes.exmo.align.impl.BasicConfidence;
import fr.inrialpes.exmo.align.impl.BasicRelation;
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

    @DBRef
    private Matcher matcher;

    private long executionTimeInMillis;

    private long totalMemoryUsed;

    private long totalDiskUsed;

    private Ontology ontology1;
    private Ontology ontology2;

    private Set<Correspondence> correspondences = new HashSet<Correspondence>();

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
        BasicAlignment basicAlignment = new BasicAlignment();
        basicAlignment = basicAlignment.createNewAlignment(ontology1, ontology2, BasicRelation.class, BasicConfidence.class);
        for (Correspondence correspondence : this.correspondences) {
            BasicCell basicCell = correspondence.toBasicCell();
            basicAlignment.addAlignCell(basicCell.getId(), basicCell.getObject1(), basicCell.getObject2(), basicCell.getRelation(), basicCell.getStrength());
        }
        return basicAlignment;
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
