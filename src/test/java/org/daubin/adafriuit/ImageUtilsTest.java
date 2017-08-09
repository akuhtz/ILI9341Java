package org.daubin.adafriuit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.daubin.adafriuit.image.ImageUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ImageUtilsTest {
    
    @Test
    public void to565RGBBytes() throws IOException {
        BufferedImage bufferedImage = getTestImage("TEST");
        
        // File outputfile = new File("image.jpg");
        // ImageIO.write(bufferedImage, "jpg", outputfile);

        byte[] to565rgbBytes = ImageUtils.to565RGBBytes(bufferedImage);
        
        Assert.assertEquals(to565rgbBytes[0], (byte) 124);
        Assert.assertEquals(to565rgbBytes[1], (byte) 83);
        Assert.assertEquals(to565rgbBytes[2], (byte) 124);
    }
    
    @Test
    public void getColor565() {
        Assert.assertEquals(32, ImageUtils.getColor565(new Color(6, 6,6)));
        Assert.assertEquals(30720, ImageUtils.getColor565(new Color(123, 3, 0)));
        
        Assert.assertEquals(65535, ImageUtils.getColor565(new Color(255, 255, 255)));
        
        Assert.assertEquals(63488, ImageUtils.getColor565(Color.RED));
    }

    private static BufferedImage getTestImage(String text) {
        BufferedImage bufferedImage = new BufferedImage(
                ILI9341.ILI9341_TFTWIDTH,
                ILI9341.ILI9341_TFTHEIGHT,
                BufferedImage.TYPE_USHORT_565_RGB);
        
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        JPanel panel = new JPanel();
        panel.setLayout(null); // new FlowLayout());
        panel.setBackground(Color.WHITE);
        panel.setSize(ILI9341.ILI9341_TFTWIDTH, ILI9341.ILI9341_TFTHEIGHT);
        
        JButton button = new JButton("test");
        panel.add(button);
        // button.setBounds(100, 100, 40, 40);
        button.setBounds(0, 0, 40, 40);
        
        JLabel label = new JLabel("Test");
        panel.add(label);
        
        panel.setVisible(true);
        
        panel.print(graphics);
        
        return bufferedImage;
    }

    public static void main(String[] args) throws IOException {
        
        final BufferedImage testImage = getTestImage("Dude");
        
        byte[] buf = ImageUtils.to565RGBBytes(testImage);
        
        final BufferedImage image = new BufferedImage(testImage.getWidth(), testImage.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
        
        drawBytes(image, buf);
        
        image.getGraphics().setColor(Color.BLUE);
        image.getGraphics().setFont(new Font("Arial Black", Font.BOLD, 30));
        image.getGraphics().drawString("After", 60, 60);
        
        EventQueue.invokeLater(new Runnable()
        {
            public void run(){
                
                displayImageFrame(testImage);
                displayImageFrame(image);
            }
        }
        );
    }

    private static void drawBytes(BufferedImage image, byte[] buf) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        for (int i = 0; i < buf.length; i+=2) {
            int pix = (buf[i] & 0xff) << 8 | (buf[i+1] & 0xff);
            dataBuffer.setElem(i/2, pix);
        }
    }

    protected static void displayImageFrame(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(), image.getHeight());
                        
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        frame.getContentPane().add(jLabel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);        
    }
}
