/**
 * 
 */
package test;

import loadModel.LoadModel;
import loadModel.Solution;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;

/**
 * @author mariane
 *
 */
public class TestKDMtoUMLFile {

	@Test
	public void test() {
		String path = "/home/mariane/workspace/ExtractModel/";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("ExtractModel").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		loadModel.showSizeOfMaps();
	}
	
	@Test
	public void test1() {
		String path = "/home/mariane/workspace/ExtractModel/";

		LoadModel loadModel = new LoadModel(URI.createFileURI(path).appendSegment("ExtractModelTESTE").
				appendFileExtension(UMLResource.FILE_EXTENSION));

		Solution s = loadModel.buildSoluction();
		loadModel.showSizeOfMaps();
	}
}
