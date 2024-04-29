package ChatProj;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(12345);
        ) {
            //List // 모든 연결 클라이언트 소켓 목록
            //List<Socket> socketlist = new ArrayList<>();
            //Map<String, List<Member>> rooms // id 와 방 멤버 정보(socket, nickname)
            Map<Integer, List<Member>> rooms = new HashMap<>();

            while (true) {
                Socket socket = serverSocket.accept();

                //클라이언트가 접속하면 서버는 사용자의 IP 주소를 출력합니다.
                System.out.println("Client IP: " + socket.getInetAddress());

                //socketlist.add(socket);

                //쓰레드 실행
                new ServerThread(socket, rooms).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
