/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author jorge
 */
public class MetricsList {
    ArrayList<Metric> lista;
    
    public MetricsList(){
        lista=new ArrayList<Metric>();
    }
    
    public void agregar(Metric m){
        lista.add(m);
    }
    
    public ArrayList<Metric> getMetricsList(){
        return lista;
    }
    /**
     * 
     * @param met Metric name
     * @return metric value
     */
    public int getValue(String met){
        int resp=0;
        for (Metric m:lista){
            if (m.getName().equals(met)) {
                resp=m.getValue();
            }
        }
        return resp;
    }
    
    /**
     * Return the metric list as CSV
     * MAybe it would be splitted 
     * @return comma separated metric list
     */
    
    public String getCSV(){
        String titulos, valores;
        titulos="";
        valores="";
        for (Metric metrica:lista){
            titulos.concat(metrica.getName());
            titulos+=",";
            
            valores.concat(Integer.toString(metrica.getValue()));
            valores+=",";
        }
        
        String csv=titulos+"\n"+valores;
        return csv;
        
    }
}
