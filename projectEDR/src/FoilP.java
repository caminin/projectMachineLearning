import java.util.ArrayList;


public class FoilP {
	
	//liste des règles
	private ArrayList<Regle> regles;
	
	//liste des + et -
	// + = true, - = false
	private ArrayList<Boolean> posAndNeg;
	
	//ensemble des cas
	private EnsembleDeCas cas;
	
	//initialisation
	public void init(){
		regles = new ArrayList<Regle>();
		posAndNeg = new ArrayList<Boolean>();
		
		//plus tard remplacé par la lecture du fichier
		String att [] = {"Prévisions", "Température", "Humidité", "Vent"};
		cas = new EnsembleDeCas(att);
	}
	
	//permet de rajouter un cas
	public void add(Cas c){
		cas.add(c);
	}
	
	
	//main
	public static void main (String args[]){
		FoilP foil = new FoilP();
		foil.init();
		//foil.read("filename");
		foil.add(new Cas(new String[]{"Soleil", "Chaude", "Elevée", "Faible", "Sport"}, false));
		System.out.println("Hello there");
		System.out.println(foil.getEnsembleDeCas());
	}
	
	
	
	
	private EnsembleDeCas getEnsembleDeCas() {
		return cas;
	}

	public static double gain(int nbPos, int nbNeg, EnsembleDeCas edc){
		return nbPos*log2(nbPos/(nbPos+nbNeg)) - log2(edc.getNbCasPositif()/(edc.size()));
	}
	
	/*public static double ent(int pplus, int pmoins){
		return 0-pplus*log2(pplus) - pmoins*log2(pmoins);
	}*/
	
	public static double log2(double x) {
	     return Math.log(x)/Math.log(2.0d);
	}
}
