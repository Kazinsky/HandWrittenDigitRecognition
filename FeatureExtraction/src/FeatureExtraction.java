import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class FeatureExtraction {
	
	private BufferedWriter currentWriter;
	
	public FeatureExtraction() {
		currentWriter = null;
	}
	
	public void startClass(String path) throws IOException {
		if (currentWriter != null) {
			currentWriter.close();
		}
		currentWriter = new BufferedWriter(new FileWriter(path));
	}
	
	public void close() throws IOException {
		if (currentWriter != null) {
			currentWriter.close();
		}
	}
	
	public void export(BufferedImage image) throws IOException {
		final int height = image.getHeight();
		final int width = image.getWidth();
		
		for (int h = height - 1; h >= 0; --h) {
			for (int w = 0; w < width; ++w) {
				Color cl = new Color(image.getRGB(w,  h));
				if (cl.equals(Color.BLACK)) {
					currentWriter.write("1");
				}
				else if (cl.equals(Color.WHITE)){
					currentWriter.write("0");
				}
			}
		}
		currentWriter.newLine();
	}
	
/*	public static void main(String args[]) throws Exception 
	{
		   for(int count = 1; count <1001; count++)
		   {
	 	  	  //Change the value from 0 to 1 till 9 for the path below
			   
		 // String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\0\\0-"+(count)+".jpg";
		  // String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\1\\1-"+(count)+".jpg";
		  // String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\2\\2-"+(count)+".jpg";
		   //String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\3\\3-"+(count)+".jpg";
		  // String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\4\\4-"+(count)+".jpg";
		   //String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\5\\5-"+(count)+".jpg";
		   //String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\6\\6-"+(count)+".jpg";
		  // String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\7\\7-"+(count)+".jpg";
		 //  String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\8\\8-"+(count)+".jpg";
		   String name  = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\NormalizeDigit\\9\\9-"+(count)+".jpg";
		   
		   Matrix2DWritter(extractFeature(name));
	   
		   }
	   }
	
	/////////////////////////////Class needed by the MAIN Method////////////////////////////////////////
	
	 public static void Matrix2DWritter(int[][] featureMatrix)
	 {
		    try {
	
		    	//This is to create the files for the features. We have 10 files, 1 file per number.
		    	/*String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\feature.xls";*/
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature0.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature1.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature2.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature3.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature4.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature5.txt";
		    	//String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature6.txt";
		    //	String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature7.txt";
		    //	String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature8.txt";
		    
/*		    	String featureFile = "C:\\Users\\Aakash\\Google Drive\\Concordia Semester 8\\COMP 472\\Aakash_FeatureExtraction\\FeatureFile\\feature9.txt";

		    	File file = new File(featureFile);

		    	BufferedWriter bwrite = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
		      
		        for (int i = 0; i < featureMatrix.length; i++) 
		        {
		            for (int j = 0; j < featureMatrix[i].length; j++) 
		            {
		                    bwrite.write(featureMatrix[i][j] + " ");
		            }
		            bwrite.newLine();//skip line in the file
		        }
		        
		        bwrite.newLine();//skip line in the file
		        bwrite.flush();
		        
		    } 
		    catch (IOException e)
		    {
		    	  System.out.println(e.getMessage());
		    }
	    }
	
			
	   	public static int[][] extractFeature(String fileName)
	   	{ 
	       	int[][] feature = new int[20][20];
	       
	      	try {
	       
	    	 	File input = new File(fileName);
	    	  
	    	 	BufferedImage image = ImageIO.read(input);
	    	 
	         int ImageWidth = image.getWidth();
	         int ImageHeight = image.getHeight();
	
	         for(int i=0; i<ImageHeight; i++)
	         {
        	    for(int j=0; j<ImageWidth; j++)
	            {
	               	   Color color = new Color(image.getRGB(j,i));
	               
		               if(((color.getRed() == 255)&&(color.getGreen() == 255)&&(color.getBlue() == 255)))//white color for pixel
		               {
		            	   feature[i][j] = 0;
		            	   //feature[i][j] +=feature[i][j];
		            	   feature[i][j] =feature[i][j];
		               }
		               else//black color for pixel
		               {
		            	   feature[i][j] = 1;
		            	   feature[i][j] = feature[i][j];      
		               }
		            }
		       }
	      } 
	      catch (Exception e)
	      {
	    	  System.out.println(e.getMessage());
	      }
	      
		return  feature;
	         
	   }
	   */
}