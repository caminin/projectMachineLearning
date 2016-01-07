import java.util.ArrayList;

import weka.core.Instances;


public class FoilP {

	//liste des règles
	private ArrayList<Regle> regles;

	//liste des + et -
	// + = true, - = false
	private ArrayList<Boolean> posAndNeg;

	//ensemble des cas
	private EnsembleDeCas ensembleDeCas;

	//initialisation
	public void init(){
		regles = new ArrayList<Regle>();
		posAndNeg = new ArrayList<Boolean>();

		//gestion lecture du fichier
		//remplacer le paramètre avec un système d'ouverture de fichier
		//+ vérification type de fichier et gestion exception
		myArffReader reader = new myArffReader("data.arff");
		Instances data = reader.getData();

		//attribution des attributs
		String att [] = new String[data.numAttributes()];
		for(int i = 0; i<att.length; i++){
			att[i] = data.attribute(i).name();
		}
		ensembleDeCas = new EnsembleDeCas(att, data.attribute(att.length-1).name());

		//attribution des cas
		String litt [];
		for(int i = 0; i<data.numInstances(); i++){
			litt = new String[data.instance(i).numAttributes()];
			for(int j = 0; j<data.instance(i).numAttributes(); j++){
				litt[j] = data.instance(i).stringValue(j);
			}
			//ajoute un cas
			// il faut comprendre ça : new Cas("sunny, cool, normal, FALSE, yes", "yes");
			// en gros je précise si ça sera un négatif ou un positif
			add(new Cas(litt, data.instance(i).stringValue(data.attribute(att.length-1))));
		}
		ensembleDeCas.determinePosNeg();
	}

	//permet de rajouter un cas
	public void add(Cas c){
		ensembleDeCas.add(c);
	}


	//main
	public static void main (String args[]){
		FoilP foil = new FoilP();
		foil.init();
		//foil.read("filename");
		System.out.println(foil.getEnsembleDeCas());
	}




	private EnsembleDeCas getEnsembleDeCas() {
		return ensembleDeCas;
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
