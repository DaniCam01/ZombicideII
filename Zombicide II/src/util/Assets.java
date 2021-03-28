package util;


import java.io.Serializable;

import javax.swing.ImageIcon;

public class Assets implements Serializable{

	private static final long serialVersionUID = 1L;
	public static ImageIcon iifondo;
	public static ImageIcon iifondoini;
    public static ImageIcon iimario;
    public static ImageIcon iidisparo;
    public static ImageIcon iizombie;
    public static ImageIcon iizombiebig;
    public static ImageIcon iifa;
    public static ImageIcon iiarma0;
    public static ImageIcon iiarma1;
    public static ImageIcon iiarma2;
    
      
    public static void loadAssets() {
		iifondo = new ImageIcon("assets/fondo.jpeg");
		iimario = new ImageIcon("assets/prueba3.png"); 
		iizombie = new ImageIcon("assets/zombiesnice.png"); 
		iizombiebig = new ImageIcon("assets/zombieBig.png"); 
		iidisparo = new ImageIcon("assets/disparosheet.png");
		iifondoini = new ImageIcon("assets/fondoInicio.jpg");
		iifa = new ImageIcon("assets/fa.png");
		iiarma0 = new ImageIcon("assets/arma0.png");
		iiarma1 = new ImageIcon("assets/arma1.png");
		iiarma2 = new ImageIcon("assets/arma2.png");

    }
}