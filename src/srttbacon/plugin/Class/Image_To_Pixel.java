package srttbacon.plugin.Class;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;

public class Image_To_Pixel
{
    public static void Init()
    {
    colors[0] = new Color(150, 52, 48);
    colors[1] = new Color(177, 166, 39);
    colors[2] = new Color(46, 56, 141);
    colors[3] = new Color(53, 70, 27);
    colors[4] = new Color(0, 0, 0);
    colors[5] = new Color(221, 221, 221);
    colors[6] = new Color(172, 105, 65);
    colors[7] = new Color(46, 110, 137);
    colors[8] = new Color(126, 61, 181);
    colors[9] = new Color(64, 64, 64);
    colors[10] = new Color(106, 138, 201);
    colors[11] = new Color(208, 132, 153);
    colors[12] = new Color(65, 174, 56);
    colors[13] = new Color(179, 80, 188);
    colors[14] = new Color(154, 161, 161);
    colors[15] = new Color(79, 50, 31);
    colors[16] = new Color(125, 125, 125);
    colors[17] = new Color(134, 96, 67);
    colors[18] = new Color(219, 211, 160);
    colors[19] = new Color(131, 123, 123);
    colors[20] = new Color(158, 164, 176);
    colors[21] = new Color(20, 18, 29);
    colors[22] = new Color(103, 121, 103);
    colors[23] = new Color(165, 110, 44);
    colors[24] = new Color(156, 127, 78);
    colors[25] = new Color(122, 122, 122);
    colors[26] = new Color(218, 210, 158);
    colors[27] = new Color(146, 99, 86);
    colors[28] = new Color(219, 219, 219);
    colors[29] = new Color(249, 236, 78);
    colors[30] = new Color(97, 219, 213);
    colors[31] = new Color(29, 71, 165);
    colors[32] = new Color(100, 67, 50);
    colors[33] = new Color(143, 118, 69);
    colors[34] = new Color(40, 20, 20);
    colors[35] = new Color(84, 64, 51);
    colors[36] = new Color(239, 251, 251);
    String[] colorIDs = new String[37];
    colorIDs[0] = "35:14";
    colorIDs[1] = "35:4";
    colorIDs[2] = "35:11";
    colorIDs[3] = "35:13";
    colorIDs[4] = "35:15";
    colorIDs[5] = "35:0";
    colorIDs[6] = "35:1";
    colorIDs[7] = "35:9";
    colorIDs[8] = "35:10";
    colorIDs[9] = "35:7";
    colorIDs[10] = "35:3";
    colorIDs[11] = "35:6";
    colorIDs[12] = "35:5";
    colorIDs[13] = "35:2";
    colorIDs[14] = "35:8";
    colorIDs[15] = "35:12";
    colorIDs[16] = "01";
    colorIDs[17] = "03";
    colorIDs[18] = "12";
    colorIDs[19] = "13";
    colorIDs[20] = "82";
    colorIDs[21] = "49";
    colorIDs[22] = "48";
    colorIDs[23] = "86";
    colorIDs[24] = "05";
    colorIDs[25] = "04";
    colorIDs[26] = "24";
    colorIDs[27] = "45";
    colorIDs[28] = "42";
    colorIDs[29] = "41";
    colorIDs[30] = "57";
    colorIDs[31] = "22";
    colorIDs[32] = "25";
    colorIDs[33] = "89";
    colorIDs[34] = "87";
    colorIDs[35] = "88";
    colorIDs[36] = "80";
    for (int i = 0; i <= 36; i++)
      cMap.put(Integer.valueOf(colors[i].getRGB()), colorIDs[i]); 
    cMap.put(Integer.valueOf(-1), "0");
  }
  public static boolean onCommand(cn.nukkit.command.CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
	  if (cmd.getName().equalsIgnoreCase("Image_To_Pixel_1"))
	  {
		    URL u;
		    if (args.length < 1) {
		      sender.sendMessage(TextFormat.RED + "You need to specify an image!");
		      return true;
		    } 
		    try {
		      u = new URL(args[0]);
		    } catch (MalformedURLException e) {
		      sender.sendMessage(TextFormat.RED + "Your parameter is malformed!");
		      return true;
		    } 
		    try {
		      BufferedImage img = ImageIO.read(u);
		      int p1 = 0;
		      int p2 = 0;
		      int p3 = 0;
		      sender.sendMessage("横:" + img.getWidth() + " | 縦:" + img.getHeight());
		      for (int x = 0; x <= img.getWidth() - 1; x++) {
		        for (int y = 0; y <= img.getHeight() - 1; y++) {
		          int closestMatch = getClosestMatch(img, x, y);
		          int blockID = Integer.parseInt(((String)cMap.get(Integer.valueOf(closestMatch))).split(":")[0]);
		          /*if ((((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")).length > 1) {
		            blockData = Integer.parseInt(((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")[1]);
		          } else {
		            blockData = 0;
		          } */
		          if (commandLabel.equalsIgnoreCase("imagegen1")) {
		            p1 = x;
		            p2 = -y + img.getHeight() - 1;
		            p3 = 0;
		          } else if (commandLabel.equalsIgnoreCase("imagegen2")) {
		            p1 = 0;
		            p2 = -y + img.getHeight() - 1;
		            p3 = x;
		          } else {
		            p1 = x;
		            p2 = 0;
		            p3 = -y + img.getHeight() - 1;
		          } 
		          ((cn.nukkit.Player)sender).level.setBlock(new Vector3(p1, p2, p3), Block.get(blockID));
		          //((cn.nukkit.Player)sender).getLocation().add(p1, p2, p3).getBlock().setData((byte)blockData);
		        } 
		      } 
		    } catch (IOException e) {
		      sender.sendMessage(TextFormat.RED + "Couldn't locate the image!");
		      return true;
		    } 
	  }
	  else if (cmd.getName().equalsIgnoreCase("Image_To_Pixel_2"))
	  {
		    URL u;
		    if (args.length < 1) {
		      sender.sendMessage(TextFormat.RED + "You need to specify an image!");
		      return true;
		    } 
		    try {
		      u = new URL(args[0]);
		    } catch (MalformedURLException e) {
		      sender.sendMessage(TextFormat.RED + "Your parameter is malformed!");
		      return true;
		    } 
		    try {
		      BufferedImage img = ImageIO.read(u);
		      int p1 = 0;
		      int p2 = 0;
		      int p3 = 0;
		      sender.sendMessage("横:" + img.getWidth() + " | 縦:" + img.getHeight());
		      for (int x = 0; x <= img.getWidth() - 1; x++) {
		        for (int y = 0; y <= img.getHeight() - 1; y++) {
		          int closestMatch = getClosestMatch(img, x, y);
		          int blockID = Integer.parseInt(((String)cMap.get(Integer.valueOf(closestMatch))).split(":")[0]);
		          /*if ((((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")).length > 1) {
		            blockData = Integer.parseInt(((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")[1]);
		          } else {
		            blockData = 0;
		          } */
		          p1 = 0;
		            p2 = -y + img.getHeight() - 1;
		            p3 = x;
		          ((cn.nukkit.Player)sender).level.setBlock(new Vector3(p1, p2, p3), Block.get(blockID));
		          //((cn.nukkit.Player)sender).getLocation().add(p1, p2, p3).getBlock().setData((byte)blockData);
		        } 
		      } 
		    } catch (IOException e) {
		      sender.sendMessage(TextFormat.RED + "Couldn't locate the image!");
		      return true;
		    } 
	  }
	  if (cmd.getName().equalsIgnoreCase("Image_To_Pixel_3"))
	  {
		    URL u;
		    if (args.length < 1) {
		      sender.sendMessage(TextFormat.RED + "You need to specify an image!");
		      return true;
		    } 
		    try {
		      u = new URL(args[0]);
		    } catch (MalformedURLException e) {
		      sender.sendMessage(TextFormat.RED + "Your parameter is malformed!");
		      return true;
		    } 
		    try {
		      BufferedImage img = ImageIO.read(u);
		      int p1 = 0;
		      int p2 = 0;
		      int p3 = 0;
		      sender.sendMessage("横:" + img.getWidth() + " | 縦:" + img.getHeight());
		      for (int x = 0; x <= img.getWidth() - 1; x++) {
		        for (int y = 0; y <= img.getHeight() - 1; y++) {
		          int closestMatch = getClosestMatch(img, x, y);
		          int blockID = Integer.parseInt(((String)cMap.get(Integer.valueOf(closestMatch))).split(":")[0]);
		          /*if ((((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")).length > 1) {
		            blockData = Integer.parseInt(((String)this.cMap.get(Integer.valueOf(closestMatch))).split(":")[1]);
		          } else {
		            blockData = 0;
		          } */
		          p1 = x;
		            p2 = 0;
		            p3 = -y + img.getHeight() - 1;
		          ((cn.nukkit.Player)sender).level.setBlock(new Vector3(p1, p2, p3), Block.get(blockID));
		          //((cn.nukkit.Player)sender).getLocation().add(p1, p2, p3).getBlock().setData((byte)blockData);
		        } 
		      } 
		    } catch (IOException e) {
		      sender.sendMessage(TextFormat.RED + "Couldn't locate the image!");
		      return true;
		    } 
	  }
    return true;
  }
  
  static int getClosestMatch(BufferedImage img, int x, int y) {
    if ((img.getRGB(x, y) >> 24 & 0xFF) < 10)
      return -1; 
    int prevMin = 765;
    int closestMatch = 15727611;
    for (int i = 0; i <= 36; i++) {
      if (i != 0)
        if (getColorDifference(img.getRGB(x, y), colors[i]) < prevMin) {
          prevMin = getColorDifference(img.getRGB(x, y), colors[i]);
          closestMatch = colors[i].getRGB();
        }  
    } 
    return closestMatch;
  }
  
  static int getColorDifference(int c1, Color c2) {
    int diff = 0;
    c = new Color(c1);
    diff += Math.abs(c.getRed() - c2.getRed());
    diff += Math.abs(c.getGreen() - c2.getGreen());
    diff += Math.abs(c.getBlue() - c2.getBlue());
    return diff;
  }
  
  static Color[] colors = new Color[37];
  
  static Color c;
  
  static Map<Integer, String> cMap = new HashMap<Integer, String>();
}