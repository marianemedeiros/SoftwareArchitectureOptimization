package architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import loadModel.Solution;


public class ArchitectureExample {

	/* used in TestCoesion because has internalRelation*/
	public Solution generateSoluction1() {
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();
		ArrayList<int[]> internalRelation = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(0);
		classesComp1.add(1);
		classesComp1.add(2);
		components.put(0, classesComp1);

		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(3);
		classesComp2.add(4);
		components.put(1, classesComp2);

		Set<Integer> classesComp3 = new HashSet<Integer>();
		classesComp3.add(5);
		classesComp3.add(6);
		classesComp3.add(7);
		classesComp3.add(8);
		components.put(2, classesComp3);

		// interface
		int interface_[] = new int[2];
		interface_[0] = 1; interface_[1] = 4;
		interfaces.add(interface_);

		int interface_1[] = new int[2];
		interface_1[0] = 2; interface_1[1] = 3;
		interfaces.add(interface_1);

		int interface_2[] = new int[2];
		interface_2[0] = 0; interface_2[1] = 8;
		interfaces.add(interface_2);

		int interface_3[] = new int[2];
		interface_3[0] = 4; interface_3[1] = 5;
		interfaces.add(interface_3);

		// class class
		int class_[] = new int[2];
		class_[0] = 0; class_[1] = 2;
		internalRelation.add(class_);

		int class_1[] = new int[2];
		class_1[0] = 1; class_1[1] = 2;
		internalRelation.add(class_1);

		int class_2[] = new int[2];
		class_2[0] = 3; class_2[1] = 4;
		internalRelation.add(class_2);

		int class_3[] = new int[2];
		class_3[0] = 5; class_3[1] = 8;
		internalRelation.add(class_3);

		int class_4[] = new int[2];
		class_4[0] = 6; class_4[1] = 7;
		internalRelation.add(class_4);

		Solution r = new Solution(components,interfaces,internalRelation,null,null);
		return r;
	}

	/*used in Coupling test because doesn't have internal relation*/
	public Solution generateSoluction2() {
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(0);
		classesComp1.add(1);
		classesComp1.add(2);
		components.put(0, classesComp1);

		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(3);
		classesComp2.add(4);
		components.put(1, classesComp2);

		Set<Integer> classesComp3 = new HashSet<Integer>();
		classesComp3.add(5);
		classesComp3.add(6);
		classesComp3.add(7);
		classesComp3.add(8);
		components.put(2, classesComp3);

		int interface_[] = new int[2];
		interface_[0] = 1; interface_[1] = 4;
		interfaces.add(interface_);

		int interface_1[] = new int[2];
		interface_1[0] = 2; interface_1[1] = 3;
		interfaces.add(interface_1);

		int interface_2[] = new int[2];
		interface_2[0] = 0; interface_2[1] = 8;
		interfaces.add(interface_2);

		int interface_3[] = new int[2];
		interface_3[0] = 4; interface_3[1] = 5;
		interfaces.add(interface_3);

		Solution r = new Solution(components,interfaces,null,null,null);
		return r;
	}

	/** Caso que esta dando errado o valor da métrica
	  	- Component 0 has classes: 
		-- Class 0
		-- Class 1
		-- Class 3
		- Component 1 has classes: 
		-- Class 2
		Interface between class <2,0>
	 * */
	public Solution generateSoluction3() {
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(0);
		classesComp1.add(1);
		classesComp1.add(3);
		components.put(0, classesComp1);

		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(2);
		components.put(1, classesComp2);

		Set<Integer> classesComp3 = new HashSet<Integer>();
		classesComp3.add(5);
		classesComp3.add(6);
		classesComp3.add(7);
		classesComp3.add(8);
		components.put(2, classesComp3);

		int interface_[] = new int[2];
		interface_[0] = 2; interface_[1] = 0;
		interfaces.add(interface_);

		Solution r = new Solution(components,interfaces,null,null,null);
		return r;
	}

	
	/** Caso que esta dando errado o valor da métrica
  	- Component 0 has classes: 
	-- Class 0
	-- Class 1
	-- Class 3
	- Component 1 has classes: 
	-- Class 2
	 **/
	public Solution generateSoluction4() {
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(0);
		classesComp1.add(1);
		classesComp1.add(3);
		components.put(0, classesComp1);

		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(2);
		components.put(1, classesComp2);

		Set<Integer> classesComp3 = new HashSet<Integer>();
		classesComp3.add(5);
		classesComp3.add(6);
		classesComp3.add(7);
		classesComp3.add(8);
		components.put(2, classesComp3);

		Solution r = new Solution(components,interfaces,null,null,null);
		return r;
	}
	
