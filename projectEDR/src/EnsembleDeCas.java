import java.util.ArrayList;
public class EnsembleDeCas extends ArrayList<Cas>{
	private int nbAttributs;
	private String[] attributs;
	public EnsembleDeCas(String[] s) {
		attributs = s;
		nbAttributs = s.length;


	}
	public int getNbCasPositif() {
		//compter les cas positifs pour le calcul du gain
		int nbCasPositif = 0;
		for(Cas c : this){
			if(c.getB()){
				nbCasPositif++;
			}
		}
		return nbCasPositif;
	}
	
	public String toString(){
		String s = "Cas  ";
		for(int i = 0 ; i < attributs.length; i++){
			s+=attributs[i]+"  ";
		}
		s += "\n";
		int counter = 1;
		for(Cas c : this){
			s += counter +" "+ c;
			counter++;
		}
		return s;
	}
}