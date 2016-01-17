package projectEDR;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class MyFrame extends JFrame implements ActionListener{
	
	private Box button_box;
	private JButton button_open;
	private JButton button_config;
	private JLabel affichage_att;
	private JScrollPane affichage;
	private JButton button_run;
	private FoilP foil;
	private String attributPositive;
	private File file;
	private static final long serialVersionUID = 1L;

	public MyFrame(){
		super("Choisissez le fichier de données");
		buildComponent();
		buildEvents();
		buildInterface();		
	}
	
	//Assigne les composants
	public void buildComponent(){
		attributPositive="";
		button_box=Box.createHorizontalBox();;
		button_open=new JButton("Ouvrir le fichier");
		button_config=new JButton("Choisir la conclusion");
		affichage_att=new JLabel();
		affichage=new JScrollPane();
		button_run=new JButton("Lancer le test");
		
		button_run.setVisible(false);
		button_config.setVisible(false);
		affichage.setVisible(true);
	}
	
	//Construit l'interface à partir des composants
	public void buildInterface(){
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		button_box.add(button_open);
		button_box.add(button_config);
		button_box.add(affichage_att);
		
		
		this.add(button_box, BorderLayout.NORTH);
		this.add(affichage,BorderLayout.CENTER);
		this.add(button_run, BorderLayout.SOUTH);
		
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/5);
		this.setSize((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/2);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//Assigne les listener aux boutons
	private void buildEvents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.button_open.addActionListener(this);
		this.button_config.addActionListener(this);
		this.button_run.addActionListener(this);
	}
	
	//Récupère le nom de l'attribut utilisé dans la conclusion
	public String getAttributPos(){
		Vector<String> columnNames = foil.getHeader();
		String s=columnNames.elementAt(columnNames.size()-1);
		return s;
	}
	
	//Change la valeur de l'attribut positif, est utilisé par la fenetre de config
	public void setAttribut(String att){
		attributPositive=att;
		String s=getAttributPos();
		affichage_att.setText("Conclusion : "+s+" = "+att);
		repaint();
	}
	
	//Met à jour l'algo avec la donnée file et l'attribut positif
	private void paint(){
		if(file!=null){
			Vector<String> content=foil.init(file.getAbsolutePath(), attributPositive);
			Vector<String> columnNames = foil.getHeader();
			
			Vector<Vector<String>> rowData = new Vector<Vector<String>>();
			for(int j=columnNames.size();j<content.size();j=j+columnNames.size()){
				Vector<String> testing = new Vector<String>();
				for(int i=0;i<columnNames.size();i++){
					testing.add(content.get(j+i));
				}
				rowData.addElement(testing);
			}	
			JTable table = new JTable(rowData, columnNames); 
			this.remove(affichage);
			affichage = new JScrollPane(table);
			this.add(affichage,BorderLayout.CENTER);
			affichage.setVisible(true);
			button_run.setVisible(true);
			repaint();
		}	
	}
	
	//Ouvre la fenetre pour choisir le fichier
	private void openFile(){
		final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		int returnVal = fc.showOpenDialog(MyFrame.this);
		
		if (returnVal == JFileChooser.FILES_ONLY) {
            file = fc.getSelectedFile();            
            foil = new FoilP();
            button_config.setVisible(true);
            paint();
            callConfig();
    		
        }
	}
	//Actions du listener en fonction du bouton
	@Override
	public void actionPerformed(ActionEvent boutton) {
		JButton b=(JButton) boutton.getSource();
		if (b.equals(this.button_open)){
			openFile();
		}
		else if (b.equals(this.button_run)){
			if(b.getText().equals("Retour")){//Si on a afficher les règles et quon revient en arrière
				b.setText("Lancer le test");
				paint();
			}
			else {//Si on doit afficher les règles
				foil.init(file.getAbsolutePath(), attributPositive);
				this.remove(affichage);
				b.setText("Retour");
				HashMap<Regle,Integer> res_regle=foil.algo();
				String s="";
				Set<Regle> cles = res_regle.keySet();
				Iterator<Regle> it = cles.iterator();
				while(it.hasNext()){
					Regle cle = (Regle) it.next();
					int valeur = res_regle.get(cle); 
					s=s+cle.toString(getAttributPos()+" = "+attributPositive)+" avec "+valeur+" exemples associés"+"\n";
				}
				s=s+"\n"+"NOMBRE TOTAL D'EXEMPLES AVEC CETTE CONCLUSION : "+foil.getNombreConclusion();
				JTextArea text=new JTextArea(s);
				text.setEditable(false);
				affichage=new JScrollPane(text);
				affichage.setVisible(true);
				this.add(affichage,BorderLayout.CENTER);
				repaint();
			}
		}
		else if(b.equals(this.button_config)){
			callConfig();
		}
	}
	
	//Appelle la fenetre de configuration
	public void callConfig(){
		String s=getAttributPos();
		Vector<String> list_res=new Vector<>();
		list_res.add(s);
		ArrayList<Cas> ens_cas=foil.getEnsembleDeCas();
		
		for(Cas c:ens_cas){
			String att=c.getAttributResultat();
			if(list_res.contains(att)==false){
				list_res.add(att);
			}
		}
		new ConfigFrame(list_res,this);
	}
		
}
