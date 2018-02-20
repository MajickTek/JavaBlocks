public class Block
{
    private int id, x, y, width, height;
   
    public Block(int pid, int px, int py, int pwidth, int pheight){
        id = pid;
        x = px;
        y = py;
        width = pwidth;
        height = pheight;
    }
    public int getID(){return id;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getW(){return width;}
    public int getH(){return height;}
}
