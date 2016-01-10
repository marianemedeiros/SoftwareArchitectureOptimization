/**
 * 
 */
package org.eclipse.uml2.examples.gettingstarted;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * @author mariane
 *
 */
public class MyTry {
	
	public static void main(String[] args)
			throws Exception {
		Model model = GettingStartedWithUML2.createModel("myModel");
		
		Class class1 = GettingStartedWithUML2.createClass(model, "class1", false);
		Class class2 = GettingStartedWithUML2.createClass(model, "class2", false);
		Class class3 = GettingStartedWithUML2.createClass(model, "class3", false);
		
		Interface interface1 = GettingStartedWithUML2.createInterface(model, "interface1");
		Interface interface2 = GettingStartedWithUML2.createInterface(model, "interface2");
		
		Component component1 = GettingStartedWithUML2.createComponent(model, "component1");
		
		Realization realization1 = GettingStartedWithUML2.createRealization(model, "realization1", interface1, class1);
		
		// Save our model to a file in the user-specified output directory
		File outputDir = new File("/home/mariane/workspace/org.eclipse.uml2.examples.gettingstarted/");
		URI outputURI = URI.createFileURI(outputDir.getAbsolutePath()).appendSegment("myExample")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		GettingStartedWithUML2.banner("Saving the model to %s.", outputURI.toFileString());		
		GettingStartedWithUML2.save(model, outputURI);
	}
			

}
