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
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.io.IOException;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;

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
    private Anotacion anotacion; //Objeto de la clase Anotacion
    //Variables de control de tiempo de la animación
    private long tiempoActual;
    private long tiempoInicial;
    private boolean pause;
    private boolean choca;
    private boolean presionaI;
    private boolean balonMove;
    private boolean ladoIzq; 
    private boolean ladoDer;
    private boolean activaSonido;
    private boolean presionaG;
    private boolean presionaC;
    private int velocI;
    private double t;
    private double gravedad;
    private double angulo;
    private double anguloRadianes;
    private double cos;
    private double sin;
    private int caidas; //cuenta las veces que cae el balon
    private int score; // puntaje del jugador
    private String nombreArchivo;    //Nombre del archivo.
    private Vector vec;    // Objeto vector para agregar el puntaje.
    
    

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
        balon = new Balon(0, 500);
        anotacion = new Anotacion(getWidth()/2, getHeight()-80);
        
        setBackground(Color.white);
        addKeyListener(this);
        addMouseListener(this);
        
        nombreArchivo = "Puntaje.txt";
        vec = new Vector();
        caidas = 0;
        score = 0;
        
        presionaI = false;
        ladoIzq = false;
        ladoDer = false;
        presionaG = false;
        presionaC = false;
        activaSonido = true; // El sonido esta activado al iniciar el juego
        //Se cargan los sonidos.
        
        bomb = new SoundClip("sounds/Explosion.wav");
        anota = new SoundClip("sounds/Cheering.wav");
        velocI = 250;
        t=.15;
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
        try{
            if (presionaC) {
               leeArchivo();    //lee el contenido del archivo 
            }
            if (presionaG) {
                vec.add(new Puntaje(score));    //Agrega el contenido del nuevo puntaje al vector.
                grabaArchivo();    //Graba el vector en el archivo.
            }
            
	}catch(IOException e){
            System.out.println("Error en " + e.toString());
	}
    }

    /**
     * Metodo usado para actualizar la posicion de objetos elefante y raton.
     *
     */
    public void actualiza() {
        //Dependiendo de la direccion del elefante es hacia donde se mueve.
        if (!isPause() && !presionaI) {
            switch (direccion) {
                case 3: {
                    if (!ladoIzq && balonMove){
                        anotacion.setPosX(anotacion.getPosX() - 1);
                        break;    //se mueve hacia izquierda
                    }
                    
                }
                case 4: {
                    if (!ladoDer && balonMove) {
                        anotacion.setPosX(anotacion.getPosX() + 1);
                        break;    //se mueve hacia derecha
                    }	
                }
            }
            
          //Checa que el jugador no se salga del applet  
          if (anotacion.getAncho() + anotacion.getPosX() >= getWidth()) {
              ladoDer = true;
          }
          else {
              ladoDer = false;
          }
          if (anotacion.getPosX() <= getWidth()/2) {
              ladoIzq = true;
          }
          else {
              ladoIzq = false;
          }

          if(balonMove){
               //Guarda el tiempo actual
            
                long tiempoTranscurrido =
                        System.currentTimeMillis() - getTiempoActual();
                setTiempoActual(getTiempoActual() + tiempoTranscurrido);
              setAnguloRadianes(45);
              setCos(Math.cos(getAnguloRadianes()));
              setSin(Math.sin(getAnguloRadianes()));
              int x = (int) (112 * getCos() * t);
              int y = (int) ((112* sin* t) - ( .5*9.8*t*t));
              balon.setPosX(x);
              balon.setPosY(-y+ 500);
              System.out.println( "cos: " + cos+ " " + " sin: " + sin + " tiempo: " + t);
              System.out.println( "x: " + x+ " " + " y: " + y);
           

            //Actualiza la animación en base al tiempo transcurrido
            balon.actualiza(tiempoTranscurrido);
            anotacion.actualiza(tiempoTranscurrido);
            t = t + .1;
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
//        switch (direccion) {
//            case 1: { //se mueve hacia arriba con la flecha arriba.
//                if (balon.getPosY() < 0) {
//                    direccion = 3;
//                    sonido.play();
//                }
//                break;
//            }
//            case 3: { //se mueve hacia abajo con la flecha abajo.
//                if (balon.getPosY() + balon.getAlto() > getHeight()) {
//                    direccion = 1;
//                    sonido.play();
//                }
//                break;
//            }
//            case 4: { //se mueve hacia izquierda con la flecha izquierda.
//                if (balon.getPosX() < 0) {
//                    direccion = 2;
//                    sonido.play();
//                }
//                break;
//            }
//            case 2: { //se mueve hacia derecha con la flecha derecha.
//                if (balon.getPosX() + balon.getAncho() > getWidth()) {
//                    direccion = 4;
//                    sonido.play();
//                }
//                break;
//            }
//        }
        if (balon.getPosY() > getHeight()){
            balonMove=false;
            balon.setPosX(0);
            balon.setPosY(100);
            t=.15;
            if (activaSonido) {
                bomb.play();
            }
            caidas++; //Cuenta cuando hay una caida
            if (caidas == 3) {
                vidas--;// se resta una vida cuando el balon cae 3 veces
                caidas = 0;
            }
        }
        
        if (balon.intersecta(anotacion)) {
            if (activaSonido) {
                anota.play();
            }
            balonMove=false;
            balon.setPosX(0);
            balon.setPosY(500);
            t=.15;
            score = score + 2;
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
        
        // Quita el sonido
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (activaSonido) {
                activaSonido = false;
            }
            else {
                activaSonido = true;
            }
        }
        
        // Tecla para guardar archivo
        if (e.getKeyCode() == KeyEvent.VK_G) {
            if (presionaG) {
                presionaG = false;
            }
            else {
                presionaG = true;
            }
        }
        
        // Tecla para cargar archivo
        if (e.getKeyCode() == KeyEvent.VK_C) {
            if (presionaC) {
                presionaC = false;
            }
            else {
                presionaC = true;
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
                //Dibuja la imagen en la posicion actualizada
                g.drawImage(anotacion.getImagenI(), anotacion.getPosX(), anotacion.getPosY(), this);
//                g.drawString("Puntos : " + list.get(0).getNum(), 10, 10);
                //Muestra las vidas
                g.drawString("Vidas: "+ vidas, getWidth()/2 - 10, 80);
                //Muestra el puntaje
                g.drawString("Score: " + score, balon.getAncho(), 80);
                if (isPause()) {
                    g.drawString(balon.getPAUSE(), balon.getPosX() + 15, balon.getPosY() + 30);
                }
                if (isChoca()) {
                    g.drawString(balon.getDISP(), balon.getPosX() + 15, balon.getPosY() + 30);
                    choca = false;
                }
                if (presionaI) {
                    g.drawString("Instrucciones:", getWidth()/4 + getWidth()/8, 200);
                    g.drawString("Mueve el jugador con las flechas del teclado", getWidth()/4 + getWidth()/8, 220);
                    g.drawString("para que atrape el balón de americano. Cada vez", getWidth()/4 + getWidth()/8, 240);
                    g.drawString("que es atrapado ganas dos puntos y si el balón", getWidth()/4 + getWidth()/8, 260);
                    g.drawString("cae tres veces pierdes una vida.", getWidth()/4 + getWidth()/8, 280);
                    g.drawString("Teclas: ", getWidth()/4 + getWidth()/8, 300);
                    g.drawString("Flecha izquierda - se mueve a la izquierda", getWidth()/4 + getWidth()/8, 320);
                    g.drawString("Flecha derecha - se mueve a la derecha", getWidth()/4 + getWidth()/8, 340);
                    g.drawString("I - muestra/oculta instrucciones", getWidth()/4 + getWidth()/8, 360);
                    g.drawString("G - guarda el juego", getWidth()/4 + getWidth()/8, 380);
                    g.drawString("C - carga el juego", getWidth()/4 + getWidth()/8, 400);
                    g.drawString("P - pausa el juego", getWidth()/4 + getWidth()/8, 420);
                    g.drawString("S - activa/desactiva el sonido del juego", getWidth()/4 + getWidth()/8, 440);
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

    /**
     * @return the velocI
     */
    public int getVelocI() {
        return velocI;
    }

    /**
     * @param velocI the velocI to set
     */
    public void setVelocI(int velocI) {
        this.velocI = velocI;
    }

    /**
     * @return the t
     */
    public double getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(double t) {
        this.t = t;
    }

    /**
     * @return the gravedad
     */
    public double getGravedad() {
        return gravedad;
    }

    /**
     * @param gravedad the gravedad to set
     */
    public void setGravedad(double gravedad) {
        this.gravedad = gravedad;
    }

    /**
     * @return the angulo
     */
    public double getAngulo() {
        return angulo;
    }

    /**
     * @param angulo the angulo to set
     */
    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    /**
     * @return the anguloRadianes
     */
    public double getAnguloRadianes() {
        return anguloRadianes;
    }

    /**
     * @param anguloRadianes the anguloRadianes to set
     */
    public void setAnguloRadianes(double anguloRadianes) {
        this.anguloRadianes = anguloRadianes;
    }

    /**
     * @return the cos
     */
    public double getCos() {
        return cos;
    }

    /**
     * @param cos the cos to set
     */
    public void setCos(double cos) {
        this.cos = cos;
    }

    /**
     * @return the sin
     */
    public double getSin() {
        return sin;
    }

    /**
     * @param sin the sin to set
     */
    public void setSin(double sin) {
        this.sin = sin;
    }

    /**
     * @return the anotacion
     */
    public Anotacion getAnotacion() {
        return anotacion;
    }

    /**
     * @param anotacion the anotacion to set
     */
    public void setAnotacion(Anotacion anotacion) {
        this.anotacion = anotacion;
    }
    
    /**
     * Metodo que lee a informacion de un archivo y lo agrega a un vector.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException{
    	BufferedReader fileIn;
    	try{
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	} catch (FileNotFoundException e){
    		File puntos = new File(nombreArchivo);
    		PrintWriter fileOut = new PrintWriter(puntos);
    		fileOut.println("100,demo");
    		fileOut.close();
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	}
    	String dato = fileIn.readLine();

    	while(dato != null) {
                //No entiendo esta parte del arreglo ni para que se usa
    		//arr = dato.split(",");
    		//int num = (Integer.parseInt(arr[0])); //ni esta
    		//vec.add(new Puntaje(num));
    		dato = fileIn.readLine();
    	}
    	fileIn.close();
    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException{
    	PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
    	for (int i=0; i<vec.size(); i++) {
    		Puntaje x;
    		x = (Puntaje) vec.get(i);
    		fileOut.println(x.toString());
    	}
    	fileOut.close();	
    }
    
}
