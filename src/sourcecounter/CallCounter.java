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
    HashSet<String> nombreClases;
    boolean yaContados;
    int fouts;
    
    public CallCounter(Collection<JavaClass> c){
        clases=c;
        Iterator itcl=clases.iterator();
        nombreClases=new HashSet<String>();
        yaContados=false;
        fouts=0;
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
    
    public int differentCalls(String fn, JavaClass clase, String nm){
        int calls;
        fouts=0;
        System.out.println("Revisado método "+nm+" de "+clase.getName());
        String fuente=fn;
        String[] lineas=fuente.split("\\n");
        HashSet<String> llamadas=new HashSet();
        Pattern pattern1 = Pattern.compile("[a-zA-Z]*\\S[^(][(][a-zA-Z]*[)]");
        for (String linea:lineas){
        Matcher matcher=pattern1.matcher(linea);
        while (matcher.find()){
          String retrieved=matcher.group(0);
          if ((!retrieved.contains("=(")) && (!retrieved.contains("((")) && (! retrieved.contains("return"))){
           String methodName=retrieved.substring(0,retrieved.indexOf("("));
           llamadas.add(methodName);
           System.out.println("llamada "+methodName+" desde el método "+nm+" de la clase "+clase);
           for (JavaClass revisante:clases){
               if (!revisante.equals(clase)){
                   if (isAMethodOf(methodName,revisante)){
                       System.out.println("Hallado "+methodName+" de la clase "+revisante.getName());
                   fouts++; //descartar llamadas a la clase en revisación
                   break;
                   }                   
               }
             }
               }
            } 
        }
        yaContados=true;
        calls=llamadas.size();
        return calls;
    }
    
    /**
     * devuelve el fanOut(Henry y Kafura) de un método pasado por parámetro
     * @param met JavaMethod método a analizar, instancia de JavaMethod
     * @return suma de las clases referenciadas
    */
   
    public int fanout(String nm, String fnt, JavaClass deClase){
        // first, count different classes in parameters list
        int resul=0;
        if (yaContados) {
            resul=fouts;
        } else {
            int calls=differentCalls(fnt,deClase,nm);
            resul=fouts;
        }
        yaContados=false;
        return resul;
    }
    
    public boolean isAMethodOf(String met, JavaClass c){
    List<JavaMethod> metodos=c.getMethods();
    // HashSet<String> nombresMetodos=new HashSet<String>();
    boolean esta=false;
    if (met.equals(c.getName())){
        esta=true;
    }
    for (JavaMethod metact:metodos){ 
        if (esta) {
            break;
           }
        String nombreActual=metact.getName();
        if (nombreActual.equals(met)){
            esta=true;
        }  
    }
    return esta;
}
    
}