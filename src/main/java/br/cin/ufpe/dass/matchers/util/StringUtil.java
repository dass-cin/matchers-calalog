package br.cin.ufpe.dass.matchers.util;

/**
 * Created by diego on 20/03/17.
 */
public class StringUtil {

    public static String normalizeString(String label){
        if(label==null) return null;

        //Remove anything from a string that isn't a Character or a space
        //e.g. numbers, punctuation etc.
        String result = "";
        for(int i=0; i<label.length(); i++){
            if( Character.isLetter(label.charAt(i)) || Character.isWhitespace( label.charAt(i) ) ){
                result += label.charAt(i);
            }
        }
        label = result;

        String lower = label.toLowerCase();

        //Remove non-content words
        if(lower.startsWith("has"))
            label = label.substring(3);
        else if(lower.startsWith("is"))
            label = label.substring(2);
        else if(lower.startsWith("are"))
            label = label.substring(3);
        else if(lower.startsWith("be"))
            label = label.substring(2);
        else if(lower.endsWith(" by"))
            label = label.substring(0, label.length()-3);
        else if(lower.endsWith(" in"))
            label = label.substring(0, label.length()-3);
        else if(lower.endsWith(" at"))
            label = label.substring(0, label.length()-3);
        else if(lower.endsWith(" to"))
            label = label.substring(0, label.length()-3);
        else if(lower.endsWith(" on"))
            label = label.substring(0, label.length()-3);
        else if(lower.endsWith(" for"))
            label = label.substring(0, label.length()-4);

        int len = label.length();
        //Separate words with spaces
        for(int i=0;i<len-1; i++){
            if( Character.isLowerCase(label.charAt(i)) &&  Character.isUpperCase(label.charAt(i+1)) ){

                label = label.substring(0,i+1) + " " + label.substring(i+1); len++;}
        }
        label.toLowerCase();

        return label.trim();
    }

}
