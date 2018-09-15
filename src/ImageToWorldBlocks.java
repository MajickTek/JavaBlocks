import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageToWorldBlocks {
    
    private int[] pixels; // you need a 1-dimension array to read pixels from an image
    private int[][] pixels2D; // the pixels will then be moved into this 2-d array
    private int width = 0, height = 0; // used for pixels size of image
    static ArrayList<Block> blocks = new ArrayList<Block>(); // make a list of every object found
    
    // MS Paint default colors, in hex
    private int red = 0xED1C24; // raw hex = ED1C24; rgb = (237,28,36)
    private int green = 0x22B14C; // raw hex = 22B14C; rgb = (34,177,76)
    private int lime = 0xB5E61D; // raw hex = B5E61D; rgb = (181,230,29)
    private int turq = 0x00A2E8; // raw hex = 00A2E8; rgb = (0,162,232)
    private int lightturq = 0x99D9EA; // raw hex = 99D9EA; rgb = (153,217,234)
    private int orange = 0xFF7F27; // raw hex = FF7F27; rgb = (255,127,39)
    private int purple = 0xA349A4; // raw hex = A349A4; rgb = (163,73,164)
    private int white = 0xFFFFFF; // raw hex = FFFFFF; rgb = (255,255,255)
    //END DEFAULT COLORS
    
    //here is a list of standard RGB colors, in hex (not including duplicates):
    private int black = 0x000000; // raw hex = 000000; rgb = (0,0,0)
    private int rgb_red = 0xFF0000; // raw hex = FF0000; rgb = (255,0,0)
    private int rgb_lime = 0x00FF00; // raw hex = 00FF00; rgb = (0,255,0)
    private int blue = 0x0000FF; // raw hex = 0000FF; rgb = (0,0,255)
    private int yellow = 0xFFFF00; //raw hex = FFFF00; rgb = (255,255,0)
    private int cyan = 0x00FFFF; // raw hex = 00FFFF; rgb = (0,255,255)
    private int magenta = 0xFF00FF; // raw hex = FF00FF; rgb = (255,0,255)
    private int silver = 0xC0C0C0; // raw hex = C0C0C0; rgb = (192,192,192)
    private int grey = 0x808080; // raw hex = 808080; rgb = (128,128,128)
    private int maroon = 0x800000; // raw hex = 800000; rgb = (128,0,0)
    private int olive = 0x808000; // raw hex = 808000; rgb = (128,128,0)
    private int rgb_green = 0x008000; // raw hex = 008000; rgb = (0,128,0)
    private int rgb_purple = 0x800080; // raw hex = 800080; rgb = (128,0,128)
    private int teal = 0x008080; // raw hex = 008080; rgb = (0,128,128)
    private int navy = 0x000080; // raw hex = 008080; rgb = (0,0,128)
    
    public ImageToWorldBlocks(){
        //Empty Constructor; this is for instantiating the class like below (in main)
    }
    public static void main(String[] args){
        ImageToWorldBlocks i2wb = new ImageToWorldBlocks();
        i2wb.getPixels(args[1]); // your image
        i2wb.searchPixels();
        i2wb.outputCode();
    }
    public void getPixels(String filename){ // puts image pixels into array
        System.out.println("Reading \"" + filename + "\"...");
        try{
            BufferedImage image = ImageIO.read(ImageToWorldBlocks.class.getResource(filename));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            pixels2D = new int[width][height];
            image.getRGB(0,0,width,height,pixels,0,width);
            for(int i=0; i<pixels.length; i++){
                pixels2D[i%width][i/width] = (pixels[i] + 0x1000000);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void searchPixels(){
        // starts at top left corner (0,0)
        // when it finds a defined pixel color (red, green, white, etc) it will create a block object
        // the block type (brick, grass, water, other) is determined by the color
        // the width and height are measured by looking for black pixels in other corners of the object        
        System.out.println("\nSearching for objects...");
        int id, w, h;
        String name = "";
        for(int i=0; i<width; i++){ // nested for loop to look through every pixel, look for key pixels
            for(int j=0; j<height; j++){
                if(pixels2D[i][j] == white){ // white for undefined object
                    id = 999;
                    name = "unknown";
                }
                else if(pixels2D[i][j] == red){ // red for bricks
                    id = 20;
                    name = "brick";
                }
                else if(pixels2D[i][j] == green || pixels2D[i][j] == lime){ // green/lime for grass
                    id = 10;
                    name = "grass";
                }
                else if(pixels2D[i][j] == turq || pixels2D[i][j] == lightturq){ // turq/lightturq for water
                    id = 0;
                    name = "water";
                }
                else continue; // if not a key pixel, go to next pixel (skip the following code)
                w = readW(i,j); // find the width of the object
                h = readH(i,j); // find the height
                blocks.add(new Block(id, i, j, w, h)); // add the new object to the arraylist
                for(int x=i; x<i+w; x++){ // replace the used pixels with black to avoid reading them again
                    for(int y=j; y<j+h; y++){
                        pixels2D[x][y] = 0;
                    }
                }
                System.out.println("Found " +name+ " object of size "
                        +w+ " x " +h+ " at location (" +i+ ", " +j+ ")");
            }
        }
    }
    public int readW(int i, int j){ // reads width of object starting at top left pixel
        int count = 0; // count how many pixels to the right are the same color
        int color = pixels2D[i][j];
        while(pixels2D[i][j] == color){
            count++;
            i++;
        }
        return count;
    }
    public int readH(int i, int j){ // reads height of object starting at top left pixel
        int count = 0; // count how many pixels down
        int color = pixels2D[i][j];
        while(pixels2D[i][j] == color){
            count++;
            j++;
        }
        return count;
    }
    public void outputCode(){ // prints information from list of blocks
        System.out.println("\nThe Slime Boy level code [Java] is:");
        for(Block b:blocks){
            System.out.println("World.blocks.add(new Block("
                    +b.getID()+ ", " +b.getX()+ ", " +b.getY()+ ", " +b.getW()+ ", " +b.getH()+ "));");
        }
        System.out.println("");
    }
}
