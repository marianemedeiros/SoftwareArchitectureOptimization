package main;

import aco.entities.Architecture;

public class Main {
	// constants
	public static boolean SHOW_LOGS = false; 
	public static int ITERATIONS = 10;
	public static int ANTS = 5;
	public static double RO = 0.7; //used in uptadePheromone() at class Colony.
	public static double ALPA = 0.9; //used in calculatesProbability() at class Probability. 
	public static double BETA = 0.0;  //used in calculatesProbability() at class Probability. (heuristic information)
	
	public static String path = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo em Camada/ModeloCamada";
	public static String path1 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/Estilo ClienteServidor/ModeloClienteServidor3";
	public static String path2 = "/home/mariane/Dropbox/TCC-Mariane/modelos UML/elementosSoltos";
	public static String apacheAnt = "/home/mariane/Dropbox/TCC-Mariane/modelos UML";

	public static String ant11 = "apache-ant11";
	public static String ant11Style = "apache-ant11Style";
	public static String ant16 = "apache-ant16";
	public static String ant15 = "apache-ant15";
	public static String ant13 = "apache-ant13";
			
	public static void main(String[] args) throws Exception {
		if(args.length != 0){
			ITERATIONS = Integer.valueOf(args[0]);
			ANTS = Integer.valueOf(args[1]);
			RO = Integer.valueOf(args[2]);
			ALPA = Integer.valueOf(args[3]);
			BETA = Integer.valueOf(args[4]);
		}
		
		
		Architecture architecture = new Architecture(apacheAnt, ant11);
		//Architecture architecture = new Architecture(path,"model");
		architecture.initAntSystem();
	}
}
