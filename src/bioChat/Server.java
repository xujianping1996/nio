package bioChat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    static volatile Vector<Socket> sockets = new Vector<Socket>();
    public static void main (String args[]){
       startServer();
    }

    public static void startServer(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7890);
            System.out.println("服务器已启动");
            while (true){
                getCon(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static class ConnetThread implements Runnable {
        Socket socket = null;
        public ConnetThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
                String loginMesg = socket.getInetAddress().toString() +":进入聊天室--------";
                sockets.add(socket);
                System.out.println(loginMesg);
                byte [] bytes = loginMesg.getBytes();
                int length = bytes.length;
                forwardData(socket,bytes,length);
                acceptData(socket);
        }
    }
    /**
     * 获取链接
     */
    public static void getCon(ServerSocket serverSocket){
        Socket socket1 = null;
        try {
            socket1 = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new ConnetThread(socket1)).start();
    }
    public static void forwardData(Socket socket, byte[] data, int len){

                for (Socket currentSocket:sockets){
                    if (currentSocket.isConnected()){
                        if (!currentSocket.equals(socket)){
                        OutputStream outputStream = null;
                        try {
                            outputStream = currentSocket.getOutputStream();
                            outputStream.write(data,0,len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        }
                    }
                }


    }
    public static byte[] acceptData(Socket socket){
        while (true){
            byte [] bt = new byte[1024];
            InputStream inputStream = null;
            int len = 0;
            try {
                inputStream = socket.getInputStream();
                len = inputStream.read(bt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            forwardData(socket,bt,len);
        }
    }
}
