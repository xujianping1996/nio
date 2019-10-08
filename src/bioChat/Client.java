package bioChat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket socket = null;

    public static void main(String args[]){

        try {
            socket = new Socket("127.0.0.1",7890);
            writeData(socket);
            readData(socket);
        } catch (IOException e) {

        }
    }

    public static void writeData(Socket socket){
        new Thread(new WriteDataThread(socket)).start();
    }
    public static void readData(Socket socket){
        new Thread(new ReadDataThread(socket)).start();
    }
    static class ReadDataThread implements Runnable {
        private Socket socket = null;
        public ReadDataThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            while (true){
                try {
                    byte[] bt = new byte[1024];
                    InputStream inputStream = socket.getInputStream();
                    int len = inputStream.read(bt);
                    System.out.println("_____"+new String(bt,0,len));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class WriteDataThread implements Runnable {
        private Socket socket = null;
        public WriteDataThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                OutputStream outputStream = socket.getOutputStream();
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String str = scanner.next();
                    byte [] bt = str.getBytes();
                    int len = bt.length;
                    outputStream.write(bt,0,len);
                }
            } catch (IOException e) {

            }
        }
    }
}
