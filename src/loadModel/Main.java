/**
 * 
 */
package loadModel;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverKDMModelFromJavaProject;


/**
 * @author mariane
 *
 */
public class Main{
	public static void main(String[] args) throws DiscoveryException{
		String projectName = "ExtractModel";

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IJavaProject javaProject = JavaCore.create(project);

		//DiscoverJavaModelFromJavaProject discoverJavaModelFromJavaProject = new DiscoverJavaModelFromJavaProject();
		//discoverJavaModelFromJavaProject.setDeepAnalysis(false);
		
		// projeto to kdm model
		// https://www.eclipse.org/forums/index.php/t/628769/
		DiscoverKDMModelFromJavaProject kdmDiscoverer = new DiscoverKDMModelFromJavaProject();
		kdmDiscoverer.setSerializeTarget(true);
		
		kdmDiscoverer.discoverElement(javaProject, new NullProgressMonitor());
		Resource kdmResource = kdmDiscoverer.getTargetModel();

		//kdm to uml model
		//http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.modisco.infra.omg.doc%2Fmediawiki%2Fkdm_to_uml_converter%2Fuser.html
		//DiscoverUmlModelFromKdmModel discoverer = new DiscoverUmlModelFromKdmModel();
		//discoverer.discoverElement(kdmModelFile, new NullProgressMonitor());
		//Resource umlModel = discoverer.getTargetModel();

	}

}