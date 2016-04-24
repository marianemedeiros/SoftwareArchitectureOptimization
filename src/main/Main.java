package main;

import aco.entities.Architecture;

public class Main {
	// constants
	public static final boolean SHOW_LOGS = false; 
	public static final int ITERATIONS = 50;
	public static final int ANTS = 5;
	public static final double RO = 0.3; //used in uptadePheromone() at class Colony.
	public static final double ALPA = 0.8; //used in calculatesProbability() at class Probability. 
	public static final double BETA = 0.5;  //used in calculatesProbability() at class Probability. (heuristic information)
	
	public static String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";
	public static String path1 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor3";
	public static String path2 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/elementosSoltos";
	
	public static void main(String[] args) throws Exception {
		Architecture architecture = new Architecture(path1,"model");
		architecture.initAntSystem();
	}
}
