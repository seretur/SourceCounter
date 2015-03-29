/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jorge
 */
public class CallCounter {
    Collection<JavaClass>clases;
    String aver;
    
    public CallCounter(Collection<JavaClass> c){
        clases=c;
    }
    
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
                if (actual.getName().contains(met)){
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
                } else {
                }
            }
        }
        
        return ll;
    }
    
    
    
}
