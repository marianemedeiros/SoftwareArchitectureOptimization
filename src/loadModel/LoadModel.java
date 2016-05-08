/**
 * 
 */
package loadModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * @author mariane
 *
 */


public class LoadModel {
	private Logger logger = Logger.getLogger(LoadModel.class);
	
	public static final String W3C = "w3c";
	public static final String SAX = "sax";
	public static final String EXTERNALS = "externals";
	public static final String LAYER = "Layer";
	public static final String CLIENT_SERVER = "ClientServer";
	public static final String CLIENT = "Client";
	public static final String SERVER = "Server";

	public static final String package_ = "Package";
	public static final String component_ = "Component";
	public static final String class_ = "Class";
	public static final String interface_ = "Interface";
	public static final String usage_ = "Usage";
	public static final String interfaceRealization_ = "InterfaceRealization";
	public static final String association_ = "Association";


	public static final String base_Classifier = "base_Classifier";
	public static final String ID = "id";
	public static final String name = "name";
	public static final String supplier = "supplier";
	public static final String client = "client";

	private EStructuralFeature featureID;
	private EStructuralFeature featureBaseClassifier;

	private String architectureStyle = "";

	private static ResourceSet RESOURCE_SET;
	private Resource resource;

	private Integer class_id = 0;
	private Integer component_id = 0;
	private Integer interface_id = 0;
	private Integer layer_id = 0;

	private HashMap<String, Integer> mapClassName2Id;
	private HashMap<String, Integer> mapComponentName2Id;
	private HashMap<String, Integer> mapInterfaceName2Id;
	private HashMap<String, Integer> mapLayerName2Id;

	private HashMap<Integer, String> mapId2ClassName;
	private HashMap<Integer, String> mapId2ComponentName;
	private HashMap<Integer, String> mapId2InterfaceName;
	private HashMap<Integer, String> mapId2LayerName;


	private HashMap<Integer, String> mapClassServer;// map every class that realize an interface that are server
	private HashMap<Integer, String> mapClassClient; // map every class that realize an interface that are client


	public HashMap<Integer,Integer> classComponent;
	public HashMap<Integer, Set<Integer>> componentClasses ; // component - classes in component
	public HashMap<Integer,Integer> componentLayer; // componentId - layerId
	public ArrayList<Integer[]> interfaces_;
	public ArrayList<Integer[]> internalRelations;

	public LoadModel(URI uri) {
		// Create a resource-set to contain the resource(s) that we load and
		// save
		RESOURCE_SET = new ResourceSetImpl();

		// Initialize registrations of resource factories, library models,
		// profiles, Ecore metadata, and other dependencies required for
		// serializing and working with UML resources. This is only necessary in
		// applications that are not hosted in the Eclipse platform run-time, in
		// which case these registrations are discovered automatically from
		// Eclipse extension points.
		UMLResourcesUtil.init(RESOURCE_SET);

		mapClassName2Id = new HashMap<String, Integer>();
		setMapId2ClassName(new HashMap<Integer, String>());

		mapComponentName2Id = new HashMap<String, Integer>();
		mapId2ComponentName = new HashMap<Integer, String>();

		mapInterfaceName2Id = new HashMap<String, Integer>();
		mapId2InterfaceName = new HashMap<Integer, String>();

		classComponent = new HashMap<Integer, Integer>();
		componentClasses = new HashMap<Integer, Set<Integer>>();
		interfaces_ = new ArrayList<Integer[]>();
		internalRelations = new ArrayList<Integer[]>();

		load(uri);
	}


