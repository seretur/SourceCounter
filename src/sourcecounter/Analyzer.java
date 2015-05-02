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

/**
 *
 * @author jorge
 * Class for extract source metrics from a source directory 
 */
public class Analyzer {
    // Metrics Variables
    int NOM,NOC,CC,PM,CALL,FOUT;
    
    public Analyzer(){
        NOM=0;
        NOC=0;
        CC=1;
        PM=0;
        CALL=0;
        FOUT=0;
    }
    /**
     * Capture a set of metrics from sources under a directory
     * @param s root directory of sources to scan
     * @param ml array list where metrics will be stored
     */
    
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
                int totalMetodos=listaMetodos.size()+clase.getConstructors().size();
                NOM+=totalMetodos;
                System.out.println("Revisando la clase "+clase.getName()+" con "+totalMetodos);
                //browsing methods
                Iterator<JavaMethod> itera=listaMetodos.iterator();
                while (itera.hasNext()){
                    JavaMethod metodo=itera.next();
                    CallCounter cc=new CallCounter(listaClases);
                    int llamanThis=cc.differentCalls(metodo);
                    CALL+=llamanThis;
                    if (metodo.isPublic())
                    {
                        PM+=1;
                    }
                    FOUT+=cc.fanout(metodo);
                    // get and 'clean' method source code
                    String fuente=metodo.getSourceCode();
                    String copiaFuente=fuente;
                    SourceCleaner cleaner=new SourceCleaner();
                    if (cleaner.separate(fuente)){
                       fuente=cleaner.getCleaned();
                       copiaFuente=cleaner.getCommentLines();
                    }
                      
                    int j=1;
                    j=new CCCounter().getCC(fuente);
                    if (j<3){
                        System.out.println("Fuentes analizadas");
                        System.out.println(metodo.getName());
                        System.out.println(copiaFuente);;
                        System.out.println("-------------");
                        System.out.println(fuente);
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
        Metric mfout=new Metric("FOUT",FOUT);
        ml.agregar(mfout);
        
         System.out.println("Finished");
        
    }
    
}
