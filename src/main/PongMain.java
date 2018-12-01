package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import hauptmenu.PongFrame;

public class PongMain {

	public static void main(String[] args) {
//		Dimension programmedSize = new Dimension(1920, 1080);
//		double programmed_width_height_ratio = 1920. / 1080.;
//		double programmed_height_width_ratio = 1080. / 1920.;
//		Dimension fullScreenSize = new Dimension(1000, 4000);
//		Dimension fullScreenSize = new Dimension(2100, 900);
//		Dimension fullScreenSize = new Dimension(1920, 1200);
//		Dimension fullScreenSize = new Dimension(2560, 1440);
//CENTER IT: 
		// int top = (screenHeight - newHeight)/2;
		// int left = (screenWidth - newWidth)/2;
		
		//TODO: Globale! Variable für Aspect-Ratio anlegen, und Jegliche! Positionen(Point) und größen(Dimension) damit multiplizieren.
		// In der PongFrame-Klasse das Fenster auf Volle Auflösung einstellen, und das Haupt-Panel mittig positionieren,
		//sowie dem Haupt-Panel die per aspect-ratio berechnete größe ausrechnen
		
//		Dimension fullScreenSize = new Dimension(1920, 1080);
//		System.out.println("1st: " + Double.toString((double)fullScreenSize.width / (double)fullScreenSize.height));
//		System.out.println("2nd: " + Double.toString((double)programmedSize.width / (double)programmedSize.height));
		//Die FullScreen-Achse (X(Horizontal, width), oder Y(Vertikal, height)) die die kleinste Veränderung im Vergleich mit
		//den Seitenverhältnissen der programmedSize hat, soll primär genutzt werden. Die andere Achse wird dann nurnoch mit
		//dem 16:9 faktor ausgerechnet. Damit soll verhindert werden, dass zB. bei 21:9 das Bild unten abgeschnitten ist, und bei 4:4 das Bild rechts abgeschitten ist
//		Dimension paintSize = new Dimension(0,0);
//		if(fullScreenSize.width / fullScreenSize.height >= programmedSize.width / programmedSize.height) {
//		if(fullScreenSize.width <= fullScreenSize.height) {//Die Horizontale ist die längere Seite
			//zB. 16:10, 4:4 o.ä. UND 16:9
//			paintSize = new Dimension((int) fullScreenSize.getWidth(),(int) Math.round(fullScreenSize.getWidth() / programmed_width_height_ratio));
//			System.out.println("Der Bildschirm hat ein kleineres Seitenverhältnis als 16:9");
//		}else if(fullScreenSize.width / fullScreenSize.height < programmedSize.width / programmedSize.height) {
//		}else if(fullScreenSize.height < fullScreenSize.width) {//Die Vertikale ist die längere Seite
			//zB. 21:9
//			paintSize = new Dimension((int) Math.round(fullScreenSize.getHeight() * programmed_height_width_ratio),(int)fullScreenSize.getHeight());
//			System.out.println("Der Bildschirm hat ein Seitenverhältnis von 16:9 und mehr");
//		Dimension paintSize = new Dimension((int) fullScreenSize.getWidth(),
//				(int) Math.round(fullScreenSize.getWidth() / programmed_width_height_ratio));
//		System.out.println("new Dimension: " + paintSize + " ratioWH: " + programmed_width_height_ratio+" ratioHW: "+programmed_height_width_ratio);
					new PongFrame();
	}

}