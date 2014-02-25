/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiroparabolico;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author jorgelp94
 */
public class Anotacion extends Base{
    private static int conteo = 0;
    //private Animacion anim;

    //Metodo constructor que hereda los atributos de la clase ObjetoEspacial
    // posX es la posicion en x del planeta
    // posY es la posicion en y del planeta
    
    public Anotacion(int posX, int posY) {
        super(posX, posY);
        Image jugador1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-5.jpg"));
	Image jugador2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-6.jpg"));
	Image jugador3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-7.jpg"));
        Image jugador4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-8.jpg"));
	Image jugador5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-9.jpg"));
	Image jugador6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-10.jpg"));
        Image jugador7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-11.jpg"));
	Image jugador8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-12.jpg"));
	Image jugador9 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-13.jpg"));
        Image jugador10 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Unknown-14.jpg"));
	
        
        //Se inicializa la animación
        anim = new Animacion();
	anim.sumaCuadro(jugador1, 100);
	anim.sumaCuadro(jugador2, 100);
	anim.sumaCuadro(jugador3, 100);
        anim.sumaCuadro(jugador4, 100);
        anim.sumaCuadro(jugador5, 100);
        anim.sumaCuadro(jugador6, 100);
        anim.sumaCuadro(jugador7, 100);
        anim.sumaCuadro(jugador8, 100);
        anim.sumaCuadro(jugador9, 100);
        anim.sumaCuadro(jugador10, 100);
        conteo++;
    }
    
    public static int getConteo() {
        return conteo;
    }
    
    public static void setConteo(int cont) {
        conteo = cont;
    }
    
    //Método que actualiza la animacion
    public void actualiza(long tiempo) {
        anim.actualiza(tiempo);
    }
}
