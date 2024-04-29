package ChatProj;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(12345);
        ) {
            Map<Integer, List<Member>> rooms = new HashMap<>();

            while (true) {
                Socket socket = serverSocket.accept();

                //클라이언트가 접속하면 서버는 사용자의 IP 주소를 출력합니다.
                System.out.println("Client IP: " + socket.getInetAddress());
                

                //쓰레드 실행
                new ServerThread(socket, rooms).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
