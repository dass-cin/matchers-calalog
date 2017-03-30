package br.cin.ufpe.dass.yamwrapper.service;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Correspondence;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import yamSS.datatypes.mapping.GMapping;
import yamSS.datatypes.mapping.GMappingScore;
import yamSS.datatypes.mapping.GMappingTable;
import yamSS.loader.ontology.OntoBuffer;
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

    public Alignment match(String source, String target) throws IOException {

        long startDate = System.currentTimeMillis();


        OntoBuffer onto1 = new OntoBuffer(source.replaceAll("file://", ""));
        OntoBuffer onto2 = new OntoBuffer(target.replaceAll("file://", ""));

        GMappingTable<String> alignmentString = yam.align(onto1,onto2);

        long endDate = System.currentTimeMillis();

        Iterator<GMapping<String>> it = alignmentString.getIterator();

        Alignment alignment = new Alignment();
        alignment.setExecutionTimeInMillis(endDate - startDate);

        while(it.hasNext()) {
            GMappingScore<String> row = (GMappingScore<String>) it.next();
            Correspondence correspondence = new Correspondence();
            correspondence.setRelation((row.getRelation().equals("=")?"Equivalence":row.getRelation()));
            correspondence.setSimilarityValue(row.getSimScore());
            correspondence.setSourceElement(URI.create(row.getEl1()));
            correspondence.setTargetElement(URI.create(row.getEl2()));
            alignment.addCorrespodence(correspondence);
        }

        return alignment;

    }


}
