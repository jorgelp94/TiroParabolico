/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;

public class JframeExam extends JFrame implements Runnable, KeyListener, MouseListener {

    private static final long serialVersionUID = 1L;
    // Se declaran las variables.
    private int direccion;    // Direccion del elefante
    private int incX;    // Incremento en x
    private int incY;    // Incremento en y
    private int vidas;    // vidas del elefante.
//    private final int MIN = -5;    //Rango minimo al generar un numero al azar.
//    private final int MAX = 6;    //Rango maximo al generar un numero al azar.
    private Image dbImage;    // Imagen a proyectar
    private Image gameover;    //Imagen a desplegar al acabar el juego.	 
    private Graphics dbg;	// Objeto grafico
    private SoundClip sonido;    // Objeto SoundClip
    private SoundClip anota;    // Objeto SoundClip
    private SoundClip bomb;    //Objeto SoundClip 
    private Balon balon;    // Objeto de la clase Balon
    //Variables de control de tiempo de la animación
    private long tiempoActual;
    private long tiempoInicial;
    private boolean pause;
    private boolean choca;
    private boolean presionaI;
    private boolean balonMove;
    

    /**
     * Metodo <I>init</I> sobrescrito de la clase
     * <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el
     * <code>Applet</code> y se definen funcionalidades.
     */
        public JframeExam() {
            
        this.setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPause(false);
        vidas = 5;    // Le asignamos un valor inicial a las vidas
        balon = new Balon(0, getHeight()-80);
        
        setBackground(Color.yellow);
        addKeyListener(this);
        addMouseListener(this);
        
        presionaI = false;
        //Se cargan los sonidos.
        
        bomb = new SoundClip("sounds/Explosion.wav");
        anota = new SoundClip("sounds/Cheering.wav");
        
        URL goURL = this.getClass().getResource("gameover.jpg");
        gameover = Toolkit.getDefaultToolkit().getImage(goURL);
        start();
    }

    /**
     * Metodo <I>start</I> sobrescrito de la clase
     * <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este
     * <code>Applet</code>
     *
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * Metodo <I>run</I> sobrescrito de la clase
     * <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se
     * incrementa la posicion en x o y dependiendo de la direccion, finalmente
     * se repinta el
     * <code>Applet</code> y luego manda a dormir el hilo.
     *
     */
    public void run() {
        while (vidas > 0) {
            actualiza();
            checaColision();

            // Se actualiza el <code>Applet</code> repintando el contenido.
            repaint();

            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }
    }

