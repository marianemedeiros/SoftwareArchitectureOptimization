/**
 * 
 */
package metrics.style;

import static org.junit.Assert.assertNotNull;
import loadModel.Solution;

import org.junit.Test;

import aco.entities.AntSystem;
import aco.entities.Matrix;
import aco.entities.Probability;
import architecture.ArchitectureExample;

/**
 * @author mariane
 *
 */
public class TestLayerArch {

	@Test
	public void test() throws Exception {
		ArchitectureExample architectureExample = new ArchitectureExample();
		Solution s =  architectureExample.generateSoluction6();
		
		assertNotNull(s.classComponent);
		assertNotNull(s.componentLayer);
		
		Probability probability = new Probability(new Matrix(s.componentClasses.size(), s.classComponent.size()));
		Matrix p = probability.verifyRelation(s);
		
		// instancia a classe AntSystem passando a arquitetura inicial
		AntSystem antSystem = new AntSystem(p,s);
		antSystem.execute();
		
		Solution soluctionChoose = antSystem.colony.getBestAnt().getSolution();
		soluctionChoose.componentLayer = s.componentLayer;
		
		assertNotNull("Map classComponent can't be null", soluctionChoose.classComponent);
		assertNotNull("Map componentLayer can't be null", soluctionChoose.componentLayer);
		
		// matriz de feromonio atualizada de acordo com a penalidade dada
		Matrix newPheromoneMatrix = new Matrix(soluctionChoose.componentClasses.size(), soluctionChoose.classComponent.size());

		//MetricLayerArch layerArch = new MetricLayerArch();
		//newPheromoneMatrix = layerArch.verifyStyle(soluctionChoose, newPheromoneMatrix);
		newPheromoneMatrix.showMatrix();
		
		
		
	}

}
