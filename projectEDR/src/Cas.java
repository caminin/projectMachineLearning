import java.util.ArrayList;


public class Cas {
	private boolean pos;
	private String[] litteraux;
	
	//exemple d'instanciation :
	//Cas c = new Cas();
	public Cas(String[] l, boolean p){
		pos = p;
		litteraux = l;
	}
	
	public boolean getB(){
		return pos;
	}
	
	public String toString(){
		String s = "";
		for(int i = 0; i<litteraux.length; i++){
			s += litteraux[i]+"  ";
		}
		return s;
	}
}
