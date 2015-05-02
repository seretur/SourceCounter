/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter.Facades;

import java.io.IOException;
import java.util.ArrayList;
import sourcecounter.Analyzer;
import sourcecounter.Metric;
import sourcecounter.MetricsList;
// import java.util.ArrayList;


/**
 *
 * @author jorge
 */
public class SourceCounter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Testing DirectoryReader
        
//        DirectoryReader lector=new DirectoryReader("/home/jorge/");
//        ArrayList<String> archivos=lector.getDirs();
//        Iterator itl=archivos.iterator();
//        while(itl.hasNext()){
//            System.out.println(itl.next());
//        }
        
        String s="/home/jorge/NetBeansProjects/SourceCounter/src";
        if (args.length!=0){
            s=args[0];
        } 
        Analyzer revisador=new Analyzer();
        MetricsList metricas=new MetricsList();
        
        revisador.analyze(s, metricas);
        
        ArrayList<Metric> resultados=metricas.getMetricsList();
        for (Metric m : resultados) {
            System.out.println(m.toString());
        }
        double ec=(double)metricas.getValue("NOM")/metricas.getValue("NOC");
        double ia=(double)metricas.getValue("CALL")/metricas.getValue("NOM");
        System.out.println("Estructuración de clases: "+ec);
        System.out.println("Intensidad de Acoplamiento: "+ia);
        
        double wmc=(double)metricas.getValue("CC")/metricas.getValue("NOM");
        System.out.println("WMC: "+wmc);
        
        
        
    }
        
        
    
}
