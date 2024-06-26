# 💬 채팅방 기본 사항
---
### 1. 서버 연결 및 닉네임 설정
- 클라이언트는 12345 포트로 대기 중인 `ChatServer` 에 접속합니다. 
- 서버에 접속하면, 사용자는 닉네임을 입력받아 서버에 전송합니다. 
- 서버는 사용자의 닉네임을 받고 “OO 님이 입장하셨습니다.”라고 출력합니다. 
- 클라이언트가 접속하면 서버는 사용자의 IP 주소를 출력합니다.

### 2. 메시지 수신 및 발신
- 클라이언트는 닉네임을 입력한 후 채팅방에 입장합니다. 채팅방에 입장하지 않고 메시지 전송 시 서버는 “방에 입장해주세요.”라고 출력합니다.
- 사용자가 메시지를 입력하면 서버에 전송합니다.
- 사용자가 `“/bye”`를 입력하면 연결을 종료하고 프로그램을 종료합니다. 서버도 “OO 님이 연결을 끊었습니다.”를 출력하고 연결을 종료합니다.

# 💬 채팅방 관리 기능
---
### 1. 명령어 모음
- 서버는 클라이언트가 접속하면 아래 명령어들을 클라이언트에게 전송합니다.

  ```
  방 목록 보기 : /list
  방 생성 : /create
  방 입장 : /join [방번호]
  귓속말 : /whisper [닉네임] [메시지]
  방 나가기 : /exit
  접속종료 : /bye
  ```

### 2. 대화방 생성
- 클라이언트가 `“/create”`를 입력하면 서버는 새 방을 생성하고 클라이언트를 그 방으로 이동시킵니다.
- 방은 1부터 시작하는 번호로 관리되며, 생성 시 “방 번호 [방번호] 번이 생성되었습니다.”를 출력합니다.
- 클라이언트를 방으로 이동시킬 때 “OO 님이 방에 입장했습니다.”를 출력합니다.

### 3. 방 목록 보기
- `“/list”` 명령을 입력하면 서버는 생성된 모든 방의 목록을 출력합니다.
- 만약 채팅방이 하나도 없을 시 “현재 채팅방이 없습니다.”를 출력합니다.

### 4. 방 입장
- `“/join [방번호]”`를 통해 특정 방에 입장할 수 있습니다. 방에 입장하면, “OO 님이 방에 입장했습니다.”라는 메시지를 전달합니다.

### 4. 귓속말
- 클라이언트가 속한 채팅방 내에서 `“/whisper [닉네임] [메시지]”` 명령을 사용하여 특정 사용자에게만 메시지를 전송합니다.

### 5. 방 나가기
- 방에서 `“/exit”`를 입력하면, “OO 님이 방을 나갔습니다.”라는 메세지를 출력합니다.
- 방에 아무도 남지 않으면 해당 방을 삭제하고 “방 번호 [방번호] 번이 삭제되었습니다.”를 출력합니다.
