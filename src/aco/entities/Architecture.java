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
import java.util.Map.Entry;
import java.util.Set;

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
	private BufferedWriter bufferedWriter;

	public int []evolutionClassRelationE;
	public int []evolutionClassRelationI;
	public int []evolutionComponentClass;

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
		evolutionClassRelationE = new int[initialSolution.classComponent.size()];
		evolutionClassRelationI = new int[initialSolution.classComponent.size()];
		evolutionComponentClass = new int[initialSolution.componentClasses.size()];
	}

	public Solution initAntSystem(int trial) throws Exception{
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

			Matrix m = new Matrix(initialSolution.componentClasses.size(), initialSolution.classComponent.size());
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
		saveExtracInfosClass(generatedSolution, nameFile + "_" + "modeloOtimizado" + "_"+parametros.ITERATIONS +"_" +parametros.ANTS+"_"+parametros.RO+"_"+parametros.ALPA+"_"
				+parametros.BETA+ "_infos_relationXclasses" + ".csv", trial);
		saveExtracInfosComponents(generatedSolution,  nameFile + "_" + "modeloOtimizado" + "_"+parametros.ITERATIONS +"_" +parametros.ANTS+"_"+parametros.RO+"_"+parametros.ALPA+"_"
				+parametros.BETA + "_infos_componentXclasses"+".csv", trial);

		return generatedSolution;
	}

	public void saveExtracInfosComponents(Solution s, String nameFile, int trial) throws IOException{
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Component,Class,Trial");
		bufferedWriter.newLine();


		for (Entry<Integer, Set<Integer>> comp : s.componentClasses.entrySet()) {
			bufferedWriter.write(comp.getKey()+ "," + comp.getValue().size() + "," +trial);
			bufferedWriter.newLine();
			if(s.classQtdRelationI.get(comp.getKey()) != null && comp.getValue().size() != 0){
				this.evolutionComponentClass[comp.getKey()] = this.evolutionComponentClass[comp.getKey()] + comp.getValue().size();
			}
		}
		bufferedWriter.close();
	}


	public void saveExtracInfosClass(Solution s, String nameFile, int trial) throws IOException{
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write("Class,Internal,External,Trial");
		bufferedWriter.newLine();

		for (Entry<Integer, Integer> class_ : s.classComponent.entrySet()) {
			bufferedWriter.write(class_.getKey()+ "," + s.classQtdRelationI.get(class_.getKey()) + ","+ s.classQtdRelationE.get(class_.getKey()) +"," +trial);
			bufferedWriter.newLine();

			if(s.classQtdRelationE.get(class_.getKey()) != null){
				this.evolutionClassRelationE[class_.getKey()] = this.evolutionClassRelationE[class_.getKey()] +  s.classQtdRelationE.get(class_.getKey());
			}
			if(s.classQtdRelationI.get(class_.getKey()) != null){
				this.evolutionClassRelationI[class_.getKey()] = this.evolutionClassRelationI[class_.getKey()] +  s.classQtdRelationI.get(class_.getKey());
			}
		}
		bufferedWriter.close();
	}

	private void savePheromoneMatrix(String nameFile) throws IOException{
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file);
		bufferedWriter = new BufferedWriter(fileWriter);

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

		bufferedWriter.write("\n Class x Class \n");
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
		bufferedWriter = new BufferedWriter(fileWriter);
		loadModel.showSolution(s,bufferedWriter, this.initialSolution.mapNewId2OldId, this.initialSolution.mapOldId2NewId);
		bufferedWriter.close();
	}

	private void saveValues(Solution s, double d, String nameFile, int trial) throws IOException {
		File file = new File(this.diretorioResults,nameFile);
		FileWriter fileWriter = new FileWriter(file,true);
		bufferedWriter = new BufferedWriter(fileWriter);

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
