package multiplayer.datapacks;

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class ClientAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	String IP = "0.0.0.0", name = "";
	PrintWriter writer;
	Socket client;

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ClientAttributes(String IP, String name) {

		this.IP = IP;
		this.name = name;
	}

	public ClientAttributes(PrintWriter writer, Socket client) {
		this.writer = writer;
		this.client = client;
		this.IP = client.getInetAddress().getHostAddress();
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return ("{NAME=" + getName() + "}{IP=" + getIP() + "}{SOCKET=" + client + "}{WRITER=" + writer + "}");

	}
}