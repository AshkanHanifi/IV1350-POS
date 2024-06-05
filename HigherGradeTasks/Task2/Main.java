

public class Main {
    public static void main(String[] args) {
        RandomComposition randomComp=new RandomComposition();
        RandomInheritance randomInheritance=new RandomInheritance();

        int arraySize=5;

        int[] randomArrayComp=randomComp.randomArray(arraySize);
        int[] randomArrayInheritance=randomInheritance.randomArray(arraySize);

        printArray(randomArrayComp, "composition");
        printArray(randomArrayInheritance, "inheritance");


    }

    private static void printArray(int[] arr, String msg){
        System.out.println("Printing array of random ints made through " + msg + ": ");
        for(int elem:arr){
            System.out.print(elem + " ");
        }
        System.out.println();
        System.out.println();
    }
}
