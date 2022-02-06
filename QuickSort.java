/* Reference : https://github.com/vbohush/SortingAlgorithmAnimations
작성자 : 김민지
QuickSort를 선택했을 때 visualize 시켜 보여주는 클래스 
코드 마지막 작성날짜 : 2021년 05월 31일
*/

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JPanel;

public class QuickSort extends JPanel {
	private static final long serialVersionUID = 1L;

	protected static final int BORDER_WIDTH = 10;
	private static int size = 100;
	protected int sleepTime = 2;

	String[] split = new String[size];

	int[] list;

	private int redColumn = -1; // 기준원소

	private Image offScreenImage;
	private Graphics offScreen;
	private Image img;
	private Thread th;

	private int x, y;
	private boolean usedbuffer = true;

	public QuickSort() {
		x = 0;
		y = 120;

		MediaTracker tracker = new MediaTracker(this);
		img = Toolkit.getDefaultToolkit().getImage("car1.gif");
		tracker.addImage(img, 0);
		try {
			// MediaTracker을 사용하여 이미지가 완전히 로딩될때까지 대기
			tracker.waitForAll();
		} catch (InterruptedException e) {
		}

		// Thread생성
		th = new Thread();
		th.start();
	}

	int count;

	private Sound sound;

	void setScores(int[] list) {
		reset();
		this.list = list;
	}

	private void reset() {
		// TODO Auto-generated method stub
		redColumn = -1;
	}

	public void sort() {
		quicksort(list);

		redColumn = -1;
		paint(getGraphics());
	}

	public void quicksort(int[] list) {
		pivotsort(list, 0, list.length - 1);
	}

	private void pivotsort(int[] list, int p, int r) {
		if (p >= r) {
			return;
		}
		int pivot = partition(list, p, r);

		pivotsort(list, p, pivot - 1);
		pivotsort(list, pivot + 1, r);

	}

	private int partition(int[] list, int start, int end) {
		int p = start;
		int r = end;
		int pivot = list[end]; // 기준원소 : 맨 뒤 원소

		redColumn = end;
		paint(getGraphics());

		sound = new Sound("src/sound/bboop.wav", false);
		sound.start();

		while (p < r) {
			while (list[p] < pivot && p < r) {
				p++;

				count++;

				paint(getGraphics());
			}
			while (list[r] >= pivot && p < r) {
				r--;
			}
			swap(list, p, r);
		}
		swap(list, end, r);

		try {
			FileOutputStream output = new FileOutputStream("C:\\output.txt");
			for (int i = 0; i < list.length - 1; i++) {
				split[i] = Integer.toString(list[i]);

				String str = split[i] + " ";

				output.write(str.getBytes());
			}

			output.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	private void swap(int[] list, int i, int j) {
		int temp = list[i];
		list[i] = list[j];
		list[j] = temp;

		if (i == redColumn) {
			redColumn = j;
			paint(getGraphics());

		} else if (j == redColumn) {
			redColumn = i;
			paint(getGraphics());
		}
		i++;
		j--;
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

		// 더블버퍼링 사용
		if (offScreen != null) {
			paintingjob(offScreen, w, h);
			g.drawImage(offScreenImage, 0, 0, this);
		}
		// 일반적인 방식으로 출력
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
			Font font = new Font("함초롬돋움", Font.PLAIN, columnWidth + 4);
			g.setFont(font);
			g.drawString("비교 횟수 : " + Integer.toString(count), 2 * BORDER_WIDTH, getHeight() - 50);

			for (int i = 0; i < list.length; i++) {
				if (list[i] > 0) {
					g.setColor(Color.BLACK);
					g.fillRect(2 * BORDER_WIDTH + (columnWidth + 1) * i,
							getHeight() - list[i] * columnHeight - 2 * BORDER_WIDTH - 100, columnWidth,
							(list[i] + 1) * columnHeight);
				}
			}

			Font fontbasic = new Font("", Font.PLAIN, columnWidth - 2);

			for (int i = 0; i < list.length; i++) {
				g.setColor(Color.WHITE);
				g.setFont(fontbasic);
				g.drawString(Integer.toString(list[i]), 2 * BORDER_WIDTH + (columnWidth + 1) * i + 1,
						getHeight() - 2 * BORDER_WIDTH - 100);
			}

			if (redColumn != -1) {
				g.setColor(Color.RED);
				g.fillRect(2 * BORDER_WIDTH + (columnWidth + 1) * redColumn,
						getHeight() - list[redColumn] * columnHeight - 2 * BORDER_WIDTH - 100, columnWidth,
						(list[redColumn] + 1) * columnHeight);

				g.setColor(Color.WHITE);
				g.setFont(fontbasic);
				g.drawString(Integer.toString(list[redColumn]), 2 * BORDER_WIDTH + (columnWidth + 1) * redColumn + 1,
						getHeight() - 2 * BORDER_WIDTH - 100);
			}
		}
	}
}
