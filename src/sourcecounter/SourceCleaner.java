/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

/**
 *
 * @author jorge
 */
public class SourceCleaner {
    int lc;
    String cleaned, comments;
    
    public SourceCleaner(){
        lc=0;
        cleaned="";
        comments="";
    }
    /**
     * Separate comments lines and source lines
     * @param sror Original Source Code
     * @return 
     */
    
    public boolean separate(String sror){
        boolean resp=false;
        String[] lineas=sror.split("\\n");
        System.out.println("Líneas del método: "+lineas.length);
        for (String linea : lineas) {
            String l = linea.trim();
            l=l+"\n";
            if (l.contains("//") || l.startsWith("/*") || l.startsWith("*")){
                comments=comments.concat(l);
                lc++;
                //System.out.println(l);
            } else {
                cleaned=cleaned.concat(l);
            }
        }
        if (cleaned.length()>0){
            resp=true;
        }
        return resp;
    }
 
   /**
    * resets Cleaner attributes
    */
    public void reset(){
        lc=0;
        cleaned="";
        comments="";
    }
    
    /**
     * return sourcecode without comments
    */
    public String getCleaned(){
        return cleaned;
    }
    
    public String getCommentLines(){
        return comments;
    }
    
}
