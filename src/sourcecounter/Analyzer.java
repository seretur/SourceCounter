/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.io.File;
import java.io.IOException;
// import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author jorge
 */
public class Analyzer {
    int NOM,NOC,CC,PM,CALL;
    
    public Analyzer(){
        NOM=0;
        NOC=0;
        CC=1;
        PM=0;
        CALL=0;
    }
    
    public void analyze(String s, MetricsList ml){
        JavaProjectBuilder project=new JavaProjectBuilder();
        System.out.println("Starting...");
        project.addSourceTree(new File(s)); //TODO directory validation
        
        Collection<JavaClass> listaClases=project.getClasses();
        if (listaClases.size()>0) {
            NOC=listaClases.size();
            Metric mnoc=new Metric("NOC",NOC);
            ml.agregar(mnoc);
            System.out.println("encontré "+listaClases.size()+" clases ");
            
            //browsing classes
            List<JavaMethod> listaMetodos;
            Iterator<JavaClass> it=listaClases.iterator();
            while (it.hasNext()){
                JavaClass clase=it.next();
                listaMetodos=clase.getMethods();
                NOM+=listaMetodos.size();
                
                //browsing methods
                Iterator<JavaMethod> itera=listaMetodos.iterator();
                while (itera.hasNext()){
                    JavaMethod metodo=itera.next();
                    CallCounter cc=new CallCounter(listaClases);
                    int llamanThis=cc.contarLlamadores(metodo.getName());
                    CALL+=llamanThis;
                    if (metodo.isPublic())
                    {
                        PM+=1;
                    }
                    
                    String fuente=metodo.getSourceCode();
                    int j=1;
                    Pattern patif;
                    patif = Pattern.compile("if|else|case|default|catch|for|//&//&|//?");
                    
                   Matcher matif=patif.matcher(fuente);
                   while (matif.find()){
                       j++;
                   }
                   // System.out.println("CC del método "+metodo.getName()+":"+j);
                   CC+=j;
                    
                }
            }
       } else{
            System.out.println("Files doesn't found");
        }
        Metric mnom=new Metric("NOM",NOM);
        ml.agregar(mnom);
        Metric mpn=new Metric("PN",PM);
        ml.agregar(mpn);
        Metric mcc=new Metric("CC",CC);
        ml.agregar(mcc);
        Metric mcall=new Metric("CALL",CALL);
        ml.agregar(mcall);
        
         System.out.println("Finished");
        
    }
    
}
