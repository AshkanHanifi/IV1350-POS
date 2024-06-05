import java.util.Random;

public class RandomInheritance extends Random {
    
    public RandomInheritance(){
        super();
    }


    /**
     * Creates an array with random ints
     * @param arraySize the size of the array
     * @return an array with random ints as elements
     */
    public int[] randomArray(int arraySize){
        int[] randomArray=new int[arraySize];
        for(int i=0; i<arraySize;i++){
            randomArray[i]=super.nextInt();
        }
        return randomArray;
    }
}
