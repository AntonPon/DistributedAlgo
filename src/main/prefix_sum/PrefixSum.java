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
//        int arraySize = 100000000;
//        int[] numbers = new int[arraySize];
//        for(int i = 0; i < numbers.length; i++){
//            numbers[i] = 1;
//        }
//
//        long startTime = System.currentTimeMillis();
//        int[] res =  prefixSum(numbers);
//    long endTime = System.currentTimeMillis() - startTime;
//
////    for (int i = 0; i < res.length; i++){
////        System.out.print(res[i]+ " ");
////    }
//        System.out.println('\n' + "time " + endTime/1000.0);

        List<Integer> array =  Collections.synchronizedList(new ArrayList<Integer>());
        for (int i = 0; i < 16; i ++){
            array.add(new Integer(1));
        }

        runDistributedPrefixSum(array);
        for (Integer i: array){
            System.out.print(i + " ");
        }


       // System.out.println(Runtime.getRuntime().availableProcessors());
}

public static void runDistributedPrefixSum(List<Integer> array) {
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
                System.out.println(i + " out of " + depth);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}


}
