package br.cin.ufpe.dass.matchers.service;

import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.core.OntologyMetrics;
import br.cin.ufpe.dass.matchers.core.OntologyProfile;
import br.cin.ufpe.dass.matchers.repository.OntologyRepository;
import br.cin.ufpe.dass.matchers.util.OntologyUtils;
import br.cin.ufpe.dass.matchers.util.StringUtil;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.springframework.stereotype.Service;

/**
 * Created by diego on 07/03/17.
 */
@Service
public class OntologyProfileService {

    private final WordNetDatabase wordNetDatabase;

    private final OntologyService ontologyService;

    private final OntologyRepository ontologyRepository;

    enum WordnetType {LOCALNAMES, LABELS, COMMENTS};

    public OntologyProfileService(WordNetDatabase wordNetDatabase, OntologyService ontologyService, OntologyRepository ontologyRepository) {
        this.wordNetDatabase = wordNetDatabase;
        this.ontologyService = ontologyService;
        this.ontologyRepository = ontologyRepository;
    }

    public OntologyProfile generateOntologyProfile(String ontologyId) {

        Ontology ontology = ontologyRepository.findOne(ontologyId);

        OntologyProfile profile = this.buildProfile(ontology);
        profile.setMetrics(this.calculateMetrics(ontology, profile));
        ontology.setProfile(profile);

        ontologyRepository.save(ontology);

        return profile;

    }

    private OntologyProfile buildProfile(Ontology ontology) {

        OntologyProfile profile = new OntologyProfile();

        OntModel model = ontologyService.getJenaOntologyModel(ontology.getFile().toString());

        for(OntProperty prop: model.listDatatypeProperties().toList()){
            if(prop.isAnon()) continue;
//            if(!prop.getURI().startsWith(ontology.getURI().toString())) continue;
            profile.setDataProperties(profile.getDataProperties()+1);
        }

        for(OntProperty prop: model.listObjectProperties().toList()){
            if(prop.isAnon()) continue;
//            if(!prop.getURI().startsWith(ontology.getURI().toString())) continue;
            profile.setObjectProperties(profile.getObjectProperties()+1);
        }

        float totalDepth = 0;

        /** Parsing classes **/
        for (OntClass cl: model.listClasses().toList()) {
            if(cl.isAnon()) continue;

            profile.setClasses(profile.getClasses()+1);

            int instSize = cl.listInstances().toList().size();
            if(instSize!=0){
                profile.setInstancesNumber(profile.getInstancesNumber()+instSize);
                profile.setClassesWithInstances(profile.getClassesWithInstances()+1);
            }

            String label = cl.getLabel(null);

            if(label==null) profile.setNullLabels(profile.getNullLabels()+1);
            else if(!label.equals(cl.getLocalName())){
                profile.setDiffLabelLocalName(profile.getDiffLabelLocalName()+1);;
            }

            String comment = cl.getComment(null);
            if(comment==null) profile.setNullComments(profile.getNullComments()+1);

            int depth = OntologyUtils.getClassDepth(cl,1);
            totalDepth += depth;
        }

        profile.setAvgDepth(totalDepth / profile.getClasses());

        /** Parsing data type properties **/
        for (OntProperty pr: model.listDatatypeProperties().toList()) {

            String label = pr.getLabel(null);

            if(label==null) profile.setNullLabels(profile.getNullLabels()+1);
            else if(!label.equals(pr.getLocalName())){
                profile.setDiffLabelLocalName(profile.getDiffLabelLocalName()+1);
            }

            String comment = pr.getComment(null);
            if(comment==null) profile.setNullComments(profile.getNullComments()+1);
        }

        /** Parsing object properties **/
        for (OntProperty pr: model.listObjectProperties().toList()) {

            String label = pr.getLabel(null);

            if(label==null) profile.setNullLabels(profile.getNullLabels()+1);
            else if(!label.equals(pr.getLocalName())){
                profile.setDiffLabelLocalName(profile.getDiffLabelLocalName()+1);
            }

            String comment = pr.getComment(null);
            if(comment==null) profile.setNullComments(profile.getNullComments()+1);
        }

        profile.setTotalElements(profile.getClasses() + profile.getDataProperties() + profile.getObjectProperties());


        /** Parsing for subClassesOf **/
        int subClassesOf = 0;
        for (OntClass cl : model.listClasses().toList()) {
            if (!cl.isAnon()) {
                subClassesOf += cl.listSubClasses().toList().size();
            }
        }
        profile.setTotalSubClassesOf(subClassesOf);

        profile.setAttributes(model.listDatatypeProperties().toList().size());

        /** Relations that are not described as SubClassOf  **/
        profile.setOtherRelations(model.listObjectProperties().toList().size());

        return profile;

    }

