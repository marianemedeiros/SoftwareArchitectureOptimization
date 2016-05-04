package main;

import aco.entities.Architecture;

public class Main {
	// constants
	public static boolean SHOW_LOGS = false; 
	public static int ITERATIONS = 50;
	public static int ANTS = 10;
	public static double RO = 0.3; //used in uptadePheromone() at class Colony.
	public static double ALPA = 0.8; //used in calculatesProbability() at class Probability. 
	public static double BETA = 0.8;  //used in calculatesProbability() at class Probability. (heuristic information)
	
	public static String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";
	public static String path1 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor3";
	public static String path2 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/elementosSoltos";
	public static String teste = "/home/mariane/Downloads";
	
	public static void main(String[] args) throws Exception {
		if(args.length != 0){
			ITERATIONS = Integer.valueOf(args[0]);
			ANTS = Integer.valueOf(args[1]);
			RO = Integer.valueOf(args[2]);
			ALPA = Integer.valueOf(args[3]);
			BETA = Integer.valueOf(args[4]);
		}
		//Architecture architecture = new Architecture(teste,"apache-ant");
		Architecture architecture = new Architecture(path,"model");
		architecture.initAntSystem();
	}
}
