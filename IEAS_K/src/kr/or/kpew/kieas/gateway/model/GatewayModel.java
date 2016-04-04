package kr.or.kpew.kieas.gateway.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.network.ITransmitter;

import com.google.publicalerts.cap.Alert.Status;

public class GatewayModel extends IntegratedEmergencyAlertSystem implements IOnMessageHandler
{
	private ITransmitter transmitter;
	private Profile profile;

	private List<IssuerProfile> issuers = new ArrayList<>();
	private List<AlertSystemProfile> alertsystems = new ArrayList<>();
	private Map<String, String> addresses = new HashMap<>();
	private List<Profile> receivers;
	
	private AckAggregator ackAggregator;

	
	public class Pair
	{
		public Type type;
		public Object object;

		public Pair(Type type, Object object)
		{
			this.type = type;
			this.object = object;
		}
	}

	public GatewayModel(ITransmitter transmitter, Profile profile)
	{
		super(profile);

		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);
	}

	/**
	 * View로 상태변화를 알릴 때, 상태의 종류를 알리기 위한 열거형
	 * 
	 * @author raychani
	 */
	public enum Type
	{
		Log, GatewayId, AlertMessage, RegisterIssuer, RegisterAlertSystem, Ack
	}

	public void init()
	{
		this.ackAggregator = new AckAggregator();
		transmitter.init(profile.getSender());
		setChangedAndNotify(Type.GatewayId, profile.getSender());
	}

	public void openGateway() {
		transmitter.init(profile.getSender());

		String log = "GW:" + " Open";
		System.out.println(log);
		notifyLog(log);
	}

	public void closeGateway() {
		transmitter.close();

		String log = "GW:" + " Close";
		System.out.println(log);

		notifyLog(log);
	}

	public void registerIssuer(IssuerProfile profile)
	{
		issuers.add(profile);
		setChangedAndNotify(Type.RegisterIssuer, profile);
	}

	public void registerAlertSystem(AlertSystemProfile profile)
	{
		alertsystems.add(profile);
		setChangedAndNotify(Type.RegisterAlertSystem, profile);
	}

	@Override
	public void onRegister(String sender, String address)
	{
		if (!addresses.containsKey(sender))
		{
			addresses.put(sender, address);
		}

	}

	/**
	 * 메시지를 수신하였을 때 처리하기 위한 메소드이다. <status>가 Actual은 항상 발령대라고 가정하고 System은
	 * 경보시스템으로 가정하였다.
	 */
	@Override
	public void onMessage(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		String sender = kieasMessageBuilder.getSender();

		switch (kieasMessageBuilder.getStatus())
		{
		case ACTUAL:
			onMessageFromIssuer(sender, message);
			break;
		case SYSTEM:
			onMessageFromAlertSystem(sender, message);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 발령대로부터 메시지를 받은 경우 처리하기 위한 메소드이다. 발령대로부터는 Actual/Alert 만 받는다고 가정하였다. 즉
	 * status=Actual인 경우 msgType=Alert로 가정하였다.
	 * 
	 * @param message
	 */
	public void onMessageFromIssuer(String senderId, String message)
	{
		KieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		Status status = kieasMessageBuilder.getStatus();
//		this.senderId = senderId;
		String log = "GW:" + " Received Message From Alerter (" + senderId + ") : ";
		System.out.println(log);
		notifyLog(log);
		
		switch (status)
		{
		// 경보메시지를 수신한 경우 각 경보시스템에 전달한다.
		case ACTUAL:
			onAlertMessage(senderId, message);
			break;
		default:
			System.out.println("GW: unknown message from " + kieasMessageBuilder.getSender());
			break;
		}
	}

	/**
	 * 이기종 경보시스템으로부터 메시지를 받은 경우 처리하기 위한 메소드이다. 경보시스템으로부터는 System/Ack 만 받는다고
	 * 가정하였다. 즉 status=System인 경우 msgType=Ack로 가정하였다.
	 * 
	 * @param message
	 */
	public void onMessageFromAlertSystem(String senderAddress, String message)
	{
		KieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		
		Status status = kieasMessageBuilder.getStatus();
		switch (status)
		{
		case SYSTEM:
			onAckMessage(senderAddress, message);
			break;
		default:
			System.out.println("GW: unknown message from " + kieasMessageBuilder.getSender());
			break;
		}
	}

	private void onAckMessage(String senderAddress, String message)
	{
		KieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		String log = "GW:" + " Received Ack From AlertSystem : " + kieasMessageBuilder.getSender();
		System.out.println(log);

		String references = kieasMessageBuilder.getReferences();
		String[] parsedReferences = kieasMessageBuilder.parseReferences(references);
		
		setChangedAndNotify(Type.Ack, parsedReferences[1]);
				String[] items = ackAggregator.checkAck(senderAddress, parsedReferences[1]);
		if(items != null && items.length > 0)
		{
			System.out.println("GW: Send Aggregated Ack");
			kieasMessageBuilder.parse(items[2]);
			sendAcknowledge(items[1], kieasMessageBuilder);
		}
	}

	protected void sendAcknowledge(String senderId, IKieasMessageBuilder receivedMessageBuilder)
	{		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		String ackMessage = receivedMessageBuilder.createAckMessage(createMessageId(), profile.getSender(), senderId);

		String log = "GW: Send Acknowledge to (" + senderId + ") : ";
		transmitter.sendTo(senderId, ackMessage);

		System.out.println(log);

		notifyLog(log);
	}

	/**
	 * 수신한 경보메시지를 현재 등록되어 있는 모든 경보시스템에 전달한다.
	 * 
	 * @param message
	 *            수신한 경보메시지
	 */
	private void onAlertMessage(String senderAddress, String message)
	{
		KieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		// 경보를 수신한 즉시 Ack를 전송한다.
		sendAcknowledge(senderAddress, kieasMessageBuilder);

		// 전달할 대상의 주소를 찾는다.
		this.receivers = routeMessage(kieasMessageBuilder);
		System.out.println("GW: routed alertsystems: " + this.receivers.size());
		ackAggregator.addProfileTracing(senderAddress, message, receivers);
		
		// 수신한 경보를 모든 경보시스템에 전달한다.
		// transmitter.broadcast(rawData);	
		String log = "GW: Send Message To AlertSystems";
		for (Profile target : receivers)
		{
			transmitter.sendTo(target.getSender(), message);
		}
		System.out.println(log);
		notifyLog(log);

		setChangedAndNotify(Type.AlertMessage, kieasMessageBuilder);
	}

	/**
	 * 전송 대상 경보시스템을 정하기 위한 메소드이다.
	 * 
	 * @param builder
	 *            수신한 경보메시지
	 * @return 전송 대상 경보시스템의 주소
	 */
	private List<Profile> routeMessage(IKieasMessageBuilder builder) {
		List<Profile> receivers = new ArrayList<>();

		for (AlertSystemProfile profile : alertsystems) {
			if (profile.getAddress() == null) {
				profile.setAddress(addresses.get(profile.getSender()));

			}

			switch (builder.getScope()) {
			case PUBLIC:
				receivers.add(profile);
				break;
			case RESTRICTED:
				if (builder.getRestriction().equalsIgnoreCase(profile.getType().name()))
					receivers.add(profile);
				break;
			case PRIVATE:
				if (builder.getAddresses().contains(profile.getSender()))
					receivers.add(profile);
				break;
			}
		}
		return receivers;

	}

	public void exit() {
		transmitter.close();
	}

	/**
	 * Observable의 setChanged를 자동으로 호출하고, Pair(Type, Object) 객체를 자동으로 생성하여
	 * notify 하기 위한 메소드이다.
	 * 
	 * @param type
	 *            알리려는 메시지의 종류
	 * @param object
	 *            알리려는 메시지
	 */
	public void setChangedAndNotify(Type type, Object object) {
		setChanged();
		notifyObservers(new Pair(type, object));
	}

	private void notifyLog(String log) {
		setChangedAndNotify(Type.Log, log);
	}

	public void setTransmitter(ITransmitter transmitter) {
		this.transmitter = transmitter;

	}

	@Override
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

}
