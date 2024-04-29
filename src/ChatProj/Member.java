package ChatProj;

import java.io.PrintWriter;
import java.net.Socket;

public class Member {
    public Socket socket;
    String nickname;
    PrintWriter out;


    public Member(Socket socket, String nickname) {
        this.socket = socket;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
