package projectEDR;


import java.util.ArrayList;

public class Regle {
	private ArrayList<Literal> lits;
	private boolean conclusion;
	public Regle(ArrayList<Literal> l, boolean c){
		lits = l;
		conclusion = c;
	}
}
