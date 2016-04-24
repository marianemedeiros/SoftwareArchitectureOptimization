/**
 * 
 */
package aco;

import java.util.HashMap;

import org.junit.Test;

/**
 * @author mariane
 *
 */
public class TestPheromoneMatrix {

	@Test
	public void test() { // test if the architecture give to algorithm already have relation.
	}

	private HashMap<Integer, Integer> buildClassComp(){
		HashMap<Integer, Integer> classComp = new HashMap<Integer,Integer>();
		
		classComp.put(0, 0);
		classComp.put(1, 0);
		classComp.put(2, 0);
		classComp.put(3, 1);
		classComp.put(4, 1);
		classComp.put(5, 2);
		classComp.put(6, 2);
		classComp.put(7, 2);
		classComp.put(8, 2);
		
		return classComp;
	}
	
	private HashMap<Integer, Integer[]> buildClassClassInt(){
		HashMap<Integer, Integer[]> class_classInt = new HashMap<Integer,Integer[]>(); 
		Integer vet[] = new Integer[2];
		vet[0] = 2;
		class_classInt.put(1, vet);
		
		Integer vet1[] = new Integer[2];
		vet1[0] = 4;
		class_classInt.put(3, vet1);
		
		Integer vet2[] = new Integer[2];
		vet2[0] = 6; vet2[1] = 7;
		class_classInt.put(5, vet2);
		
		return class_classInt;
	}
	
	private HashMap<Integer, Integer[]> buildClassClassExt(){
		HashMap<Integer, Integer[]> class_classEx = new HashMap<Integer,Integer[]>();
		
		Integer vet3[] = new Integer[2];
		vet3[0] = 4; 
		class_classEx.put(1, vet3);
		
		Integer vet4[] = new Integer[2];
		vet4[0] = 3; 
		class_classEx.put(2, vet4);
		
		Integer vet5[] = new Integer[2];
		vet5[0] = 5; 
		class_classEx.put(4, vet5);
		
		Integer vet6[] = new Integer[2];
		vet6[0] = 8; 
		class_classEx.put(0, vet6);
		
		return class_classEx;
	}
}
