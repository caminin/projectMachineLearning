package projectEDR;

public class Literal {
	public String attribut="";
	public String valeur="";
	
	public Literal(String _attribut, String _valeur){
		attribut=_attribut;
		valeur=_valeur;
	}
	
	public String toString(){
		String s = "";
		s += attribut+"/"+valeur;
		return s;
	}
	
	public boolean equals(Literal l){
		boolean res;
		if(l.attribut.equalsIgnoreCase(attribut)&&l.valeur.equalsIgnoreCase(valeur)){
			res=true;
		}
		else{
			res=false;
		}
		return res;
	}
}
