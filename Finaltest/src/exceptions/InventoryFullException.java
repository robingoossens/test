package exceptions;


public class InventoryFullException extends Exception{
	public InventoryFullException(String str){
        super(str);
    }
}
