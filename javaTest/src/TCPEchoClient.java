
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
public class TCPEchoClient{
	public static void main(String[] args) throws IOException{
		// TODOAuto-generatedmethodstub
		// 判断从控制台接受的参数是否正确
		if((args.length<2)||(args.length>3))
			throw new IllegalArgumentException("Parameter(s):<Server><Word>[<Port>]]");
		// 获取服务器地址
		String server=args[0];
		// 获取需要发送的信息
		byte[] data=args[1].getBytes();
		// 如果有三个从参数那么就获取发送信息的端口号，默认端口号为8099
		int servPort=(args.length==3)?Integer.parseInt(args[2]):8099;
		// 根据服务器地址和端口号实例化一个Socket实例
		Socket socket=new Socket(server,servPort);
		System.out.println("Connected to server...sending echo string =="+args[1]);
		// 返回此套接字的输入流，即从服务器接受的数据对象
		InputStream in=socket.getInputStream();
		// 返回此套接字的输出流，即向服务器发送的数据对象
		OutputStream out=socket.getOutputStream();
		// 向服务器发送从控制台接收的数据
		out.write(data);
		// 接收数据的计数器，将写入数据的初始偏移量
		int totalBytesRcvd=0;
		// 初始化接收数据的总字节数
		int bytesRcvd;
		while(totalBytesRcvd<data.length){
			// 服务器关闭连接，则返回-1,read方法返回接收数据的总字节数
			if((bytesRcvd=in.read(data,totalBytesRcvd,data.length-totalBytesRcvd))==-1)
				throw new SocketException("与服务器的连接已关闭");
			totalBytesRcvd+=bytesRcvd;
		}
		// 打印服务器发送来的数据
		System.out.println("Received:"+new String(data));
		// 关闭连接
		socket.close();
		}
	}