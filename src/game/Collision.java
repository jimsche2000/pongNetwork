package game;

public class Collision {

	
	/*
	 * Methode circleToRect
	 * 
	 * Parameter:
	 * 
	 * float cx: X-Position des Kreises
	 * float cy: Y-Position des Kreises
	 * int radius: Radius des Kreises
	 * float rectx: X-Position des Rechtecks
	 * float recty: Y-Position des Rechtecks
	 * int width: Breite des Rechtecks
	 * int height: Höhe des Rechtecks
	 * 
	 * 
	 * Rückgabewert:
	 * 
	 * boolean (true wenn Kollision)
	 * 
	 * 
	 */
	public static boolean circleToRect(float cx, float cy, int radius, float rectx, float recty, 
			int width, int height){
		
//		System.out.println("---------------------------\nCollision:\nCX: "+cx+" CY: "+cy+" radius: "+radius+"\nrectx: "+rectx+" recty: "+recty+" width: "+width+" height: "+height+"\n--------------------------");
		
			if(cx >= rectx&&cx <= rectx + width)
			{
		
			if((cy - recty) * (cy - recty) <= radius * radius){
//				System.out.println("I BIMS 1 KOLLISION TRU"); 
				return true;
			}
			if((cy - (recty + height)) * (cy - (recty + height)) <= radius * radius){
//				System.out.println("I BIMS 2 KOLLISION TRU");
				return true;
			}
		}
	
		if(cy >= recty&&cy <= recty + height)
		{
			if((cx - rectx) * (cx - rectx) <= radius * radius){
//				System.out.println("I BIMS 3 KOLLISION TRU");
				return true;
			}
			if((cx - (rectx + width)) * (cx - (rectx + width)) <= radius * radius){
//				System.out.println("I BIMS 4 KOLLISION TRU");
				return true;
			}
		}
		for(int x = 0;x < 2;x++)
		{
			for(int y = 0;y <2;y++)
			{
				float absx = cx - (rectx + x * width);
				float absy = cy- (recty + y * height);
				float abs = absx * absx + absy * absy;
				if(abs <= radius * radius){
//					System.out.println("I BIMS 5 KOLLISION TRU");
					return true;
				}
			}
		}
		if(cx >= rectx&&cx <= rectx + width&&cy >= recty&&cy <= recty + height){
//			System.out.println("I BIMS 6 KOLLISION TRU");
			return true;		
		}
				
		return false;
	}
}
