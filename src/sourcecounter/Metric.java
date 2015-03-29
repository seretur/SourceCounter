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
public class Metric {
    private String name;
    private int value;
    private String Description;
    
    public Metric(String n){
        name=n;
        value=0;
    }
    
    public Metric(String n, int v){
        name=n;
        value=v;
    }
    
    public String getName(){
        return name;
    }
    
    public void setValue(int v){
        value=v;
    }
    
    public int getValue(){
        return value;
    }
    
    @Override
    public String toString(){
        String s=name+": "+value;
        return s;
    }
    
}
