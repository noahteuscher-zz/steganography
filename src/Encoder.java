// package application;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

	
public class Encoder {
	private Image image;
	private WritableImage encodedimage;
	private String message;
	
	private int pixel;
	private int newchar;
	private int pixelmask;
	private int charmask;
	
	public Encoder(String message, Image image){
		this.image = image;
		this.message = message;
		encodedimage = new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight());
	}
	
	

	public WritableImage encode(){
		
		
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		//attach unwritable to signify end of message
		message = message + "" + ((char)29);
		
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		
		PixelReader pr = image.getPixelReader();
		
		boolean done = false;
		for(int y = 0; y < height && !done; y++){
			for(int x = 0; x < width && !done; x++){
				
				int space = y * width + x;
				
				
				if( space >= message.length() ) {
					done = true;
				}
				else {

					pixel = pr.getArgb(x, y);
					
					newchar = (int)message.charAt(y * width + x);
					
					System.out.println("about to encode the following character: " + Integer.toBinaryString(newchar) + " into the following pixel: " + pixel);
							
					//define masks
					charmask = 0x000000c0;
					pixelmask = 0xfcffffff;
							
					for(int i = 3; i >= 0; i--){
						
						//the character
						newchar = (int)message.charAt(y * width + x);
							
						//apply masks
						newchar = newchar & charmask;	
						pixel = pixel & pixelmask;
							
						//shift masked char into correct position
						newchar = newchar << 6*i;
						
						//put two bits from char into the pixel
						pixel = pixel | newchar;
						
						//shift masks
						charmask = charmask >> 2;	
						pixelmask = pixelmask >> 8;
						
					
					}		
					
					String thepixel = Integer.toBinaryString(pixel);
					while(thepixel.length() != 32){
						thepixel = "0" + thepixel;
					}
					System.out.println("This is the new pixel: " + thepixel);
					
					encodedimage.getPixelWriter().setArgb(x, y, pixel);
					

				}
			}
		}
		
		return encodedimage;
	}
}
