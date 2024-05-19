package se.kth.iv1350.pos.model;

public class NoSuchItemException extends Exception{
    private String itemIdentifier;

    public NoSuchItemException(String itemIdentifier){
        super("Unable to find item with identifier: " + itemIdentifier);
        this.itemIdentifier=itemIdentifier;
    }

    public String getItemThatCanNotBeFound(){
        return itemIdentifier;
    }

}