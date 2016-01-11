package projectEDR;
import java.util.ArrayList;


public class Cas {
	private boolean pos;
	private ArrayList<Literal> litteraux;
	private String attributResultat;

	public Cas(ArrayList<Literal> l, String ar){
		litteraux=l;
		attributResultat = ar;
	}
	

	public boolean getB(){
		return pos;
	}
	
	public String getAttributResultat(){
		return attributResultat;
	}

	public String toString(){
		String s = "";
		for(int i = 0; i<litteraux.size(); i++){
			s += litteraux.get(i).valeur;
			s+="  ";		
		}
		s+= getB();
		return s;
	}
	public int size(){
		return litteraux.size();
	}
	public void setB(boolean b) {
		pos = b;
	}
	
	public ArrayList<Literal> getListLiteral(){return litteraux;}
	
	public Literal getLiteral(int index){
		return litteraux.get(index);
	}
}
