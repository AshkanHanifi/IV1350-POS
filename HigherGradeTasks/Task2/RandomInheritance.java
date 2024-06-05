import java.util.Random;

public class RandomInheritance extends Random {
    
    public RandomInheritance(){
        super();
    }


    public int[] randomArray(int arraySize){
        int[] randomArray=new int[arraySize];
        for(int i=0; i<arraySize;i++){
            randomArray[i]=nextInt();
        }
        return randomArray;
    }
}
