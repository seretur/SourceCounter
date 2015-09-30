/*
 * Copyright (C) 2015 jorge Ramirez (seretur)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sourcecounter;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaConstructor;
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
     // define representing metrics variables
        int NOM,NOC,CC,PM,CALL,FOUT,MSLOC,MCL;
    
    public Analyzer(){
        // initialize all variables
        NOM=0;
        NOC=0;
        CC=1;
        PM=0;
        CALL=0;
        FOUT=0;
        MSLOC=0;
        MCL=0;
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
                
                // extracting metrics from constructors
                List<JavaConstructor> constructores=clase.getConstructors();
                Iterator itCo=constructores.iterator();
                while(itCo.hasNext()){
                    JavaConstructor cons=(JavaConstructor)itCo.next();
                    CallCounter cc=new CallCounter(listaClases);
                    int llamanThis=cc.differentCalls(cons.getSourceCode(),clase,cons.getName());
                    String fuenteCons=cons.getSourceCode();
                    CALL+=llamanThis;
                    FOUT+=cc.fanout(cons.getName(),fuenteCons,clase);
                    int j=1;
                    j=new CCCounter().getCC(fuenteCons);
                    CC+=j;
                }
                
                Iterator<JavaMethod> itera=listaMetodos.iterator();
                while (itera.hasNext()){
                    JavaMethod metodo=itera.next();
                    CallCounter cc=new CallCounter(listaClases);
                    int llamanThis=cc.differentCalls(metodo.getSourceCode(),clase,metodo.getName());
                    CALL+=llamanThis;
                    if (metodo.isPublic())
                    {
                        PM+=1;
                    }
                   
                    // get and 'clean' method source code
                    String fuente=metodo.getSourceCode();
                    String copiaFuente=fuente;
                    FOUT+=cc.fanout(metodo.getName(),fuente,clase);
                    SourceCleaner cleaner=new SourceCleaner();
                    if (cleaner.separate(fuente)){
                       fuente=cleaner.getCleaned();
                       copiaFuente=cleaner.getCommentingLines();
                       MSLOC+=cleaner.getSCL();
                       MCL+=cleaner.getCLNumber();
                    }
                      
                    int j=1;
                    j=new CCCounter().getCC(fuente);
                   CC+=j;
                    
                }
            }
       } else{
            System.out.println("Files doesn't found");
        }
        
        // metric list adding to ml
        Metric mnom=new Metric("NOM",NOM);
        ml.agregar(mnom);
        Metric mpn=new Metric("PM",PM);
        ml.agregar(mpn);
        Metric mcc=new Metric("CC",CC);
        ml.agregar(mcc);
        Metric mcall=new Metric("CALL",CALL);
        ml.agregar(mcall);
        Metric mfout=new Metric("FOUT",FOUT);
        ml.agregar(mfout);
        
        Metric scl=new Metric("MSCL",MSLOC);
        ml.agregar(scl);
        Metric cl=new Metric("CL",MCL);
        ml.agregar(cl);
        
         System.out.println("Finished");
        
    }
    
}
