import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JPanel;

public class MergeSort extends JPanel{
	private static final long serialVersionUID = 1L;

	protected static final int BORDER_WIDTH = 10;
	private static int size = 100;
		
	String [] split = new String[size];
	
	int [] list;
	
	private int grayColumnStart = -1;
	private int grayColumnEnd = -1;
	
	private Image offScreenImage;
	private Graphics offScreen;
	private Image img;
	private Thread th;

	private int x, y;
	private boolean usedbuffer = true;
	
	public MergeSort() {
		x = 0; y = 120;
		
		MediaTracker tracker = new MediaTracker(this);
        img = Toolkit.getDefaultToolkit().getImage("car1.gif");
        tracker.addImage(img, 0);
        try {
            // MediaTracker을 사용하여 이미지가 완전히 로딩될때까지 대기
            tracker.waitForAll();
        } catch (InterruptedException e) {}

        //Thread생성
        th = new Thread();
        th.start();
	}
	
	int count = 0;
	
	private Sound sound;
	
	void setScores(int[] list) {
		reset();
		this.list = list;
	}
	
	private void reset() {
		// TODO Auto-generated method stub
		grayColumnStart = -1;
		grayColumnEnd = -1;	
	}
	
	public void sort() {
		try {
			mergeSort(0, list.length - 1);
			grayColumnStart = 0;
			grayColumnEnd = size - 1;
		} catch (InterruptedException e) {}
		paint(getGraphics());
	}
	public void mergeSort(int start, int end) throws InterruptedException {
		if (start < end) {
			int mid = (start+end) / 2;
			mergeSort(start, mid);
			mergeSort(mid + 1, end);
			merge(start, mid, mid + 1, end);
		}
	}

	public void merge(int start1, int end1, int start2, int end2) throws InterruptedException {
		int[] list1 = new int[end1 - start1 + 1];
		int[] list2 = new int[end2 - start2 + 1];
		int[] tmp = new int[list1.length + list2.length];
		
		System.arraycopy(list, start1, list1, 0, list1.length);
		System.arraycopy(list, start2, list2, 0, list2.length);

		paint(getGraphics());
		
	    int current1 = 0; int current2 = 0; int current3 = 0;

		while (current1 < list1.length && current2 < list2.length) {

			paint(getGraphics());
			if (list1[current1] < list2[current2]) {
				tmp[current3++] = list1[current1++];
			} else {
				tmp[current3++] = list2[current2++];
	
			}

			paint(getGraphics());
		}

		while (current1 < list1.length) {
			tmp[current3++] = list1[current1++];
	
			paint(getGraphics());
		}

		while (current2 < list2.length) {
			tmp[current3++] = list2[current2++];

			paint(getGraphics());
		}
		grayColumnStart = start1;
		
		for (int i = 0; i < tmp.length; i++) {
			grayColumnEnd = start1 + i;
			list[start1 + i] =  tmp[i];
			paint(getGraphics());
			
			count++;
			
			sound = new Sound("src/sound/bboop.wav", false);
			sound.start();
		}
		grayColumnStart = -1;
		grayColumnEnd = -1;
		
		//파일에 입력
		FileOutputStream output;
		try {
			output = new FileOutputStream("C:\\output.txt");
			
			for(int i = 0 ; i < list.length-1 ; i++) {
				split[i] = Integer.toString(list[i]);
			
				String str = split[i] + " ";
				
				output.write(str.getBytes());
			}
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	public void paint(Graphics g) {
		int w = this.getSize().width;
		int h = this.getSize().height;
	        
		if (offScreen == null && usedbuffer) {
			try {
				offScreenImage = createImage(w, h);
				offScreen = offScreenImage.getGraphics();
			} catch (Exception e) {
				offScreen = null;
			}
		}
	        
		//더블버퍼링 사용
		if (offScreen != null) {
			paintingjob(offScreen, w, h);
			g.drawImage(offScreenImage, 0, 0, this);
		}
		//일반적인 방식으로 출력
		else {
			paintingjob(g, w, h);
		}		
	}
	
	private void paintingjob(Graphics g, int w, int h) {
		// TODO Auto-generated method stub
		g.clearRect(0, 0, w, h);

		g.drawImage(img, x, y, this);
		if (usedbuffer) {        	
			int columnWidth = (getWidth() - 9 * BORDER_WIDTH) / size;
			int columnHeight = (getHeight() - 20 * BORDER_WIDTH) / size;
	    		
			g.setColor(Color.BLACK);
			Font font = new Font("함초롬돋움", Font.PLAIN, columnWidth+4);
			g.setFont(font);
			g.drawString("비교 횟수 : " + Integer.toString(count), 2 * BORDER_WIDTH, getHeight() - 50);
	    	   		
			for(int i = 0 ; i < list.length ; i++) {
				if(list[i] > 0) {
					g.setColor(Color.BLACK);
					g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * i, getHeight() - list[i] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[i]+1) * columnHeight);
				}	
			}
			
			Font fontbasic = new Font("", Font.PLAIN, columnWidth-2);
			for(int i = 0 ; i < list.length ; i++) {
				g.setColor(Color.PINK);
				g.setFont(fontbasic);
				g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i + 1,  getHeight() - 2 * BORDER_WIDTH-100);
			}
			
			if((grayColumnStart != -1) && (grayColumnEnd != -1)) {
				for (int i = grayColumnStart; i <= grayColumnEnd; i++) {
					g.setColor(Color.GRAY);
					g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * i, getHeight() - list[i] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[i]+1) * columnHeight);
				
					g.setColor(Color.WHITE);
					g.setFont(fontbasic);
					g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i + 1,  getHeight() - 2 * BORDER_WIDTH-100);
				}
			}	
		}
	}
}
