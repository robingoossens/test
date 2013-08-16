package gui;

/**
 * Class used to transfer the selection for use back to the main window.
 */
public class Selection {
	private Object selection;
	
	public Object getSelection(){
		return selection;
	}
	
	public void setSelection(Object s){
		selection = s;
	}
	
	public void clear(){
		selection = null;
	}
}
