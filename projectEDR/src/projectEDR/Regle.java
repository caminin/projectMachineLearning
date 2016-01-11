package projectEDR;


import java.util.ArrayList;

public class Regle {
	private ArrayList<Literal> lits;
	private boolean conclusion;
	public Regle(ArrayList<Literal> l, boolean c){
		lits = l;
		conclusion = c;
	}

	public void add(Literal l){
		lits.add(l);
	}
	
	public ArrayList<Literal> getListLit(){
		return lits;
	}
	
	public String toString(){
		String res="";
		res="Règle : ";
		for(Literal l:lits){
			res=res+l;
		}
		res=res+" Conclusion : "+conclusion;
		
		return res;
	}
}