/*
 * Copyright (C) 2015 jorge
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

package sourcecounter.testing;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sourcecounter.SourceCleaner;

/**
 *
 * @author jorge
 */
public class TestAPI {

    public static void main(String args[]){
          String s="/home/jorge/Documentos/videojuegos/desarrollos/azares";
          JavaProjectBuilder project=new JavaProjectBuilder();
        System.out.println("Starting...");
        project.addSourceTree(new File(s)); 
        
        Collection<JavaClass> listaClases=project.getClasses();
        
        JavaClass[] listado;
        listado=new JavaClass[listaClases.size()];
        Iterator itLC=listaClases.iterator();
        int pos=0;
        while (itLC.hasNext()){
            listado[pos]=(JavaClass)itLC.next();
            pos++;
        }
        
        JavaClass probatus=listado[1];
        
        
        List<JavaMethod> metodos=probatus.getMethods();
        
        System.out.println("Prueba API Qdox");
        System.out.println("Encontradas "+listaClases.size()+ " Clases");
        System.out.println("Clase: "+probatus.getCanonicalName());
        
        System.out.println("Métodos");
        
        for (JavaMethod metact:metodos){
            System.out.println(metact.getName());
             int calls;
        
        String fuenteCruda=metact.getSourceCode();
        SourceCleaner limpia=new SourceCleaner();
        boolean separado= limpia.separate(fuenteCruda);
        String fuente=limpia.getCleaned();
        
        String[] lineas=fuente.split("\\n");
        HashSet<String> llamadas=new HashSet();
        
        Pattern pattern1 = Pattern.compile("[a-zA-Z]*\\S[^(][(][a-zA-Z]*[)]");
        for (String linea:lineas){
        Matcher matcher=pattern1.matcher(linea);
        if (matcher.find()){
          String retrieved=matcher.group(0);
          if ((!retrieved.contains("=(")) && (!retrieved.contains("((")) && (! retrieved.contains("return"))){
           String methodName=retrieved.substring(0,retrieved.indexOf("("));
           llamadas.add(methodName);
           System.out.println("---- llamada: "+methodName);
           System.out.println("Revisando clases ");
           for (JavaClass clase:listado){
               if (isAMethodOf(methodName,clase)){
                   if (!clase.equals(probatus)){
                   System.out.println("de la clase: "+clase.getName());
                   }
               }
           }
           
               }
            } 
        }
        calls=llamadas.size();
        System.out.println("llamadas encontradas en "+metact.getName()+": "+calls);
        }
        
        
    }
public static boolean isAMethodOf(String met, JavaClass c){
    List<JavaMethod> metodos=c.getMethods();
    // HashSet<String> nombresMetodos=new HashSet<String>();
    boolean esta=false;
    for (JavaMethod metact:metodos){
        String nombreActual=metact.getName();
        if (nombreActual.equals(met)){
            esta=true;
            break;
        }
    }
    return esta;
}
}
