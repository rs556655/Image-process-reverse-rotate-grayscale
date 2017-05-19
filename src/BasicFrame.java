import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class BasicFrame extends JFrame {

	private static final long serialVersionUID = 4390493953307669741L;
	JPanel cotrolPanel = new JPanel();
	ImagePanel imagePanel = new ImagePanel();
	JButton btnShow, btnNegative, btnGrayScale, btnUpDown, btnLeftRight, btnRight90, btnLeft90, btnRight180;

	final int[][][] data;
	int height, width;
	BufferedImage img = null;
	
	ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnNegative) 		processNegative();
			else if (e.getSource() == btnGrayScale) processGrayScale();
			else if (e.getSource() == btnUpDown) 	processUpDown();
			else if (e.getSource() == btnLeftRight) processLeftRight();
			else if (e.getSource() == btnRight90) 	processRight90();
			else if (e.getSource() == btnLeft90) 	processLeft90();
			else if (e.getSource() == btnRight180) 	processRight180();
			else imagePanel.showImage(width, height, data);
		}
	};
	
	protected BasicFrame(){
		setTitle("影像處理");
		
		try {
		    img = ImageIO.read(new File("file/Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3]; 
		
		this.setSize(width + 15, height + 77);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Utils.getR(rgb);
				data[y][x][1] = Utils.getG(rgb);
				data[y][x][2] = Utils.getB(rgb);
			}
		
		btnShow = new JButton("顯示");
		btnNegative = new JButton("負片");
		btnGrayScale = new JButton("灰階");
		btnUpDown = new JButton ("上下反轉");
		btnLeftRight = new JButton ("左右反轉");
		btnRight90 = new JButton ("右旋90");
		btnLeft90 = new JButton ("左旋90");
		btnRight180 = new JButton ("右旋180");

		btnShow.addActionListener(buttonActionListener);
		btnNegative.addActionListener(buttonActionListener);
		btnGrayScale.addActionListener(buttonActionListener);
		btnUpDown.addActionListener(buttonActionListener);
		btnLeftRight.addActionListener(buttonActionListener);
		btnRight90.addActionListener(buttonActionListener);
		btnLeft90.addActionListener(buttonActionListener);
		btnRight180.addActionListener(buttonActionListener);
		
		cotrolPanel.add(btnShow);
		cotrolPanel.add(btnNegative);
		cotrolPanel.add(btnGrayScale);
		cotrolPanel.add(btnUpDown);
		cotrolPanel.add(btnLeftRight);
		cotrolPanel.add(btnRight90);
		cotrolPanel.add(btnLeft90);
		cotrolPanel.add(btnRight180);
		
		setLayout(new BorderLayout());	 
	    add(cotrolPanel, BorderLayout.PAGE_START);
	    add(imagePanel, BorderLayout.CENTER);
	}
	
	private void processNegative() {
		int [][][] fdata = new int [height][width][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fdata[i][j][0] = Utils.checkPixelBound(255-data[i][j][0]);
				fdata[i][j][1] = Utils.checkPixelBound(255-data[i][j][1]);
				fdata[i][j][2] = Utils.checkPixelBound(255-data[i][j][2]);
			}
		}
		imagePanel.showImage(width, height, fdata);
	}
	
	private void processGrayScale() {
		int [][][] fdata = new int [height][width][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int scale = Utils.checkPixelBound((int) (0.2126*data[i][j][0]+0.7152*data[i][j][1]+0.0722*data[i][j][2]));
				fdata[i][j][0] = scale;
				fdata[i][j][1] = scale;
				fdata[i][j][2] = scale;
			}
		}
		imagePanel.showImage(width, height, fdata);
	}
	
	private void processUpDown() {
		int [][][] fdata = new int [height][width][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fdata[height-i-1][j][0] = Utils.checkPixelBound(data[i][j][0]);
				fdata[height-i-1][j][1] = Utils.checkPixelBound(data[i][j][1]);
				fdata[height-i-1][j][2] = Utils.checkPixelBound(data[i][j][2]);
			}
		}
		imagePanel.showImage(width, height, fdata);
	}
	
	private void processLeftRight() {
		int [][][] fdata = new int [height][width][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fdata[i][width-j-1][0] = Utils.checkPixelBound(data[i][j][0]);
				fdata[i][width-j-1][1] = Utils.checkPixelBound(data[i][j][1]);
				fdata[i][width-j-1][2] = Utils.checkPixelBound(data[i][j][2]);
			}
		}
		imagePanel.showImage(width, height, fdata);
	}
	
	private void processRight90() {
		int [][][] fdata = imageRight90(data);
		imagePanel.showImage(fdata[0].length, fdata.length, fdata);
	}
	
	private void processLeft90() {
		int [][][] fdata = imageLeft90(data);
		imagePanel.showImage(fdata[0].length, fdata.length, fdata);
	}
	
	private void processRight180() {
		int [][][] fdata = imageRight90(imageRight90(data));
		imagePanel.showImage(fdata[0].length, fdata.length, fdata);
	}
	
	private int [][][] imageLeft90(int [][][] data) {
		int width = data[0].length, height = data.length;
		int [][][] fdata = new int [width][height][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fdata[width-j-1][i][0] = Utils.checkPixelBound(data[i][j][0]);
				fdata[width-j-1][i][1] = Utils.checkPixelBound(data[i][j][1]);
				fdata[width-j-1][i][2] = Utils.checkPixelBound(data[i][j][2]);
			}
		}
		return fdata;
	}
	
	private int [][][] imageRight90(int [][][] data) {
		int width = data[0].length, height = data.length;
		int [][][] fdata = new int [width][height][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fdata[j][height-i-1][0] = Utils.checkPixelBound(data[i][j][0]);
				fdata[j][height-i-1][1] = Utils.checkPixelBound(data[i][j][1]);
				fdata[j][height-i-1][2] = Utils.checkPixelBound(data[i][j][2]);
			}
		}
		return fdata;
	}
}
