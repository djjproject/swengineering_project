package kr.or.kpew.kieas.alertsystem;

import java.applet.AudioClip;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.File;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.FileOutputStream;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.SpeechDataEvent;
import com.voicerss.tts.SpeechDataEventListener;
import com.voicerss.tts.SpeechErrorEvent;
import com.voicerss.tts.SpeechErrorEventListener;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.MessageSaver;
import kr.or.kpew.kieas.common.Papago;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.common.TrayNotification;

public class AlertSystemView implements Observer {
	private AlertSystemController controller;

	private JFrame frame;
	private Container alertPane;
	private GridBagConstraints gbc;
	private JTextArea alertArea;
	private JScrollPane alertAreaPane;
	private JPanel buttonPane;
	private JTabbedPane mainTabbedPane;

	private JTextField systemType;
	private AlertSystemProfile profile;

	private JTextArea target;

	// settings panel objects
	private Map<String, JRadioButton> nameToRadio;
	TrayNotification notification;

	// message panel
	private JTextArea messageTextArea;
	private JList messageList;
	private DefaultListModel messageListModel;
	Map<String,String> map;
	MessageSaver dictionary = new MessageSaver(); // dictionary 객체 생성 및 초기화



	public AlertSystemView(AlertSystemProfile profile) {
		this.profile = profile;
	}

	public void show() {
		frame.setVisible(true);
	}

	public void init() {
		initLookAndFeel();
		initFrame();
		notification = new TrayNotification();
		initMessageList();

	}

