package Client;

import java.io.*;
import java.net.Socket;

public class ChatManage {
    private ChatManage() {}
    private static final ChatManage instance = new ChatManage();

    public static ChatManage getChatManage() { return instance; }

    Window win;
    Socket sock;
    BufferedReader reader;
    BufferedWriter writer;

    public void setWindow(Window win) {
        this.win = win;
    }

    public void connect(String ip) {
       new Thread() {
           @Override
           public void run() {
               try {
                   sock = new Socket(ip, 5000);
                   reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                   writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

                   String line = null;
                   while ((line = reader.readLine()) != null) {
                       win.appendText(sock.getInetAddress() + line);
                   }
                   writer.close();
                   reader.close();
                   writer = null;
                   reader = null;
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }.start();
    }

    public void sendMessage(String message) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Remote implements Runnable {

        @Override
        public void run() {
            try {
                String line = null;
                System.out.println("get");
                while ((line = reader.readLine()) != null) {
                    win.appendText("\n" + sock.getInetAddress() + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
