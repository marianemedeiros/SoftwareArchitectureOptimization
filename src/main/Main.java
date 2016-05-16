package main;

import java.io.File;
import java.util.ArrayList;

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

		 /*
		if(args.length != 0){
			ITERATIONS = Integer.valueOf(args[0]);
			ANTS = Integer.valueOf(args[1]);
			RO = Integer.valueOf(args[2]);
			ALPA = Integer.valueOf(args[3]);
			BETA = Integer.valueOf(args[4]);
		}*/
		
		Parametro parametro1 = new Parametro(10,   10,  0.7, 0.9, 0.1);
		Parametro parametro2 = new Parametro(10,   100,  0.7, 0.9, 0.3);
		Parametro parametro3 = new Parametro(10,   100, 0.7, 0.9, 0.5);
		Parametro parametro4 = new Parametro(20,   100, 0.4, 0.9, 0.8);
		Parametro parametro5 = new Parametro(20,   100, 0.2, 0.4, 0.8);
		Parametro parametro6 = new Parametro(20,   100, 0.4, 0.4, 0.8);
		Parametro parametro7 = new Parametro(20,   100, 0.4, 0.4, 0.4);
		Parametro parametro8 = new Parametro(20,   100, 0.4, 0.9, 0.6);
		Parametro parametro9 = new Parametro(20,   100, 0.4, 0.4, 0.6);
		Parametro parametro10 = new Parametro(20,  200, 0.4, 0.9, 0.9);
		Parametro parametro11 = new Parametro(20,  200, 0.2, 0.9, 0.6);
		Parametro parametro12 = new Parametro(20,  200, 0.7, 0.9, 0.9);
		Parametro parametro13 = new Parametro(100, 100, 0.2, 0.3, 0.5);
		Parametro parametro14 = new Parametro(100, 500, 0.2, 0.3, 0.6);
		Parametro parametro15 = new Parametro(500, 100, 0.2, 0.2, 0.6);
		
		Parametro parametro16 = new Parametro(100, 300, 0.2, 0.3, 0.5);
		Parametro parametro17 = new Parametro(100, 300, 0.2, 0.5, 0.3);
		Parametro parametro18 = new Parametro(500, 300, 0.2, 0.2, 0.6);

		ArrayList<Parametro> parametros = new ArrayList<Parametro>();
		parametros.add(parametro1);
		parametros.add(parametro2);
		parametros.add(parametro3);
		parametros.add(parametro4);
		parametros.add(parametro5);
		parametros.add(parametro6);
		parametros.add(parametro7);
		parametros.add(parametro8);
		parametros.add(parametro9);
		parametros.add(parametro10);
		parametros.add(parametro11);
		parametros.add(parametro12);
		parametros.add(parametro13);
		parametros.add(parametro14);
		parametros.add(parametro15);
		parametros.add(parametro16);
		parametros.add(parametro17);
		parametros.add(parametro18);
		
		for (Parametro parametro : parametros) {
			Architecture architecture = new Architecture(abspath+apacheAntStyle,ant11Style,parametro);// arquitetura do apache-ant sem estilo
			architecture.initAntSystem();
			System.out.println("-------------------------------------------------------------------------------------------------");
		}
		

		Parametro parametro11_ = new Parametro(10,   100,  0.7, 0.9, 0.0);
		Parametro parametro22 = new Parametro(10,   100,  0.7, 0.9, 0.0);
		Parametro parametro33 = new Parametro(10,   100, 0.7, 0.9, 0.0);
		Parametro parametro44 = new Parametro(20,   100, 0.4, 0.9, 0.0);
		Parametro parametro55 = new Parametro(20,   100, 0.2, 0.4, 0.0);
		Parametro parametro66 = new Parametro(20,   100, 0.4, 0.4, 0.0);
		Parametro parametro77 = new Parametro(20,   100, 0.4, 0.4, 0.0);
		Parametro parametro88 = new Parametro(20,   100, 0.4, 0.9, 0.0);
		Parametro parametro99 = new Parametro(20,   100, 0.4, 0.4, 0.0);
		Parametro parametro100 = new Parametro(20,  200, 0.4, 0.9, 0.0);
		Parametro parametro111 = new Parametro(20,  200, 0.2, 0.9, 0.0);
		Parametro parametro122 = new Parametro(20,  200, 0.7, 0.9, 0.0);
		Parametro parametro133 = new Parametro(100, 100, 0.2, 0.3, 0.0);
		Parametro parametro144 = new Parametro(100, 500, 0.2, 0.3, 0.0);
		Parametro parametro155 = new Parametro(500, 100, 0.2, 0.2, 0.0);
		
		Parametro parametro177 = new Parametro(100, 300, 0.2, 0.5, 0.0);
		Parametro parametro188 = new Parametro(500, 300, 0.2, 0.2, 0.0);

		ArrayList<Parametro> parametros2 = new ArrayList<Parametro>();
		parametros.add(parametro11_);
		parametros.add(parametro22);
		parametros.add(parametro33);
		parametros.add(parametro44);
		parametros.add(parametro55);
		parametros.add(parametro66);
		parametros.add(parametro77);
		parametros.add(parametro88);
		parametros.add(parametro99);
		parametros.add(parametro100);
		parametros.add(parametro111);
		parametros.add(parametro122);
		parametros.add(parametro133);
		parametros.add(parametro144);
		parametros.add(parametro155);
		parametros.add(parametro177);
		parametros.add(parametro188);
		
		for (Parametro parametro : parametros2) {
			Architecture architecture = new Architecture(abspath+apacheAnt,ant11,parametro);// arquitetura do apache-ant sem estilo
			architecture.initAntSystem();
			System.out.println("-------------------------------------------------------------------------------------------------");
		}
		
		//Architecture architecture = new Architecture(pathSS, "modelSS"); //arquitetura dos exemplos sem estilo
		//Architecture architecture = new Architecture(apacheAntStyle,ant11Style,parametro1); //arquitetura dos exemplos com estilo
		//architecture.initAntSystem();
		//Architecture architecture = new Architecture(apacheAntStyle,ant11Style);// arquitetura do apache-ant com estilo em camadas
		//Architecture architecture = new Architecture(apacheAnt,ant11);// arquitetura do apache-ant sem estilo
		
		//Architecture architecture = new Architecture(apacheAntStyle,ant11Style);// arquitetura do apache-ant com estilo em camadas
		//Architecture architecture = new Architecture(apacheAnt,ant13);// arquitetura do apache-ant sem estilo

		//architecture.initAntSystem();
	}
}
