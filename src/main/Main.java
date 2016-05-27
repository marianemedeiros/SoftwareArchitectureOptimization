package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import loadModel.Solution;
import aco.entities.Architecture;
import aco.entities.Parametro;

public class Main {
	public static boolean SHOW_LOGS = false; 
	public final static int trials = 10;

	/*
	public static String path = "/modelos_UML/Estilo_em_Camada/ModeloCamada";
	public static String pathSS = "/modelos_UML/Estilo_em_Camada/ModeloCamadaSemStyle";
	public static String path1 = "/modelos_UML/Estilo_ClienteServidor/ModeloClienteServidor3";
	public static String path2 = "/modelos_UML/elementosSoltos";

	public static String apacheAnt = "/modelos_UML";
	public static String apacheAntStyle = "/modelos_UML/ApacheAnt11Style";
	public static String apacheAnt13Style = "/modelos_UML/ApacheAnt13Style";

	public static String ant11 = "apache-ant11";
	public static String ant16 = "apache-ant16";
	public static String ant15 = "apache-ant15";
	public static String ant13 = "apache-ant13";
	public static String acoProject = "aco2architectureoptimization";		

	public static String ant11Style = "apache-ant11Style";
	public static String ant13Style = "apache-ant13Style";
	*/

	public static String entrada = "entrada";
	public static double[] alphas;
	public static double[] betas;
	public static double[] evaporationRates;
	public static int[] iterations;
	public static int[] ants;
	
	
	public static String nameFile;
	public static String pathFile;
	private static String results;
	
	public static void main(String[] args) throws Exception {
		File file = new java.io.File("");   //Dummy file
		String  abspath = file.getAbsolutePath();
		extractDatas(abspath);
		/*
		double[] alphas = {0.4, 0.5, 0.6, 0.7, 0.8, 1.0};
		double[] betas = {0.8, 0.2, 0.4, 0.5, 0.6, 0.7, 0.8, 1.0};
		double[] evaporationRates = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
		int[] iterations = {20, 40, 80, 160, 320, 400};
		int[] ants = {100, 40, 80, 160, 320}; 
		 */

		Architecture architecture = null;
		Solution s = null;
		for (int ant : ants) {
			for (int iteration : iterations) {
				for (double alpha : alphas) {
					for (double beta : betas) {
						for (double ro :evaporationRates) {
							Parametro param = new Parametro(iteration, ant, ro, alpha, beta);
							architecture = new Architecture(Main.pathFile,Main.nameFile,param,results);// arquitetura do apache-ant sem estilo
							for (int i = 0; i < trials; i++) {
								s = architecture.initAntSystem(i);
							}
							saveMediaInfoRelationClasses(s, results, Main.nameFile + "_" + "modeloOtimizado" + "_" +
									iteration + "_" + ant + "_" + ro + "_" + 
									alpha + "_" + beta + "_infos_relationXclasses_medias"+".csv", architecture.evolutionClassRelationI,
									architecture.evolutionClassRelationE,1);
							saveMediaInfoComponentsClasses(s, results, Main.nameFile + "_" + "modeloOtimizado" + "_" +
									iteration + "_" + ant + "_" + ro + "_" + 
									alpha + "_" + beta + "_infos_componentXclasses_medias"+".csv", architecture.evolutionComponentClass,1);
						}
					}
				}
			}
		}
	}

	public static void saveMediaInfoRelationClasses(Solution s, String dir, String nameFile, int[] v1, int[] v2, int a) throws IOException{
		File file = new File(dir,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Class,Internal,External");
		bufferedWriter.newLine();

		for (Entry<Integer, Integer> class_ : s.classComponent.entrySet()) {
			if(a == 1)
				bufferedWriter.write(class_.getKey() + "," + (v1[class_.getKey()]/(double)Main.trials) + "," + (v2[class_.getKey()]/(double)Main.trials));
			else
				bufferedWriter.write(class_.getKey() + "," + (v1[class_.getKey()]) + "," + (v2[class_.getKey()]));
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}

	public static void saveMediaInfoComponentsClasses(Solution s, String dir, String nameFile, int[] v1, int aux) throws IOException{
		File file = new File(dir,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Comp,Class");
		bufferedWriter.newLine();

		for (Entry<Integer, Set<Integer>> comp_ : s.componentClasses.entrySet()) {
			if(aux == 1)
				bufferedWriter.write(comp_.getKey() + "," + (v1[comp_.getKey()]/(double)Main.trials));
			else
				bufferedWriter.write(comp_.getKey() + "," + (v1[comp_.getKey()]));
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}
	
	private static void extractDatas(String abspath) throws IOException{
		System.err.println();
		File file = new File(abspath,Main.entrada);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		
		String line = bufferedReader.readLine();
		while(line != null){
			if(line.contains("abspath")){
				String[] s = line.replace(" ", "").split("=");
				Main.pathFile = s[1];
			}else if(line.contains("name")){
				Main.nameFile = line.replace(" ", "").split("=")[1];
			}else if(line.contains("alphas")){
				String[] v = line.replace(" ", "").split("=")[1].split(",");
				Main.alphas = new double[v.length];
				for (int i = 0; i < v.length; i++) {
					Main.alphas[i] = Double.valueOf(v[i]);
				}
			}else if(line.contains("betas")){
				String[] v = line.replace(" ", "").split("=")[1].split(",");
				Main.betas = new double[v.length];
				for (int i = 0; i < v.length; i++) {
					Main.betas[i] = Double.valueOf(v[i]);
				}
			}else if(line.contains("evaporationRates")){
				String[] v = line.replace(" ", "").split("=")[1].split(",");
				Main.evaporationRates = new double[v.length];
				for (int i = 0; i < v.length; i++) {
					Main.evaporationRates[i] = Double.valueOf(v[i]);
				}
			}else if(line.contains("iterations")){
				String[] v = line.replace(" ", "").split("=")[1].split(",");
				Main.iterations = new int[v.length];
				for (int i = 0; i < v.length; i++) {
					Main.iterations[i] = Integer.valueOf(v[i]);
				}
			}else if(line.contains("ants")){
				String[] v = line.replace(" ", "").split("=")[1].split(",");
				Main.ants = new int[v.length];
				for (int i = 0; i < v.length; i++) {
					Main.ants[i] = Integer.valueOf(v[i]);
				}
			}else if(line.contains("results")){
				Main.results = line.replace(" ", "").split("=")[1];
			}
			line = bufferedReader.readLine();
		}
	}
	

}

