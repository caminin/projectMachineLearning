package projectEDR;
import java.util.ArrayList;
public class EnsembleDeCas extends ArrayList<Cas>{
	private int nbAttributs;
	private String[] attributs;
	private String attributResultat;
	public EnsembleDeCas(String[] s, String ar) {
		attributs = s;
		nbAttributs = s.length;
		//Est l'attribut qui permet de comparer les résultats
		//Par exemple, play pour l'exemple de cours, on vérifie ensuite qu'il y a bien que
		//2 résultats possibles
		//ensuite on associe un résultat à true et l'autre à false
		attributResultat = ar;
	}
	
	//à partir des données, assigne le positif et le négatif (true, false)
	public void determinePosNeg(){
		String actuel = "";
		String precedent = this.get(0).getAttributResultat();
		this.get(0).setB(true);
		for(int i = 1; i<this.size(); i++){
			actuel = this.get(i).getAttributResultat();
			if(!actuel.equals(precedent)){
				this.get(i).setB(!this.get(i-1).getB());
			}else{
				this.get(i).setB(this.get(i-1).getB());
			}
			precedent = actuel;
		}
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
	public String getAttributResultat() {
		return attributResultat;
	}
	public String[] getAttributs() {
		return attributs;
	}
}