package main.prefix_sum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.log;

public class PrefixSum {

public static int[] prefixSum(int[]  array ){
 int[] result = new int[array.length + 1];
 result[0] = 0;
 for (int i = 0; i < array.length; i ++){
     result[i+1] = result[i] + array[i];
 }

 return result;
}


    public static void main(String[] args) throws InterruptedException {
        int power =  (int) (Math.log(100000000)/log(2));
        int arraySize =(int) Math.pow(2, power);
        int[] numbers = new int[arraySize];
        for(int i = 0; i < numbers.length; i++){
            numbers[i] = 1;
        }

        long startTime = System.currentTimeMillis();
        int[] res =  prefixSum(numbers);
    long endTime = System.currentTimeMillis() - startTime;
        System.out.println("for non parallel algorithm " + endTime);
//    for (int i = 0; i < res.length; i++){
//        System.out.print(res[i]+ " ");
//    }


        List<Integer> array =  Collections.synchronizedList(new ArrayList<Integer>());
        for (int i = 0; i < arraySize; i ++){
            array.add(new Integer(1));
        }
        long startTime2 = System.currentTimeMillis();
        runFirstPrefixSum(array);
        Integer lastNumber = array.get(array.size()-1);
        array.set(array.size()-1, 0);
        runSecondPrefixSum(array);
        array.add(lastNumber);
        long endTime2 = System.currentTimeMillis() - startTime2;
        System.out.println("for parallel algorithm " + endTime2);
//        for (Integer i: array){
//            System.out.print(i + " ");
//        }


       // System.out.println(Runtime.getRuntime().availableProcessors());
}

public static void runSecondPrefixSum(List<Integer> array){
    int threadsNumber = Runtime.getRuntime().availableProcessors();
    int depth = (int) (log(array.size())/log(2));
    for (int i = depth+1; i > 0; i--){
        int distPerComputation = (int)Math.pow(2, i);
        int lenPerCore = array.size()/distPerComputation;

        int perCore = 0;
        if (lenPerCore  >= threadsNumber){
            perCore = lenPerCore/threadsNumber;
        }else{
            perCore = 1;
        }

        Thread[] threads = new Thread[threadsNumber];
        int begin = 0;
        int end = distPerComputation*perCore-1;
        int step =(int)Math.pow(2, i-1);
        int step1 = step-1;
        for (int j = 0; j < Math.min(lenPerCore,threads.length); j++){
            threads[j] = new Thread( new DownSummator(begin, end, step, step1, array));
            begin = end+1;
            if (j == threads.length-2){
                end = array.size()-1;
            }else{
                end += distPerComputation*perCore;
            }
            threads[j].start();
        }
        for (int j = 0; j < Math.min(lenPerCore,threads.length); j++){
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

public static void runFirstPrefixSum(List<Integer> array) {
    int threadsNumber = Runtime.getRuntime().availableProcessors();
    int depth = (int) (log(array.size())/log(2));
    for (int i = 1; i < depth+1; i ++){
        int distPerComputation = (int)Math.pow(2, i);
        int lenPerCore = array.size()/distPerComputation;

        int perCore = 0;
        if (lenPerCore  >= threadsNumber){
            perCore = lenPerCore/threadsNumber;
        }else{
            perCore = 1;
        }

        Thread[] threads = new Thread[threadsNumber];
        int begin = 0;
        int end = distPerComputation*perCore-1;
        int step =(int)Math.pow(2, i-1);
        int step1 = step-1;
        for (int j = 0; j < Math.min(lenPerCore,threads.length); j++){
            threads[j] = new Thread( new Summator(begin, end, step, step1, array));
            begin = end+1;
            if (j == threads.length-1){
                end = array.size()-1;
            }else{
                end += distPerComputation*perCore;
            }
            threads[j].start();
        }
        for (int j = 0; j < Math.min(lenPerCore,threads.length); j++){
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}


}
