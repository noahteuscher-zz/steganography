// package application;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

	
public class Decoder {
	private Image image;
	
	private int pixel;
	private int pixelmask;
	
	int blank;
	
	public Decoder(Image image){
		this.image = image;
	}
	
	
	
	public String decode(){
		
		String themessage = "";
		
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		
		PixelReader pr = image.getPixelReader();
		
		boolean done = false;
		for(int y = 0; y < height && !done; y++){
			for(int x = 0; x < width && !done; x++){

					//set masks and blank
					blank = 0x00000000;
					pixelmask = 0x03000000;
					
					pixel = pr.getArgb(x, y);
					String thepixel = Integer.toBinaryString(pixel);
					while(thepixel.length() != 32){
						thepixel = "0" + thepixel;
					}
					
					System.out.println("This is the pixel: " + thepixel);
							
					for(int i = 3; i >= 0; i--){
						
						//reset pixel
						pixel = pr.getArgb(x, y);
						
						//apply mask to isolate two bits
						pixel = pixel & pixelmask;
						
						//shift pixel into proper position
						pixel = pixel >> 6*i;
						
						//write two selected bits into blank
						blank = blank | pixel;
							
						//shift mask
						pixelmask = pixelmask >> 8;
					
					}		
					
					//add newly harvest char to message
					themessage = themessage + (char)blank;
							
					String message = Integer.toBinaryString(blank);
					
					System.out.println("This is the encoded message: " + message);
					
					System.out.println("This is the secret char: " + Integer.toBinaryString((char)29));
					
					//check for unwritable attached to signify end of message
					if(message.equals((Integer.toBinaryString((char)29)))){
						System.out.println("FOUND THE STOPPING POINT!");
						done = true;
					}					
			}
		}
		return themessage;
	}
}
