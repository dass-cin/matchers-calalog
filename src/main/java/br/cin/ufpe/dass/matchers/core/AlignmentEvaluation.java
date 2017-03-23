package br.cin.ufpe.dass.matchers.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.net.URI;

/**
 * Created by diego on 08/03/17.
 */
public class AlignmentEvaluation {

    private AlignmentMetrics metrics = new AlignmentMetrics();

    @DBRef
    @Indexed(unique = true)
    @JsonIgnore
    private Alignment evaluatedAlignment;

    private URI referenceAlignmentFile; //@TODO implementar carregamento do arquivo para o objeto Alignment

    @DBRef
    private Matcher matcher;

    public Alignment getEvaluatedAlignment() {
        return evaluatedAlignment;
    }

    public void setEvaluatedAlignment(Alignment evaluatedAlignment) {
        this.evaluatedAlignment = evaluatedAlignment;
    }

    public URI getReferenceAlignmentFile() {
        return referenceAlignmentFile;
    }

    public void setReferenceAlignmentFile(URI referenceAlignmentFile) {
        this.referenceAlignmentFile = referenceAlignmentFile;
    }

    public AlignmentMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(AlignmentMetrics metrics) {
        this.metrics = metrics;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }
}
