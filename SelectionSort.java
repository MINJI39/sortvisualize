import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import java.io.FileOutputStream;

import javax.swing.JPanel;

public class SelectionSort extends JPanel {

	private static final long serialVersionUID = 1L;
	
	int[] list;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	protected static final int BORDER_WIDTH = 10;
	
	private static int size = 100;
	
	int columnWidth = (getWidth() - 4 * BORDER_WIDTH) / 100;
	int columnHeight = (getHeight() - 4 * BORDER_WIDTH) / 100;
	
	String [] split = new String[size];
	
	private int redColumn = -1; //가장 작은 수
	private int blueColumn = -1; //비교 중인 값
	private int grayColumn = -1; // 정렬이 완료된 값
	
	private Image offScreenImage;
	private Graphics offScreen;
	private Image img;
	private Thread th;
	private int x, y;
	private boolean usedbuffer = true;
	
	public SelectionSort() {
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
		redColumn = -1;
		blueColumn = -1;
		grayColumn = -1;	
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void sort() {
		// TODO Auto-generated method stub
		try {						
			for (int i = 0; i < list.length ; i++) {		
				int currentMinIndex = i;
				
				redColumn = currentMinIndex;
	
				for (int j = i + 1; j < list.length; j++) {
					blueColumn = j;
					
					count++;
					
					paint(getGraphics());
	
					Thread.sleep(10); //절대 바꾸지마
					if (list[currentMinIndex] > list[j]) {
						currentMinIndex = j;
						
						redColumn = currentMinIndex;

						sound = new Sound("src/sound/ddiring.wav", false);
						sound.start();				
					}
				}

				if (currentMinIndex != i) {
					swap(list, currentMinIndex, i);
				}
				grayColumn++;
				paint(getGraphics());
			
				sound = new Sound("src/sound/bboop.wav", false);
				sound.start();
			}
			grayColumn++;
			redColumn = -1;
			blueColumn = -1;
	
			FileOutputStream output = new FileOutputStream("C:\\output.txt");
			for(int i = 0 ; i < list.length-1 ; i++) {
				split[i] = Integer.toString(list[i]);
			
				String str = split[i] + " ";
				
				output.write(str.getBytes());
			}
			output.close();
			
		} catch (InterruptedException e) {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}	
	
	private void swap(int[] list, int i, int j) {
		int temp = list[i];
		list[i] = list[j];
		list[j] = temp;
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
		} else { //일반적인 방식
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
	    		
			  Font font = new Font("함초롬돋움", Font.PLAIN, columnWidth+4);
			  g.setFont(font);
			  g.setColor(Color.BLACK);
			  g.drawString("비교 횟수 : " + Integer.toString(count), 2 * BORDER_WIDTH, getHeight() - 50);
	    		
			  for(int i = 0 ; i < list.length ; i++) {
				  if(list[i] > 0 && grayColumn <= i) {	
					  g.setColor(Color.BLACK);
					  g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * i, getHeight() - list[i] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[i]+1) * columnHeight);			
				  }	
			  }
	    		
			  Font fontbasic = new Font("", Font.PLAIN, columnWidth-2);
			  Font fontbig = new Font("", Font.PLAIN, columnWidth);
	    		
			  for(int i = 0 ; i < list.length ; i++) {
				  if(list[i] == 100) {
					  Font font100 = new Font("", Font.PLAIN, columnWidth-4);
					  g.setColor(Color.WHITE);
					  g.setFont(font100);
					  g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i -1,  getHeight() - 2 * BORDER_WIDTH-100);
				  } else if(list[i] >= 1 && list[i] < 10) {	
					  g.setColor(Color.WHITE);
					  g.setFont(fontbig);
					  g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i + 2,  getHeight() - 2 * BORDER_WIDTH-100);
				  }	else {
					  g.setColor(Color.WHITE);
					  g.setFont(fontbasic);
					  g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i + 1,  getHeight() - 2 * BORDER_WIDTH-100);
				  }
			  }
	    		
			  if(redColumn != -1) {	
				  g.setColor(Color.RED);
				  g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * redColumn, getHeight() - list[redColumn] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[redColumn]+1) * columnHeight);
				  		  
				  g.setColor(Color.WHITE);
				  g.setFont(fontbasic);
				  g.drawString(Integer.toString(list[redColumn]), 2 * BORDER_WIDTH + (columnWidth+1) * redColumn + 1,  getHeight() - 2 * BORDER_WIDTH-100);
			  }
	    		
			  if(blueColumn != -1) {
				  g.setColor(Color.BLUE);
				  g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * blueColumn, getHeight() - list[blueColumn] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[blueColumn]+1) * columnHeight);
			  
				  g.setColor(Color.WHITE);
				  g.setFont(fontbasic);
				  g.drawString(Integer.toString(list[blueColumn]), 2 * BORDER_WIDTH + (columnWidth+1) * blueColumn + 1,  getHeight() - 2 * BORDER_WIDTH-100);
			  }
	    		
			  if(grayColumn < size) {
				  for (int i=0 ; i <= grayColumn ; i++) {
					  g.setColor(Color.GRAY);
					  g.fillRect(2 * BORDER_WIDTH + (columnWidth+1) * i, getHeight() - list[i] * columnHeight - 2 * BORDER_WIDTH-100, columnWidth, (list[i]+1) * columnHeight);			
	
					  g.setColor(Color.BLACK);
					  g.setFont(fontbasic);
					  g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth+1) * i,  getHeight() - 2 * BORDER_WIDTH-100);
				  } 
			  }
		  } else {}
	}
}

