package com.agiledge.atom.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

import com.agiledge.atom.service.SocketDeviceService;

public class SocketThread extends Thread {

	private ServerSocket serverSocket;
	private int port;
	private boolean running = false;

	public SocketThread(int port) {
		this.port = port;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(port);
			this.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				System.out.println("Listening for a connection");

				// Call accept() to receive the next connection
				Socket socket = serverSocket.accept();

				// Pass the socket to the RequestHandler thread for processing
				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class RequestHandler extends Thread {
		private Socket socket;

		RequestHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				System.out.println("Received a connection");

				// Get input and output streams
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String line = "";
				int c = 0;
				while ((c = in.read()) != -1) {
					char character = (char) c;
					try {
						if (character == '}') {
							String passdata = "";
							line += character;
							passdata = line;
							line = "";
							character = ' ';
							try {
								new SocketDeviceService().ProcessData(passdata);
							} finally {
								line = "";
								character = ' ';
								passdata = "";
							}

						} else
							line += character;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// if( line != null && line.length() > 0 )
				// {
				// // out.println( "Echo: " + line );
				// // out.flush();
				// //line = in.readLine();
				// System.out.println("data from server"+line);
				// new
				// SocketDeviceService().ProcessData("{KA02JE1266@1259.15861,N,07732.73867,E#}");
				// //data+=line;
				// //System.out.println( " seperate data"+line);
				// }
				// Close our connection
				in.close();
				// out.close();
				socket.close();
				// data=URLDecoder.decode(line);
				// int i=data.indexOf("#");
				// System.out.println("i"+i);
				// String data1=data.substring(data.indexOf(",")+1,
				// data.lastIndexOf("."));

				System.out.println("Connection closed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