    /**
     * Metodo usado para actualizar la posicion de objetos elefante y raton.
     *
     */
    public void actualiza() {
        //Dependiendo de la direccion del elefante es hacia donde se mueve.
        if (!isPause()) {
            switch (direccion) {
                case 1: {
                    balon.setPosY(balon.getPosY() - 1);
                    break;    //se mueve hacia arriba
                }
                case 3: {
                    balon.setPosY(balon.getPosY() + 1);
                    break;    //se mueve hacia abajo
                }
                case 4: {
                    balon.setPosX(balon.getPosX() - 1);
                    break;    //se mueve hacia izquierda
                }
                case 2: {
                    balon.setPosX(balon.getPosX() + 1);
                    break;    //se mueve hacia derecha	
                }
            }

          if(balonMove){
            long tiempoTranscurrido =
                    System.currentTimeMillis() - getTiempoActual();

            //Guarda el tiempo actual
            setTiempoActual(getTiempoActual() + tiempoTranscurrido);

            //Actualiza la animación en base al tiempo transcurrido
            balon.actualiza(tiempoTranscurrido);
          }
        }
    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton con
     * las orillas del
     * <code>Applet</code>.
     */
    public void checaColision() {
        //Colision del elefante con el Applet dependiendo a donde se mueve.
        switch (direccion) {
            case 1: { //se mueve hacia arriba con la flecha arriba.
                if (balon.getPosY() < 0) {
                    direccion = 3;
                    sonido.play();
                }
                break;
            }
            case 3: { //se mueve hacia abajo con la flecha abajo.
                if (balon.getPosY() + balon.getAlto() > getHeight()) {
                    direccion = 1;
                    sonido.play();
                }
                break;
            }
            case 4: { //se mueve hacia izquierda con la flecha izquierda.
                if (balon.getPosX() < 0) {
                    direccion = 2;
                    sonido.play();
                }
                break;
            }
            case 2: { //se mueve hacia derecha con la flecha derecha.
                if (balon.getPosX() + balon.getAncho() > getWidth()) {
                    direccion = 4;
                    sonido.play();
                }
                break;
            }
        }

       
    }

    /**
     * Metodo <I>update</I> sobrescrito de la clase
     * <code>Applet</code>, heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint(Graphics g) {
        // Inicializan el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {
//		if (e.getKeyCode() == KeyEvent.VK_UP) {    //Presiono flecha arriba
//			direccion = 1;
//		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {    //Presiono flecha abajo
//			direccion = 2;
//		} else
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (isPause()) {
                setPause(false);
            } else {
                setPause(true);
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono flecha izquierda
                direccion = 3;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
                direccion = 4;
            }
        }
        
        //Si se presiona la tecla I, presionaI cambia a verdadero. si se vuelve a presionar presionaI cambia a falso
        // Salen instrucciones del juego
        if (e.getKeyCode() == KeyEvent.VK_I) {
            if (presionaI) {
                presionaI = false;
            }
            else {
                presionaI = true;
            }
        }
    }

    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que
     * no es de accion.
     *
     * @param e es el <code>evento</code> que se genera en al presionar las
     * teclas.
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla
     * presionada.
     *
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Metodo <I>mousePressed</I>
     * En este metodo se valida en donde dio click el usario para determinar la direccion
     *
     * @param e es el <code>Mouse Event</code> usado para determinar dodne dio click.
     */
    public void mousePressed(MouseEvent e) {
//        if (e.getPoint().getX() <= getWidth() / 2 && e.getPoint().getY() <= getHeight() / 2) {
//            //4
//            direccion = 4;
//        } else if (e.getPoint().getX() >= getWidth() / 2 && e.getPoint().getY() <= getHeight() / 2) {
//            //1
//            direccion = 1;
//        } else if (e.getPoint().getX() >= getWidth() / 2 && e.getPoint().getY() >= getHeight() / 2) {
//            //2 
//            direccion = 2;
//        } else if (e.getPoint().getX() <= getWidth() / 2 && e.getPoint().getY() >= getHeight() / 2) {
//            //3 
//            direccion = 3;
//        }
        if(balon.getPerimetro().contains(e.getPoint())) setBalonMove(true);
    }
    /**
     * Metodo <I>mouseReleased</I>
     * En este metodo se valida en donde dio click el usario para determinar la direccion
     *
     * @param e es el <code>Mouse Event</code> usado para determinar dodne dio click.
     */
    public void mouseReleased(MouseEvent e) {
    }
    /**
     * Metodo <I>mouseReleased</I>
     * En este metodo se valida en donde dio click el usario para determinar la direccion
     *
     * @param e es el <code>Mouse Event</code> usado para determinar dodne dio click.
     */
    public void mouseEntered(MouseEvent e) {
    }
/**
     * Metodo <I>mouseReleased</I>
     * En este metodo se valida en donde dio click el usario para determinar la direccion
     *
     * @param e es el <code>Mouse Event</code> usado para determinar dodne dio click.
     */
    public void mouseExited(MouseEvent e) {
    }
/**
     * Metodo <I>mouseReleased</I>
     * En este metodo se valida en donde dio click el usario para determinar la direccion
     *
     * @param e es el <code>Mouse Event</code> usado para determinar dodne dio click.
     */
    public void mouseClicked(MouseEvent e) {
    }
    /**
     * Metodo <I>paint</I> sobrescrito de la clase
     * <code>Applet</code>, heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint1(Graphics g) {
        if (vidas > 0) {
            if (balon != null) {
                //Dibuja la imagen en la posicion actualizada
                g.drawImage(balon.getImagenI(), balon.getPosX(), balon.getPosY(), this);
//                g.drawString("Puntos : " + list.get(0).getNum(), 10, 10);
                if (isPause()) {
                    g.drawString(balon.getPAUSE(), balon.getPosX() + 15, balon.getPosY() + 30);
                }
                if (isChoca()) {
                    g.drawString(balon.getDISP(), balon.getPosX() + 15, balon.getPosY() + 30);
                    choca = false;
                }
                if (presionaI) {
                    g.drawString("Instrucciones:...................", getWidth()/2, 200);
                }
            } else {
                //Da un mensaje mientras se carga el dibujo	
                g.drawString("No se cargo la imagen..", 20, 20);
            }
        } else {
            g.drawImage(gameover, 0, 0, this);
        }
    }

    /**
     * @return the tiempoActual
     */
    public long getTiempoActual() {
        return tiempoActual;
    }

    /**
     * @param tiempoActual the tiempoActual to set
     */
    public void setTiempoActual(long tiempoActual) {
        this.tiempoActual = tiempoActual;
    }

    /**
     * @return the tiempoInicial
     */
    public long getTiempoInicial() {
        return tiempoInicial;
    }

    /**
     * @param tiempoInicial the tiempoInicial to set
     */
    public void setTiempoInicial(long tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    /**
     * @return the pause
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * @param pause the pause to set
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * @return the choca
     */
    public boolean isChoca() {
        return choca;
    }

    /**
     * @param choca the choca to set
     */
    public void setChoca(boolean choca) {
        this.choca = choca;
    }

    /**
     * @return the balonMove
     */
    public boolean isBalonMove() {
        return balonMove;
    }

    /**
     * @param balonMove the balonMove to set
     */
    public void setBalonMove(boolean balonMove) {
        this.balonMove = balonMove;
    }
}
