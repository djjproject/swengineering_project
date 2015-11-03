package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class AlerterCapGeneratePanel
{
	private static AlerterCapGeneratePanel alerterCapElementPanel;
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;

	private JScrollPane capGenerateScrollPanel;
	private JPanel capGeneratePanel;

	private JScrollPane textAreaPane;
	private JTextArea mTextArea;

	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;
	private JTextField mSaveTextField;
	private JTextField mLoadTextField;
	private HashMap<String, Component> panelComponenets;

	private JPanel alertPanel;
	private HashMap<String, Component> alertComponents;
	private JButton alertApplyButton;

	private JTabbedPane infoPanel;
	private ArrayList<JPanel> infoIndexPanels;
	private ArrayList<HashMap<String, Component>> infoComponents;
	private int infoCounter;

	public static final String TEXT_AREA = "TextArea";
	public static final String TEXT_FIELD = "TextField";
	public static final String COMBO_BOX = "ComboBox";

	public static final String LOAD_TEXT_FIELD = "LoadTextField";
	public static final String SAVE_TEXT_FIELD = "SaveTextField";

	public static final String IDENTIFIER = "Identifier";
	public static final String SENDER = "Sender";
	public static final String SENT = "Sent";
	public static final String STATUS = "Status";
	public static final String MSG_TYPE = "MsgType";
	public static final String SCOPE = "Scope";
	public static final String CODE = "Code";

	public static final String LANGUAGE = "Language";
	public static final String CATEGORY = "Category";
	public static final String EVENT = "Event";
	public static final String URGENCY = "Urgency";
	public static final String SEVERITY = "Severity";
	public static final String CERTAINTY = "Certainty";
	public static final String EVENT_CODE = "EventCode";
	public static final String EFFECTIVE = "Effective";
	public static final String SENDER_NAME = "SenderName";
	public static final String HEADLINE = "Headline";
	public static final String DESCRIPTION = "Description";
	public static final String WEB = "Web";
	public static final String CONTACT = "Contact";


	public static AlerterCapGeneratePanel getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterCapElementPanel == null)
		{
			alerterCapElementPanel = new AlerterCapGeneratePanel(alerterActionListener);
		}
		return alerterCapElementPanel;
	}

	private AlerterCapGeneratePanel(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		this.capGenerateScrollPanel = initFrame("alertViewPanel");			
	}

	private JScrollPane initFrame(String name)
	{		
		this.panelComponenets = new HashMap<>();
		
		this.capGeneratePanel = new JPanel();
		capGeneratePanel.setLayout(new BoxLayout(capGeneratePanel, BoxLayout.Y_AXIS));

		capGeneratePanel.add(initTextArea());
		capGeneratePanel.add(initButtonPanel());	
		capGeneratePanel.add(initCapAlertPanel());
		capGeneratePanel.add(initCapInfoPanel());

		return new JScrollPane(capGeneratePanel);
	}

	private Component initTextArea()
	{
		this.mTextArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(mTextArea);
		mTextArea.setEditable(false);
		panelComponenets.put("TextArea", mTextArea);
		textAreaPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return textAreaPane;
	}

	private Component initButtonPanel()
	{
		this.buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		Box loadBox = Box.createHorizontalBox();		
		this.loadCapDraftButton = createButton("LoadCapDraft");
		loadBox.add(loadCapDraftButton);		
		this.mLoadTextField = new JTextField();
		panelComponenets.put("LoadTextField", mLoadTextField);
		mLoadTextField.setText("cap/HRA.xml");
		loadBox.add(mLoadTextField);
		loadBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(loadBox);

		Box saveBox = Box.createHorizontalBox();
		this.saveCapButton = createButton("SaveCap");
		saveBox.add(saveCapButton);
		this.mSaveTextField = new JTextField();
		panelComponenets.put("SaveTextField", mSaveTextField);
		mSaveTextField.setText("cap/out.xml");
		saveBox.add(mSaveTextField);
		saveBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(saveBox);

		this.alertApplyButton = createButton("Apply");
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(alertApplyButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return buttonPane;
	}

	private JPanel initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
		alertPanel.setBorder(BorderFactory.createEtchedBorder());
		this.alertComponents = new HashMap<>();

		alertPanel.add(addBox(IDENTIFIER, TEXT_FIELD));
		alertPanel.add(addBox(SENDER, TEXT_FIELD));
		alertPanel.add(addBox(SENT, TEXT_FIELD));
		alertPanel.add(addBox(STATUS, COMBO_BOX));
		alertPanel.add(addBox(MSG_TYPE, COMBO_BOX));
		alertPanel.add(addBox(SCOPE, COMBO_BOX));
		alertPanel.add(addBox(CODE, TEXT_FIELD));
		alertPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return alertPanel;
	}

	private Component initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		this.infoIndexPanels = new ArrayList<JPanel>();
		this.infoComponents = new ArrayList<HashMap<String, Component>>();

		this.infoCounter = 0;
		addInfoIndexPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return infoPanel;
	}

	public void addInfoIndexPanel()
	{
		removeTabPanel();
		infoComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox(LANGUAGE, COMBO_BOX, infoCounter));
		panel.add(addBox(CATEGORY, COMBO_BOX, infoCounter));
		panel.add(addBox(EVENT, TEXT_FIELD, infoCounter));
		panel.add(addBox(URGENCY, COMBO_BOX, infoCounter));
		panel.add(addBox(SEVERITY, COMBO_BOX, infoCounter));
		panel.add(addBox(CERTAINTY, COMBO_BOX, infoCounter));
		panel.add(addBox(EVENT_CODE, COMBO_BOX, infoCounter));
		panel.add(addBox(EFFECTIVE, TEXT_FIELD, infoCounter));
		panel.add(addBox(SENDER_NAME, TEXT_FIELD, infoCounter));
		panel.add(addBox(HEADLINE, TEXT_FIELD, infoCounter));
		panel.add(addBox(DESCRIPTION, TEXT_AREA, infoCounter));
		panel.add(addBox(WEB, TEXT_FIELD, infoCounter));
		panel.add(addBox(CONTACT, TEXT_FIELD, infoCounter));

		
		infoIndexPanels.add(panel);
		infoPanel.addTab("Info" + infoCounter, infoIndexPanels.get(infoCounter));
		infoCounter++;
		addTabPanel();
	}
	
	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		return button;
	}

	private Box addBox(String labelName, String type)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int offset = (int) (100 - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);	

		switch (type)
		{
		case COMBO_BOX:
			JComboBox<String> comboBox = new JComboBox<>();
			for (String value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboBox.addItem(value);
			}
			alertComponents.put(labelName, comboBox);
			box.add(comboBox);
			return box;			
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			alertComponents.put(labelName, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			alertComponents.put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}

	private Box addBox(String labelName, String type, int index)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int offset = (int) (100 - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);

		switch (type)
		{
		case COMBO_BOX:
			JComboBox<String> comboBox = new JComboBox<>();
			for (String value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboBox.addItem(value);		
			}
			infoComponents.get(index).put(labelName + index, comboBox);
			box.add(comboBox);
			return box;
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			infoComponents.get(index).put(labelName + index, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			infoComponents.get(index).put(labelName + index, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}


	private void addTabPanel()
	{
		JPanel panel =  new JPanel();
		createAndAddInfoAddButton("Add Info", panel);
		infoPanel.addTab("+", panel);
		infoPanel.setSelectedIndex(infoCounter - 1);
	}

	private void removeTabPanel()
	{
		for(int i = 0 ; i < infoPanel.getComponentCount(); i++)
		{
			if(infoPanel.getTitleAt(i).equals("+"))
			{
				infoPanel.remove(infoPanel.getSelectedComponent());
			}
		}
	}

	private JButton createAndAddInfoAddButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(button);

		return button;
	}


	public JScrollPane getPanel()
	{
		return this.capGenerateScrollPanel;
	}

	public void applyAlertElement() {
		//		ieasMessage.setIdentifier(alertComponents.get(KIEAS_Constant.[0].getText());
		//		ieasMessage.setSender(alertValues[1].getText());
		//		ieasMessage.setSent(alertValues[2].getText());
		//		ieasMessage.setStatus(alertValues[3].getText());
		//		ieasMessage.setMsgType(alertValues[4].getText());
		//		ieasMessage.setScope(alertValues[5].getText());
		//		ieasMessage.setCode(alertValues[6].getText());
		//
		//		textArea.setText(ieasMessage.getMessage());
	}

//	private void setAlertValue(String target, String value)
//	{
//		if (alertComponents.get(target) instanceof JTextField)
//		{
//			((JTextField) alertComponents.get(target)).setText(value);
//			return;
//		}
//		if (alertComponents.get(target) instanceof JComboBox<?>)
//		{
//			((JComboBox<?>) alertComponents.get(target)).setSelectedItem(value);
//			return;
//		}
//	}

	
	public void updateView(String target, String value) {

		if(target.equals(TEXT_AREA))
		{
			this.mTextArea.setText(value);
			return;
		}
		if(target.equals(LOAD_TEXT_FIELD))
		{
			this.mLoadTextField.setText(value);
			return;
		}
		if(target.equals(SAVE_TEXT_FIELD))
		{
			this.mSaveTextField.setText(value);
			return;
		}
		if (alertComponents.get(target) instanceof JTextField)
		{
			((JTextField) alertComponents.get(target)).setText(value);
			return;
		}
		if (alertComponents.get(target) instanceof JComboBox<?>)
		{
			((JComboBox<?>) alertComponents.get(target)).setSelectedItem(value);
			return;
		}
		for(int i = 0; i < infoCounter; i++)
		{
			if (infoComponents.get(i).get(target) instanceof JTextField)
			{
				((JTextField) infoComponents.get(i).get(target)).setText(value);
				return;
			}
			if (infoComponents.get(i).get(target) instanceof JComboBox<?>)
			{
				((JComboBox<?>) infoComponents.get(i).get(target)).setSelectedItem(value);
				return;
			}
			if (infoComponents.get(i).get(target) instanceof JTextArea)
			{
				((JTextArea) infoComponents.get(i).get(target)).setText(value);
				return;
			}
		}
	}
}

