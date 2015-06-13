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

package sourcecounter.testing;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sourcecounter.Analyzer;
import sourcecounter.CCCounter;
import sourcecounter.Metric;
import sourcecounter.MetricsList;
import sourcecounter.SourceCleaner;

/**
 *
 * @author jorge
 */
public class ShowMetrics {
    
   public static void main(String[] args) throws IOException{
       JavaProjectBuilder testProject=new JavaProjectBuilder();
       File fuente=new File("/home/jorge/soft/javas/pruebas/CreateASet.java");
       testProject.addSource(fuente);
       
       Collection<JavaClass>clases=testProject.getClasses();
       
       for (JavaClass clase:clases){
           System.out.println("analizando la clase "+clase.getName());
           List<JavaMethod> metodos=clase.getMethods();
           CCCounter mideCC=new CCCounter();
           for (JavaMethod metodo:metodos){
               System.out.println("----> Analizando el método: "+metodo.getName());
               String codigo=metodo.getSourceCode();
               
               System.out.println("COMPLEJIDAD CICLOMATICA: ");
               SourceCleaner cleaner=new SourceCleaner();
               cleaner.separate(codigo);
               String fuenteSinComent=cleaner.getCleaned();
               CCCounter ccc=new CCCounter();
               System.out.println(ccc.getCC(fuenteSinComent));
               
           }
       }
   }
}
