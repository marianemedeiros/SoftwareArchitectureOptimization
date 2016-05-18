/**
 * 
 */
package aco.entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author mariane
 *
 */
public class Penalty {
	
	public ArrayList<Integer[]> listBadRel = new ArrayList<Integer[]>(); // relações que quebram a regra
	public HashMap<Integer,Integer> classBreak = new HashMap<Integer, Integer>(); // quantidade de vezes que uma classe quebra a regra.
	public double heuristicInformation = 0.0;
	
	public void addToClassBreak(Integer idClass){
		if(!classBreak.containsKey(idClass)){
			classBreak.put(idClass, 1);
		}else{
			classBreak.put(idClass, classBreak.get(idClass)+1);
		}
	}
	
	public void addToListBasRel(Integer[] i){
		listBadRel.add(i);
	}
	
}
