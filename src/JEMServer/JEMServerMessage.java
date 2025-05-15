package JEMServer;

/**********************************************
 * 
 * @author v068554
 * Object defines the message object which
 * defines the protocol used by the JEM client
 * and server.
 **********************************************/



public class JEMServerMessage {
	
	
	/*Message types		Format
	 * 1 = Login		1;user;password;null
	 * 2 = Message		2;fromUser;toUser;message
	 * 3 = KeepAlive    3;null;null;null
	 * 4 = List			4;null;null;user1.user2.user3.userN
	 * 5 = Register		5;user;password;null
	 * 6 = AdminMessage	6;null;null;message
	 * 7 = StatusChange 7;user;newStatus;null
	 * 8 = ChangePassword 8;user;password;null
	 * 9 = Register new user	9;user;password;userinfo
	 * 10 = Multichat message	10;fromUser;toUser;user1,user2..userN;message
	 * 11 = Invite multichat    11;fromUser;toUser;user1,user2..userN
	 * 12 = Leave multichat		12;fromUser;toUser;user1,user2..userN
	 * 13 = Add multichatuser	13;toUser;userToAdd;user1,user2..userN
	 * 14 = Set note			14;fromUser;note;null
	 * 16 = Search User			15;fromUser;null;firstName,lastName,nickName
	 * 17 = Add User			16;fromUser;userToAdd;null
	 * 18 = Remove User			17;fromUser;userToRemove;null
	 */
	
	private int typeOfMessage;
	private String header1;
	private String header2;
	private String messageBody;
	
	public JEMServerMessage(int TypeOfMessage, String Header1, String Header2, String MessageBody)
	{
		typeOfMessage = TypeOfMessage;
		header1 = Header1;
		header2 = Header2;
		messageBody = MessageBody;
	}

	public int getTypeOfMessage() {
		return typeOfMessage;
	}

	public String getHeader1() {
		return header1;
	}

	public String getHeader2() {
		return header2;
	}

	public String getMessageBody() {
		return messageBody;
	}

}
