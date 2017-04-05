package br.cin.ufpe.dass.matcherswrappers.FCAMap;

import Method.FCA_Map;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Correspondence;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import br.cin.ufpe.dass.matchers.wrapper.exception.AlignmentException;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class FCAMapWrapperService {

    private final FCA_Map fcaMap;

    public FCAMapWrapperService(FCA_Map fcaMap) {
        this.fcaMap = fcaMap;
    }

    public Alignment match(MatcherParameters parameters) throws IOException, OWLOntologyCreationException, AlignmentException {

        Alignment alignment = new Alignment();

        long startDate = System.currentTimeMillis();

        try {
            URL uri = fcaMap.returnAlignmentFile(URI.create(parameters.getSource()).toURL(), URI.create(parameters.getTarget()).toURL());

            AlignmentParser aparser = new AlignmentParser(0);
            org.semanticweb.owl.align.Alignment fcaMapAlignment = aparser.parse( uri.toURI() );

            long endDate = System.currentTimeMillis();
            alignment.setExecutionTimeInMillis(endDate - startDate);

            Iterator<Cell> it = fcaMapAlignment.iterator();

            while(it.hasNext()) {
                Cell mapping = it.next();
                Correspondence correspondence = new Correspondence();
                correspondence.setRelation((mapping.getRelation().toString()));
                correspondence.setSimilarityValue((float) mapping.getStrength());
                correspondence.setSourceElement(mapping.getObject1AsURI());
                correspondence.setTargetElement(mapping.getObject2AsURI());
                alignment.addCorrespodence(correspondence);
            }

        } catch (Exception e) {
            throw new AlignmentException("Exception while executing FCAMAP matcher", e);
        }

        return alignment;

    }

}