	private org.eclipse.uml2.uml.Package load(URI uri) {
		logger.info("Loading file " + uri.path());
		org.eclipse.uml2.uml.Package package_ = null;

		try {
			// Load the requested resource
			resource = RESOURCE_SET.getResource(uri, true);

			// Get the first (should be only) package from it
			package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), 
					UMLPackage.Literals.PACKAGE);
		} catch (WrappedException we) {
			//err(we.getMessage());
			logger.error("execption" + we.getMessage());
			System.exit(1);
		}

		return package_;
	}

	/**
	 * This method passes over the context of file and get all classes, components, layers and interfaces.
	 * */
	private void collectComponentsClassesInterfacesAndLayers(){
		for (TreeIterator<EObject> i = resource.getAllContents(); i .hasNext();) {
			EObject current = i.next();

			if (!(current instanceof NamedElement))
				i.prune();

			if(!(current instanceof ProfileApplication) && !(current instanceof EAnnotation) && 
					!(current instanceof ClassifierTemplateParameter) &&
					!(current instanceof TemplateBinding)){
				
				if(current instanceof Generalization){
					// TODO
				}
				else if(current instanceof AnyTypeImpl){
					AnyTypeImpl d = (AnyTypeImpl) current;
					if(d.eClass().getName().equals("Layered")){// layer
						architectureStyle  = LAYER;
						if(mapLayerName2Id == null && mapId2LayerName == null && componentLayer == null){
							mapLayerName2Id = new HashMap<String, Integer>();
							mapId2LayerName = new HashMap<Integer, String>();
							componentLayer = new HashMap<Integer, Integer>();
						}
						String name = (String) d.getAnyAttribute().get(1).getValue();
						mapId2LayerName.put(layer_id, name);
						mapLayerName2Id.put(name, layer_id);
						layer_id++;
					}else if(d.eClass().getName().equals(CLIENT)){
						architectureStyle = CLIENT_SERVER;
						if(mapClassClient == null){
							mapClassClient = new HashMap<Integer, String>();
						}
					}else if(d.eClass().getName().equals(SERVER)){
						architectureStyle = CLIENT_SERVER;
						if(mapClassServer == null){
							mapClassServer = new HashMap<Integer, String>();
						}
					}
				}
				else if(current instanceof DynamicEObjectImpl){ // get Layers
					DynamicEObjectImpl d = (DynamicEObjectImpl) current;
					featureBaseClassifier =  d.eClass().getEStructuralFeature(base_Classifier);

					if(d.eClass().getName().equals(LAYER)){// layer
						architectureStyle  = LAYER;
						if(mapLayerName2Id == null && mapId2LayerName == null && componentLayer == null){
							mapLayerName2Id = new HashMap<String, Integer>();
							mapId2LayerName = new HashMap<Integer, String>();
							componentLayer = new HashMap<Integer, Integer>();
						}
						featureID =  d.eClass().getEStructuralFeature(ID);
						mapId2LayerName.put(layer_id, d.eSetting(featureID).get(true).toString());
						mapLayerName2Id.put(d.eSetting(featureID).get(true).toString(), layer_id);
						layer_id++;
					}else if(d.eClass().getName().equals(CLIENT)){
						architectureStyle = CLIENT_SERVER;
						if(mapClassClient == null){
							mapClassClient = new HashMap<Integer, String>();
						}
					}else if(d.eClass().getName().equals(SERVER)){
						architectureStyle = CLIENT_SERVER;
						if(mapClassServer == null){
							mapClassServer = new HashMap<Integer, String>();
						}
					}
				}else{
					NamedElement asNamed = (NamedElement) current;
					if(asNamed.eClass().getName().equals(package_)){ // get Packages
						String componentName = asNamed.getLabel();
						//System.err.println(">>>> " + componentName);
						mapComponentName2Id.put(componentName,component_id); // map <<NAME,ID>>
						mapId2ComponentName.put(component_id, componentName); // map <<ID,NAME>>
						component_id++;
					}
					else if(asNamed.eClass().getName().equals(component_)){ // get Components
						String componentName = asNamed.getLabel();
						mapComponentName2Id.put(componentName,component_id); // map <<NAME,ID>>
						mapId2ComponentName.put(component_id, componentName); // map <<ID,NAME>>
						component_id++;

					}else if(asNamed.eClass().getName().equals(interface_) &&
							(!asNamed.getNamespace().getQualifiedName().contains(EXTERNALS) &&
									(!asNamed.getNamespace().getQualifiedName().contains(SAX)) &&
									(!asNamed.getNamespace().getQualifiedName().contains(W3C)))){ // get Interfaces
						String interfaceName = asNamed.getLabel();
						mapInterfaceName2Id.put(interfaceName,interface_id);
						mapId2InterfaceName.put(interface_id, interfaceName);
						interface_id++;

					}
					else if(asNamed.eClass().getName().equals(class_) &&
							(!asNamed.getNamespace().getQualifiedName().contains(EXTERNALS)) &&
							(!asNamed.getNamespace().getQualifiedName().contains(SAX)) &&
							(!asNamed.getNamespace().getQualifiedName().contains(W3C))){ // get Classes
						String className = asNamed.getLabel();
						//System.err.println("Class: " + className);
						mapClassName2Id.put(className,class_id);
						getMapId2ClassName().put(class_id, className);
						class_id++;
					}
				}
			}
		}
	}		

	/**
	 * This method passes over file and get all relationships that are relevant.
	 * 	RelationShips observed:
	 * 		<ul> Usage : that mark a relation between two classes in different components, that use an interface to communicate, or
	 * 			 mark a relationship between two classes in the same component.
	 * 		<ul> Class : that mark a relationship between a class and the component that the class belongs.
	 * 		<ul> DynamicEObjectImpl: mark a relationship between a component and the layer that the component belongs.
	 * */
	public Solution buildSoluction(){
		Solution solution = null;
		collectComponentsClassesInterfacesAndLayers();
		for (TreeIterator<EObject> i = resource.getAllContents(); i .hasNext();) {
			EObject current = i.next();

			if (!(current instanceof NamedElement))
				i.prune();

			if(!(current instanceof ProfileApplication) && !(current instanceof EAnnotation) &&
					!(current instanceof ClassifierTemplateParameter) &&
					!(current instanceof TemplateBinding)){
				
				if(current instanceof Generalization){
					// TODO
				}
				else if(current instanceof AnyTypeImpl){
					AnyTypeImpl d1 = (AnyTypeImpl) current;
					
				}
				else if(current instanceof DynamicEObjectImpl){ // verify stereotype
					DynamicEObjectImpl d = (DynamicEObjectImpl) current;
					stereotype_(d);
				}else{ // other types of elements
					NamedElement asNamed = (NamedElement) current;
					if(asNamed.eClass().getName().equals(class_)){
						if(!asNamed.getNamespace().getQualifiedName().contains(EXTERNALS) &&
								(!asNamed.getNamespace().getQualifiedName().contains(SAX)) &&
								(!asNamed.getNamespace().getQualifiedName().contains(W3C)))
							classes_(asNamed);
					}
					else if(asNamed.eClass().getName().equals(usage_)){
						Usage usage = (Usage) asNamed;
						usage_(usage);
					}else if(asNamed.eClass().getName().equals(association_)){
						Association a = (Association) asNamed;
						association_(a);
					}
				}
			}
		}
		if(architectureStyle.equals(LAYER)){
			solution = new Solution(componentClasses, interfaces_, internalRelations, componentLayer, classComponent);
		}else if(architectureStyle.equals(CLIENT_SERVER)){
			solution = new Solution(componentClasses, interfaces_, mapClassServer, mapClassClient, internalRelations, classComponent);
		}else if(architectureStyle.equals("") && interfaces_.size() == 0 && internalRelations.size() == 0){
			solution = new Solution(mapComponentName2Id.size(),mapClassName2Id.size());
		}else if(architectureStyle.equals("") && (interfaces_.size() != 0 || internalRelations.size() != 0)){
			logger.warn("Initial Architecture without style.");
			solution = new Solution(componentClasses, interfaces_, internalRelations, classComponent);
		}

		return solution;		
	}
	
	
	/**
	 * This function analyze association between two element from model. Association can be bidirectional or unidirectional. 
	 * @param a
	 */
	private void association_(Association a) {
		int index = 0;

		Integer[] cl1 = new Integer[2];
		Integer[] cl2 = new Integer[2];

		Interface[] inter = new Interface[2];
		int indexInter = 0;

		for (Type element : a.getEndTypes()) {
			if(element instanceof org.eclipse.uml2.uml.Class){
				cl1[index] = mapClassName2Id.get(element.getName());
				index++;
			}else if(element instanceof Interface){
				inter[indexInter] = (Interface) element;
				indexInter++;
			}

		}

		/* We consider just two kind of association:
		 * 1 - between two classes in the same component
		 * 2 - between two interfaces in different components. 
		 * 
		 * We need to remember that Association is used to bidirectional relationship. 
		 * To unidirectional relationship is used Usage.
		 * */
		if((classComponent.get(cl1[0]) == classComponent.get(cl1[1])) && cl1[0] != null && cl1[1] != null){
			internalRelations.add(cl1);
			cl2[0] = cl1[1]; cl2[1] = cl1[0];
			internalRelations.add(cl2);
			logger.info("<<"+ getMapId2ClassName().get(cl1[0]) + ">> and <<" + getMapId2ClassName().get(cl1[1]) +" >> have a bidirectional relationship,"
					+ " but they are in the same component." );

		}else if(inter[0] != null && inter[1] != null){
			Integer class0 = getWhoRealizeInterface(inter[0]); // class that realize inter[0]
			Integer class1 = getWhoRealizeInterface(inter[1]); // class that realize inter[1]

			if(class0 != -1 && class1 != -1 && classComponent.get(class0) != classComponent.get(class1)){
				Integer[] rl1 = new Integer[2];
				Integer[] rl2 = new Integer[2];
				rl1[0] = class0; rl1[1] = class1;
				rl2[0] = class1; rl2[1] = class0;
				interfaces_.add(rl1); interfaces_.add(rl2);
				logger.info("<<"+ getMapId2ClassName().get(class0) + ">> and <<" + getMapId2ClassName().get(class1) +
						" >> have a bidirectional relationship,"
						+ " and they are in different components." );
			}
		}
	}


	/**
	 * Get relationship between classes.
	 * @param usage
	 */
	private void usage_(Usage usage) {
		ArrayList<Integer> clients = new ArrayList<Integer>();
		ArrayList<Integer> relationships = new ArrayList<Integer>();

		for (NamedElement client : usage.getClients()) {
			if(client instanceof org.eclipse.uml2.uml.Class){
				Integer id = mapClassName2Id.get(client.getName());
				clients.add(id);
				verifyMapClassComponent((org.eclipse.uml2.uml.Class) client);
			}if(client instanceof org.eclipse.uml2.uml.Interface){
				clients = getWhoRealizeInterfaceAndAddToList((Interface) client);
			}
		}

		for (NamedElement s : usage.getSuppliers()) {
			if(s instanceof Interface){
				Interface inter = (Interface) s;
				relationships = getWhoRealizeInterfaceAndAddToList(inter);
			}else if(s instanceof org.eclipse.uml2.uml.Class){
				verifyMapClassComponent((org.eclipse.uml2.uml.Class) s);
				Integer idS = mapClassName2Id.get(s.getLabel());
				for (Integer client : clients) {
					if(classComponent.get(idS) == classComponent.get(client)){
						logger.info("<<"+ getMapId2ClassName().get(client) + ">> and <<" + getMapId2ClassName().get(idS) +" >> have a relation, but they "
								+ "are in the " );
						Integer[] r = new Integer[2];
						r[0] = client; r[1] = idS;
						internalRelations.add(r);
					}
				}
			}
		}
		if(!relationships.isEmpty()){
			for (Integer client : clients) {
				for (Integer relation : relationships) {
					Integer[] r = new Integer[2];
					r[0] = client; r[1] = relation;
					interfaces_.add(r);
					logger.info("Class <<" + getMapId2ClassName().get(client) + ">> and <<" + getMapId2ClassName().get(relation) + ">> have a relationship.");
				}
			}			
		}
	}


	/**
	 * 
	 * This method get what class realize the interface_.
	 * @param interface_
	 */
	//TODO por que colocar a classe que realiza a interface em uma lista, se vai ser sempre uma classe??? (VER PQ)
	private ArrayList<Integer> getWhoRealizeInterfaceAndAddToList(Interface interface_) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Relationship relationship : interface_.getRelationships()) {
			if(relationship instanceof InterfaceRealization){
				InterfaceRealization x = (InterfaceRealization) relationship;
				if(x.getImplementingClassifier() instanceof org.eclipse.uml2.uml.Class){
					Integer id = mapClassName2Id.get(x.getImplementingClassifier().getName());
					list.add(id);
				}
			}
		}
		return list;
	}

	/**
	 * This method get what class realize the interface_.
	 * @param interface_
	 */
	private Integer getWhoRealizeInterface(Interface interface_) {
		for (Relationship relationship : interface_.getRelationships()) {
			if(relationship instanceof InterfaceRealization){
				InterfaceRealization x = (InterfaceRealization) relationship;
				if(x.getImplementingClassifier() instanceof org.eclipse.uml2.uml.Class){
					verifyMapClassComponent((org.eclipse.uml2.uml.Class) x.getImplementingClassifier());
					Integer id = mapClassName2Id.get(x.getImplementingClassifier().getName());
					return id;
				}
			}
		}
		return -1;
	}


	/**
	 * Verify if client are in a component if not, add to map of ClassComponent this relationship. 
	 * @param client
	 */
	private void verifyMapClassComponent(org.eclipse.uml2.uml.Class client) {
		Integer idComponent = mapComponentName2Id.get(client.getNamespace().getName());
		Integer idOfClient = mapClassName2Id.get(client.getName());

		if(classComponent.get(idOfClient) == null){
			classComponent.put(idOfClient, idComponent);
		}
	}


	/**
	 * Get relationship between class and component.
	 * @param asNamed
	 */
	private void classes_(NamedElement asNamed) {
		String className = asNamed.getLabel();
		String componentName = asNamed.getNamespace().getName();
		//to get class within class (that is, class declared within other class)
		String nameComplete = asNamed.getNamespace().getQualifiedName();
		//model::apache-ant::org::apache::tools::ant::ProjectHelper
		String []vetName = nameComplete.split("::");
		String lastName = vetName[vetName.length-1];

		Character first = lastName.charAt(0);
		if(Character.isUpperCase(first)){
			int x = nameComplete.lastIndexOf("::");
			String aux = nameComplete.substring(0, x);
			String []y = aux.split("::");
			componentName = y[y.length-1];
			//System.err.println(y[y.length-1]);
		}
		
		// relation between class and component
		Integer component = mapComponentName2Id.get(componentName); // get ID of component x
		if(componentClasses.get(component) == null){ // component it's not in map, add
			Set<Integer> classes = new HashSet<Integer>();
			classes.add(mapClassName2Id.get(className));
			componentClasses.put(component, classes);
		}else{ // component it's in map, so update
			componentClasses.get(component).add(mapClassName2Id.get(className));
		}


		verifyMapClassComponent((org.eclipse.uml2.uml.Class)asNamed);
		//classComponent.put(mapClassName2Id.get(className), component);
		logger.info("Class <<" + mapClassName2Id.get(className) + ":"+ className +">> in component <<" + nameComplete + ">>");

	}


	/**
	 * Get relationship between component and a layer or can verify if an element it's a client or a server.
	 * @param d
	 */
	private void stereotype_(DynamicEObjectImpl d) {
		if(architectureStyle.equals(LAYER)){
			featureID =  d.eClass().getEStructuralFeature(ID);
			featureBaseClassifier =  d.eClass().getEStructuralFeature(base_Classifier);

			String nameLayer = d.eSetting(featureID).get(true).toString();

			Component component = (Component) d.eSetting(featureBaseClassifier).get(true);
			// relation between component and layer
			Integer componentId = mapComponentName2Id.get(component.getLabel()); // get ID of component x
			componentLayer.put(componentId,mapLayerName2Id.get(nameLayer));

			logger.info("Component <<"+ mapId2ComponentName.get(componentId) + ">> in layer <<" + nameLayer + ">>");			

		}else if(architectureStyle.equals(CLIENT_SERVER)){
			if(d.eClass().getName().equals(CLIENT))
				setTypeOfElement(CLIENT, mapClassClient, d);
			else
				setTypeOfElement(SERVER, mapClassServer, d);
		}

	}


	/**
	 * Set in mapInterface if interface is a client or a server.
	 * @param type 
	 * @param mapClass
	 * @param d
	 */
	private void setTypeOfElement(String type,
			HashMap<Integer, String> mapClass, DynamicEObjectImpl d) {
		featureBaseClassifier =  d.eClass().getEStructuralFeature(base_Classifier);

		if(d.eSetting(featureBaseClassifier).get(true) instanceof Interface){
			Interface in = (Interface) d.eSetting(featureBaseClassifier).get(true);
			Integer cl = getWhoRealizeInterface(in);
			mapClass.put(cl, type);
		}
	}

	public void showSizeOfMaps(){
		System.out.println("Classes: " + mapClassName2Id.size());
		System.out.println("Component: " + mapComponentName2Id.size());
		if(mapLayerName2Id != null)
			System.out.println("Layers: " + mapLayerName2Id.size());
		System.out.println("Interface: " + mapInterfaceName2Id.size());
		System.out.println("Relationships: " + interfaces_.size());
		System.out.println("InternalRelationship: " + internalRelations.size());

		if(architectureStyle.equals(CLIENT_SERVER)){
			System.out.println("Server:");
			for (Entry<Integer, String> server : mapClassServer.entrySet()) {
				System.out.println(getMapId2ClassName().get(server.getKey()) + " -- " + server.getValue() + " -- " + server.getKey());
			}
			System.out.println("Client:");
			for (Entry<Integer, String> server : mapClassClient.entrySet()) {
				System.out.println(getMapId2ClassName().get(server.getKey()) + " -- " + server.getValue() + " -- " + server.getKey());
			}
		}
	}

	public void showMap(){
		System.out.println("Interfaces");
		for (Entry<String, Integer> iterable_element : mapInterfaceName2Id.entrySet()) {
			System.out.println(iterable_element.getKey() + " -- " + iterable_element.getValue());
		}
		System.out.println("Classes and Components");
		for (Entry<Integer, Integer> iterable_element : classComponent.entrySet()) {
			System.out.println(getMapId2ClassName().get(iterable_element.getKey()) + " -- " + mapId2ComponentName.get(iterable_element.getValue()));
		}
		System.out.println("Interfaces");
		for (Integer[] iterable_element : interfaces_) {
			System.out.println(getMapId2ClassName().get(iterable_element[0]) + "--" + getMapId2ClassName().get(iterable_element[1]));
		}
	}
	
	public void showSolution(Solution solution){
		for (Entry<Integer, Set<Integer>> element : solution.componentClasses.entrySet()) {
			String componentName = mapId2ComponentName.get(element.getKey());
			for (Integer class_ : element.getValue()) {
				String className = getMapId2ClassName().get(class_);
				System.err.println("Class <<" + mapClassName2Id.get(className) + ":"+ className +">> in component <<" + componentName + ">>");
			}
		}
		
		for (Integer[] interfaces : solution.interfaces) {
			String class1 = getMapId2ClassName().get(interfaces[0]);
			String class2 = getMapId2ClassName().get(interfaces[1]);
			System.err.println("Class <<" + class1 + ">> and <<" + class2 + ">> have a relationship.");
		}
		
		for (Integer[] internalRelation : solution.internalRelations) {
			String class1 = getMapId2ClassName().get(internalRelation[0]);
			String class2 = getMapId2ClassName().get(internalRelation[1]);
			System.err.println("<<"+ class1 + ">> and <<" + class2 +" >> have a relation, but they "
					+ "are in the same component" );
		}

	}

	public void showComponents(Solution solution){
		for (Entry<Integer, Set<Integer>> string : solution.componentClasses.entrySet()) {
			System.err.println("--> " + mapId2ComponentName.get(string.getKey()));
		}
	}
	
	public HashMap<Integer, String> getMapId2ClassName() {
		return mapId2ClassName;
	}


	public void setMapId2ClassName(HashMap<Integer, String> mapId2ClassName) {
		this.mapId2ClassName = mapId2ClassName;
	}

}
