package projectEDR;

import java.util.ArrayList;

import weka.core.Instances;


public class FoilP {

	//liste des règles
	private ArrayList<Regle> regles;

	//liste des + et -
	// + = true, - = false
	private ArrayList<Cas> pos;
	private ArrayList<Cas> neg;

	//ensemble des cas
	private EnsembleDeCas ensembleDeCas;

	//initialisation
	public void init(String file){
		regles = new ArrayList<Regle>();

		//gestion lecture du fichier
		//remplacer le paramètre avec un système d'ouverture de fichier
		//+ vérification type de fichier et gestion exception
		myArffReader reader = new myArffReader(file);
		Instances data = reader.getData();

		//attribution des attributs
		String att [] = new String[data.numAttributes()];
		for(int i = 0; i<att.length; i++){
			att[i] = data.attribute(i).name();
		}
		ensembleDeCas = new EnsembleDeCas(att, data.attribute(att.length-1).name());

		//attribution des cas
		ArrayList<Literal> litt;
		for(int i = 0; i<data.numInstances(); i++){
			litt = new ArrayList<>();
			for(int j = 0; j<data.instance(i).numAttributes(); j++){
				litt.add(new Literal(data.attribute(j).name(), data.instance(i).stringValue(j)));
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
		foil.init("data.arff");
		System.out.println(foil.getMaxGain(foil.getEnsembleDeCas().getPos(),foil.getEnsembleDeCas().getNeg()));
		System.out.println(foil.getEnsembleDeCas());
	}

	public ArrayList<Regle> algo(){
		//Apprendre(Pos, Neg)
		learn();
		//Début
		//Règles <- vide

		//Tant que Pos différent d'ensemble vide
		while(!pos.isEmpty()){
			Regle newRegle = new Regle(new ArrayList<Literal>(), true);

			//Neg2 <- Neg
			//Pos2 <- Pos
			ArrayList<Cas> neg2 = (ArrayList<Cas>) neg.clone();
			ArrayList<Cas> pos2 = (ArrayList<Cas>) pos.clone();
			//Tant que Neg2 différent d'ensemble vide
			while(!neg2.isEmpty()){

			}
		}


		return regles;
	}


	private void learn() {
		pos = ensembleDeCas.getPos();
		neg = ensembleDeCas.getNeg();
	}

	public EnsembleDeCas getEnsembleDeCas() {
		return ensembleDeCas;
	}

	public static double gain(Literal l,EnsembleDeCas edc,ArrayList<Cas> pos2, ArrayList<Cas> neg2){
		double nbPos=pos2.size();
		double nbNeg=neg2.size();
		double p=edc.getNbPositif(l);
		double n=edc.getNbNegatif(l);
		double gain = p*log2(p/(p+n)) - log2(nbPos/(nbPos+nbNeg));
		System.out.println("gain de "+l+" : P:"+nbPos+" N:"+nbNeg+" p:"+p+" n:"+n+" val "+gain);
		return gain;
	}

	public Literal getMaxGain(ArrayList<Cas> pos2, ArrayList<Cas> neg2){
		ArrayList<Literal> list_l=new ArrayList<Literal>();//=ensembleDeCas.getListLiteraux();
		int i=0;
		for(Cas c:ensembleDeCas){
			System.out.println(c);
			for(int j = 0; j<c.getListLiteral().size()-1; j++){
				boolean res=true;
				for(Literal l2:list_l){
					if(l2.equals(c.getListLiteral().get(j))){
						res=false;
					}
				}
				if(res){
					list_l.add(c.getListLiteral().get(j));
				}
			}
		}
		for(Literal l : list_l)
			System.out.println(l);
		double max_gain=-1;
		Literal best_l=null;
		for(Literal l:list_l){
			double res_gain=gain(l,ensembleDeCas,pos2,neg2);
			if(res_gain>max_gain){
				max_gain=res_gain;
				best_l=l;
			}
		}
		return best_l;
	}

	public static double log2(double x) {
		return Math.log(x)/Math.log(2.0d);
	}
}
