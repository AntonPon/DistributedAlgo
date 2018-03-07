package main.prefix_sum;

public class PrefixSum {

public static int[] prefixSum(int[]  array ){
 int[] result = new int[array.length + 1];
 result[0] = 0;
 for (int i = 0; i < array.length; i ++){
     result[i+1] = result[i] + array[i];
 }

 return result;
}


    public static void main(String[] args){
    int[] numbers = new int[]{1,1,1,1,1,1,1,1,1};
    int[] res =  prefixSum(numbers);

    for (int i = 0; i < res.length; i++){
        System.out.print(res[i]+ " ");
    }
}

}
