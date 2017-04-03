package br.cin.ufpe.dass.matcherswrappers.AML;

import aml.AML;
import aml.match.Mapping;
import aml.ontology.Ontology2Match;
import aml.settings.*;
import br.cin.ufpe.dass.matchers.core.Alignment;
import br.cin.ufpe.dass.matchers.core.Correspondence;
import br.cin.ufpe.dass.matchers.wrapper.MatcherParameters;
import br.cin.ufpe.dass.matchers.wrapper.exception.AlignmentException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.stereotype.Service;
import yamSS.datatypes.mapping.GMapping;
import yamSS.datatypes.mapping.GMappingScore;
import yamSS.datatypes.mapping.GMappingTable;
import yamSS.loader.ontology.OntoBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class AMLWrapperService {

    private final AML aml;

    public AMLWrapperService(AML aml) {
        this.aml = aml;
    }

    public Alignment match(MatcherParameters parameters) throws IOException, OWLOntologyCreationException, AlignmentException {

        long startDate = System.currentTimeMillis();

        String source = parameters.getSource();
        String target = parameters.getTarget();

        String config = (String) parameters.getConfigParams().get("config");
        String bkPath = (String) parameters.getConfigParams().get("bkPath");
        String mode = (String) parameters.getConfigParams().get("mode");
        String inputPath = (String) parameters.getConfigParams().get("inputPath");
        String outputPath = (String) parameters.getConfigParams().get("outputPath");
        String dir = aml.getPath();

        aml.openOntologies(source.replaceAll("file://", ""), target.replaceAll("file://", ""));

        aml.match.Alignment alignment = aml.getAlignment();

        if (mode != null && mode.equals("repair")) {
            try {
                aml.openAlignment(inputPath);
            } catch (Exception e) {
                throw new AlignmentException("Error: Could not open input alignment", e);
            }
            aml.repair();
        } else {
            if (mode != null && mode.equals("manual")) {
                String configFile = dir + "/" +config;
                readConfigFile(configFile, bkPath);
                aml.matchManual();
            } else {
                aml.matchAuto();
            }

            if (inputPath != null && !inputPath.isEmpty()) {
                try
                {
                    aml.openReferenceAlignment(inputPath);
                }
                catch(Exception e)
                {
                    throw new AlignmentException("Error: Could not open input alignment", e);
                }
                aml.evaluate();
            }
        }

        if (outputPath != null && !outputPath.isEmpty()) {
            try {
                aml.saveAlignmentRDF(outputPath);
            } catch (Exception e) {
                throw new AlignmentException("Error: Could not save alignment", e);
            }
        }

        long endDate = System.currentTimeMillis();

        Alignment newAlignment = new Alignment();
        newAlignment.setExecutionTimeInMillis(endDate - startDate);

        Iterator<Mapping> it = aml.getAlignment().iterator();

        while(it.hasNext()) {
            Mapping mapping = it.next();
            Correspondence correspondence = new Correspondence();
            correspondence.setRelation((mapping.getRelationship().equals(MappingRelation.EQUIVALENCE)?"Equivalence":mapping.getRelationship().name()));
            correspondence.setSimilarityValue((float) mapping.getSimilarity());
            correspondence.setSourceElement(URI.create(mapping.getSourceURI()));
            correspondence.setTargetElement(URI.create(mapping.getTargetURI()));
            newAlignment.addCorrespodence(correspondence);
        }

        return newAlignment;

    }

    private void readConfigFile(String configFile, String bkPath)
    {
        File conf = new File(configFile);
        if(!conf.canRead())
        {
            System.out.println("Warning: Config file not found");
            System.out.println("Matching will proceed with default configuration");
        }
        try
        {
            Vector<MatchStep> selection = new Vector<MatchStep>();
            BufferedReader in = new BufferedReader(new FileReader(conf));
            String line;
            while((line=in.readLine()) != null)
            {
                if(line.startsWith("#") || line.isEmpty())
                    continue;
                String[] option = line.split("=");
                option[0] = option[0].trim();
                option[1] = option[1].trim();
                if(option[0].equals("use_translator"))
                {
                    if(option[1].equalsIgnoreCase("true") ||
                            (option[1].equalsIgnoreCase("auto") &&
                                    aml.getMatchSteps().contains(MatchStep.TRANSLATE)))
                        selection.add(MatchStep.TRANSLATE);
                }
                else if(option[0].equals("bk_sources"))
                {
                    if(option[1].equalsIgnoreCase("none"))
                        continue;
                    selection.add(MatchStep.BK);
                    if(!option[1].equalsIgnoreCase("all"))
                    {
                        Vector<String> sources = new Vector<String>();
                        for(String s : option[1].split(","))
                        {
                            String source = s.trim();
                            File bk = new File(bkPath + source);
                            if(bk.canRead())
                                sources.add(source);
                        }
                        aml.setSelectedSources(sources);
                    }
                }
                else if(option[0].equals("word_matcher"))
                {
                    if(option[1].equalsIgnoreCase("none") ||
                            (option[1].equalsIgnoreCase("auto") &&
                                    !aml.getMatchSteps().contains(MatchStep.WORD)))
                        continue;
                    selection.add(MatchStep.WORD);
                    if(!option[1].equalsIgnoreCase("auto"))
                        aml.setWordMatchStrategy(WordMatchStrategy.parseStrategy(option[1]));
                }
                else if(option[0].equals("string_matcher"))
                {
                    if(option[1].equalsIgnoreCase("none"))
                        continue;
                    selection.add(MatchStep.STRING);
                    boolean primary = aml.primaryStringMatcher();
                    if(option[1].equalsIgnoreCase("global"))
                        primary = true;
                    else if(option[1].equalsIgnoreCase("local"))
                        primary = false;
                    aml.setPrimaryStringMatcher(primary);
                }
                else if(option[0].equals("string_measure"))
                {
                    StringSimMeasure sm = StringSimMeasure.parseMeasure(option[1]);
                    aml.setStringSimMeasure(sm);
                }
                else if(option[0].equals("struct_matcher"))
                {
                    if(option[1].equalsIgnoreCase("none") ||
                            (option[1].equalsIgnoreCase("auto") &&
                                    !aml.getMatchSteps().contains(MatchStep.STRUCT)))
                        continue;
                    selection.add(MatchStep.STRUCT);
                    if(!option[1].equalsIgnoreCase("auto"))
                        aml.setNeighborSimilarityStrategy(NeighborSimilarityStrategy.parseStrategy(option[1]));
                }
                else if(option[0].equals("match_properties"))
                {
                    if(option[1].equalsIgnoreCase("true") ||
                            (option[1].equalsIgnoreCase("auto") &&
                                    aml.getMatchSteps().contains(MatchStep.PROPERTY)))
                        selection.add(MatchStep.PROPERTY);
                }
                else if(option[0].equals("selection_type"))
                {
                    if(option[1].equalsIgnoreCase("none"))
                        continue;
                    selection.add(MatchStep.PROPERTY);
                    if(!option[1].equalsIgnoreCase("auto"))
                        aml.setSelectionType(SelectionType.parseSelector(option[1]));
                }
                else if(option[0].equals("repair_alignment"))
                {
                    if(option[1].equalsIgnoreCase("true"))
                        selection.add(MatchStep.REPAIR);
                }
            }
            in.close();
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not read config file");
            e.printStackTrace();
            System.out.println("Matching will proceed with default configuration");
            aml.defaultConfig();
        }
    }


}
