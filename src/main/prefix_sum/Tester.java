package main.prefix_sum;

public class Tester {

    public static void main(String ... args){
        System.out.println(Math.pow(2 ,3));
        int[] in = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        int start = 2;
        int end = 7;
        int step =0;
        int step1= 1;
        for (int i = start+step; i < end; i+=2*step1 ){
            int result =  in[i] + in[i+step1];
            in[i+step1] = result;
        }
        for (int i = 0; i < in.length; i++){
            System.out.print(in[i] + " ");
        }
    }
}
