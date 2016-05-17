package main;

import java.io.File;

import aco.entities.Architecture;
import aco.entities.Parametro;

public class Main {
	public static boolean SHOW_LOGS = false; 

	public static String results = "/home/mariane/Dropbox/TCC-Mariane/resultados/";

	public static String path = "/modelos_UML/Estilo_em_Camada/ModeloCamada";
	public static String pathSS = "/modelos_UML/Estilo_em_Camada/ModeloCamadaSemStyle";
	public static String path1 = "/modelos_UML/Estilo_ClienteServidor/ModeloClienteServidor3";
	public static String path2 = "/modelos_UML/elementosSoltos";

	public static String apacheAnt = "/modelos_UML";
	public static String apacheAntStyle = "/modelos_UML/ApacheAnt11Style";
	public static String apacheAnt13Style = "/modelo_UML/ApacheAnt13Style";

	public static String ant11 = "apache-ant11";
	public static String ant16 = "apache-ant16";
	public static String ant15 = "apache-ant15";
	public static String ant13 = "apache-ant13";
	public static String acoProject = "aco2architectureoptimization";		

	public static String ant11Style = "apache-ant11Style";
	public static String ant13Style = "apache-ant13Style";

	public static void main(String[] args) throws Exception {
		File file = new java.io.File("");   //Dummy file
		String  abspath = file.getAbsolutePath();

		double[] alphas = {0.1, 0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4};
		double[] betas = {0.0, 0.1, 0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4};
		double[] evaporationRates = {0.1, 0.2, 0.4, 0.6, 0.8,1.0, 1.2, 1.4};
		int[] iterations = {10, 20, 40, 80, 160, 320, 640, 1280};
		int[] ants = {5, 10, 20, 40, 80, 160, 320, 640, 1280};
		int trials = 10;

		for (int ant : ants) {
			for (int iteration : iterations) {
				for (double alpha : alphas) {
					for (double beta : betas) {
						for (double ro :evaporationRates) {
							for (int i = 0; i < trials; i++) {
								Parametro param = new Parametro(iteration, ant, ro, alpha, beta);
								Architecture architecture = new Architecture(abspath+path,"modelCS",param);// arquitetura do apache-ant sem estilo
								architecture.initAntSystem(i);

								architecture = new Architecture(abspath+apacheAnt,ant11,param);//arquitetura do apache-ant sem estilo
								architecture.initAntSystem(i);
							}
						}
					}
				}
			}
		}
	}
}
