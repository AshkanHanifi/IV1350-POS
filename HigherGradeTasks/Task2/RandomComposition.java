import java.util.Random;

public class RandomComposition {

    Random random = new Random();

    public int[] randomArray(int arraySize){
        int[] randomArray=new int[arraySize];
        for(int i=0; i<arraySize;i++){
            randomArray[i]=random.nextInt();
        }
        return randomArray;
    }
    
}
