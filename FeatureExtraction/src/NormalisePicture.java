import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import java.awt.image.RenderedImage;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;

public class NormalisePicture {
	
	
	public static void main(String [] args) throws Exception
	{
		final int NUMBER_OF_CLASS = 10;
		FeatureExtraction feNormalize = new FeatureExtraction();
		FeatureExtraction feSkeleton = new FeatureExtraction();

		try{
			for (int c = 0; c < NUMBER_OF_CLASS; ++c) {
				File folder = new File("images\\Samples\\"+c);
				new File("images\\Normalized\\" + c).mkdirs();
				new File("images\\Skeleton\\" + c).mkdirs();
				new File("images\\PreSkeleton\\" + c).mkdirs();
				
				feNormalize.startClass("..\\DigitRecognition\\Features\\naive\\feature" + c + ".txt");
				feSkeleton.startClass("..\\DigitRecognition\\Features\\skeleton\\feature" + c + ".txt");
				for (final File entry : folder.listFiles()) {
					
					  FileInputStream input = new FileInputStream(entry);
					  FileChannel medium = input.getChannel();
					 
					  ByteBuffer buffer = ByteBuffer.allocate((int)medium.size());
					  medium.read(buffer);
					  BufferedImage originalImage = load(buffer.array());
						
					  int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
					  BufferedImage normalizeImage = resize(originalImage, type, 20, 20);
					  BufferedImage preSkeleton = resize(originalImage, type, 45, 45);
					  BufferedImage skeleton = getSkeleton(preSkeleton);
					
					  feNormalize.export(normalizeImage);
					  feSkeleton.export(skeleton);
					  
					  /*ImageIO.write(normalizeImage, "jpg", new File("images\\Normalized\\" + c + "\\" + entry.getName() + ".jpg"));
					  ImageIO.write(skeleton, "jpg", new File("images\\Skeleton\\" + c + "\\" + entry.getName() + ".jpg"));
					  ImageIO.write(preSkeleton, "jpg", new File("images\\PreSkeleton\\" + c + "\\" + entry.getName() + ".jpg"));*/
					  
					  input.close();
				}
				
				feNormalize.close();
				feSkeleton.close();
				System.out.println("Finished class " + c);
			}
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}

	}
	
	/////////////////////Methods needed in the MAIN Class//////////////////////////////////
	
	//This method is used for reading TIFF Files in Java
	private static BufferedImage load(byte[] tiffPic) throws Exception
	{
	    BufferedImage tiff = null;
	    SeekableStream streamer = new ByteArraySeekableStream(tiffPic);
		
	    String[] imageName = ImageCodec.getDecoderNames(streamer);
		ImageDecoder decodeImg = ImageCodec.createImageDecoder(imageName[0], streamer, null);
		
		RenderedImage image = decodeImg.decodeAsRenderedImage();
		
		tiff = PlanarImage.wrapRenderedImage(image).getAsBufferedImage();
		return tiff;
	}
	  
	//This is to resize the pictures to 20pixels by 20pixels
    public static BufferedImage resize(BufferedImage original, int type, int height, int width)
    {
	
    	BufferedImage newImg = new BufferedImage(width, height, type);
    	Graphics2D graphics = newImg.createGraphics();
    	graphics.drawImage(original, 0, 0, width, height, null);
    	graphics.dispose();

    	return newImg;
    }

    public static BufferedImage getSkeleton(BufferedImage image) {
    	BufferedImage skeleton = new BufferedImage(15, 15, image.getType());
    	Graphics2D g2d = skeleton.createGraphics();
    	boolean isSet;

    	g2d.setPaint(Color.WHITE);
    	g2d.fillRect ( 0, 0, skeleton.getWidth(), skeleton.getHeight() );
    	for (int rs = 14; rs >= 0; --rs) {
    		for (int cs = 0; cs < 15; ++cs) {
    			isSet = false;
    			for (int ri = rs * 3; !isSet && ri < rs * 3 + 3; ++ri) {
    				for (int ci = cs * 3; !isSet && ci < cs * 3 + 3; ci++) {
    					Color cl = new Color(image.getRGB(ri, ci));
    					
    					if (cl.equals(Color.BLACK)) {
    						skeleton.setRGB(rs, cs, 0);
    						isSet = true;
    					}
    				}
    			}
    		}
    	}

    	return skeleton;
    }
  
}