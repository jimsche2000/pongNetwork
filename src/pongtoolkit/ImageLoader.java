package pongtoolkit;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageLoader {
	private static String folder = "img/";

	public static ImageIcon loadIcon(String name, int width, int height) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(dimg);
	}
	public static ImageIcon loadIcon(String name, Dimension size) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
		return new ImageIcon(dimg);
	}
	public static ImageIcon loadIcon(String name) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		return new ImageIcon(img);
	}

	public static Image loadImage(String name, int width, int height) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return dimg;
	}
	public static Image loadImage(String name, Dimension size) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
		return dimg;
	}

	public static Image loadImage(String name) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		return (Image) img;
	}

	public static BufferedImage loadBufferedImage(String name, int width, int height) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return (BufferedImage) dimg;
	}

	public static BufferedImage loadBufferedImage(String name) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(folder + name));
		} catch (IOException e) {
			System.out.println("ERR: Image \"" + name + "\" couldn't load:\n");
			e.printStackTrace();
		}
		return img;
	}
    public static void invertImage(String imageName) {
        BufferedImage inputFile = null;
        try {
        	inputFile = ImageIO.read(ClassLoader.getSystemResource(folder + imageName));
//            inputFile = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                                255 - col.getGreen(),
                                255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        try {
            File outputFile = new File("C:\\invert-"+imageName);
            ImageIO.write(inputFile, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//		invertImage("swapLeftRight.png");
//	}
}