    private OntologyMetrics calculateMetrics(Ontology ontology, OntologyProfile profile) {
        OntologyMetrics metrics = new OntologyMetrics();

        metrics.setRelationshipRichness((float)profile.getOtherRelations()/(profile.getOtherRelations()+profile.getTotalSubClassesOf()));
        metrics.setAttributeRichness((float)profile.getAttributes()/profile.getClasses());
        metrics.setInheritanceRichness((float)profile.getTotalSubClassesOf() / profile.getTotalElements());
        metrics.setClassRichness((float)profile.getClassesWithInstances() / profile.getClasses());
        metrics.setAvgPopulation((float)profile.getInstancesNumber() / profile.getClasses());
        metrics.setDiffLabelLocPerc((float)profile.getDiffLabelLocalName() / profile.getTotalElements());
        metrics.setAvgDepth(profile.getAvgDepth());
        metrics.setLabelWordnet((float)calculateWordnetCoverage(ontology, WordnetType.LABELS));
        metrics.setLocalWordnet((float)calculateWordnetCoverage(ontology, WordnetType.LOCALNAMES));
        metrics.setNullCommentPerc((float)profile.getNullComments() / profile.getTotalElements());
        metrics.setNullLabelPerc((float)profile.getNullLabels() / profile.getTotalElements());

        return metrics;
    }

    public double calculateWordnetCoverage(Ontology ontology, WordnetType type) {

        int count = 0;

        int coveredCount = 0;

        OntModel model = ontologyService.getJenaOntologyModel(ontology.getFile().toString());

        String string = null;

        // Parsing classes list
        for (OntClass cl : model.listClasses().toList()) {

            if (cl.getURI() == null) continue;

            count++;
            switch(type) {
                case LOCALNAMES:
                    string = cl.getLocalName();
                    break;
                case LABELS:
                    string = cl.getLabel(null);
                    break;
                default:
                    continue;
            }

            if(string == null){
                continue;
            }

            string = StringUtil.normalizeString(string);

            String[] tokenized = string.split("\\s");

            Synset[] synsets;

            boolean covered = false;
            for (int i = 0; i < tokenized.length; i++) {
                synsets = wordNetDatabase.getSynsets(tokenized[i],null);
                if(synsets.length>0) covered = true;
            }

            if(covered) coveredCount++;

        }

        for(OntProperty pr: model.listAllOntProperties().toList()){
            if(pr.getURI()==null) continue;

            count++;
            switch(type) {
                case LOCALNAMES:
                    string = pr.getLocalName();
                    break;
                case LABELS:
                    string = pr.getLabel(null);
            }

            if(string == null){
                continue;
            }

            string = StringUtil.normalizeString(string);

            String[] tokenized = string.split("\\s");

            Synset[] synsets;

            boolean covered = false;
            for (int i = 0; i < tokenized.length; i++) {
                synsets = wordNetDatabase.getSynsets(tokenized[i],null);
                if(synsets.length>0) covered = true;
            }

            if(covered) coveredCount++;
        }

        return (double)coveredCount/count;
    }

}
