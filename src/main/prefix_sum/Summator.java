package main.prefix_sum;

import java.util.List;
import java.util.concurrent.Callable;

public class Summator implements Runnable{

    private List<Integer> array;
    private int start;
    private int end;
    private int step;
    private int step1;


    public Summator(int start, int end, int step, int step1,List<Integer> array){
        this.array = array;
        this.step = step;
        this.start = start;
        this.end = end;
        this.step1 = step1;
    }
    @Override
    public void run() {
        for (int i = start+step1; i < end; i+= 2*step){
            Integer result =  new Integer(array.get(i).intValue() + array.get(i+step).intValue());
            array.set(i+step, result);
        }
    }

}
