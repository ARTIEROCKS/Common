package artie.sensor.common.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 *
 * @author Luis-Eduardo Imbernon Cuadrado
 */
public class BufferedImageSerializable implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Attributes
    private BufferedImage bufferedImage;
    
    //Properties
    public BufferedImage getBufferedImage(){
        return this.bufferedImage;
    }
    public void setBufferedImage(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
    }
    
    /**
     * Default constructor
     */
    public BufferedImageSerializable(){
        
    }
    /**
     * Parameterized constructor
     * @param bufferedImage 
     */
    public BufferedImageSerializable(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
    }
    
    /**
     * Image Serialization method
     */
    public String imageSerialization() throws IOException {
         
         byte[] imageInByte;
         ByteArrayOutputStream baos;
         String imageString;

         //Transforming the screenCapture into a byte array
         baos = new ByteArrayOutputStream();
         ImageIO.write(this.bufferedImage, "jpg", baos);
         imageInByte = baos.toByteArray();
         baos.flush();
         baos.close(); 
         imageString = Base64.getEncoder().encodeToString(imageInByte);
         
         return imageString;
    }
    
    /**
     * Function to transform a byte array into a buffered image
     * @param webcamCaptureBytes
     * @return
     * @throws IOException 
     */
    public BufferedImageSerializable imageDeserialization(String bufferedImage) throws IOException{
        
         byte[] webcamCaptureBytes = Base64.getDecoder().decode(bufferedImage);
         BufferedImage bImageFromConvert =ImageIO.read(new ByteArrayInputStream(webcamCaptureBytes));
         BufferedImageSerializable bufferedImageSerializable = new BufferedImageSerializable(bImageFromConvert);
         
         return bufferedImageSerializable;
        
    }
    
    /**
     * Custom serializable write function
     * @param o
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream o)
    	    throws IOException { 
    	
    	//Converts the buffered image in a byte array
    	byte[] imageInByte;
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(this.bufferedImage, "jpg", baos);
    	baos.flush();
        imageInByte = baos.toByteArray();
        baos.close();
    	
        o.write(imageInByte);
    }
    
    /**
     * Custom serializable read function
     * @param o
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream o)
    	    throws IOException, ClassNotFoundException {  
    	
    	 //Converts the byte array in a buffered image
        InputStream in = new ByteArrayInputStream((byte[]) o.readObject());
        this.bufferedImage = ImageIO.read(in);
    	
    }
    
}
