/* COPYING/LICENSEReference : https://github.com/vbohush/SortingAlgorithmAnimations
작성자 : 김민지
프로그램의 화면을 보여주는 클래스
코드 마지막 작성날짜 : 2021년 05월 31일
*/

import javax.swing.*;
import java.awt.Color;

import java.awt.Container;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.*;
import java.io.BufferedReader;

import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.util.Random;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame {
	SelectionSort selection = new SelectionSort();
	BubbleSort bubble = new BubbleSort();
	InsertionSort insertion = new InsertionSort();
	MergeSort merge = new MergeSort();
	QuickSort quick = new QuickSort();

	protected static final int BORDER_WIDTH = 10;

	private static final long serialVersionUID = 1L;

	JFileChooser fc, fc2;
	JButton btn = new JButton("파일선택");

	FileOutputStream input;

	File[] f;
	String path, path2;

	public static int[] list;
	public static int[] tmp;
	int[] listfirst;

	String[] split;

	private static int size = 100;
	protected int sleepTime = 2;

	public Main() {
		super("정렬 프로그램");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);

		JLabel lblmethod = new JLabel("정렬방식");
		lblmethod.setBounds(30, 0, 60, 40);
		buttonPanel.add(lblmethod);

		JRadioButton rb1 = new JRadioButton("선택정렬");
		JRadioButton rb2 = new JRadioButton("버블정렬");
		JRadioButton rb3 = new JRadioButton("삽입정렬");
		JRadioButton rb4 = new JRadioButton("병합정렬");
		JRadioButton rb5 = new JRadioButton("퀵정렬");

		rb1.setBounds(100, 0, 90, 40);
		rb2.setBounds(190, 0, 90, 40);
		rb3.setBounds(280, 0, 90, 40);
		rb4.setBounds(370, 0, 90, 40);
		rb5.setBounds(460, 0, 90, 40);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);
		bg.add(rb4);
		bg.add(rb5);
		buttonPanel.add(rb1);
		buttonPanel.add(rb2);
		buttonPanel.add(rb3);
		buttonPanel.add(rb4);
		buttonPanel.add(rb5);

		JLabel lblfile = new JLabel("입력파일");
		lblfile.setBounds(30, 45, 50, 30);
		buttonPanel.add(lblfile);

		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		btn.setBounds(100, 45, 110, 30);

		buttonPanel.add(btn);

		try {
			// 파일 쓰기
			input = new FileOutputStream("C:\\input.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e1) {
			System.out.println(e1);
		}

		// 입력파일 파일선택 버튼
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				FileNameExtensionFilter filter = new FileNameExtensionFilter("텍스트", "txt");
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(btn);
				if (result == JFileChooser.APPROVE_OPTION) {
					// File[] f = fc.getSelectedFiles();
					f = fc.getSelectedFiles();

					for (File n : f)
						btn.setText(n.getName());
				}

				try {
					Container contentPane = getContentPane();

					// 파일 읽기
					path = fc.getSelectedFile().getAbsolutePath();
					File file = new File(path);
					BufferedReader in = new BufferedReader(new FileReader(file));
					String s;

					while ((s = in.readLine()) == null) {
						int list[] = new int[size];
						for (int i = 0; i < list.length; i++) {
							list[i] = i + 1;
						}

						Random rd = new Random();
						String[] split = new String[list.length];
						for (int i = 0; i < list.length; i++) {
							list[i] = rd.nextInt(100) + 1;
							split[i] = Integer.toString(list[i]);
							String str = split[i] + "  ";

							input.write(str.getBytes());
						}
						input.close();

						if (rb1.isSelected()) {
							selection.setScores(list);
							contentPane.add(selection);
						}

						if (rb2.isSelected()) {
							bubble.setScores(list);
							contentPane.add(bubble);
						}

						if (rb3.isSelected()) {
							insertion.setScores(list);
							contentPane.add(insertion);
						}

						if (rb4.isSelected()) {
							merge.setScores(list);
							contentPane.add(merge);
						}

						if (rb5.isSelected()) {
							quick.setScores(list);
							contentPane.add(quick);
						}
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IndexOutOfBoundsException e1) {
					System.out.println(e1);
				}
			}
		});

		JLabel lblfile2 = new JLabel("출력파일");
		lblfile2.setBounds(30, 80, 50, 30);
		buttonPanel.add(lblfile2);

		fc2 = new JFileChooser();
		fc2.setMultiSelectionEnabled(true);
		JButton btn2 = new JButton("파일선택");
		btn2.setBounds(100, 80, 110, 30);

		buttonPanel.add(btn2);

		// 출력파일 파일선택 버튼
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (btn2.getText() == "output.txt") {
					JOptionPane.showMessageDialog(null, "C:에서 output.txt 파일을 확인하세요");
				}
			}
		});

		RoundedButton reset = new RoundedButton("RESET");
		reset.setBounds(520, 60, 110, 30);
		// reset.setEnabled(false);
		buttonPanel.add(reset);

		// 리셋 버튼
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 닫고 다시 하기
				dispose();
				new Main();
			}
		});

		RoundedButton btn3 = new RoundedButton("정렬 시작");
		btn3.setBounds(400, 60, 110, 30);
		btn3.setBackground(new Color(255, 255, 255));
		// btn3.addActionListener(this); //수정 필요
		buttonPanel.add(btn3);

		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				reset.setEnabled(true);

				if (rb1.isSelected()) {// 선택정렬
					try {
						selection.sort();

						btn2.setText("output.txt");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error 발생");
					}

				}

				if (rb2.isSelected()) { // 버블정렬
					try {
						bubble.sort();

						btn2.setText("output.txt");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error 발생");
					}
				}

				if (rb3.isSelected()) { // 삽입정렬
					try {
						// 삽입정렬 알고리즘
						insertion.sort();

						btn2.setText("output.txt");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error 발생");
					}
				}

				if (rb4.isSelected()) { // 병합정렬
					try {
						// 병합정렬 알고리즘
						merge.sort();

						btn2.setText("output.txt");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error 발생");
					}
				}

				if (rb5.isSelected()) {
					try {
						// 퀵 정렬 알고리즘
						quick.sort();

						btn2.setText("output.txt");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error 발생");
					}
				}
			}
		});
		add(buttonPanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width - 10, screenSize.height - 10);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String args[]) {
		new Main();
	}
}