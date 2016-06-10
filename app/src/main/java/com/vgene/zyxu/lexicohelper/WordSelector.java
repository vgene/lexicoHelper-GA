package com.vgene.zyxu.lexicohelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


import com.vgene.zyxu.lexicohelper.GA.Chromosome;
import com.vgene.zyxu.lexicohelper.GA.Fitness;
import com.vgene.zyxu.lexicohelper.GA.GeneticAlgorithm;
import com.vgene.zyxu.lexicohelper.GA.Population;
import com.vgene.zyxu.lexicohelper.GA.IterartionListener;

public class WordSelector {

    public static void main(String[] args) {
        Population<WordGroup> population = createInitialPopulation(Constants.POPULATION_SIZE);

        Fitness<WordGroup, Double> fitness = new MyFitness();

        GeneticAlgorithm<WordGroup, Double> ga = new GeneticAlgorithm<WordGroup, Double>(population, fitness);

        addListener(ga);

        ga.evolve(Constants.ITERATION_TIME);
    }

    public static ArrayList<MorphemeEntity> getWordList(int wordNum){

        int populationSize = wordNum/Constants.WORD_GROUP_COUNT;
        if (populationSize>Constants.POPULATION_SIZE)
            populationSize = Constants.POPULATION_SIZE;
        Population<WordGroup> population = createInitialPopulation(populationSize);
        if (population==null)
            return null;

        Fitness<WordGroup, Double> fitness = new MyFitness();

        GeneticAlgorithm<WordGroup, Double> ga = new GeneticAlgorithm<WordGroup, Double>(population, fitness);

        addListener(ga);
        ga.evolve(Constants.ITERATION_TIME);

        HashSet h = new HashSet(ga.getBest().wordEntities);
        ArrayList<MorphemeEntity> entities = new ArrayList<>();
        entities.addAll(h);
        return  entities;
    }

    /**
     * The simplest strategy for creating initial population <br/>
     * in real life it could be more complex
     */
    private static Population<WordGroup> createInitialPopulation(int populationSize) {
        Population<WordGroup> population = new Population<WordGroup>();

        ArrayList<MorphemeEntity> entities =MorphemeSelector.getRandomMorphemeList(populationSize * Constants.WORD_GROUP_COUNT);
        System.out.print(entities.size());
        try {
            for (int i = 0; i < populationSize; i++) {
                // each member of initial population
                // is mutated clone of base chromosome

                WordGroup chr = new WordGroup();
                for (int j=0;j<Constants.WORD_GROUP_COUNT;j++)
                {
                    MorphemeEntity wordEntity = entities.get(i*Constants.WORD_GROUP_COUNT+j);
                    chr.wordEntities.add(wordEntity);
                }

                population.addChromosome(chr);
            }
        }catch(Exception e){
            return null;
        }

        return population;
    }

    /**
     * After each iteration Genetic algorithm notifies listener
     */
    private static void addListener(GeneticAlgorithm<WordGroup, Double> ga) {
        // just for pretty print
        System.out.println(String.format("%s\t%s\t%s", "iter", "fit", "chromosome"));

        // Lets add listener, which prints best chromosome after each iteration
        ga.addIterationListener(new IterartionListener<WordGroup, Double>() {

            private final double threshold = 1e-5;

            @Override
            public void update(GeneticAlgorithm<WordGroup, Double> ga) {

                WordGroup best = ga.getBest();
                double bestFit = ga.fitness(best);
                int iteration = ga.getIteration();

                // Listener prints best achieved solution
                System.out.println(String.format("%s\t%s\t%s", iteration, bestFit, best));

                // If fitness is satisfying - we can stop Genetic algorithm
//                if (bestFit < this.threshold) {
//                    ga.terminate();
//                }
            }
        });
    }

    /**
     * Chromosome, which represents vector of five integers
     */
    public static class WordGroup implements Chromosome<WordGroup> {

        private static final Random random = new Random();

        private ArrayList<MorphemeEntity> wordEntities = new ArrayList<>(Constants.WORD_GROUP_COUNT);

        /**
         * Returns clone of current chromosome, which is mutated a bit
         */
        @Override
        public WordGroup mutate() {
            WordGroup result = this.clone();

            // just select random element of vector
            // and increase or decrease it on small value
            int mutation_num = (int) (Constants.MUTATING_RATE * Constants.WORD_GROUP_COUNT);
            if (mutation_num==0)
                mutation_num=1;

            while (mutation_num>0) {
                int index = random.nextInt(Constants.WORD_GROUP_COUNT);
                //// TODO: 16-6-9  use index to select morpheme
                MorphemeEntity entity= MorphemeSelector.getRandomMorpheme();
                boolean hasSame=false;

                for (int i=0;i<wordEntities.size();i++) {
                    assert entity != null;
                    if (wordEntities.get(i).getMorpheme().equals(entity.getMorpheme())){
                        hasSame=true;
                        break;
                    }
                }

                if (!hasSame)
                    wordEntities.set(index,MorphemeSelector.getRandomMorpheme());
                mutation_num--;
            }

            return result;
        }

        /**
         * Returns list of siblings
         * Siblings are actually new chromosomes,
         * created using any of crossover strategy
         */
        @Override
        public List<WordGroup> crossover(WordGroup other) {
            WordGroup thisClone = this.clone();
            WordGroup otherClone = other.clone();

            ArrayList<MorphemeEntity> entities = thisClone.wordEntities;

//            for (int i=0;i<otherClone.wordEntities.size();i++){
//                if (!entities.contains(otherClone.wordEntities.get(i)))
//                    entities.add(otherClone.wordEntities.get(i));
//            }

            // 将index1和index2中间的部分交换
            int index1 = random.nextInt(this.wordEntities.size());
            int index2 = random.nextInt(this.wordEntities.size());

            //保证index1小于index2
            if (index2<index1){
                int tmp = index2;
                index2 = index1;
                index1 = tmp;
            }

            for (int i = index1; i <= index2; i++) {
                MorphemeEntity tmp = thisClone.wordEntities.get(i);
                thisClone.wordEntities.set(i, otherClone.wordEntities.get(i));
                otherClone.wordEntities.set(i, tmp);
            }

            return Arrays.asList(thisClone, otherClone);
        }

        public WordGroup clone() {
            WordGroup clone = new WordGroup();
            clone.wordEntities.addAll(this.wordEntities);
            return clone;
        }

        @Override
        public String toString() {
            return Arrays.toString(this.wordEntities.toArray());
        }

    }

    /**
     * Fitness function, which calculates difference between chromosomes vector
     * and target vector
     */
    public static class MyFitness implements Fitness<WordGroup, Double> {



        @Override
        public Double calculate(WordGroup chromosome) {
            double deltaF1 = 0;
            double deltaF2;
            double delta;

            //计算F1
            int size = chromosome.wordEntities.size();
            for (int i=0;i<size; i++){
                deltaF1 += chromosome.wordEntities.get(i).getFitness();
            }

            //TODO 根据联系计算F2
            //TODO 根据首字母计算连通度

            int connectNum=0;
            for (int i=0;i<size;i++){
                for (int j=i+1;j<size;j++)
                    if (chromosome.wordEntities.get(i).getMorpheme().charAt(0)==chromosome.wordEntities.get(j).getMorpheme().charAt(0))
                        connectNum++;
            }

            deltaF2 = connectNum;
//            System.out.print("connnectNum:"+connectNum);

            delta = Constants.WEIGHT_F1*deltaF1 - Constants.WEIGHT_F2*deltaF2;

            return delta;
        }



    }
}
