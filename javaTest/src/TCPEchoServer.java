

import java.net.*;
import java.text.MessageFormat;
import java.io.*;

public class TCPEchoServer {
	private static final int BUFSIZE = 32;

	public static void main(String[] args) throws IOException {
		// TODOAuto-generatedmethodstub
		// 从控制台获取需要监听的端口号
		if (args.length != 1)
			throw new IllegalArgumentException("Parameter(s):<Port>");
		// 获取端口号
		int servPort = Integer.parseInt(args[0]);
		// 实例化一个ServerSocket对象实例
		ServerSocket servSocket = new ServerSocket(servPort);
		System.out.println(MessageFormat.format("开始启动监听，端口号:{0}", args[0]));
		// 初始接收数据的总字节数
		int recvMsgSize;
		// 接收数据的缓冲区
		byte[] receiveBuf = new byte[BUFSIZE];
		// 循环迭代，监听端口号,处理新的连接请求
		while (true) {
			// 阻塞等待，每接收到一个请求就创建一个新的连接实例
			Socket clntSocket = servSocket.accept();
			// 获取连接的客户端的SocketAddress
			SocketAddress clientAddress = clntSocket.getRemoteSocketAddress();
			// 打印输出连接客户端地址信息
			System.out.println("Handling client at" + clientAddress);
			// 从客户端接收数据的对象
			InputStream in = clntSocket.getInputStream();
			// 向客户端发送数据的对象
			OutputStream out = clntSocket.getOutputStream();
			// 读取客户端发送的数据后，再发送到客户端
			while ((recvMsgSize = in.read(receiveBuf)) != -1) {
				out.write(receiveBuf, 0, recvMsgSize);
			}
			// 客户端关闭连接时，关闭连接
			System.out.println("客户端关闭连接");
			clntSocket.close();
		}
	}
}