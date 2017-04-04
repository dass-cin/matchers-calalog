package br.cin.ufpe.dass.matcherswrappers.LogMap;

import uk.ac.ox.krr.logmap2.Parameters;
import uk.ac.ox.krr.logmap2.io.ReadFile;

import java.io.File;

/**
 * Created by diego on 04/04/17.
 */
public class LogMapParameters extends Parameters {

    public static void readParameters() {
        try {
            File e = new File(LogMapParameters.class.getResource("logmap/parameters.txt").toURI().toString());
            if(!e.exists()) {
                throw new Exception("Error reading LogMap parameters. File \'parameters.txt\' is not available. Using default parameters.");
            }

            accepted_annotation_URIs_for_classes.clear();
            accepted_data_assertion_URIs_for_individuals.clear();
            ReadFile reader = new ReadFile("parameters.txt");

            String line;
            while((line = reader.readLine()) != null) {
                if(!line.startsWith("#") && line.indexOf("|") >= 0) {
                    String[] elements = line.split("\\|");
                    if(elements[0].equals("print_output")) {
                        print_output = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("bad_score_scope")) {
                        bad_score_scope = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("good_isub_anchors")) {
                        good_isub_anchors = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("good_isub_candidates")) {
                        good_isub_candidates = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("good_confidence")) {
                        good_confidence = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("good_sim_coocurrence")) {
                        good_sim_coocurrence = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("min_conf_pro_map")) {
                        min_conf_pro_map = Double.valueOf(elements[1]).doubleValue();
                    } else if(elements[0].equals("max_ambiguity")) {
                        max_ambiguity = Integer.valueOf(elements[1]).intValue();
                    } else if(elements[0].equals("good_ambiguity")) {
                        good_ambiguity = Integer.valueOf(elements[1]).intValue();
                    } else if(elements[0].equals("use_overlapping")) {
                        use_overlapping = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("min_size_overlapping")) {
                        min_size_overlapping = Integer.valueOf(elements[1]).intValue();
                    } else if(elements[0].equals("instance_matching")) {
                        perform_instance_matching = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("output_class_mappings")) {
                        output_class_mappings = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("output_prop_mappings")) {
                        output_prop_mappings = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("output_instance_mappings")) {
                        output_instance_mappings = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("output_instance_mapping_files")) {
                        output_instance_mapping_files = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("annotation_URI")) {
                        accepted_annotation_URIs_for_classes.add(elements[1]);
                    } else if(elements[0].equals("data_assertion_URI_Indiv")) {
                        accepted_data_assertion_URIs_for_individuals.add(elements[1]);
                    } else if(elements[0].equals("data_assertion_URI_Indiv_deep2")) {
                        accepted_data_assertion_URIs_for_individuals_deep2.add(elements[1]);
                    } else if(elements[0].equals("object_assertion_URI_Indiv")) {
                        accepted_object_assertion_URIs_for_individuals.add(elements[1]);
                    } else if(elements[0].equals("reason_datatypes")) {
                        reason_datatypes = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("reasoner")) {
                        reasoner = elements[1];
                    } else if(elements[0].equals("timeout")) {
                        timeout = Integer.valueOf(elements[1]).intValue();
                    } else if(elements[0].equals("output_equivalences_only")) {
                        output_equivalences_only = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("second_chance_conflicts")) {
                        second_chance_conflicts = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("ratio_second_chance_discarded")) {
                        ratio_second_chance_discarded = Integer.valueOf(elements[1]).intValue();
                    } else if(elements[0].equals("use_umls_lexicon")) {
                        use_umls_lexicon = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("reverse_labels")) {
                        reverse_labels = Boolean.valueOf(elements[1]).booleanValue();
                    } else if(elements[0].equals("allow_interactivity")) {
                        allow_interactivity = Boolean.valueOf(elements[1]).booleanValue();
                    }
                }
            }

            reader.closeBuffer();
        } catch (Exception var4) {
            System.err.println("Error reading LogMap 2 parameters file: " + var4.getLocalizedMessage());
        }

    }

}
