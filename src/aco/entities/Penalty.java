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

}
