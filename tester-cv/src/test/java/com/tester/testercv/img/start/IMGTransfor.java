package com.tester.testercv.img.start;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IMGTransfor {

	public static void main(String[] args) throws IOException{
		String input = "D:\\trans\\005.bmp";
		String output = "D:\\trans\\09\\007.jpg";
		transfor2JPEG(input,output,"jpg");
		//transfor2JPG();
	}
	
	public static void transfor2JPEG(String input,String output,String targetFormat) throws IOException{
		BufferedImage bufferedImage;
		bufferedImage = ImageIO.read(new File(input));
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
	            bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
		ImageIO.write(newBufferedImage, targetFormat, new File(output));
	}
	
	
	private static void transfor2JPG(){
		String imgs = "D:\\trans\\005.jpg";
		int index = imgs.lastIndexOf(".");
		imgs = imgs.substring(0,index);
		System.out.println(imgs);
	}
	
}
