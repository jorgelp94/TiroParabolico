
package tiroparabolico;
/**
 * Clase Balon
 *
 * @author Antonio Mejorado
 * @version 1.00 2008/6/13
 */
import java.awt.Image;
import java.awt.Toolkit;

public class Balon extends Base {

    private final static String PAUSE = "PAUSADO";
    private final static String DISP = "DESAPARECE";

    /**
     * @return the DISP
     */
    public static String getDISP() {
        return DISP;
    }

    /**
     * @return the PAUSE
     */
    public static String getPAUSE() {
        return PAUSE;
    }

    /**
     * Metodo constructor que hereda los atributos de la clase
     * <code>Base</code>.
     *
     * @param posX es la <code>posiscion en x</code> del objeto elefante.
     * @param posY es el <code>posiscion en y</code> del objeto elefante.
     * @param elefN es la <code>imagen</code> del los objetos elefs.
     * @param anim es la <code>Animacion</code> del objeto elefante.
     * @param num es la cantidad de elefes <code>Int</code> del objeto elefante.
     */
    public Balon(int posX, int posY) {
        super(posX, posY);
        //Se cargan las imágenes(cuadros) para la animación
//        Image balon1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/balon1.gif"));
        Image balon2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/balon2.gif"));
        Image balon3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/balon3.gif"));
        Image balon4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/balon4.gif"));
        Image balon5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/balon5.gif"));

        //Se crea la animación
        anim = new Animacion();
//        anim.sumaCuadro(balon1, 100);
        anim.sumaCuadro(balon2, 100);
        anim.sumaCuadro(balon3, 100);
        anim.sumaCuadro(balon4, 100);
        anim.sumaCuadro(balon5, 100);
    }
    /**
     * Metodo que hace llamada al metodo de anim para actualizar la imagen segun el tiempo
     * <code>Animacion</code>.
     *
     * @param tiempo es el tiempo <code>Int</code> del objeto Animacion.
     */
    public void actualiza(long tiempo) {
        anim.actualiza(tiempo);
    }
}
