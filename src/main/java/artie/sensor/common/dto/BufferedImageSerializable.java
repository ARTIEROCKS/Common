package artie.sensor.common.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 *
 * @author Luis-Eduardo Imbernon Cuadrado
 */
public class BufferedImageSerializable implements Serializable {
    
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
