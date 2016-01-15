package projectEDR;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConfigFrame extends JFrame implements ActionListener{
	private JButton button_open;
	private JScrollPane affichage;
	private JTextField entree;
	private Box box_contenu;
	private Box box_bouttons;
	private Vector<String> list_res;
	
	
	public ConfigFrame(Vector<String> _list_res){
		super("Choisissez la valeur de la conclusion");
		list_res=_list_res;
		buildComponent();
		buildEvents();
		buildInterface();
	}
	
	public void buildComponent(){
		button_open=new JButton("Envoyer");
		affichage=new JScrollPane(new JTextArea("plop"));
		box_contenu=Box.createVerticalBox();
		box_bouttons=Box.createHorizontalBox();
		entree=new JTextField();
	}
	
	public void buildEvents(){
		this.button_open.addActionListener(this);
	}
	
	public void buildInterface(){
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		JTable table=new JTable(null,list_res);
		affichage=new JScrollPane(table);
		affichage.setVisible(true);
		
		box_bouttons.add(entree);
		box_bouttons.add(button_open);
		
		box_contenu.add(affichage);
		box_contenu.add(box_bouttons);
		
		this.add(box_contenu);	
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/5);
		this.setSize((int)tailleEcran.getWidth()/6,(int)tailleEcran.getHeight()/8);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