	public void setController(AlertSystemController controller) {
		this.controller = controller;
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private Box createRadio(String name) {
		Box box = Box.createHorizontalBox();
		JPanel hole = new JPanel();
		hole.setMinimumSize(new Dimension(400, 20));

		JLabel label = new JLabel(name, JLabel.CENTER);

		box.add(label);
		// 일단 수평 박스를 만들고 라디오 버튼을 생성한다.
		box.add(Box.createHorizontalGlue());

		JRadioButton trueRadio1;
		JRadioButton falseRadio1;

		if (name.equals("언어 설정")) {
			trueRadio1 = new JRadioButton("한국어");
			falseRadio1 = new JRadioButton("영어");
		}

		else {
			trueRadio1 = new JRadioButton("사용");
			falseRadio1 = new JRadioButton("사용 안함");
		}



		// 라디오 버튼 2개 생성

		// 라디오 버튼 그룹에 버튼 넣음 (한개만 선택이 됨)
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(trueRadio1);
		buttonGroup.add(falseRadio1);
		trueRadio1.setSelected(true);
		// 기본적으로 1:다 로 선택 되도록 설정

		box.add(trueRadio1);
		box.add(hole);
		box.add(Box.createHorizontalGlue());
		box.add(falseRadio1);
		box.add(hole);
		// 박스에 true false 추가함

		if (name.equals("언어 설정")) {
			nameToRadio.put("korean", trueRadio1);
			nameToRadio.put("english", falseRadio1);
		} else if (name.equals("소리 알림")){
			nameToRadio.put("sound", trueRadio1);
			nameToRadio.put("noSound", falseRadio1);
		} else if (name.equals("푸쉬 알림")) {
			nameToRadio.put("notification", trueRadio1);
			nameToRadio.put("noNotification", falseRadio1);
		} else if (name.equals("읽어 주기")) {
			nameToRadio.put("read", trueRadio1);
			nameToRadio.put("noRead", falseRadio1);
		}
		// HashMap 에 넣는다.
		return box;
	}

	private void initMessageList() {
		messageListModel.removeAllElements();
		dictionary.setWords(new TreeMap<>());
		try {
			File file = new File("MessageList.xml"); // 파일 경로 지정 및 파일 객체 생성
			// unmarshalling 을 위한 unmarshaller 생성 및 촉리화
			JAXBContext jaxbContext = JAXBContext.newInstance(MessageSaver.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// dictionary 객체를 생성 (파일로 부터)
			MessageSaver dic = (MessageSaver) jaxbUnmarshaller.unmarshal(file);

			// 생성된 객체 dic 에서 entry 들을 프린트하는 것
			for (Map.Entry<String, String> entry : dic.getWords().entrySet()) { // 꾸러미를 꺼내서 해라 , 외워라, 어떤 collection 같은것은 이런식의 iteration 도 가능
				dictionary.getWords().put(entry.getKey(),entry.getValue()); // 이미 있는 파일을 해쉬 맵에 넣는 작업 , 초기화 방지
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File file = new File("MessageList.xml"); // 파일 경로 지정 및 파일 객체 생성
			// unmarshalling 을 위한 unmarshaller 생성 및 촉리화
			JAXBContext jaxbContext = JAXBContext.newInstance(MessageSaver.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// dictionary 객체를 생성 (파일로 부터)
			MessageSaver dic = (MessageSaver) jaxbUnmarshaller.unmarshal(file);

			// 생성된 객체 dic 에서 entry 들을 프린트하는 것
			for (Map.Entry<String, String> entry : dic.getWords().entrySet()) { // 꾸러미를 꺼내서 해라 , 외워라, 어떤 collection 같은것은 이런식의 iteration 도 가능
				messageListModel.addElement(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void initFrame() {

		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(controller);


		this.gbc = new GridBagConstraints();
		initAlertPane();
		initButtonPane();

		this.mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("경보메시지", alertPane);

		//		JPanel projectPanel = createProjectPanel();
		//		mainTabbedPane.addTab("프로젝트", projectPanel);

		JPanel messagePanel = messagePanel();
		mainTabbedPane.addTab("메시지 로그",messagePanel);

		JPanel settingPanel = createSettingPanel();
		mainTabbedPane.addTab("설정",settingPanel);


		Container container = frame.getContentPane();
		container.add(mainTabbedPane);

		frame.setSize(400, 400);
		//		frame.setLocation(IntegratedAlertSystemMain.xLocation, IntegratedAlertSystemMain.yLocation);
		//		IntegratedAlertSystemMain.xLocation += IntegratedAlertSystemMain.xIncrement;
		//		IntegratedAlertSystemMain.yLocation += IntegratedAlertSystemMain.yIncrement;
		frame.setPreferredSize(new Dimension(512, 256));
	}

	private JPanel messagePanel () {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		messageList = new JList(new DefaultListModel());
		messageListModel = (DefaultListModel) messageList.getModel();
		messageTextArea = new JTextArea();


		messageList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg) {
				// TODO Auto-generated method stub

				messageTextArea.setText(null);
				messageTextArea.setLineWrap(true);
				try {
					File file = new File("MessageList.xml"); // 파일 경로 지정 및 파일 객체 생성
					// unmarshalling 을 위한 unmarshaller 생성 및 초기화
					JAXBContext jaxbContext = JAXBContext.newInstance(MessageSaver.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

					// dictionary 객체를 생성 (파일로 부터)
					MessageSaver dic = (MessageSaver) jaxbUnmarshaller.unmarshal(file);

					// 생성된 객체 dic 에서 entry 들을 프린트하는 것
					for (Map.Entry<String, String> entry : dic.getWords().entrySet()) { // 꾸러미를 꺼내서 해라 , 외워라, 어떤 collection 같은것은 이런식의 iteration 도 가능
						if (messageList.getSelectedValue().equals(entry.getKey())) { // 동일한 데이터가 나오면 area2 에 초기화
							messageTextArea.setText(entry.getValue());
							break; // 같은걸 찾았으니 끝냄
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		panel.add(new JScrollPane(messageList),"East");
		panel.add(messageTextArea,"West");


		return panel;

	}

	private JPanel createSettingPanel() {

		nameToRadio = new HashMap<>();


		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(400, 125));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder("설정"));

		panel.add(createRadio("언어 설정"));
		panel.add(createRadio("소리 알림"));
		panel.add(createRadio("푸쉬 알림"));
		panel.add(createRadio("읽어 주기"));

		return panel;

	}

	private void initAlertPane() {
		alertPane = new JPanel();
		alertPane.setLayout(new GridBagLayout());

		alertArea = new JTextArea();
		alertArea.setLineWrap(true);
		alertAreaPane = new JScrollPane(alertArea);

		alertArea.setText("");

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 1, 1, 1, 8);
		alertPane.add(alertAreaPane, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 1, 1, 1, 2);

	}

	private void initButtonPane() {
		this.buttonPane = new JPanel();

		systemType = new JTextField(15);
		systemType.setEnabled(false);
		systemType.setText("<경보시스템 종류>");

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(controller);
		buttonPane.add(clearButton, BorderLayout.WEST);

		buttonPane.add(systemType, BorderLayout.WEST);

		alertPane.add(buttonPane, gbc);
	}

	private void saveMessageList() {
		try {
			// init 초기화 메소드 참고
			// 아래 2줄은 그냥 갖다 쓰면 됨
			// marshalling 을 위한 marshaller 생성 및 초기화
			JAXBContext jaxbContext = JAXBContext.newInstance(MessageSaver.class);
			Marshaller marshaller = jaxbContext.createMarshaller();

			// xml 띄어쓰기 하도록 설정하는 부분 (포맷 설정)
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// marshaller.marshal(dictionary, System.out); // 생성된 XML 프린트
			marshaller.marshal(dictionary, new File("MessageList.xml")); // 저장
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}


	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}

	public void systemExit() {
		String question = "표준경보시스템 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";

		if (JOptionPane.showConfirmDialog(frame, question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			saveMessageList();
			System.exit(0);
		} else {
			System.out.println("AS: cancel exit program");
		}
	}

	//model의 notifyObservers(Object obj)에 의해 호출되는 함수. obj를 처리하면 된다.
	@Override
	public void update(Observable o, Object arg) {

		Papago translate = new Papago();



		if(arg instanceof String) {	
			//실제 view에서 하는 일
			IKieasMessageBuilder alert = new KieasMessageBuilder();
			alert.parse((String)arg);

			String translated = translate.getTranslate(alert.getDescription(0).toString().concat(alert.getInstruction(0).toString()), Papago.ko_to_en).replace("\\n", "\n");
			String original = alert.getDescription(0).toString().concat(alert.getInstruction(0).toString());


			//CAP 메시지를 이용하는 기능들 구현해보자





			if (nameToRadio.get("notification").isSelected()) {

				try {
					notification.displayTray(alert.getIdentifier().toString());
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}




			if (nameToRadio.get("korean").isSelected()) {
				alertArea.setText(original);
				dictionary.getWords().put(alert.getDate(),original);
				saveMessageList(); initMessageList();
			}
			else {
				alertArea.setText(translated);
				dictionary.getWords().put(alert.getDate(),translated);
				saveMessageList(); initMessageList();
			}

			if (nameToRadio.get("sound").isSelected()) {
				File sound = new File("media/sound.wav");
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(sound));
					clip.start();
					Thread.sleep(clip.getMicrosecondLength() / 1000);
				} catch (Exception e) {
					System.out.println("sound error");
				}
			}

			if (nameToRadio.get("read").isSelected()) {
				VoiceProvider tts = new VoiceProvider("94db255da50543b3b49e3337075be1a7");

				String lang="";
				String desc="";

				if (nameToRadio.get("korean").isSelected()) {
					lang = Languages.Korean;
					desc = original;
				}
				else if (nameToRadio.get("english").isSelected()) {
					lang = Languages.English_UnitedStates;
					desc = translated;
				}

				VoiceParameters params = new VoiceParameters(desc,lang);
				params.setCodec(AudioCodec.WAV);
				params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
				params.setBase64(false);
				params.setSSML(false);
				params.setRate(0);

				try {

					byte[] voice = tts.speech(params);

					FileOutputStream fos = new FileOutputStream("voice.mp3");
					fos.write(voice, 0, voice.length);
					fos.flush();
					fos.close();

				} catch (Exception e) {
					System.out.println("tts error");
				}

				File sound = new File("voice.mp3");
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(sound));
					clip.start();
					Thread.sleep(clip.getMicrosecondLength() / 1000);
				} catch (Exception e) {
					System.out.println("sound error");
				}
			}

			String url = alert.getWeb(0).toString();
			if (!url.equals("")) {
				try {
					Process oProcess = new ProcessBuilder("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe","--qt-minimal-view",url).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}




			//			StringBuffer sb = new StringBuffer();
			//			//1.다중언어
			//			String language = profile.getLanguage(); //수신기 언어
			//			for(int i = 0; i < alert.getInfoCount(); i++) {
			//				if(language.equals(alert.getLanguage(i))) {
			//					sb.setLength(0);
			//					sb.append("수신기 언어: ").append(language).append("\n").
			//					append("경보내용: ").append("\n").
			//					append(alert.getDescription(i));
			//					
			//					String text = sb.toString();
			//					target.setText(text);
			//					System.out.println(text);
			//				}
			//			}
			//			
			//			//2.지역/수신기맞춤
			//			String geo = profile.getAgency(); //수신기 지역명
			////			String geo = profile.getGeoCode(); 지역코드로도 가능
			//			for(int i = 0; i < alert.getAreaCount(0); i++) {
			//				if(geo.equals(alert.getAreaDesc(0, i))) {
			//					sb.setLength(0);
			//					sb.append("수신기 지역: ").append(geo).append("\n").
			//					append("경보내용: ").append("\n").
			//					append(alert.getDescription(i));
			//					
			//					String text = sb.toString();
			//					target.setText(text);
			//					System.out.println(text);
			//				}
			//			}
			//			
			//			AlertSystemType alertSystemType = profile.getType(); //수신기 유형
			//			for (String type : alert.getAddresses()) {
			//				if(type.equals(alertSystemType.getDescription())) {
			//					sb.setLength(0);
			//					sb.append("수신기 유형: ").append(type).append("\n").
			//					append("경보내용: ").append("\n").
			//					append(alert.getDescription(0));
			//					
			//					String text = sb.toString();
			//					target.setText(text);
			//					System.out.println(text);
			//
			//				}
			//			}
			//			
			//			//3.이미지/오디오 처리
			//			String capability = profile.getCapability();
			//			for(int i = 0; i < alert.getResourceCount(0); i++) {
			//				//alert.info.resource.mimeType 요소는 첨부된 리소스의 파일 형식을 정의한다.
			//				if(capability.equals(alert.getMimeType(0, i))) {
			//					sb.setLength(0);
			//					sb.append("수신기 표출기능: ").append(capability);
			//					
			//					String text = sb.toString();
			//					target.setText(text);
			//					System.out.println(text);
			//
			//					//이미지 or 오디오 처리
			//				}
			//			}
			//			
			//4.파일/DB 저장
			//파일 저장은 쉬움. jFileChooser 혹은 내가 구현한 kr.or.kpew.kieas.issuer.model.XmlReaderAndWriter 를 써보자.
			//http://blog.naver.com/PostView.nhn?blogId=cracker542&logNo=40119977325 jFileChooser는 여기 참조.
			//DB는 알아서 해보자. 파일 저장처럼 cap.xml 통으로 저장할 수 있으면 된다. (cap 요소별 컬럼으로 나눠서 저장할 필요 없다) 시간 부족으로 안해봄.

			//5.비디오 처리
			//자바를 이용한 video play 기능 구현
			//java media framework; jmf 라이브러리를 쓰면 된다더라. 시간 부족으로 안해봄.
			//http://newstars.tistory.com/34 여기 참조

			//6.자막 처리
			//xuggler 는 영상 인코딩/디코딩 하는 라이브러리 라더라.
			//http://www.xuggle.com/downloads 여기서 다운받아서 해보자.
			//이걸로 영상 위에 자막 올리는게 될 것 같다고 하더라
			//https://stackoverflow.com/questions/17811068/xuggler-can-we-write-text-on-video 스택오버플로 질문 참조

		}
		else if(arg instanceof AlertSystemProfile) {
			AlertSystemProfile profile = (AlertSystemProfile)arg;
			frame.setTitle(profile.getSender());
			systemType.setText(profile.getType().getDescription());
		}
	}

	public void clear() {
		alertArea.setText("");
	}
}
