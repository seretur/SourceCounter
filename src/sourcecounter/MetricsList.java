/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import java.util.ArrayList;


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
    
    public int getValue(String met){
        int resp=0;
        for (Metric m:lista){
            if (m.getName().equals(met)) {
                resp=m.getValue();
            }
        }
        return resp;
    }
}
