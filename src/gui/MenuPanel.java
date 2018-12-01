//package gui;
//
//import java.awt.Dimension;
//import java.awt.Point;
//
//import javax.swing.JPanel;
//
//import hauptmenu.PongFrame;
//
//public class MenuPanel extends JPanel{
//	private PongFrame pongFrame;
//	
//	public MenuPanel(PongFrame pongFrame) {
//		this.pongFrame = pongFrame;
//	}
//	public void setSize(Dimension size) {
////		System.out.print("[MenuPanel]Called setSize (width,height): "+width+","+height+" : ");
//		if(size.width<pongFrame.getGraphicResolution().width && size.height < pongFrame.getGraphicResolution().height) {
//		size.width = Math.round(size.width*pongFrame.getASPECT_RATIO());
//		size.height = Math.round(size.height*pongFrame.getASPECT_RATIO());
////		System.out.println(+width+","+height+" set?");
//		size = new Dimension(size.width, size.height);
//		}
//		this.setMinimumSize(size);
//		this.setMaximumSize(size);
//		this.setPreferredSize(size);
//		super.setSize(size.width, size.height);
////		System.out.println("getSize: "+getSize()+" setSize: "+width+","+height);
//	}
//	public void setSize(int width, int height) {
////		System.out.print("Called setSize (width,height): "+width+","+height+" : ");
//		Dimension size = new Dimension(width, height);
//		if(width<pongFrame.getGraphicResolution().width && height < pongFrame.getGraphicResolution().height) {
//		width = Math.round(width*pongFrame.getASPECT_RATIO());
//		height = Math.round(height*pongFrame.getASPECT_RATIO());
////		System.out.println(+width+","+height+" set?");
//		size = new Dimension(width, height);
//		}
//		this.setMinimumSize(size);
//		this.setMaximumSize(size);
//		this.setPreferredSize(size);
//		super.setSize(width, height);
////		System.out.println("getSize: "+getSize()+" setSize: "+width+","+height);
//	}	
//	public void setBounds(int x, int y, int width, int height) {
//		x = Math.round(x*pongFrame.getASPECT_RATIO());
//		x = Math.round(y*pongFrame.getASPECT_RATIO());
//		width = Math.round(width*pongFrame.getASPECT_RATIO());
//		height = Math.round(height*pongFrame.getASPECT_RATIO());
//		super.setBounds(x, y, width, height);
//	}
//	public void setLocation(Point p) {
//		p.x = Math.round(p.x*pongFrame.getASPECT_RATIO());
//		p.y = Math.round(p.y*pongFrame.getASPECT_RATIO());
//		super.setLocation(p);
//	}
//	public void setLocation(int x, int y) {
//		x = Math.round(x*pongFrame.getASPECT_RATIO());
//		y = Math.round(y*pongFrame.getASPECT_RATIO());
//		super.setLocation(x,y);
//	}
//}
