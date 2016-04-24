/**
 * 
 */
package aco.entities;

import loadModel.LoadModel;
import loadModel.Solution;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;
import metrics.architecture.ModulatizationQuality;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * @author mariane
 *
 */
public class Architecture {
	
	private LoadModel loadModel;
	private AntSystem antSystem;
	private Solution solution;
	
	public Architecture(String pathUmlFile, String nameFile) throws Exception {
		loadModel = new LoadModel(URI.createFileURI(pathUmlFile).appendSegment(nameFile).
				appendFileExtension(UMLResource.FILE_EXTENSION));
		
		solution = loadModel.buildSoluction();
	}

	public void initAntSystem() throws Exception{
	
		if(this.solution.interfaces != null && this.solution.internalRelations != null){// arquitetura ja estruturada
			MetricCoupling coupling = new MetricCoupling();
			this.solution = coupling.calculate(solution);

			MetricCoesion coesion = new MetricCoesion();
			this.solution = coesion.calculate(solution);

			ModulatizationQuality  modulatizationQuality = new ModulatizationQuality();
			this.solution = modulatizationQuality.calculate(solution);

			System.out.println("\n Initial Architecture Defined!!! \n So architecture style that will be evaluated is <<" + this.solution.type + ">>");
			System.out.println(" Modularization Qualidaty of that architecture: " + this.solution.mMetric);
			
			Probability probability = new Probability(new Matrix(solution.componentClasses.size(), solution.classComponent.size()));
			Matrix p = probability.verifyRelation(solution);
			antSystem = new AntSystem(p,solution);
			
			
		}else{// para arquiteturas com componentes e classes soltos

			System.out.println("\n No established architecture!!!");
			antSystem = new AntSystem(this.solution.number_comp, this.solution.number_class);
		}
		
		Solution generatedSolution = antSystem.execute();
		loadModel.showSolution(generatedSolution);
	}
}
