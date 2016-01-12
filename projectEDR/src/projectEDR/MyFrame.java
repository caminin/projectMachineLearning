package projectEDR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyFrame extends JFrame implements ActionListener{
	
	private JButton button_open;
	private JScrollPane affichage;
	private JButton button_run;
	private FoilP foil;
	private static final long serialVersionUID = 1L;

	public MyFrame(){
		super("Choisissez le fichier de données");
		buildComponent();
		buildEvents();
		buildInterface();		
	}
	
	public void buildComponent(){
		button_open=new JButton("Ouvrir le fichier");
		affichage=new JScrollPane();
		affichage.setBackground(Color.WHITE);
		
		button_run=new JButton("Lancer le test");
		button_run.setVisible(false);
		
		affichage.setVisible(true);
	}
	
	public void buildInterface(){
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		this.add(button_open, BorderLayout.NORTH);
		this.add(affichage,BorderLayout.CENTER);
		//this.add(tableau,BorderLayout.CENTER);
		this.add(button_run, BorderLayout.SOUTH);
		
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/5);
		this.setSize((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/2);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void buildEvents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.button_open.addActionListener(this);
		this.button_run.addActionListener(this);
	}
	
	private void openFile(){
		final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		int returnVal = fc.showOpenDialog(MyFrame.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();            
            foil = new FoilP();
    		Vector<String> content=foil.init(file.getAbsolutePath());
			Vector<String> columnNames = foil.getHeader();
			
			Vector<Vector> rowData = new Vector<Vector>();
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
    		//affichage.setText(foil.getEnsembleDeCas().toString());
    		button_run.setVisible(true);
        } else {
        	//affichage.setText("Erreur lors de la lecture du fichier");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent boutton) {
		JButton b=(JButton) boutton.getSource();
		if (b.equals(this.button_open)){
			openFile();
		}
		else if (b.equals(this.button_run)){
			if(b.getText().equals("Retour")){
				this.remove(affichage);
				affichage=new JScrollPane();
				this.add(affichage,BorderLayout.CENTER);
				affichage.setVisible(true);
				b.setText("Lancer le test");
				repaint();
			}
			else {
				this.remove(affichage);
				b.setText("Retour");
				//c'est là qu'on met le texte dans l'affichage avec l'aglo
				ArrayList<Regle> res_regle=foil.algo(foil);
				String s="";
				for(Regle r:res_regle){
					s=s+r+"\n";
				}
				JTextArea text=new JTextArea(s);
				affichage=new JScrollPane(text);
				this.add(affichage,BorderLayout.CENTER);
				affichage.setVisible(true);
				repaint();
			}
		}
	}
		
}
