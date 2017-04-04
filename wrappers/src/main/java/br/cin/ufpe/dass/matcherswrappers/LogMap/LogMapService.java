package br.cin.ufpe.dass.matcherswrappers.LogMap;

import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Correspondence;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import br.cin.ufpe.dass.matchers.wrapper.exception.AlignmentException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.stereotype.Service;
import uk.ac.ox.krr.logmap2.LogMap2Core;
import uk.ac.ox.krr.logmap2.Parameters;
import uk.ac.ox.krr.logmap2.mappings.objects.MappingObjectStr;
import uk.ac.ox.krr.logmap2.utilities.Utilities;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class LogMapService  {

    private LogMap2Core logmap2;

    private Set<MappingObjectStr> logmap2Mappings = new HashSet<MappingObjectStr>();

    public Alignment match(MatcherParameters parameters) throws IOException, OWLOntologyCreationException, AlignmentException {

        long startDate = System.currentTimeMillis();

        Alignment alignment = new Alignment();

        try {
            LogMapParameters.readParameters();

            String source = parameters.getSource();
            String target = parameters.getTarget();
            String outputPath = (String) parameters.getConfigParams().get("outputPath");
            boolean evalImpact = (boolean) parameters.getConfigParams().get("evalImpact");

            logmap2 = new LogMap2Core(source, target, outputPath, evalImpact);
            createObjectMappings();

            long endDate = System.currentTimeMillis();

            alignment.setExecutionTimeInMillis(endDate - startDate);

            Iterator<MappingObjectStr> it = logmap2Mappings.iterator();

            while(it.hasNext()) {
                MappingObjectStr mapping = it.next();
                Correspondence correspondence = new Correspondence();
                correspondence.setRelation(mapping.getMappingDirection()==Utilities.EQ?"Equality":"");
                correspondence.setSimilarityValue((float) mapping.getConfidence());
                correspondence.setSourceElement(URI.create(mapping.getIRIStrEnt1()));
                correspondence.setTargetElement(URI.create(mapping.getIRIStrEnt2()));
                alignment.addCorrespodence(correspondence);
            }

        } catch (Exception e) {
            throw new AlignmentException("Exception while executing LogMap matcher", e);
        }

        return alignment;

    }


    private void createObjectMappings() throws AlignmentException {

        int dir_mapping;


        try {

            if (Parameters.output_class_mappings){

                for (int ide1 : logmap2.getClassMappings().keySet()){
                    for (int ide2 : logmap2.getClassMappings().get(ide1)){

                        dir_mapping = logmap2.getDirClassMapping(ide1, ide2);

                        if (dir_mapping!= Utilities.NoMap){

                            if (dir_mapping!=Utilities.R2L){

                                //GSs in OAIE only contains, in general, equivalence mappings
                                if (Parameters.output_equivalences_only){
                                    dir_mapping=Utilities.EQ;
                                }


                                logmap2Mappings.add(
                                        new MappingObjectStr(
                                                logmap2.getIRI4ConceptIdentifier(ide1),
                                                logmap2.getIRI4ConceptIdentifier(ide2),
                                                logmap2.getConfidence4ConceptMapping(ide1, ide2),
                                                dir_mapping,
                                                Utilities.CLASSES));

                            }
                            else{

                                if (Parameters.output_equivalences_only){
                                    dir_mapping=Utilities.EQ;
                                }

                                logmap2Mappings.add(
                                        new MappingObjectStr(
                                                logmap2.getIRI4ConceptIdentifier(ide2),
                                                logmap2.getIRI4ConceptIdentifier(ide1),
                                                logmap2.getConfidence4ConceptMapping(ide1, ide2),
                                                dir_mapping,
                                                Utilities.CLASSES));
                            }
                        }
                    }
                }
            }


            if (Parameters.output_prop_mappings){

                for (int ide1 : logmap2.getDataPropMappings().keySet()){

                    logmap2Mappings.add(
                            new MappingObjectStr(
                                    logmap2.getIRI4DataPropIdentifier(ide1),
                                    logmap2.getIRI4DataPropIdentifier(logmap2.getDataPropMappings().get(ide1)),
                                    logmap2.getConfidence4DataPropConceptMapping(ide1, logmap2.getDataPropMappings().get(ide1)),
                                    Utilities.EQ,
                                    Utilities.DATAPROPERTIES));
                }

                for (int ide1 : logmap2.getObjectPropMappings().keySet()){

                    logmap2Mappings.add(
                            new MappingObjectStr(
                                    logmap2.getIRI4ObjectPropIdentifier(ide1),
                                    logmap2.getIRI4ObjectPropIdentifier(logmap2.getObjectPropMappings().get(ide1)),
                                    logmap2.getConfidence4ObjectPropConceptMapping(ide1, logmap2.getObjectPropMappings().get(ide1)),
                                    Utilities.EQ,
                                    Utilities.OBJECTPROPERTIES));
                }
            }


            //Output for individuals
            if (Parameters.perform_instance_matching && Parameters.output_instance_mappings){

                for (int ide1 : logmap2.getInstanceMappings().keySet()){
                    for (int ide2 : logmap2.getInstanceMappings().get(ide1)){

                        logmap2Mappings.add(
                                new MappingObjectStr(
                                        logmap2.getIRI4InstanceIdentifier(ide1),
                                        logmap2.getIRI4InstanceIdentifier(ide2),
                                        logmap2.getConfidence4InstanceMapping(ide1, ide2),
                                        Utilities.EQ,
                                        Utilities.INSTANCES));

                    }
                }


            }

            logmap2.clearIndexStructures();

        }
        catch (Exception e){
            throw new AlignmentException("Error creating object mappings", e);
        }
    }
}
