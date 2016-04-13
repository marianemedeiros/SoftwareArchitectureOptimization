/**
 * 
 */
package loadModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <code>Solution</code> is an element that include all things that represents an architecture. Maps that associate
 * class and components, components and layers and lists that contains relations between classes. Besides that, this
 * object contains attributes of quality metrics that represents cohesion, coupling and modularization quality of a represented architecture.
 * 
 * @author mariane
 *
 */
public class Solution {
	public String type;
	
	public HashMap<Integer,Integer> componentLayer;
	
	public HashMap<Integer, String> mapClassServer;
	public HashMap<Integer, String> mapClassClient;
	
	public HashMap<Integer,Integer> classComponent;
	public HashMap<Integer, Set<Integer>> componentClasses ; // component - classes in component
	public ArrayList<Integer[]> interfaces; // interfaces provided between components
	public ArrayList<Integer[]> internalRelations; // internal relations between classes of same component
	
	public double coesionMetric;
	public double acopMetric;
	public double mMetric; 
	public double totalPenalty; //total de penalidades sofrida pela Solution

	public ArrayList<Integer[]> listBadRel; //list of elements that break rule of layered architecture style. 

	public int number_comp;
	public int number_class;
	
	/**
	 * To instantiate a Solution with separate elements:
	 * 
	 *  @param qtd_comp number of components
	 *  @param qtd_class number of classes
	 *  
	 * */
	public Solution(int qtd_comp, int qtd_class) {
		number_class = qtd_class;
		number_comp = qtd_comp;
	}
	
	/**
	 * To instantiate a Solution with only components and classes relationship:
	 * 
	 *  @param componentClass map with component and classes that belong to component.
	 *  @param classComponent map with class and component that this class belongs.
	 *  
	 * */
	public Solution(HashMap<Integer, Set<Integer>> componentClasses, HashMap<Integer,Integer> classComponent) {
		this.componentClasses = componentClasses;
		this.classComponent = classComponent;
	}
	
	/**
	 * To instantiate a Solution with LAYERED style, it's necessary:
	 * 
	 *  @param componentClass map with component and classes that belong to component.
	 *  @param interfaces that doing communication between classes of different components.
	 *  @param internalRelation relation between classes in a same component.
	 *  @param componentLayer map with component and the Layer that this component belongs.
	 *  @param classComponent map with class and component that this class belongs.
	 * */
	public Solution(HashMap<Integer, Set<Integer>> componentClass, ArrayList<Integer[]> interfaces,
			ArrayList<Integer[]> internalRelations, HashMap<Integer,Integer> componentLayer, HashMap<Integer,Integer> classComponent) {
		this.type = LoadModel.LAYER;
		
		this.componentClasses = componentClass;
		this.interfaces = interfaces;
		this.internalRelations = internalRelations;
		
		this.componentLayer = componentLayer;
		this.classComponent = classComponent;
	}
	
	/**
	 * To instantiate a Solution with CLIENT/SERVER style, it's necessary:
	 * 
	 *  @param componentClass map with component and classes that belong to component.
	 *  @param mapInterfaceServer map of elements that are Servers.
	 *  @param mapInterfaceClient map of elements that are Clients.
	 *  @param interfaces that doing communication between classes of different components.
	 *  @param internalRelation relation between classes in a same component.
	 *  @param classComponent map with class and component that this class belongs.
	 *  
	 * */
	public Solution(HashMap<Integer, Set<Integer>> componentClass, ArrayList<Integer[]> interfaces,
			HashMap<Integer, String> mapClassServer,
			HashMap<Integer, String> mapClassClient,
			ArrayList<Integer[]> internalRelations, HashMap<Integer,Integer> classComponent) {
		this.type = LoadModel.CLIENT_SERVER;
		
		this.componentClasses = componentClass;
		this.classComponent = classComponent;

		this.interfaces = interfaces;
		this.internalRelations = internalRelations;
		
		this.mapClassClient = mapClassClient;
		this.mapClassServer = mapClassServer;
	}
	
	public void showSolution(){
		System.out.println("\n!!!!!!!!!!!!!!!!! SHOW SOLUTION !!!!!!!!!!!!!!!!!!!!\n");
		for (Entry<Integer, Set<Integer>> element : this.componentClasses.entrySet()) {
			System.out.println("- Component " + element.getKey() + " has classes: ");
				for (Integer classes : element.getValue()) {
					System.out.println("-- Class " + classes);
				}
		}
		
		for (Integer[] i : interfaces) {
			System.out.println("Interface between class <" + i[0] + "," + i[1] + ">");
		}
		
		for (Integer[] i : internalRelations) {
			System.out.println("Internal Relation between class <" + i[0] + "," + i[1] + ">");
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

}
