/**
 * 
 */
package aco.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import loadModel.LoadModel;
import loadModel.Solution;
import main.Main;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;
import metrics.architecture.ModulatizationQuality;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * @author mariane
 *
 */
public class Architecture {
	public NumberFormat formatter = new DecimalFormat("#0.00000");
	private String nameFile;
	private LoadModel loadModel;
	private AntSystem antSystem;
	private Solution initialSolution;
	private Parametro parametros;

	private String diretorioResults;

	public Architecture(String pathUmlFile, String nameFile, Parametro p, String s) throws Exception {
		this.nameFile = nameFile;
		loadModel = new LoadModel(URI.createFileURI(pathUmlFile).appendSegment(nameFile).appendFileExtension(UMLResource.FILE_EXTENSION));

		long startTime = System.currentTimeMillis();
		initialSolution = loadModel.buildSoluction();
		long stopTime = System.currentTimeMillis();
		this.diretorioResults = s;
		if(Main.SHOW_LOGS)
			System.out.println("Execution time is " + formatter.format((stopTime - startTime) / 1000d) + " seconds to extract architecture");
		this.parametros = p;
		saveExtractArch(initialSolution,nameFile + "_"+"modeloExtraido");

	}

	public void initAntSystem(int trial) throws Exception{
		if(this.initialSolution.interfaces != null && this.initialSolution.internalRelations != null){// arquitetura ja estruturada
			MetricCoupling coupling = new MetricCoupling();
			this.initialSolution = coupling.calculate(initialSolution);

			MetricCoesion coesion = new MetricCoesion();
			this.initialSolution = coesion.calculate(initialSolution);

			ModulatizationQuality  modulatizationQuality = new ModulatizationQuality();
			this.initialSolution = modulatizationQuality.calculate(initialSolution);

			//if(this.initialSolution.type.equals(""))
			//	System.out.println("\n Initial Architecture Defined without any style architectural!!!");
			//else
			//	System.out.println("\n Initial Architecture Defined!!! \n So architecture style that will be evaluated is <<" + this.initialSolution.type + ">>");

			if(Main.SHOW_LOGS){
				System.out.println(" Modularization Qualidaty of that architecture: " + this.initialSolution.mMetric);
				System.out.println("Iterations: " + parametros.ITERATIONS + " Ants: " + parametros.ANTS + " Ro: " + parametros.RO + " Alpha: " + parametros.ALPA + " Beta: " + parametros.BETA);
				System.out.println(" Number of components: " + initialSolution.componentClasses.size() + "\n Number of classes: " + initialSolution.classComponent.size());
			}

			Matrix m = new Matrix(initialSolution.componentClasses.size(), initialSolution.classComponent.size(),0.5);
			antSystem = new AntSystem(m,initialSolution,parametros);
		}else{// to architectures with loose components and classes.
			//System.out.println("\n No established architecture!!!");
			antSystem = new AntSystem(this.initialSolution.number_comp, this.initialSolution.number_class,parametros);
		}

		long startTime = System.currentTimeMillis();
		Solution generatedSolution = antSystem.execute();
		long stopTime = System.currentTimeMillis();
		//generatedSolution.showSolution();

		System.out.println("Execution time is " + formatter.format((stopTime - startTime) / 1000d) + " seconds to generate new solution.\n");
		saveExtractArch(generatedSolution, nameFile + "_" + "modeloOtimizado" + "_"+parametros.ITERATIONS +"_" +parametros.ANTS+"_"+parametros.RO+"_"+parametros.ALPA+"_"
				+parametros.BETA + "_" + trial);
		saveValues(generatedSolution,((stopTime - startTime) / 1000d),nameFile + "_dados", trial);
		//savePheromoneMatrix("pheromone_matrix_"  + "modeloOtimizado" + "_"+parametros.ITERATIONS +"_" +parametros.ANTS+"_"+parametros.RO+"_"+parametros.ALPA+"_"
		//		+parametros.BETA + "_" + trial);
	}

	private void savePheromoneMatrix(String nameFile) throws IOException{
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Class x Component \n");
		bufferedWriter.newLine();
		for (int i = 0; i < antSystem.colony.getPheromoneMatrix().classes; i++) {
			bufferedWriter.write("[");
			for (int j = 0; j < antSystem.colony.getPheromoneMatrix().components; j++) {
				bufferedWriter.write(antSystem.colony.getPheromoneMatrix().componentClass[i][j] + ", ");
			}
			bufferedWriter.write("]");
			bufferedWriter.newLine();
		}

		System.out.println("\n Class x Class \n");
		for (int i = 0; i < antSystem.colony.getPheromoneMatrix().classes; i++) {
			bufferedWriter.write("[");
			for (int j = 0; j < antSystem.colony.getPheromoneMatrix().classClass[0].length ; j++) {
				bufferedWriter.write(antSystem.colony.getPheromoneMatrix().classClass[i][j] + ", ");
			}
			bufferedWriter.write("]");
			bufferedWriter.newLine();
		}

		bufferedWriter.close();

	}

	private void saveExtractArch(Solution s, String nameFile) throws IOException {
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		loadModel.showSolution(s,bufferedWriter, this.initialSolution.mapNewId2OldId, this.initialSolution.mapOldId2NewId);
		bufferedWriter.close();
	}

	private void saveValues(Solution s, double d, String nameFile, int trial) throws IOException {
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Trial: " + trial);
		bufferedWriter.newLine();
		bufferedWriter.write("Iterations: " + parametros.ITERATIONS);
		bufferedWriter.newLine();	            
		bufferedWriter.write("Ants:" + parametros.ANTS);
		bufferedWriter.newLine();
		bufferedWriter.write("RO: " + parametros.RO);
		bufferedWriter.newLine();
		bufferedWriter.write("ALPA: " + parametros.ALPA);
		bufferedWriter.newLine();
		bufferedWriter.write("BETA: " + parametros.BETA);
		bufferedWriter.newLine();
		bufferedWriter.write("MQ Inicial: " + initialSolution.mMetric);
		bufferedWriter.newLine();
		bufferedWriter.write("MQ: " + s.mMetric);
		bufferedWriter.newLine();
		bufferedWriter.write("Media MQ: " + antSystem.mediaMq);
		bufferedWriter.newLine();

		bufferedWriter.write("Quantidade componentes: " + s.componentClasses.size());
		bufferedWriter.newLine();
		bufferedWriter.write("Quantidade de classes: " + s.classComponent.size());
		bufferedWriter.newLine();

		if(s.type != null || s.type != ""){
			bufferedWriter.write("Total of relation penalized: " + s.penaltysOfSolution.listBadRel.size());
			bufferedWriter.newLine();	
			bufferedWriter.write("Total of penalties (h): " + s.totalOfPenalties);
			bufferedWriter.newLine();
			bufferedWriter.write("Total of penalties (1 - h): " + s.totalOfPenalties2);
			bufferedWriter.newLine();

			for (int i = 0; i < antSystem.evolutionMq.length; i++) {
				bufferedWriter.write("-- Iteration " + i + " MQ value: " + antSystem.evolutionMq[i] + ", Total of penalties (h): "
						+ antSystem.penalties[i] + " Total of penalties (1 - h): " + antSystem.penalties2[i] +
						" Total of relation penalized: " + antSystem.relationPenalized[i]);
				bufferedWriter.newLine();
			}

			bufferedWriter.write("Time Execution: " + formatter.format(d));
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			bufferedWriter.close();
		}	
	}
}
