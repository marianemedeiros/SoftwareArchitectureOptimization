/**
 * 
 */
package aco.entities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import loadModel.LoadModel;
import loadModel.Solution;
import main.Main;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;
import metrics.architecture.ModulatizationQuality;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * @author mariane
 *
 */
public class Architecture {
	private Logger logger = Logger.getLogger(LoadModel.class);
	
	private LoadModel loadModel;
	private AntSystem antSystem;
	private Solution solution;
	
	public Architecture(String pathUmlFile, String nameFile) throws Exception {
		loadModel = new LoadModel(URI.createFileURI(pathUmlFile).appendSegment(nameFile).
				appendFileExtension(UMLResource.FILE_EXTENSION));
		
	    long startTime = System.currentTimeMillis();
	    solution = loadModel.buildSoluction();
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    
	    if(elapsedTime > 1000 && elapsedTime < 6000)
	    	System.out.println((int) ((elapsedTime / 1000) % 60) + " seconds, to recovery the architecture.");
	    else if(elapsedTime >= 6000)
    		System.out.println((int) ((elapsedTime / 1000) / 60) + " minutes, to recovery the architecture..");
	    else
	    	System.out.println(elapsedTime + " miliseconds, to recovery the architecture.");
	    


	    
	    HashMap<Integer, Integer> id2newId;
		//para quando tem pacotes que estao vazios, pois dentro deles só tem mais pacotes
		if(loadModel.getMapId2ClassName().size() > solution.componentClasses.size()){
			int x = 0;
			id2newId = new HashMap<Integer, Integer>();
			HashMap<Integer, Set<Integer>> novoMapcomponentClass = new HashMap<Integer, Set<Integer>>();
			for (Entry<Integer, Set<Integer>> element : solution.componentClasses.entrySet()) {
//				System.out.println("Component: " + element.getKey());
				id2newId.put(x, element.getKey());
				novoMapcomponentClass.put(x, element.getValue());
				x++;
			}
			solution.componentClasses = novoMapcomponentClass;
		}
		
		
		
//		loadModel.showSizeOfMaps();
	}

	public void initAntSystem() throws Exception{
		if(this.solution.interfaces != null && this.solution.internalRelations != null){// arquitetura ja estruturada
			MetricCoupling coupling = new MetricCoupling();
			this.solution = coupling.calculate(solution);

			MetricCoesion coesion = new MetricCoesion();
			this.solution = coesion.calculate(solution);

			ModulatizationQuality  modulatizationQuality = new ModulatizationQuality();
			this.solution = modulatizationQuality.calculate(solution);

			if(this.solution.type.equals(""))
				System.out.println("\n Initial Architecture Defined without any style architectural!!!");
			else
				System.out.println("\n Initial Architecture Defined!!! \n So architecture style that will be evaluated is <<" + this.solution.type + ">>");

			
			logger.info(" Modularization Qualidaty of that architecture: " + this.solution.mMetric);
			logger.info("Iterations: " + Main.ITERATIONS + " Ants: " + Main.ANTS + " Ro: " + Main.RO + " Alpha: " + Main.ALPA + " Beta: " + Main.BETA);
			System.out.println(" Number of components: " + solution.componentClasses.size() + "\n Number of classes: " + solution.classComponent.size());
			
//			for (Entry<Integer, Set<Integer>> element : solution.componentClasses.entrySet()) {
//				System.out.println("Component: " + element.getKey());
//			}
			
			//Probability probability = new Probability(new Matrix(solution.componentClasses.size(), solution.classComponent.size()));
			//Matrix p = probability.verifyRelation(solution);
			Matrix m = new Matrix(solution.componentClasses.size(), solution.classComponent.size());
			antSystem = new AntSystem(m,solution);
			logger.info("\n");
		}else{// para arquiteturas com componentes e classes soltos

			System.out.println("\n No established architecture!!!");
			antSystem = new AntSystem(this.solution.number_comp, this.solution.number_class);
		}
		
		long startTime = System.currentTimeMillis();
		
		Solution generatedSolution = antSystem.execute();
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;

	    if(elapsedTime > 1000 && elapsedTime < 6000)
	    	System.out.println((int) ((elapsedTime / 1000) % 60) + " seconds to optimize architecture.");
	    else if(elapsedTime >= 6000)
    		System.out.println((int) ((elapsedTime / 1000) / 60) + " minutes to optimize architecture.");
	    else
	    	System.out.println(elapsedTime + " miliseconds to optimize architecture.");
		//loadModel.showSolution(generatedSolution);
	}
}
