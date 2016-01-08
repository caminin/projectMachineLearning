package projectEDR;
import java.util.ArrayList;


public class Cas {
	private boolean pos;
	private Literal[] litteraux;
	private String attributResultat;

	public Cas(Literal[] l, String ar){
		litteraux = l;
		attributResultat = ar;
	}

	public boolean getB(){
		return pos;
	}
	
	public String getAttributResultat(){
		return attributResultat;
	}

	public String toString(){
		String s = "";
		for(int i = 0; i<litteraux.length; i++){
			if(litteraux[i].equals(attributResultat)){
				s+="|";
			}
			s += litteraux[i];
			if(litteraux[i].equals(attributResultat)){
				s+="|";
			}
			s+="  ";		
		}
		s+= getB();
		s += "\n";
		return s;
	}

	public void setB(boolean b) {
		pos = b;
	}
}
