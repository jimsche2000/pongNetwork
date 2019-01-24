package multiplayer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

import gui.JTextFieldCharLimit;
import gui.MenuButton;
import gui.MenuTextField;
import hauptmenu.PongFrame;

/*
 * Something like a chat
 * 
 * jtextarea + jtextfield + sendbutton
 * 
 * Should display all player-chat-messages, messages like "player joined/leaved the game" and Messages like "Don't Spam!"
 * 
 * Should be able to process Commands
 * 
 */
public class ClientChat extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textAreaMessages;
	private MenuTextField textFieldMessage;
	private MenuButton button_SendMessage;
	private JScrollPane scrollPaneMessages;

	// actual timestamp to wait 0.5sec
	static long last_timestamp = System.currentTimeMillis() - 500;

	final public int LEVEL_INFO = 2;
	final public int LEVEL_ERROR = 1;
	final public int LEVEL_NORMAL = 0;
	private PongFrame pongFrame;
	
	private int maxStringLength;

	public ClientChat(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		float ar = pongFrame.getASPECT_RATIO();
		if(ar >= 1.0f)maxStringLength=73;
		else if(ar<0.8f)maxStringLength = 69;
		else if(ar<1.0f)maxStringLength = 70;
//		maxStringLength  = 46 + Math.round(20 * pongFrame.getASPECT_RATIO()); // 1.334 for 2560x1440-> 72,6
//		maxStringLength  = 53 + Math.round(20 * pongFrame.getASPECT_RATIO()); // 1.0 for 1920x1080 	-> 73
//		maxStringLength  = 52 + Math.round(20 * pongFrame.getASPECT_RATIO()); // 0.875 for 1680x1050-> 69,5
//		maxStringLength  = 55 + Math.round(20 * pongFrame.getASPECT_RATIO()); // 0.711 1366x768		-> 69,22
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setOpaque(false);
		setPreferredSize(new Dimension(Math.round(900 * pongFrame.getASPECT_RATIO()),
				Math.round(675 * pongFrame.getASPECT_RATIO())));

		textAreaMessages = new JTextArea();
		textAreaMessages.setEditable(false);
		textAreaMessages.setOpaque(false);
		textAreaMessages.setForeground(Color.white);
		textAreaMessages.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f * pongFrame.getASPECT_RATIO()));

		// Scrollbalken zur textArea hinzufügen
		scrollPaneMessages = new JScrollPane(textAreaMessages);
		scrollPaneMessages.setOpaque(false);
		scrollPaneMessages.setBorder(null);
//		scrollPaneMessages.getViewport().setBorder(null);
		scrollPaneMessages.getViewport().setOpaque(false);
		scrollPaneMessages.setPreferredSize(new Dimension(Math.round(800 * pongFrame.getASPECT_RATIO()),
				Math.round(550 * pongFrame.getASPECT_RATIO())));
		scrollPaneMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPaneMessages, BorderLayout.CENTER);

		JPanel textFieldButtonPanel = new JPanel(); // 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		textFieldButtonPanel.setOpaque(false);
		textFieldButtonPanel.setLayout(new FlowLayout());
		textFieldButtonPanel.setPreferredSize(new Dimension(Math.round(900 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));

		textFieldMessage = new MenuTextField(pongFrame, "");
		textFieldMessage.setSize(new Dimension(Math.round(700 * pongFrame.getASPECT_RATIO()),
				Math.round(45 * pongFrame.getASPECT_RATIO())));
		textFieldMessage.setFont(pongFrame.getGLOBAL_FONT().deriveFont(14f * pongFrame.getASPECT_RATIO()));
		textFieldMessage.setDocument(new JTextFieldCharLimit(100));
		textFieldMessage.setOpaque(true);
		textFieldMessage.addKeyListener(new SendPressEnterListener());

		button_SendMessage = new MenuButton(pongFrame, "Senden");
		button_SendMessage.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f * pongFrame.getASPECT_RATIO()));
		button_SendMessage.setSize(new Dimension(Math.round(100 * pongFrame.getASPECT_RATIO()),
				Math.round(45 * pongFrame.getASPECT_RATIO())));
		button_SendMessage.addActionListener(new SendButtonListener());

		textFieldButtonPanel.add(textFieldMessage, FlowLayout.LEFT);
		textFieldButtonPanel.add(button_SendMessage);
		contentPane.add(textFieldButtonPanel, BorderLayout.PAGE_END);
		setOpaque(false);
		this.add(BorderLayout.CENTER, contentPane);
	}

	public void appendTextToChat(String message, int level) {
		System.out.println("APPEND TEXT TO CLIENT CHAT");
		if (level == LEVEL_ERROR) {
			textAreaMessages.append("[Error]: " + message + "\n");
			append("[Error]: " + message);

		} else if (level == LEVEL_INFO) {
//			textAreaMessages.append(message + "\n");
			append(message);

		} else { // LEVEL_NORMAL
			try {

//				textAreaMessages.append(message + "\n");
				append(message);
				textFieldMessage.setText("");

			} catch (Exception e) {
				appendTextToChat(e.getMessage(), LEVEL_ERROR);
			}
		}

		textAreaMessages.setCaretPosition(textAreaMessages.getText().length());
		textFieldMessage.requestFocus();
	}
	
//	public void setMaxTextLength(String name) {
//
//		textFieldMessage.setDocument(new JTextFieldCharLimit(100 - name.length()));
//	}
	
	private void append(String message) {
		System.out.println("MSG: {[\""+message+"\"]}");
		int start = 0;
		int end = maxStringLength;
		System.out.println("message length: "+message.length());
		
//		FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(textAreaMessages.getFont());
		
//		int width = fm.stringWidth(message);
//		int sWidth = Utilities.getTabbedTextWidth(new Segment(message.toCharArray(), 0, message.length()), 
//				getFontMetrics(getFont()), 0, 
//				(PlainView) textAreaMessages.getUI().getRootView(this).getView(0), 0); 
//		while(width > textAreaMessages.getPreferredSize().width) {
		while(message.length() > maxStringLength) {
			System.out.println("Appending message: \""+message.substring(start, maxStringLength)+"\"");
			textAreaMessages.append(message.substring(start, maxStringLength)+"\n");
			System.out.println("index: "+end+" length: "+message.length());
			message = message.substring(end);
//			width = fm.stringWidth(message);
//			start += maxStringLength;
//			end += maxStringLength;
		}
		if(!(message.length() > maxStringLength)) {
			textAreaMessages.append(message+"\n");
			System.out.println("Appending last message: \""+message+"\"");
		}
	}

	public boolean checkMessage(long timestamp, String message) {
		boolean isOkay = true;

		if (message.length() < 2 || message.length() > 100) {
			appendTextToChat("MESSAGE_RULES: 2-100 CHARACTER", LEVEL_ERROR);
			isOkay = false;
		}
		if (timestamp > System.currentTimeMillis() - 500) {

			appendTextToChat("!!!DO NOT SPAM!!!", LEVEL_ERROR);
			isOkay = false;
		} else {
			last_timestamp = System.currentTimeMillis();
		}
		return isOkay;
	}

	// Listener
	public class SendPressEnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				String message = textFieldMessage.getText();
				if (checkMessage(last_timestamp, message)) {
					pongFrame.getClientThread().executeCommand(message);
					textFieldMessage.setText("");
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	public class SendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			pongFrame.getClientThread().executeCommand(textFieldMessage.getText());
			textFieldMessage.setText("");
		}
	}

	public void clear() {
		textAreaMessages.setText("");
	}
}