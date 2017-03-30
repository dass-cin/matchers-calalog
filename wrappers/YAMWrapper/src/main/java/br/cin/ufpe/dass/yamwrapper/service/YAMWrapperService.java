package br.cin.ufpe.dass.yamwrapper.service;

import org.apache.log4j.spi.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import yamSS.main.oaei.run.YAM;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class YAMWrapperService {

    private final YAM yam;

    public YAMWrapperService(YAM yam) {
        this.yam = yam;
    }

    public void match(String source, String target) throws IOException {

        long startDate = System.currentTimeMillis();

        String alignmentString = yam.align(source.replaceAll("file://", ""), target.replaceAll("file://", ""));
        System.out.println("Alignment = "+alignmentString);

        File alignmentFile = File.createTempFile("alignment", ".rdf");
        FileWriter fw = new FileWriter(alignmentFile);
        fw.write(alignmentString);
        fw.flush();
        fw.close();



//
//        COMA_API coma_api = new COMA_API();
//        Alignment alignment = new Alignment();
//
//        try {
//            MatchResult result = coma_api.matchModels(source,
//                    target, resolution, similarityMeasure);
//
//            long endDate = System.currentTimeMillis();
//
//            alignment.setExecutionTimeInMillis(endDate - startDate);
//
//            TreeMap<Integer, Element> indexedSourceElements = new TreeMap<Integer, Element>();
//            for(Object sourceElementObject : result.getSrcMatchObjects()) {
//                Element sourceElement = (Element)sourceElementObject;
//                indexedSourceElements.put(sourceElement.getId(), sourceElement);
//            }
//
//            TreeMap<Integer, Element> indexedTargetElements = new TreeMap<Integer, Element>();
//            for(Object targetElementObject : result.getTrgMatchObjects()) {
//                Element targetElement = (Element)targetElementObject;
//                indexedTargetElements.put(targetElement.getId(), targetElement);
//            }
//
//            int counter = 0;
//            float[][] simMatrix = ((MatchResultArray) result).getSimMatrix();
//            for (int i=0; i < simMatrix.length; i++) {
//                for(int j=0; j < simMatrix[i].length; j++) {
//                    if (simMatrix[i][j] > 0 && indexedSourceElements.get(i+1) != null && indexedTargetElements.get(j+1) != null) {
//                        Correspondence correspondence = new Correspondence(IRI.create(indexedSourceElements.get(i+1).getAccession()).toURI(), IRI.create(indexedTargetElements.get(j+1).getAccession()).toURI(), "Equivalence", simMatrix[i][j]);
//                        alignment.addCorrespodence(correspondence);
//                    }
//                }
//            }
//        } catch (IndexOutOfBoundsException e) {
//            throw new AlignmentException("fail to execute alignment");
//        }


    }


}
