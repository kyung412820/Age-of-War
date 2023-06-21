<br>


# [Age-of-War] 플래시 게임 전쟁시대을 모티프로 만든 온라인 게임

<br>

<h2>목차</h2>

 - [소개](#소개) 
 - [팀원](#팀원) 
 - [개발 환경](#개발-환경)
 - [사용 기술](#사용-기술)
 - [핵심 기능](#핵심-기능)
   - [네트워크 프로토콜](#네트워크프로토콜)
   - [게임](#게임)
 - [Trouble Shooting](#trouble-shooting)


## 소개

**Age of War**는 프로토콜 형식의 게임, 게임 클라이언트 애플리케이션 입니다. 유명한 플래시 게임 전쟁시대를 모티브로 제작하였으며 JFrame을 기반으로 생성되었습니다. <br>

## 팀원

<table>
   <tr>
    <td align="center"><b><a href="https://github.com/kyung412820">이경훈</a></b></td>
  <tr>
    <td align="center"><a href="https://github.com/kyung412820"><img src="https://avatars.githubusercontent.com/u/71320521?v=4" width="100px" /></a></td>
  </tr>
  <tr>
    <td align="center"><b>프로젝트 총괄</b></td>
</table>


## 개발 환경

 - Windows
 - Eclipse
 - GitHub



## 사용 기술 

- Library & Framework : java.awt, java.io, java.net, javax
- Language : Javascript


## 핵심 기능

### 네트워크 프로토콜

- 객체 스트림(ObjectStream)을 사용하여 서버와 통신하는 기능을 구현했습니다.

- JFrame 클래스를 이용 GUI 창을 생성, 상속하여 구현합니다.

- JTextPane 컴포넌트를 사용하여 채팅 내용을 출력하고, JTextField 컴포넌트를 사용하여 메시지를 입력할 수 있습니다. Send 버튼을 클릭하거나 엔터 키를 입력하면 입력한 메시지를 서버로 전송합니다.


-  이미지 공유를 위해 JButton 컴포넌트인 imgBtn이 있으며, 이를 클릭하면 이미지 파일을 선택할 수 있는 파일 대화상자(FileDialog)가 나타납니다. 선택한 이미지는 ChatMsg 객체에 담겨 서버로 전송됩니다.

- ListenNetwork 클래스는 서버로부터 오는 메시지를 수신하고 처리하는 스레드로 동작합니다. 



### 게임

- JFrame을 이용한 GUI패널을 이용하여 만든 게임입니다.

- 자동으로 생성되는 코인을 지불하여 캐릭터를 소환 적들을 소멸시키는 것이 게임의 진행 방식입니다. 캐릭터는 서로 충돌하여 더 강력한 몹이 어느 쪽인지 파악한 후 자동으로 약한 몬스터는 소멸 처리 됩니다.

  - Math.abs를 사용하여 충돌 계산을 진행했고, 값은 사진의 가로, 세로값을 기준으로 좌표 이동을 계산하여 구현하였습니다.

- 키보드와 마우스의 조작을 기반으로 진행되며, 음성과 이미지를 기반으로 상태를 파악하도록 구성했습니다.


## Trouble Shooting

- 서버에 데이터를 전송하는 과정에서 오류가 자주 나타났습니다.
  - 단순히 네트워크로 연결하는 프로토콜에서 문제가 일어나 그 부분을 올바른 네트워크로 수정하여 구현했습니다.

- 네트워크 자제에는 문제가 없었지만 이미지의 전송이 진행이 안되었습니다.
  - 이미지에 프로토콜을 할당하지 않았던 문제라 쉽게 해결이 가능했습니다.
