/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author stormdark
 */
public class GraficacionJuego extends JPanel implements KeyListener {
    Shape figuraTanque, figuraBalaEnemiga, figuraBalaEnemiga2;
    Shape fbala1, fbala2, fbala3, fbala4;
    AffineTransform trFiguraTanque, trCanion, trBala, trBala2, trBala3, trBala4;
    AffineTransform trBalaEnem1, trBalaEnem2, trFiguraBalaE1, trFiguraBalaE2;
    AffineTransform trFbala1, trFbala2, trFbala3, trFbala4;
    int posicionTanque, gradosCanion,x1,x2, puntaje;
    ImageIcon imgTanque, imgCanion, imgBala, imgBalaEnemiga, imgFondo;
    boolean dispara, dispara2, dispara3, dispara4;
    Timer timer;

    public GraficacionJuego(){
     setPreferredSize(new Dimension(1200,650));
     setBackground(Color.WHITE);
     this.x1=(int)(Math.random()*(1300-1200+1)+1200);
     this.x2=(int)(Math.random()*(1300+1200));
     this.addKeyListener(this);
     this.setFocusable(true);
     this.posicionTanque=10;
     this.figuraTanque=new Rectangle2D.Double(posicionTanque,528,176,120);
     this.figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
     this.figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
       
     this.trFiguraTanque=new AffineTransform();
     this.trCanion=new AffineTransform();
     this.trBalaEnem1=new AffineTransform();
     this.trBalaEnem2=new AffineTransform();
     this.trFiguraBalaE1=new AffineTransform();
     this.trFiguraBalaE2=new AffineTransform();
     this.gradosCanion=0;
     this.puntaje=5;
     this.dispara=false;
     this.dispara2=false;
     this.dispara3=false;
     this.dispara4=false;
     imgTanque=new ImageIcon("tankBase.png");
     imgCanion=new ImageIcon("canion.png");
     imgBala=new ImageIcon("bala.png");
     imgBalaEnemiga=new ImageIcon("balaEnemiga.png");
     imgFondo=new ImageIcon("fondo.jpg");
     trCanion.translate(posicionTanque+107,540);
     trBalaEnem1.translate(x1,565);
     trFiguraBalaE1.setToTranslation(x1,565);
     trBalaEnem2.translate(x2,490);
     trFiguraBalaE2.setToTranslation(x2,490);
     
     timer=new Timer(20,new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
             
            if(puntaje==105){
                timer.stop();
                 JOptionPane.showMessageDialog(null,"¡Has ganado!");
                System.exit(0);
                
            }
            if(puntaje==0){
                timer.stop();
                JOptionPane.showMessageDialog(null,"¡Perdiste!");
                System.exit(0);
            }
             
             if(figuraBalaEnemiga.intersects((Rectangle2D)figuraTanque.getBounds2D())){
                x1=(int)(Math.random()*(1300-1200+1)+1200);
                figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
                trBalaEnem1=new AffineTransform();
                trFiguraBalaE1=new AffineTransform();
                trBalaEnem1.translate(x1,565);
                trFiguraBalaE1.setToTranslation(x1,565);
                puntaje-=5;
            }else{             
                trBalaEnem1.translate(-10,0);
                trFiguraBalaE1.setToTranslation(-10,0);
                figuraBalaEnemiga=trFiguraBalaE1.createTransformedShape(figuraBalaEnemiga);
             }
             
             if(figuraBalaEnemiga2.intersects((Rectangle2D)figuraTanque.getBounds2D())){
                x2=(int)(Math.random()*(1300-1200+1)+1200);
                figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
                trBalaEnem2=new AffineTransform();
                trFiguraBalaE2=new AffineTransform();
                trBalaEnem2.translate(x2,490);
                trFiguraBalaE2.setToTranslation(x2,490);
                puntaje-=5;
            }else{             
                trBalaEnem2.translate(-10,0);
                trFiguraBalaE2.setToTranslation(-10,0);
                figuraBalaEnemiga2=trFiguraBalaE2.createTransformedShape(figuraBalaEnemiga2);
             }
             repaint();
         }
     });
     timer.start();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        g2.drawImage(imgFondo.getImage(),0,0,getWidth(),getHeight(),this);
        FontRenderContext fr =g2.getFontRenderContext();
        Font fuente=new Font("Arial",Font.BOLD,50);
        GlyphVector gp=fuente.createGlyphVector(fr,"Marcador: "+puntaje);
        Shape marcador=gp.getOutline(10,50);
        g2.fill(marcador);
        //g2.draw(figuraTanque);
        //g2.draw(figuraBalaEnemiga);
        //g2.draw(figuraBalaEnemiga2);
        g2.drawImage(imgTanque.getImage(),posicionTanque,528,200,120,null);
        g2.drawImage(imgCanion.getImage(), trCanion,this);
        g2.drawImage(imgBalaEnemiga.getImage(), trBalaEnem1, this);
        g2.drawImage(imgBalaEnemiga.getImage(),trBalaEnem2,this);
        

        
        if(dispara==true){
            if(trBala.getTranslateX()>1200){
                dispara=false;
            }else{
                g2.drawImage(imgBala.getImage(), trBala, this);
                                
                fbala1=trFbala1.createTransformedShape(fbala1);
                //g2.fill(fbala1);
                if(fbala1.intersects(figuraBalaEnemiga.getBounds2D())){
                    dispara=false;
                    
                    x1=(int)(Math.random()*(1300-1200+1)+1200);
                    figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
                    trBalaEnem1=new AffineTransform();
                    trFiguraBalaE1=new AffineTransform();
                    trBalaEnem1.translate(x1,565);
                    trFiguraBalaE1.setToTranslation(x1,565);
                    puntaje+=5;
                    
                }else{
                    if(fbala1.intersects(figuraBalaEnemiga2.getBounds2D())){
                        dispara=false;
                        x2=(int)(Math.random()*(1300-1200+1)+1200);
                        figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
                        trBalaEnem2=new AffineTransform();
                        trFiguraBalaE2=new AffineTransform();
                        trBalaEnem2.translate(x2,490);
                        trFiguraBalaE2.setToTranslation(x2,490);
                        puntaje+=5;
                        
                    }else{
                        disparado();
                    }
                }
            }
        }
        if(dispara2==true){
            if(trBala2.getTranslateX()>1200){
                dispara2=false;
            }else{
                g2.drawImage(imgBala.getImage(), trBala2, this);
                
                fbala2=trFbala2.createTransformedShape(fbala2);
                //g2.fill(fbala2);
                if(fbala2.intersects(figuraBalaEnemiga.getBounds2D())){
                    dispara=false;
                    
                    x1=(int)(Math.random()*(1300-1200+1)+1200);
                    figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
                    trBalaEnem1=new AffineTransform();
                    trFiguraBalaE1=new AffineTransform();
                    trBalaEnem1.translate(x1,565);
                    trFiguraBalaE1.setToTranslation(x1,565);
                    puntaje+=5;
                    
                }else{
                    if(fbala2.intersects(figuraBalaEnemiga2.getBounds2D())){
                        dispara=false;
                        x2=(int)(Math.random()*(1300-1200+1)+1200);
                        figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
                        trBalaEnem2=new AffineTransform();
                        trFiguraBalaE2=new AffineTransform();
                        trBalaEnem2.translate(x2,490);
                        trFiguraBalaE2.setToTranslation(x2,490);
                        puntaje+=5;
                        
                    }else{
                        disparado();
                    }
                }
            }
        }
        if(dispara3==true){
            if(trBala3.getTranslateX()>1200){
                dispara3=false;
            }else{
                g2.drawImage(imgBala.getImage(), trBala3, this);
                fbala3=trFbala3.createTransformedShape(fbala3);
                //g2.fill(fbala3);
                if(fbala3.intersects(figuraBalaEnemiga.getBounds2D())){
                    dispara=false;
                    
                    x1=(int)(Math.random()*(1300-1200+1)+1200);
                    figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
                    trBalaEnem1=new AffineTransform();
                    trFiguraBalaE1=new AffineTransform();
                    trBalaEnem1.translate(x1,565);
                    trFiguraBalaE1.setToTranslation(x1,565);
                    puntaje+=5;
                    
                }else{
                    if(fbala3.intersects(figuraBalaEnemiga2.getBounds2D())){
                        dispara=false;
                        x2=(int)(Math.random()*(1300-1200+1)+1200);
                        figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
                        trBalaEnem2=new AffineTransform();
                        trFiguraBalaE2=new AffineTransform();
                        trBalaEnem2.translate(x2,490);
                        trFiguraBalaE2.setToTranslation(x2,490);
                        puntaje+=5;
                        
                    }else{
                        disparado();
                    }
                }
            }
        }
        if(dispara4==true){
            if(trBala4.getTranslateX()>1200){
                dispara4=false;
            }else{
                g2.drawImage(imgBala.getImage(), trBala4, this);
                fbala4=trFbala4.createTransformedShape(fbala4);
                //g2.fill(fbala4);
                if(fbala4.intersects(figuraBalaEnemiga.getBounds2D())){
                    dispara=false;
                    
                    x1=(int)(Math.random()*(1300-1200+1)+1200);
                    figuraBalaEnemiga=new Rectangle2D.Double(x1,565,85,65);
                    trBalaEnem1=new AffineTransform();
                    trFiguraBalaE1=new AffineTransform();
                    trBalaEnem1.translate(x1,565);
                    trFiguraBalaE1.setToTranslation(x1,565);
                    puntaje+=5;
                    
                }else{
                    if(fbala4.intersects(figuraBalaEnemiga2.getBounds2D())){
                        dispara=false;
                        x2=(int)(Math.random()*(1300-1200+1)+1200);
                        figuraBalaEnemiga2=new Rectangle2D.Double(x2,490,85,65);
                        trBalaEnem2=new AffineTransform();
                        trFiguraBalaE2=new AffineTransform();
                        trBalaEnem2.translate(x2,490);
                        trFiguraBalaE2.setToTranslation(x2,490);
                        puntaje+=5;
                        
                    }else{
                        disparado();
                    }
                }
            }
        }
        
    }
    
    public void disparado(){
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            System.out.println("error en sleep");
        }
        if(dispara){
            
            trBala.translate(30,0);
            
            trFbala1.setToTranslation(30,0);
            trFbala1.rotate(Math.toRadians(gradosCanion),fbala1.getBounds2D().getX(),fbala1.getBounds2D().getY());            
            
        }
        if(dispara2){
            trBala2.translate(30,0);
            
            trFbala2.setToTranslation(30,0);
            trFbala2.rotate(Math.toRadians(gradosCanion),fbala2.getBounds2D().getX(),fbala2.getBounds2D().getY());
        }
        if(dispara3){
            trBala3.translate(30,0);
            
            trFbala3.setToTranslation(30,0);
            trFbala3.rotate(Math.toRadians(gradosCanion),fbala3.getBounds2D().getX(),fbala3.getBounds2D().getY());
        }
        if(dispara4){
            trBala4.translate(30,0);
            
            trFbala4.setToTranslation(30,0);
            trFbala4.rotate(Math.toRadians(gradosCanion),fbala4.getBounds2D().getX(),fbala4.getBounds2D().getY());
        }
        repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.setProperty("sun.java2d.opengl","True"); //Decoment this line if you're running it on Linux
        JFrame frame=new JFrame("By Fernando Oxyde | fb.com/cyberoxyde");
        GraficacionJuego panel=new GraficacionJuego();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode()==KeyEvent.VK_LEFT){
            if(posicionTanque>0){
                trFiguraTanque.setToTranslation(-10,0);
                figuraTanque=trFiguraTanque.createTransformedShape(figuraTanque);
                posicionTanque-=10;
                trCanion.rotate(Math.toRadians(gradosCanion*-1),0,imgCanion.getIconHeight()/2);
                trCanion.translate(-10,0);
                trCanion.rotate(Math.toRadians(gradosCanion),0,imgCanion.getIconHeight()/2);
            }
        }
        if(ke.getKeyCode()==KeyEvent.VK_RIGHT){
            if(posicionTanque<820){
                trFiguraTanque.setToTranslation(10,0);
                figuraTanque=trFiguraTanque.createTransformedShape(figuraTanque);
                posicionTanque+=10;
                trCanion.rotate(Math.toRadians(gradosCanion*-1),0,imgCanion.getIconHeight()/2);
                trCanion.translate(10,0);
                trCanion.rotate(Math.toRadians(gradosCanion),0,imgCanion.getIconHeight()/2);
            }
        }
        if(ke.getKeyCode()==KeyEvent.VK_UP){
            if(gradosCanion>-45){
                trCanion.rotate(Math.toRadians(-5),0,imgCanion.getIconHeight()/2);
                gradosCanion-=5;
            }
        }
        if(ke.getKeyCode()==KeyEvent.VK_DOWN){
            if(gradosCanion<0){
                trCanion.rotate(Math.toRadians(5),0,imgCanion.getIconHeight()/2);
                gradosCanion+=5;
            }
        }
        if(ke.getKeyCode()==KeyEvent.VK_SPACE){                
            if(dispara==false){
                trBala=new AffineTransform();
                trBala.translate(posicionTanque+107,540);
                trBala.rotate(Math.toRadians(gradosCanion),0,imgBala.getIconHeight()/2);
                trBala.translate(100,0);
                dispara=true;
                
                trFbala1=new AffineTransform();
                fbala1=new Rectangle2D.Double(posicionTanque+207,540,64,40);    
                trFbala1.rotate(Math.toRadians(gradosCanion),posicionTanque+107,540);                
                trFbala1.rotate(Math.toRadians(gradosCanion),fbala1.getBounds2D().getX(),fbala1.getBounds2D().getY());            
                
                
            }else{
                if(dispara2==false){
                    trBala2=new AffineTransform();
                    trBala2.translate(posicionTanque+107,540);
                    trBala2.rotate(Math.toRadians(gradosCanion),0,imgBala.getIconHeight()/2);
                    trBala2.translate(100,0);
                    dispara2=true; 
                    
                    trFbala2=new AffineTransform();
                    fbala2=new Rectangle2D.Double(posicionTanque+207,540,64,40);    
                    trFbala2.rotate(Math.toRadians(gradosCanion),posicionTanque+107,540);                
                    trFbala2.rotate(Math.toRadians(gradosCanion),fbala2.getBounds2D().getX(),fbala2.getBounds2D().getY());
                }else{
                    if(dispara3==false){
                        trBala3=new AffineTransform();
                        trBala3.translate(posicionTanque+107,540);
                        trBala3.rotate(Math.toRadians(gradosCanion),0,imgBala.getIconHeight()/2);
                        trBala3.translate(100,0);
                        dispara3=true;  
                        
                        trFbala3=new AffineTransform();
                        fbala3=new Rectangle2D.Double(posicionTanque+207,540,64,40);    
                        trFbala3.rotate(Math.toRadians(gradosCanion),posicionTanque+107,540);                
                        trFbala3.rotate(Math.toRadians(gradosCanion),fbala3.getBounds2D().getX(),fbala3.getBounds2D().getY());
                    }else{
                        if(dispara4==false){
                        trBala4=new AffineTransform();
                        trBala4.translate(posicionTanque+107,540);
                        trBala4.rotate(Math.toRadians(gradosCanion),0,imgBala.getIconHeight()/2);
                        trBala4.translate(100,0);
                        dispara4=true;    
                        
                        trFbala4=new AffineTransform();
                        fbala4=new Rectangle2D.Double(posicionTanque+207,540,64,40);    
                        trFbala4.rotate(Math.toRadians(gradosCanion),posicionTanque+107,540);                
                        trFbala4.rotate(Math.toRadians(gradosCanion),fbala4.getBounds2D().getX(),fbala4.getBounds2D().getY());
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}