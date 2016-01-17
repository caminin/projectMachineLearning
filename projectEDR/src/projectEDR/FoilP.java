package projectEDR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import weka.core.Instances;


public class FoilP {

	//liste des règles avec leur nombre d'instances associés
	private HashMap<Regle,Integer> regles;

	//liste des + et -
	// + = true, - = false
	private ArrayList<Cas> pos;
	private ArrayList<Cas> neg;

	//ensemble des cas
	private EnsembleDeCas ensembleDeCas;

	//initialisation
	public Vector<String> init(String file, String attributePositive){
		regles = new HashMap<>();

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
		Vector<String> plop = new Vector<String>();
		//attribution des cas
		ArrayList<Literal> litt;
		for(int i = 0; i<data.numInstances(); i++){
			litt = new ArrayList<>();
			for(int j = 0; j<data.instance(i).numAttributes(); j++){
				litt.add(new Literal(data.attribute(j).name(), data.instance(i).stringValue(j)));
				plop.addElement(data.instance(i).stringValue(j));
			}
			//ajoute un cas
			// il faut comprendre ça : new Cas("sunny, cool, normal, FALSE, yes", "yes");
			// en gros je précise si ça sera un négatif ou un positif
			add(new Cas(litt, data.instance(i).stringValue(data.attribute(att.length-1))));
		}
		ensembleDeCas.determinePosNeg(attributePositive);
		return plop;
	}

	//permet de rajouter un cas
	public void add(Cas c){
		ensembleDeCas.add(c);
	}
	
	//retourne un arraylist contenant les cas qui n'ont pas listLiteralCompare comme littéraux
	private ArrayList<Cas> retireCasDifferent(ArrayList<Cas> ens_cas,ArrayList<Literal> listLiteralCompare){
		ArrayList<Cas> array_res=new ArrayList<>();
		for(Cas c:ens_cas){
			boolean mustAdd=false;
			for(Literal literalCompare:listLiteralCompare){//on parcours tous les littéraux à comparer
				boolean isContenu=false;
				for(Literal lit:c.getListLiteral()){//on parcours tous les littéraux du cas
					if(literalCompare.equals(lit)){//si le litéral à un équivalent dans le cas
						isContenu=true;
					}
				}
				if(isContenu==false){//si l'élément n'est pas contenu dans cas
					mustAdd=true;//on ajoute 
				}
				
			}
			if(mustAdd==true){// si on doit ajouter on le fait là
				array_res.add(c);
			}
		}
		return array_res;	
	}
	
	//retourne un arraylist contenant les éléments dans ens_cas qui ont literalCompare dans leur littéraux
	private ArrayList<Cas> retireCasEgaux(ArrayList<Cas> ens_cas,Literal literalCompare){
		ArrayList<Cas> array_res=new ArrayList<>();
		for(Cas c:ens_cas){
			boolean isSatisfying=false;
			for(Literal lit:c.getListLiteral()){
				if(lit.equals(literalCompare)){// si l'élément a un équivalent dans le cas
					isSatisfying=true;
				}
			}
			if(isSatisfying==true){//on l'ajoute ici
				array_res.add(c);
			}
		}
		return array_res;	
	}
	
	
//Permet d'appliquer l'algo FoilP
	@SuppressWarnings("unchecked")
	public HashMap<Regle,Integer> algo(){
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
				Literal l=getMaxGain(pos2,neg2);
				newRegle.add(l);
				pos2=retireCasEgaux(pos2,l);//on garde les cas qui ont l dans pos2
				neg2=retireCasEgaux(neg2,l);//même chose dans neg2
			}
			
			int i=pos.size();
			pos=retireCasDifferent(pos,newRegle.getListLit());//on retire dans pos les éléments qui ont les littéraux de la nouvelle règle en littéral
			i=i-pos.size();
			regles.put(newRegle,i);//on ajoute la règle
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

	//Calcul le gain d'un littéral
	public double gain(Literal l,ArrayList<Cas> pos2, ArrayList<Cas> neg2){
		double nbPos=pos2.size();
		double nbNeg=neg2.size();
		double p=getNbPositiforNegatif(l,true,pos2,ensembleDeCas);
		double n=getNbPositiforNegatif(l,false,neg2,ensembleDeCas);
		double gain;
		if(p==0){
			gain=Double.NEGATIVE_INFINITY;
		}
		else{
			gain= p*(log2(p/(p+n)) - log2(nbPos/(nbPos+nbNeg)));
		}
		 
		return gain;
	}
	
	//retourne le littéral qui a le meilleur gain
	public Literal getMaxGain(ArrayList<Cas> pos2, ArrayList<Cas> neg2){
		ArrayList<Literal> list_l=new ArrayList<Literal>();//=ensembleDeCas.getListLiteraux();
		for(Cas c:ensembleDeCas){//On récupère tous les littéraux différents en un seul exemplaire
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
		boolean init=true;
		double max_gain=-1;
		Literal best_l=null;
		for(Literal l:list_l){
			double res_gain=gain(l,pos2,neg2);
			if(init){
				max_gain=res_gain;
				best_l=l;
				init=false;
			}
			else if(res_gain>max_gain){
				max_gain=res_gain;
				best_l=l;
			}	
		}
		return best_l;
	}
	public Vector<String> getHeader(){
		return ensembleDeCas.getAttributs();
	}
	
	//calcul le log
	public static double log2(double x) {
		return Math.log(x)/Math.log(2.0d);
	}
	
	//Retourne le nombre d'élément qui ont isPos en conclusion et l en littéral
	public int getNbPositiforNegatif(Literal l,boolean isPos,ArrayList<Cas> ensCas,EnsembleDeCas ensCasAttribut){
		int index=-1,nbPos=0;
		for(int i=0;i<ensCasAttribut.getAttributs().size();i++){
			if(((String) ensCasAttribut.getAttributs().get(i)).equalsIgnoreCase(l.getAttribute())){
				index=i;
			}
		}
		for(int i=0;i<ensCas.size();i++){
			if(ensCas.get(i).getLiteral(index).equals(l)){
				if(ensCas.get(i).getB()==isPos){
					nbPos++;
				}
			}
		}
		return nbPos;
	}
	
	public int getNombreConclusion(){
		return ensembleDeCas.getNbCasPositif();
	}
}
