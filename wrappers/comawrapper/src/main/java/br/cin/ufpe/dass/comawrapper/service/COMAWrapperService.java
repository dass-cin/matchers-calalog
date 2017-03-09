package br.cin.ufpe.dass.comawrapper.service;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Correspondence;
import br.cin.ufpe.dass.matchers.repository.CorrespondenceRepository;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.service.AlignmentService;
import de.wdilab.coma.integration.COMA_API;
import de.wdilab.coma.structure.Element;
import de.wdilab.coma.structure.MatchResult;
import de.wdilab.coma.structure.MatchResultArray;
import org.semanticweb.owl.align.AlignmentException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.TreeMap;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class COMAWrapperService {

    private final OntologyRepository ontologyRepository;

    private final AlignmentService alignmentService;

    private final CorrespondenceRepository correspondenceRepository;

    public COMAWrapperService(OntologyRepository ontologyRepository, AlignmentService alignmentService, CorrespondenceRepository correspondenceRepository) {
        this.ontologyRepository = ontologyRepository;
        this.alignmentService = alignmentService;
        this.correspondenceRepository = correspondenceRepository;
    }

    public Alignment match(String source, String target, int resolution, int similarityMeasure) throws AlignmentException {

        long startDate = System.currentTimeMillis();

        COMA_API coma_api = new COMA_API();
        MatchResult result = coma_api.matchModels("file:///"+source,
                "file:///"+target, resolution, similarityMeasure);

        long endDate = System.currentTimeMillis();

        Alignment alignment = new Alignment();
        alignment.setExecutionTimeInMillis(endDate - startDate);

        TreeMap<Integer, Element> indexedSourceElements = new TreeMap<Integer, Element>();
        for(Object sourceElementObject : result.getSrcMatchObjects()) {
            Element sourceElement = (Element)sourceElementObject;
            indexedSourceElements.put(sourceElement.getId(), sourceElement);
        }

        TreeMap<Integer, Element> indexedTargetElements = new TreeMap<Integer, Element>();
        for(Object targetElementObject : result.getTrgMatchObjects()) {
            Element targetElement = (Element)targetElementObject;
            indexedTargetElements.put(targetElement.getId(), targetElement);
        }

        float[][] simMatrix = ((MatchResultArray) result).getSimMatrix();
        for (int i=0; i < simMatrix.length; i++) {
            for(int j=0; j < simMatrix[i].length; j++) {
                if (simMatrix[i][j] > 0 && indexedSourceElements.get(i) != null && indexedTargetElements.get(j) != null) {
                    Correspondence correspondence = new Correspondence( URI.create(indexedSourceElements.get(i).getName()), URI.create(indexedTargetElements.get(j).getName()), "Equivalence", simMatrix[i][j]);
                    correspondenceRepository.save(correspondence);
                    alignment.addCorrespodence(correspondence);
                }
            }
        }

        return alignment;

    }


}
