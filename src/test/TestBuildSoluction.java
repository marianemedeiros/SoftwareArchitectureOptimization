/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import loadModel.LoadModel;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;

/**
 * @author mariane
 *
 */
public class TestBuildSoluction {

	@Test
	public void testWithLayeredArchitecturalStyle() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		loadModel.buildSoluction();
		assertEquals(loadModel.componentClasses.size(), (Object)2);
		//assertEquals(loadModel.componentClasses.get(0).size(), (Object)2);
		//assertEquals(loadModel.componentClasses.get(1).size(), (Object)2);
		
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void testWithClientServerArchitecturalStyle() {
		String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("model").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		loadModel.buildSoluction();
		//assertEquals(loadModel.componentClasses.get(0).size(), (Object)2);
		//assertEquals(loadModel.componentClasses.get(1).size(), (Object)2);
		
		loadModel.showSizeOfMaps();
	}
	
	
	


}
