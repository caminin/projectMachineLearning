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
	
	public boolean getConclusion(){
		return conclusion;
	}
	
	public String toString(String conclusion){
		String res="";
		res="Règle : ";
		for(int i=0;i<lits.size();i++){
			Literal l=lits.get(i);
			res=res+l;
			if(i<lits.size()-1){
				res+=" AND ";
			}
		}
		res=res+"\n\t Conclusion : "+conclusion+"\n";
		
		return res;
	}
}