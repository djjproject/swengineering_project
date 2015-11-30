package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;


public class _AlerterTopView
{
	private static _AlerterTopView alerterTopView;
	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	private AlerterLogPanel alerterLogPanel;
	private AlerterCapGeneratePanel alerterCapGeneratePanel;
	private AlerterDatabasePanel alerterDatabasePanel;
	private AlerterAlertGeneratePanel alerterAlertGeneratePanel;
	private AleterViewActionListener alerterActionListener;

	
	public static _AlerterTopView getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterTopView == null)
		{
			alerterTopView = new _AlerterTopView(alerterActionListener);
		}
		return alerterTopView;
	}

	/**
	 * Main Frame과 각 포함되는 View Component 초기화.
	 * @param alerterActionListener 이벤트 리스너
	 */
	private _AlerterTopView(AleterViewActionListener alerterActionListener)
	{
		initLookAndFeel();
		this.alerterActionListener = alerterActionListener;
		this.alerterAlertGeneratePanel = AlerterAlertGeneratePanel.getInstance(alerterActionListener);
		this.alerterCapGeneratePanel = AlerterCapGeneratePanel.getInstance(alerterActionListener);
		this.alerterDatabasePanel = AlerterDatabasePanel.getInstance(alerterActionListener);

		initFrame();
	}

	private void initFrame()
	{
		this.mainFrame = new JFrame();
		mainFrame.setSize(1024, 768);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(alerterActionListener);

		this.mainTabbedPane = new JTabbedPane();
		Container container = mainFrame.getContentPane();
		container.add(mainTabbedPane);

		mainTabbedPane.addTab("AlertGenerate", alerterAlertGeneratePanel.getPanel());
		this.alerterLogPanel = AlerterLogPanel.getInstance(alerterActionListener);
		mainTabbedPane.addTab("경보로그", alerterLogPanel.getLogPanel());
		
		mainTabbedPane.addTab("CAP", alerterCapGeneratePanel.getPanel());
		mainTabbedPane.addTab("Database", alerterDatabasePanel.getPanel());		

		mainFrame.setVisible(true);
	}

	private void initLookAndFeel() {
		try 
		{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} 
		catch (UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
	}

	public void receiveGatewayAck(String identifier)
	{
		alerterLogPanel.receiveGatewayAck(identifier);
	}

	public void receiveAlertSystemAck(String identifier)
	{
		alerterLogPanel.receiveAlertSystemAck(identifier);		
	}

	public void addAlertTableRow(String id, String event, String addresses)
	{
		alerterLogPanel.addAlertTableRow(id, event, addresses);		
	}

	public void applyAlertElement()
	{
		//alerterCapGeneratePanel.applyAlertElement();
	}

	public void selectTableEvent()
	{
		alerterDatabasePanel.selectTableEvent();
	}

	public void getQueryResult(ArrayList<String> results)
	{
		System.out.println("topView getQueryResult");
		alerterDatabasePanel.getQueryResult(results);
	}

	public String getQuery()
	{
		return alerterDatabasePanel.getQuery();
	}

	public void addInfoIndexPanel()
	{
		alerterCapGeneratePanel.addInfoIndexPanel();
	}

	public void addResourceIndexPanel()
	{
		alerterCapGeneratePanel.addResourceIndexPanel();
	}
	
	public void addAreaIndexPanel() {
		alerterCapGeneratePanel.addAreaIndexPanel();
	}	
	
	/**
	 * Model의 데이터 값이 바뀌었을 경우 View 갱신을 위해 Model에 의해 호출된다.
	 * 
	 * @param view 갱신되어야 하는 View 클래스 이름
	 * @param target 값이 표시되는 Component의 이름
	 * @param value 표시되는 값
	 */
	public void updateView(String view, String target, String value)
	{
		switch (view)
		{
		case "AlerterCapGeneratePanel":
			alerterCapGeneratePanel.updateView(target, value);
			break;
		default:
			System.out.println("there is no such a view " + view);
			break;
		}
	}
	
	public void updateView(String view, String target, ArrayList<String> value)
	{
		switch (view)
		{
		case "AlerterDatabasePanel":
			alerterDatabasePanel.getQueryResult(value);
			break;

		default:
			System.out.println("there is no such a view " + view);
			break;
		}
	}

	public String getLoadTextField()
	{
		return alerterCapGeneratePanel.getLoadTextField();
	}

	public String getSaveTextField()
	{
		return alerterCapGeneratePanel.getSaveTextField();
	}

	public void setId(String name) 
	{
		this.mainFrame.setTitle(name);
	}

	public Component getFrame() 
	{
		return this.mainFrame;
	}

	public String getGeoCode()
	{		
		return alerterLogPanel.getGeoCode();
	}

	public String getAlertSystemType() 
	{
		return alerterLogPanel.getAlertSystemType();
	}

	public void setTextArea(String message)
	{
		alerterLogPanel.setTextArea(message);
	}
}