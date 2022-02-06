/* Reference : https://github.com/vbohush/SortingAlgorithmAnimations
작성자 : 김민지
선택된 Sort를 실행할 때 효과음을 실행하는 클래스
코드 마지막 작성날짜 : 2021년 05월 28일
*/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private Clip clip;
	private File audioFile;
	private AudioInputStream audioInputStream;
	private boolean isLoop;

	public Sound(String pathName, boolean isLoop) {
		try {
			clip = AudioSystem.getClip();
			audioFile = new File(pathName);
			audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		clip.setFramePosition(0);
		clip.start();
		if (isLoop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}
}
