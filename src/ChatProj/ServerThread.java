package ChatProj;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket;
    private String nickname;
    private Map<Integer, List<Member>> rooms;
    private PrintWriter out;
    private BufferedReader in;

    private Integer roomNumber;

    public ServerThread(Socket socket, Map<Integer, List<Member>> rooms) {
        this.socket = socket;
        this.rooms = rooms;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 서버는 사용자의 닉네임을 받고 "OOO 닉네임의 사용자가 연결했습니다."라고 출력합니다.
            nickname = in.readLine();
            out.println(nickname + " 님이 입장하였습니다.");

            // 서버는 클라이언트가 접속하면 아래 명령어들을 클라이언트에게 전송합니다.
            out.println("방 목록 보기 : /list\n" +
                    "방 생성 : /create\n" +
                    "방 입장 : /join [방번호]\n" +
                    "귓속말 : /whisper [닉네임] [메시지]\n" +
                    "방 나가기 : /exit\n" +
                    "접속종료 : /bye");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String msg = null;
        try {
            while ((msg = in.readLine()) != null) {

                if ("/bye".equalsIgnoreCase(msg)) { // 사용자가 "/bye"를 입력하면 연결을 종료하고 프로그램을 종료합니다.
                    break;
                }

                // 대화방 생성
                else if ("/create".equalsIgnoreCase(msg)) {
                    createRoom(nickname);
                }

                // 방 목록 보기
                else if ("/list".equalsIgnoreCase(msg)) {
                    printRoomList();
                }

                // 방 입장
                else if (msg.indexOf("/join") == 0) {
                    enterRoom(msg, nickname);

                // 방 나가기
                } else if ("/exit".equalsIgnoreCase(msg)) {
                    exitRoom(nickname);

                // 귓속말
                } else if (msg.startsWith("/whisper")) {
                    sendWhisper(msg);

                } else {
                    // 방 번호가 없으면 메시지 전송x
                    if (roomNumber != null) {
                        broadCast(roomNumber, nickname + ": " + msg);
                    } else {
                        out.println("방에 입장해주세요.");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(nickname + " 님이 연결을 끊었습니다.");

            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 대화방 멤버에게 메시지를 전송하는 메서드
    public void broadCast(Integer roomNumber, String msg) {
        List<Member> membersInRoom = rooms.get(roomNumber);
        if (membersInRoom != null) {
            for (Member member : membersInRoom) {
                try {
                    PrintWriter pw = new PrintWriter(member.socket.getOutputStream(), true);
                    pw.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 귓속말 메서드: /whisper [닉네임] [메시지]
    public void sendWhisper(String msg) {
        String[] str = msg.split(" ");
        String recipient = str[1];

        String whisperMessage = msg.substring(msg.indexOf(str[2]));

        for (List<Member> memberList : rooms.values()) {
            for (Member member : memberList) {
                if (member.getNickname().equals(recipient)) {
                    try {
                        PrintWriter recipientOut = new PrintWriter(member.socket.getOutputStream(), true);
                        recipientOut.println("[귓속말] " + nickname + "님으로부터: " + whisperMessage);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // 대화방 생성 메서드
    public void createRoom(String nickname) {
        Integer newRoomNumber = rooms.isEmpty() ? 1 : Collections.max(rooms.keySet()) + 1;

        roomNumber = newRoomNumber;

        List<Member> members = new ArrayList<>();
        members.add(new Member(socket, nickname));
        rooms.put(roomNumber, members);

        broadCast(roomNumber,  "방 번호 [" + roomNumber + "] 번이 생성되었습니다.");
        broadCast(roomNumber, nickname + "님이 방에 입장했습니다.");
    }

    // 방 목록 출력 메서드
    public void printRoomList() {
        if (rooms.isEmpty()) {
            try {
                out.println("현재 채팅방이 없습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (Integer key : rooms.keySet()) {
                try {
                    out.println(key + "번 방이 있습니다.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 방 입장 메서드: /join [방번호]
    public void enterRoom(String msg, String nickname) {
        String[] str = msg.split(" ");
        String roomNumberString = str[1];
//        if (roomNumberString.startsWith("[") && roomNumberString.endsWith("]")) {
//            roomNumberString = roomNumberString.substring(1, roomNumberString.length() - 1);
//        }
        roomNumber = Integer.parseInt(roomNumberString);

        List<Member> membersInRoom = rooms.get(roomNumber);
        if (membersInRoom == null) {
            membersInRoom = new ArrayList<>();
            rooms.put(roomNumber, membersInRoom);
        }
        membersInRoom.add(new Member(socket, nickname));

        broadCast(roomNumber, nickname + "님이 방에 입장했습니다.");
    }

    // 방 나가기 메서드
    public void exitRoom(String nickname) {
        if (roomNumber != null) {
            List<Member> members = rooms.get(roomNumber);
            if (members != null) {
                members.removeIf(member -> member.getNickname().equals(nickname));
                broadCast(roomNumber, nickname + "님이 방을 나갔습니다.");
                if (members.isEmpty()) {
                    rooms.remove(roomNumber); // 방 삭제
                    out.println("방 번호 [" + roomNumber + "] 번이 삭제되었습니다.");
                }
            }
            roomNumber = null; // 방을 나갔으므로 현재 방 번호를 null로 설정
        }
    }
}
