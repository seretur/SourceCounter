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

package sourcecounter.Facades;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import sourcecounter.Analyzer;
import sourcecounter.Metric;
import sourcecounter.MetricsList;
import nu.xom.*;
// import java.util.ArrayList;


/**
 *
 * @author jorge ramirez
 */
public class SourceCounter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        Double maxia=0.66;
        Double maxec=13.0;
 
        String s="/home/jorge/soft/javas/jfig/jfig-1.4.2/src";
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
        double ia=(double)metricas.getValue("FOUT")/metricas.getValue("CALL");
        System.out.println("-------------");
        System.out.println("Estructuración de clases: "+ec);
        System.out.println("Intensidad de Acoplamiento: "+ia);
        System.out.println("-------------");
        
        if (ec<maxec) {
            System.out.println("Estructuración de clases menor que el umbral ");
        } else {
            System.out.println("ADVERTENCIA: Estructuración de clases muy elevada");
        }
        
        if (ia>maxia){
            System.out.println("La intensidad de acoplamiento puede ser muy elevada");
        }
        
        double wmc=(double)metricas.getValue("CC")/metricas.getValue("NOM");
        System.out.println("WMC: "+wmc);
        
        if (args.length>1){
            //generate an XML file
            Element raiz=new Element("root");
        Document docu=new Document(raiz);
        
        Element metrics=new Element("metrics");
            for (Metric m:resultados){
                Element mt=new Element("metric");
                Attribute mtname=new Attribute("name",m.getName());
                String valac=String.valueOf(m.getValue());
                Attribute mtval=new Attribute("value",valac);
                mt.addAttribute(mtname);
                mt.addAttribute(mtval);
                metrics.appendChild(mt);
            }
            Element projName=new Element("Project");
            Attribute pname=new Attribute("name",args[1]);
            projName.addAttribute(pname);
            
            if (args.length>2) {
                Attribute version=new Attribute("version",args[2]);
                projName.addAttribute(version);
            }
            raiz.appendChild(projName);
            raiz.appendChild(metrics);
            // String texto=docu.toXML();
            // System.out.println(texto);
            
            FileOutputStream fileOutputStream = new FileOutputStream (args[1]);
            Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
            serializer.setIndent(4);
            serializer.write(docu);
        }
        
        
        
    }
        
        
    
}
