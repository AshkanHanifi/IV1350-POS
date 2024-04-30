package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ExternalInventorySystem {
    private ArrayList<ItemDTO> database=new ArrayList<>();
    public ExternalInventorySystem() {
        createItems();
    }

    public ItemDTO getItemInfo(String itemIdentifier){
        ItemDTO wantedItem=findItem(itemIdentifier);
        return wantedItem;
    }

    public void updateInventory(SaleDTO saleDTO){
        HashMap<ItemDTO, Integer> items=saleDTO.getItems();
        for(ItemDTO item : items.keySet()){
            System.out.println("Told external inventory system to decrease inventory quantity of item " +
                    item.getItemIdentifier() + " by " + items.get(item) + " units");
        }
    }

    private void createItems(){
        //ItemDTO item1=new ItemDTO("abc123", "")
        try {
            File itemFile=new File("items.txt");
            Scanner fileReader=new Scanner(itemFile);
            while (fileReader.hasNext()){
                String line=fileReader.nextLine();
                line=line.replace(":", ".");
                String[] args=line.split(";");
                database.add(new ItemDTO(args[0], args[1], new Amount(Float.valueOf(args[2])), Float.valueOf(args[3]), args[4]));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private ItemDTO findItem(String itemIdentifier){
        for(ItemDTO item : database){
            if(itemIdentifier.equals(item.getItemIdentifier())){
                return item;
            }
        }
        return null;

    }
}
