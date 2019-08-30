/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BioComputation;



import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SimpleGeneticAlgorithm {

    public static int wildCardONorOFF;
   
    public static void main(String[] args) throws FileNotFoundException, IOException {
       
        
        // Please change the dataSetNo variable to either 1 or 2 to load the corresponding data set.
        // ConL is automatically calculated by the program. 
        // Wildcard can be turned on or off
        
         int dataSetNo = 1 ;    // change this number to either select dataset 1 or dataset 2.  Select 1 for dataset 1, and 2 for dataset 2.
         
         
         String filename = null;
         
        if(dataSetNo == 1){
         filename = "data1.txt";
        }else if (dataSetNo == 2){
         filename ="data2.txt";
        }else{
         System.err.println(" \nPlEASE SELECT A CORRECT DATA SET FILE, set dataSetNo variable to either 1 or 2\n\n");
         System.exit(0);
        }
        
        
        
        wildCardONorOFF = 1; // to turn wildcard on enter 1, to off the wildcard set it to zero 0.
        int NumR = 10; // tweak this value to change the number of rules in the solution.
        
        int sizeOfPopulation = 100; // tweak here to set the population size to a desired value, the number should be of EVEN
       
        int number_of_generations = 150;  // no of generation
        
        // number of times this algorithm to repeat in order to provide an average fittest fitness score.
        int no_of_times_GA_to_run = 10; // this part controls how many genetic algorithms to run. if set 10 then 10 algorithms are going to run. This is to obtain average  result.
        
        double mutationRate = 0.02; // tweak this value to change mutation rate, toggle between 1 to 0. 1 is 100% and 0 is 0%. 0.02 is the defualt, 
        
        
       
        
         
        
         String outPutString = "";
        
        System.out.println("\n-----------------------------------------");
        System.out.println("         DATASET "+dataSetNo+"  SELECTED   "); 
        System.out.println("------------------------------------------");
        
        ArrayList<Data> data_set = create_data_set(filename);
        int ConL = data_set.get(0).Vars; 
        int gene_size = (ConL + 1) * NumR; // gene length is proportional to the 
        Individual gBest = new Individual(gene_size, NumR, ConL); // Store the fittest solution found
        Individual[] population = GA.initiateArray(sizeOfPopulation, gene_size, NumR, ConL);
        Individual[] child = GA.initiateArray(sizeOfPopulation, gene_size, NumR, ConL);
        int correct_rules = 0;
        int index = 0;
        int count =1;
        while (index < no_of_times_GA_to_run) { 
            Individual fittest = new Individual(gene_size, NumR, ConL); // this arr holds the fittest solutions
            //population is initialed with random genes
            population = GA.createPopulation(population);

            for (Individual pop : population) {
                GA.fitness_score(pop, data_set); 
            }

            int generation = 0;
            while (generation < number_of_generations) {

                // tournement selection used to create a child 
                child = GA.tournmentSelection(population);
                for (Individual pop : child) {
                    GA.fitness_score(pop, data_set);
                }

                // recombination
                child = GA.recombination(child);
                for (Individual pop : child) {
                    GA.fitness_score(pop, data_set);
                }

                // mutation 
                child = GA.mutation(child, mutationRate);
                for (Individual pop : child) {
                    GA.fitness_score(pop, data_set);
                }

                // evaluate
                fittest = GA.evaluate(child, fittest);

                for (int i = 0; i < sizeOfPopulation; i++) {
                    population[i] = new Individual(child[i]);
                }

                generation++;
                outPutString += fittest.fitness + ",";
                //test
               // System.out.print(+fittest.fitness+",");
            }
            outPutString += "\n";
            //System.out.println("\n");
           System.out.println("GA : "+count+"  Best fitness Score: " + fittest.fitness);
           count++;
            if(fittest.fitness == 64 ) correct_rules++;
            index++;
            //Comparison 
            if (fittest.fitness > gBest.fitness) {
                gBest = new Individual(fittest);
            }
        }
        // out put the result
       
        System.out.println("\nSystem ran "+no_of_times_GA_to_run+" GAs, and POPULATION_SIZE is "+sizeOfPopulation+", No OF Generation: "+number_of_generations);
        System.out.println("MUTATION_RATE is set to : "+mutationRate * 100+"% , GENE LENGTH  is "+gene_size);
        
        System.out.println("The number of rules used are :"+NumR);
        System.out.println(GA.print_rules(gBest.rulebase));
        
        if(correct_rules > 0){
        System.out.println("After " + no_of_times_GA_to_run + " runs, " + correct_rules + " correctly identified all " + data_set.size() +" items in the data set "+dataSetNo+".\n");
        }
        System.out.println("\n IN THE BELOW EACH ROW represents a GA run, AND EACH COLUMN iS BEST FITNESS SCORE OF THAT GENERATIONS\n");
        
        System.out.println(outPutString);
        System.out.println("SCROLL UP!!");
    }

 // Create an arr from the file data  
    public static ArrayList<Data> create_data_set(String filename) throws IOException {
        ArrayList<String> file_array = feed_data_from_file(filename);
        int data_size = file_array.get(0).length() - 1;
       
        ArrayList<Data> arr_temp = new ArrayList<>();

        file_array.stream().map((String a) -> {
            Data tmp = new Data(data_size);
            for (int i = 0; i < data_size; i++) {
                tmp.variables[i] = Character.getNumericValue(a.charAt(i));
            }
            tmp.type = Character.getNumericValue(a.charAt(data_size));
            return tmp;
        }).forEach((temp) -> {
            arr_temp.add(temp);
        });
        return arr_temp;
    }

     // This method is responsible for reading the file and creating a single String of contents. 
    public static ArrayList<String> feed_data_from_file(String filename) throws FileNotFoundException, IOException {
        ArrayList<String> arr = new ArrayList<>();
       
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine();
        String L1 = null;
        while ((L1 = br.readLine()) != null) {
            String str = "";
            for (int i = 0; i < L1.length(); i++) {
                if ((L1.charAt(i) == '0') || (L1.charAt(i) == '1')) {
                    str = str + L1.charAt(i);
                }
            }
            arr.add(str);
        }
        return arr;
    }

}
