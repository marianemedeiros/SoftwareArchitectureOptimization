/**
 * 
 */
package StructuredArch;

import static org.junit.Assert.*;
import loadModel.LoadModel;
import loadModel.Solution;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;
import metrics.architecture.ModulatizationQuality;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;

import aco.entities.AntSystem;
import aco.entities.Matrix;
import aco.entities.Parametro;
import aco.entities.Probability;
import architecture.ArchitectureExample;

/**
 * @author mariane
 *
 */
public class TestStructuredArch {
	public Parametro parametros = new Parametro(20, 10, 0.2, 0.9, 0.0);
	
	@Test
	public void test1() throws Exception {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";
		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));
		
		Solution solution = loadModel.buildSoluction();
		
		Probability probability = new Probability(new Matrix(solution.componentClasses.size(), solution.classComponent.size()),parametros);
		Matrix p = probability.verifyRelation(solution);
		AntSystem antSystem = new AntSystem(p,solution,parametros);
	}
	@Test
	public void test() throws Exception {
		ArchitectureExample architectureExample = new ArchitectureExample();
		Solution s =  architectureExample.generateSoluction6();
		
		assertEquals(s.componentClasses.size(), 3);
		assertEquals(s.classComponent.size(), 9);
		
		assertNotNull(s.classComponent);
		assertNotNull(s.componentLayer);
		
		// passa a matriz de feromonio inicializada com 0.5 em todas as posições,
		// então chama a função calculatesProbability que vai fazer o calculo das combinações já
		// existentes na Soluction e irá atualizar a matriz de ferômonio. Retornando a matriz de ferômonio 
		// condizente com a arquitetura já existente.
		Probability probability = new Probability(new Matrix(s.componentClasses.size(), s.classComponent.size()),parametros);
		
		Matrix p = probability.verifyRelation(s);
		assertEquals(s.componentClasses.size(), p.componentClass[0].length);
		assertEquals(s.classComponent.size(), p.classClass.length);
		
		// instancia a classe AntSystem passando a arquitetura inicial
		AntSystem antSystem = new AntSystem(p,s,parametros);
		antSystem.execute();
		
		// calculate metrics to this soluction
		MetricCoupling coupling = new MetricCoupling();
		s = coupling.calculate(s);

		MetricCoesion coesion = new MetricCoesion();
		s = coesion.calculate(s);

		ModulatizationQuality  modulatizationQuality = new ModulatizationQuality();
		s = modulatizationQuality.calculate(s);
		
		assertTrue("MQ of initial architecture (" + s.mMetric + ") should be greater than current MQ from new architecture (" + antSystem.colony.getBestAnt().getSolution().mMetric + ")", 
				s.mMetric > antSystem.colony.getBestAnt().getSolution().mMetric);
	}

}
