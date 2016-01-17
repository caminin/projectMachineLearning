package projectEDR;
import java.util.ArrayList;
import java.util.Vector;
public class EnsembleDeCas extends ArrayList<Cas>{

	private static final long serialVersionUID = 1L;
	private String[] attributs;
	private String attributResultat;
	public EnsembleDeCas(String[] s, String ar) {
		attributs = s;
		//Est l'attribut qui permet de comparer les résultats
		//Par exemple, play pour l'exemple de cours, on vérifie ensuite qu'il y a bien que
		//2 résultats possibles
		//ensuite on associe un résultat à true et l'autre à false
		attributResultat = ar;
	}
	
	//à partir des données, assigne le positif et le négatif (true, false)
	public void determinePosNeg(String attributePositif){
		
		for(int i = 0; i<this.size(); i++){
			if((this.get(i).getAttributResultat()).equals(attributePositif)){
				this.get(i).setB(true);
			}else{
				this.get(i).setB(false);
			}
		}
	}
	
	public ArrayList<Literal> getListLiteraux(){
		ArrayList<Literal> list_l=new ArrayList<Literal>();
		for(Cas c:this){
			for(Literal l:c.getListLiteral()){
				if(!list_l.contains(l)){
					list_l.add(l);
				}
			}
		}
		return list_l;
	}
	
	public ArrayList<Cas> getPos(){
		ArrayList<Cas> pos = new ArrayList<Cas>();
		for(Cas elem : this){
			if(elem.getB()){
				pos.add(elem);
			}
		}
		return pos;
	}
	
	public ArrayList<Cas> getNeg(){
		ArrayList<Cas> pos = new ArrayList<Cas>();
		for(Cas elem : this){
			if(!elem.getB()){
				pos.add(elem);
			}
		}
		return pos;
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
			s=s+attributs[i]+"  ";
		}
		s =s+ "\n";
		int counter = 1;
		for(Cas c : this){
			s =s+ counter +" "+ c+"\n";
			counter++;
		}
		return s;
	}
	
	public String getAttributResultat() {
		return attributResultat;
	}
	
	public Vector<String> getAttributs() {
		Vector<String> res=new Vector<>();
		for(String s:attributs){
			res.add(s);
		}
		return res;
	}
}