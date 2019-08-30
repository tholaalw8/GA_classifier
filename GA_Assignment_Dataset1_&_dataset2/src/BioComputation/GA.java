
package BioComputation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static BioComputation.SimpleGeneticAlgorithm.wildCardONorOFF;

public class GA {

    public GA() {
    }
   
   
    public static Individual[] initiateArray(int p_size, int gene_size, int NumR, int ConL) {
        Individual[] temp = new Individual[p_size];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new Individual(gene_size, NumR, ConL);
        } 
        return temp;
    }

    public static Individual[] createPopulation(Individual[] array) {
        // Create population. 
       
        for (Individual a : array) {
            for (int j = 0; j < a.gene.length; j++) {
                ArrayList<String> operators ;
                if (wildCardONorOFF == 1){
                 operators = new ArrayList(Arrays.asList("0", "1", "#"));
                }else{
                operators = new ArrayList(Arrays.asList("0", "1"));
                }
                a.gene[j] = operators.get(new Random().nextInt(2)); 
            } // for j              
            a.create_rulebase(); // iterate through population and convert genes to the to the rulebases 
        }        
        return array;
    }

    public static Individual[] tournmentSelection(Individual[] original) {
        
        //Selection for this algorithm is tournement selection, where two random individuals are compared and the fitter one get to mate
        int parent1, parent2;
        Individual[] temp = initiateArray(original.length, original[0].gene.length, original[0].NumR, original[0].ConL);
        for (int i = 0; i < original.length; i++) {
            parent1 = new Random().nextInt(original.length);
            parent2 = new Random().nextInt(original.length);

            if (original[parent1].fitness >= original[parent2].fitness) {
                temp[i] = new Individual(original[parent1]);
            } else {
                temp[i] = new Individual(original[parent2]);
            }
        }
        return temp;
    }

    public static Individual[] recombination(Individual[] original) {
        // Crossover
        int gene_size = original[0].gene.length;
        int populationSize = original.length;
        int NumR = original[0].rulebase.length;
        int ConL = original[0].rulebase[0].cond.length;
        Individual[] modified = new Individual[populationSize];

        for (int i = 0; i < populationSize; i += 2) {
            Individual temp1 = new Individual(gene_size, NumR, ConL);
            Individual temp2 = new Individual(gene_size, NumR, ConL);
            int x_point = new Random().nextInt(gene_size);

            for (int j = 0; j < x_point; j++) {
                temp1.gene[j] = original[i].gene[j];
                temp2.gene[j] = original[i + 1].gene[j];
            }

            for (int j = x_point; j < gene_size; j++) {
                temp1.gene[j] = original[i + 1].gene[j];
                temp2.gene[j] = original[i].gene[j];
            }

            temp1.create_rulebase();
            temp2.create_rulebase();
            modified[i] = new Individual(temp1);
            modified[i + 1] = new Individual(temp2);
        }
        return modified;
    }

    public static Individual[] mutation(Individual[] original, double mute_rate) {   
        // Mutation
        int p_size = original.length; 
        int gene_size = original[0].gene.length;         

        for (int i = 0; i < p_size; i++) { 
            for (int j = 0; j < gene_size; j++) { 
                double d = Math.random(); 
                if (d < mute_rate) { 
                    ArrayList<String> operators = null;
                    if(wildCardONorOFF == 1){
                    operators = new ArrayList(Arrays.asList("0", "1", "#"));
                    }else{
                       operators = new ArrayList(Arrays.asList("0", "1"));
                    }
                    d = Math.random();
                    for (int k = 0; k < operators.size(); k++) {
                        if (original[i].gene[j].equals(operators.get(k))) {
                            String s = operators.get(k);
                            operators.remove(k);
                            int temp;
                            if (d < 0.5) {
                                temp = 0;
                            } else {
                                temp = 1;
                            }
                            if (wildCardONorOFF == 1){
                            original[i].gene[j] = operators.get(new Random().nextInt(2)); 
                            }else{
                             original[i].gene[j] = operators.get(new Random().nextInt(1));
                            }
                            System.out.print("");
                            operators.add(s);
                        }
                        
                    }
                }
            }
            original[i].create_rulebase();
        }
        return original;
    }

    public static Individual evaluate(Individual[] array, Individual best) {
        Individual temp;
        for (Individual arr1 : array) {
            if (arr1.fitness > best.fitness) {
                best = arr1;
            }
        }
        temp = new Individual(best);
        return temp;
    }

    public static String print_rules(Rule[] rules) {
        String s = "";
        int count = 1;
        for (Rule r : rules) {
            s = s + "Rule " + count + ": ";
            for (int i = 0; i < r.cond.length; i++) {
                s = s + r.cond[i];
            }
            s = s + " = " + r.out + "\n";
            count++;
        }
        return s;
    }
    
        public static boolean matchesCond(int[] data, String[] rule) {
        for (int i = 0; i < data.length; i++) {
            String s = "" + data[i];  
            if ((rule[i].equals(s) != true) && (rule[i].equals("#") != true)) {
                return false;
            }
        }
        return true;
    }
    
    public static void fitness_score(Individual solution, ArrayList<Data> data) {

        solution.fitness = 0;
        for (int i = 0; i < data.size(); i++) {
            for (Rule rulebase : solution.rulebase) {
                if (matchesCond(data.get(i).variables, rulebase.cond) == true) {
                    String s = "" + data.get(i).type;
                    if (rulebase.out.equals(s) == true) {
                        solution.fitness++;
                    }
                    break; 
                }
            }
        }
    }

}
