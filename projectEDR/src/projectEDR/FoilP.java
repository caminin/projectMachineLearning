package projectEDR;

import java.util.ArrayList;
import java.util.Vector;

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
	public Vector<String> init(String file, String attributePositive){
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
	
	private ArrayList<Cas> isOrNotSatisfied(ArrayList<Cas> ens_cas,boolean mustSatisfied,Literal literalCompare){
		ArrayList<Cas> array_res=new ArrayList<>();
		for(Cas c:ens_cas){
			boolean isSatisfying=false;
			for(Literal lit:c.getListLiteral()){
				if(lit.equals(literalCompare)){
					isSatisfying=true;
				}
			}
			if(mustSatisfied==isSatisfying){
				array_res.add(c);
			}
		}
		return array_res;	
	}

	public ArrayList<Regle> algo(FoilP foil){
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
				Literal l=foil.getMaxGain(pos2,neg2);
				newRegle.add(l);
				if(pos2.size()>0){
					pos2=isOrNotSatisfied(pos2, true,l );
				}
				neg2=isOrNotSatisfied(neg2, true,l );
			}
			regles.add(newRegle);
			for(Literal l:newRegle.getListLit()){
				pos=isOrNotSatisfied(pos, false,l);			
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
		 
		//System.out.println("gain de "+l+" : P:"+nbPos+" N:"+nbNeg+" p:"+p+" n:"+n+" val "+gain);
		return gain;
	}

	public Literal getMaxGain(ArrayList<Cas> pos2, ArrayList<Cas> neg2){
		ArrayList<Literal> list_l=new ArrayList<Literal>();//=ensembleDeCas.getListLiteraux();
		int i=0;
		for(Cas c:ensembleDeCas){
			//System.out.println(c);
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
		/*for(Literal l : list_l)
			System.out.println(l);*/
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
				//System.out.println("Je change de nombre");
				max_gain=res_gain;
				best_l=l;
			}	
		}
		return best_l;
	}
	public Vector<String> getHeader(){
		return ensembleDeCas.getAttributs();
	}
	public static double log2(double x) {
		return Math.log(x)/Math.log(2.0d);
	}
	
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

}