	/*
	 * - Component 0 has classes: 
		-- Class 1
		-- Class 2
		-- Class 3
		- Component 1 has classes: 
		-- Class 0
		Interface between class <1,0>
		Interface between class <2,0>
		Interface between class <3,0>
		Internal Relation between class <1,3>
		Internal Relation between class <2,1>
		Internal Relation between class <3,2>
	 * */
	public Solution generateSoluction5() {
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();
		ArrayList<int[]> internalRelation = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(1);
		classesComp1.add(2);
		classesComp1.add(3);
		components.put(0, classesComp1);

		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(0);
		components.put(1, classesComp2);

		// interface
		int interface_[] = new int[2];
		interface_[0] = 1; interface_[1] = 0;
		interfaces.add(interface_);

		int interface_1[] = new int[2];
		interface_1[0] = 2; interface_1[1] = 0;
		interfaces.add(interface_1);

		int interface_2[] = new int[2];
		interface_2[0] = 3; interface_2[1] = 0;
		interfaces.add(interface_2);

		// class class
		int class_[] = new int[2];
		class_[0] = 1; class_[1] = 3;
		internalRelation.add(class_);

		int class_1[] = new int[2];
		class_1[0] = 2; class_1[1] = 1;
		internalRelation.add(class_1);

		int class_2[] = new int[2];
		class_2[0] = 3; class_2[1] = 2;
		internalRelation.add(class_2);

		Solution r = new Solution(components,interfaces,internalRelation,null,null);
		return r;
	}
	
	public Solution generateSoluction6() {
		HashMap<Integer, Integer> componentLayer = new HashMap<Integer,Integer>();
		HashMap<Integer, Integer> classComponent = new HashMap<Integer,Integer>();
		
		HashMap<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
		ArrayList<int[]> interfaces = new ArrayList<int[]>();
		ArrayList<int[]> internalRelation = new ArrayList<int[]>();

		Set<Integer> classesComp1 = new HashSet<Integer>();
		classesComp1.add(0);
		classesComp1.add(1);
		classesComp1.add(2);
		components.put(0, classesComp1);
		
		classComponent.put(0, 0);
		classComponent.put(1, 0);
		classComponent.put(2, 0);
		
		Set<Integer> classesComp2 = new HashSet<Integer>();
		classesComp2.add(3);
		classesComp2.add(4);
		components.put(1, classesComp2);

		classComponent.put(3, 1);
		classComponent.put(4, 1);
		
		Set<Integer> classesComp3 = new HashSet<Integer>();
		classesComp3.add(5);
		classesComp3.add(6);
		classesComp3.add(7);
		classesComp3.add(8);
		components.put(2, classesComp3);

		classComponent.put(5, 2);
		classComponent.put(6, 2);
		classComponent.put(7, 2);
		classComponent.put(8, 2);
		
		// interface
		int interface_[] = new int[2];
		interface_[0] = 1; interface_[1] = 4;
		interfaces.add(interface_);

		int interface_1[] = new int[2];
		interface_1[0] = 2; interface_1[1] = 3;
		interfaces.add(interface_1);

		int interface_2[] = new int[2];
		interface_2[0] = 0; interface_2[1] = 8;
		interfaces.add(interface_2);

		int interface_3[] = new int[2];
		interface_3[0] = 4; interface_3[1] = 5;
		interfaces.add(interface_3);

		// class class
		int class_[] = new int[2];
		class_[0] = 0; class_[1] = 2;
		internalRelation.add(class_);

		int class_1[] = new int[2];
		class_1[0] = 1; class_1[1] = 2;
		internalRelation.add(class_1);

		int class_2[] = new int[2];
		class_2[0] = 3; class_2[1] = 4;
		internalRelation.add(class_2);

		int class_3[] = new int[2];
		class_3[0] = 5; class_3[1] = 8;
		internalRelation.add(class_3);

		int class_4[] = new int[2];
		class_4[0] = 6; class_4[1] = 7;
		internalRelation.add(class_4);

		
		componentLayer.put(0, 0);
		componentLayer.put(1, 1);
		componentLayer.put(2, 2);
		
		Solution r = new Solution(components,interfaces,internalRelation,componentLayer,classComponent);
		return r;
	}
}
