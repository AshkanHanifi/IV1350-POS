import java.util.Random;

public class RandomComposition {

    Random random = new Random();
    /**
     * Creates an array with random ints
     * @param arraySize the size of the array
     * @return an array with random ints as elements
     */
    public int[] randomArray(int arraySize){
        int[] randomArray=new int[arraySize];
        for(int i=0; i<arraySize;i++){
            randomArray[i]=random.nextInt();
        }
        return randomArray;
    }
    
}
