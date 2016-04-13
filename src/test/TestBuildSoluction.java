/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import loadModel.LoadModel;
import loadModel.Solution;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;

/**
 * @author mariane
 *
 */
public class TestBuildSoluction {

	@Test
	public void testGetElementsSoltos() {
		String path = "/home/mariane/papyrus/elementosSoltos";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void testGetElements() {
		String path = "/home/mariane/workspace/aco2architecture/resources";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("component").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void testWithLayeredArchitecturalStyle() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		assertEquals("This architecture must be Layered", LoadModel.LAYER, s.type);
		assertEquals(loadModel.componentClasses.size(), (Object)2);
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void testWithClientServerArchitecturalStyle() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		assertEquals("This architecture must be Client/Server", LoadModel.CLIENT_SERVER, s.type);
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void testWithClientServerArchitecturalStyle2() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor2";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		assertEquals("This architecture must be Client/Server", LoadModel.CLIENT_SERVER, s.type);
		//loadModel.showSizeOfMaps();
		//loadModel.showMap();
	}
	
	@Test
	public void testWithClientServerArchitecturalStyle3() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor3";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		loadModel.showMap();
		loadModel.showSizeOfMaps();
		assertEquals("This architecture must be Client/Server", LoadModel.CLIENT_SERVER, s.type);
		
		//metricClientServerArch metricClientServerArch = new MetricClientServerArch();
		//metricClientServerArch.verifyStyle(s);
		//loadModel.showSizeOfMaps();
		//loadModel.showMap();
	}
	
	
	


}
