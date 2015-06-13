/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jorge
 */
public class CallCounter {
    Collection<JavaClass>clases;
    String aver;
    ArrayList<String> nombreClases;
    
    public CallCounter(Collection<JavaClass> c){
        clases=c;
        Iterator itcl=clases.iterator();
        nombreClases=new ArrayList<String>();
        
        //carga de nombres de clases
        while(itcl.hasNext()){
            JavaClass revisado=(JavaClass)itcl.next();
            nombreClases.add(revisado.getName());
        }
        
    }
    /**
     * This method iterates over all classes in the projects and counts 
     * those who matches an specified method
     * @param met method name
     * @return 
     */
    
    public int contarLlamadores(String met){
        int ll=0;
        Iterator<JavaClass> it=clases.iterator();
        
        while (it.hasNext()){
            //visit classes
            JavaClass visitado=it.next();
            List<JavaMethod> listaMetodos=visitado.getMethods();
            
            Iterator<JavaMethod> itm=listaMetodos.iterator();
            
            // visit all methods
            while(itm.hasNext()){
                JavaMethod actual=itm.next();
            if (!actual.getName().equals(met)){
                    List<JavaType> parametros=actual.getParameterTypes();
                    Iterator itmet=parametros.iterator();
                    boolean ya=false;
                    while(itmet.hasNext() && !ya) {
                        JavaType pactu=(JavaType)itmet.next();
                        if (pactu.getFullyQualifiedName().contains(met)){
                            ll++;
                            ya=true;
                        }

                    }

                    if (!ya){
                        String fuente=actual.getSourceCode();
                        if (fuente.indexOf(met)>=0){
                            ll++;
                        }
                    }
                }
            }
        }
        
        return ll;
    }
    
    /**
     * Compute different calls identified in a method source code
     * This method extracts call candidates by using regular expressiones
     * Then discards casts 
     * And add the name to an HashSet 
     * @param metodo method analyzed
     * @return Number of different calls
     */
    
    public int differentCalls(JavaMethod metodo){
        int calls;
        
        String fuente=metodo.getSourceCode();
        String[] lineas=fuente.split("\\n");
        HashSet<String> llamadas=new HashSet();
        Pattern pattern1 = Pattern.compile("[a-zA-Z]*\\S[^(][(][a-zA-Z]*[)]");
        for (String linea:lineas){
            Matcher matcher=pattern1.matcher(linea);
            if (matcher.find()){
                String retrieved=matcher.group(0);
                if ((!retrieved.contains("=(")) && (!retrieved.contains("(("))){
                    String methodName=retrieved.substring(0,retrieved.indexOf("("));
                    llamadas.add(methodName);
                    }
                } 
        }
        calls=llamadas.size();
        return calls;
    }
    
    /**
     * devuelve el fanOut(Henry y Kafura) de un método pasado por parámetro
     * @param met JavaMethod método a analizar, instancia de JavaMethod
     * @return suma de las clases referenciadas
    */
   
    public int fanout(JavaMethod met){
        // first, count different classes in parameters list
        int clasesReferidas=met.getParameterTypes().size();
        // count classes referred inside method's code
        String cuerpo=met.getSourceCode();
        for (String nombreRevisado : nombreClases) {
            if (cuerpo.contains(nombreRevisado)){
                clasesReferidas++;
            }
        }
        
        return clasesReferidas;
    }
    
    
    
}