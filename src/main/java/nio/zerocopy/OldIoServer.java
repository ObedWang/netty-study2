package nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : web
 * @date : 2021/7/2
 */
public class OldIoServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(7001);
        while(true){
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] buffer = new byte[4096];
            int readCount;
            while((readCount=dataInputStream.read(buffer,0,buffer.length))>=0){

            }
            System.out.println("获取文件完成");
        }
    }
}
