import javax.swing.*;
import java.io.*; // 파일 클래스 사용을 위한 임포트 추가
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import JavaGameClientView.ListenNetwork;

class game extends JFrame implements KeyListener, Runnable{
   int a=0;
   int f_width ;
   int f_height ;
   int Score; //  점수 
   int player_Hitpoint; // 체력
   int Enemy_Hitpoint=10000;
   int e_w, e_w2, e_w3; //적 이미지의 크기값을 받을 변수
   int m_w, m_w2, m_w3;
   int hp_s1=1000; //나의 히트포인트
   int hp_d1=5000; //적의 히트포인트
   int coin=1000; // 코인
   int end=1; // 스킬 한계치
   int x, y, x1, y1,x11,y11,x0,y0,x00,y00; //유닛의 x,y좌표 1=인간 0=악마 
   
   private Socket socket; // 연결소켓
   private JTextField txtInput;
   private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String UserName;
	private String GammerName="Gammer : ";
	public String msg;
	public String enemysummon;
	public String bugcoin;
	public String bugwin;
	
   boolean q = false;
   boolean w = false;
   boolean e1 = false;
   boolean c = false;
   boolean space = false;
   boolean z = false;
   //유닛,스킬 소환키 설정
   
   Random ran = new Random(); // 랜덤함수
   
   int cnt; //각종 타이밍 조절을 위해 무한 루프를 카운터할 변수

   Thread th;
    
   Toolkit tk = Toolkit.getDefaultToolkit();
   Image me_img;
   Image me_img2;
   Image me_img3;
   Image me2_img;
   Image me2_img2;
   Image me2_img3;
   Image me3_img;
   Image me3_img2;
   Image me3_img3;
   Image Missile_img;
   Image Enemy_img1; // 이미지를 받아들일 이미지 변수
   Image Enemy_img2; 
   Image Enemy_img3; 
   Image Enemy2_img1;
   Image Enemy2_img2; 
   Image Enemy2_img3;
   Image Enemy3_img1;
   Image Enemy3_img2; 
   Image Enemy3_img3;
   Image Enemy3_img4;
   Image Enemy3_img5;
   Image Background_img;
   Image Skill_img;
   ArrayList me_List = new ArrayList();
   ArrayList me2_List = new ArrayList();
   ArrayList me3_List = new ArrayList();
   ArrayList Enemy_List = new ArrayList();
   ArrayList Enemy2_List = new ArrayList();
   ArrayList Enemy3_List = new ArrayList();
   //다수의 적을 등장 시켜야 하므로 배열을 이용.

   Image buffImage; 
   Graphics buffg;


   Enemy en; //에너미 클래스 접근 키
   Enemy2 en2;
   Enemy3 en3;
   me men;
   me2 men2;
   me3 men3;
////////////////////////////////////////////////////////////////////////
   game(String username, String ip_addr, String port_no){
    init();
    start();
    Container c = getContentPane();
	UserName=GammerName.concat(username);
    c.setLayout(new FlowLayout(FlowLayout.CENTER,180,20));
    setTitle("War Game");
    setSize(f_width, f_height);
     
    Dimension screen = tk.getScreenSize();

    int f_xpos = (int)(screen.getWidth() / 2 - f_width / 2);
    int f_ypos = (int)(screen.getHeight() / 2 - f_height / 2);

    setLocation(f_xpos, f_ypos);
    setResizable(false);
    setVisible(true);
    try {// 유저 정보 전송
		socket = new Socket(ip_addr, Integer.parseInt(port_no));
//		is = socket.getInputStream();
//		dis = new DataInputStream(is);
//		os = socket.getOutputStream();
//		dos = new DataOutputStream(os);

		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());

		// SendMessage("/login " + UserName);
		ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
		SendObject(obcm);
		
		ListenNetwork net = new ListenNetwork();
		
	}catch (NumberFormatException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
   }
   public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			
		}
	}
   public void init(){ 
   x = 100;
   y = 100;
   x1=-200;
   y1=-200;// 유닛의 초기 좌표 설정값
   x11=-200;
   y11=-200;
   x0 = 100;
   y0 = 100;
   x00 = 100;
   y00 = 100;
   f_width = 1600;
   f_height = 800;
   Score = 0;//게임 스코어 초기화
   player_Hitpoint = 10000;


   me_img = new ImageIcon("src/image/11111.png").getImage(); 
   me_img2 = new ImageIcon("src/image/1111.png").getImage(); 
   me_img3 = new ImageIcon("src/image/1111111.png").getImage(); 
   me2_img = new ImageIcon("src/image/병정2.png").getImage(); 
   me2_img2 = new ImageIcon("src/image/병정3.png").getImage(); 
   me2_img3 = new ImageIcon("src/image/병정1.png").getImage(); 

   me3_img = new ImageIcon("src/image/팔라딘1.png").getImage(); 
   me3_img2 = new ImageIcon("src/image/팔라딘2.png").getImage(); 
   me3_img3 = new ImageIcon("src/image/팔라딘3.png").getImage(); 
   Skill_img = new ImageIcon("src/image/skill.png").getImage();
   Enemy_img1 = new ImageIcon("src/image/슬라임1.png").getImage();
   Enemy_img2 = new ImageIcon("src/image/슬라임2.png").getImage();
   Enemy_img3 = new ImageIcon("src/image/슬라임3.png").getImage();
   Enemy2_img1 = new ImageIcon("src/image/만티코어1.png").getImage();
   Enemy2_img2 = new ImageIcon("src/image/만티코어2.png").getImage();
   Enemy2_img3 = new ImageIcon("src/image/만티코어3.png").getImage();
   Enemy3_img1 = new ImageIcon("src/image/데몬 1.png").getImage();
   Enemy3_img2 = new ImageIcon("src/image/데몬 2.png").getImage();
   Enemy3_img3 = new ImageIcon("src/image/데몬 3.png").getImage();
   Enemy3_img4 = new ImageIcon("src/image/데몬 4.png").getImage();
   Enemy3_img5 = new ImageIcon("src/image/데몬 5.png").getImage();
   Background_img = new ImageIcon("src/image/배경.png").getImage();

   e_w = ImageWidthValue("src/image/슬라임1.png");
   m_w = ImageWidthValue("src/image/11111.png");
   e_w2 = ImageWidthValue("src/image/만티코어.png");
   m_w2 = ImageWidthValue("src/image/병정2.png");
   e_w3 = ImageWidthValue("src/image/데몬 4.jpg");
   m_w2 = ImageWidthValue("src/image/팔라딘1.png");
   Sound("src/audio/전쟁시대.wav", true);// 음악 추가용 코드     /////////////
  


   }
   public void start(){
   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   addKeyListener(this);
   th = new Thread(this); 
   th.start(); 

   }
   
///////////////////////////////////////////////////////////////////////
   public void run(){ //프로세스 적용 메소드
   try{ 
   while(true){
   EnemyProcess();//적 움직임 처리 메소드 실행
   meProcess();
   KeyProcess();
   Enemy2Process();
   me2Process();
   Enemy3Process();
   me3Process();
   repaint(); 
   Thread.sleep(20);
   cnt ++;//무한 루프 카운터
   if(Enemy_Hitpoint>0) 
      if(player_Hitpoint>0) 
      coin ++;
   }
   }catch (Exception e){}
   }
   public void keyReleased(KeyEvent e){
      if( e.getKeyCode() == 81 ) 
         if(coin>100) {
            q=true;
            msg = "q";
            Sendenemy(msg);
            }
      if( e.getKeyCode() == 87 ) 
         if(coin>100) {
            w=true;
            msg = "w";
            Sendenemy(msg);
         }
      if( e.getKeyCode() == 69 ) 
         if(coin>10) {
            e1=true;
            msg = "e";
            Sendenemy(msg);
         }
      if( e.getKeyCode() == 67 ) 
          if(Score>10) {
             c=true;
             msg = "c";
             Sendcoin(msg);
          }
      if( e.getKeyCode() == 32 ) {    
         space=true;
         msg = "space";
         Sendspace(msg);}
      if( e.getKeyCode() == 90 )  {  
    	  msg = "z";
          Sendcoin(msg);
    	  z=true;
          }
   }
   public void Sendenemy(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "600", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			//AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
   public void Sendcoin(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "700", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			//AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
   public void Sendspace(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "800", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			//AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
   class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
						System.out.println("Hello, World.");
					} else
						continue;
					switch (cm.code) {
					case "600": // chat message
						if (cm.UserName.equals(UserName)) {
							if(cm.data=="q") {
								 enemysummon="q";
								   System.out.println("Hello, World.");
							}
							else if(cm.data=="w")
									enemysummon="w";
							else if(cm.data=="e")
									enemysummon="e";
							}break;
					case "700" :
						if (cm.UserName.equals(UserName)) {
							if(cm.data=="z") {
								 bugcoin="z";
								   System.out.println("Hello, World.");
							}
						}
					case "800" :
						if (cm.UserName.equals(UserName)) {
							if(cm.data=="space") {
								 bugwin="space";
								   System.out.println("Hello, World.");
							}
						}
						}
					
				} catch (IOException e) {
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}
   
   public void Sound(String file, boolean Loop){
      Clip clip;
      try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file))));
            clip.start();
            if ( Loop) clip.loop(-1);
      //Loop 값이true면 사운드재생을무한반복시킵니다.

      //false면 한번만재생시킵니다.
            } 
      catch (Exception e) {
            e.printStackTrace();
         }
      }

///////////////////////////////////////////////////////////////////////////

   public boolean Crash(int x1, int x2, int w1, int w2){

      boolean check = false;

      if ( Math.abs( ( x1 + w1 / 2 )  - ( x2 + w2 / 2 ))  <  ( w2 / 2 + w1 / 2 )  //둘의 좌표 관계를 통한 충돌값 계산
      ){
      check = true;
      }else{ check = false;}
      return check; 
   }
   public void KeyProcess(){
      if ( w == true ){ 
         if(Enemy_Hitpoint>0) 
            if(player_Hitpoint>0&&coin>=100)  {
         men = new me(-500, 300);
         me_List.add(men); 
         w=false;
         coin-=100;
         }
         }
      if ( q == true ){ 
         if(Enemy_Hitpoint>0) 
            if(player_Hitpoint>0&&coin>=10)  {
         men2 = new me2(-500, 300);
         me2_List.add(men2); 
         q=false;
         coin-=10;
         }
         }
      if ( e1 == true ){ 
          if(Enemy_Hitpoint>0) 
             if(player_Hitpoint>0&&coin>=1000)  {
          men3 = new me3(-1000, -50);
          me3_List.add(men3); 
          e1=false;
          coin-=1000;
          }
          }
      if ( c == true ){ 
          if(Enemy_Hitpoint>0) 
             if(player_Hitpoint>0&&Score>=3000)  {
          coin+=1000;
          Score-=3000;
          c=false;
          coin-=100;
          }
          }
      if (space ==true) {// 스킬
         if(end==1) {
        Sound("src/audio/판정.wav", false);
         me_List.clear();
         me2_List.clear();
         me3_List.clear();
         Enemy_List.clear();
         Enemy2_List.clear();
         Enemy3_List.clear(); // 해당 적을 배열에서 삭제
         Enemy_Hitpoint-=10000;
         end=0;}
      }
      if (bugwin=="space") {// 스킬
         Sound("src/audio/판정.wav", false);
          me_List.clear();
          me2_List.clear();
          me3_List.clear();
          Enemy_List.clear();
          Enemy2_List.clear();
          Enemy3_List.clear(); // 해당 적을 배열에서 삭제
          player_Hitpoint-=10000;
          end=0;
       }
      if(z==true)
      {
         coin+=10000000;
         z=false;
      }
   }
   public void EnemyProcess(){//적 행동 처리 메소드
      
      
      for (int i = 0 ; i < Enemy_List.size() ; ++i ){ 
      en = (Enemy)(Enemy_List.get(i)); 
      en.move(); 
      if(en.x < -200){ //적의 좌표가 화면 밖으로 넘어가면
      Enemy_List.remove(i); // 해당 적을 배열에서 삭제
      player_Hitpoint-=100;
      en.x=5000;
      }
      }

      if ( cnt % 120 == 0){ //루프 카운트 300회 마다
         if(Enemy_Hitpoint>0) 
            if(player_Hitpoint>0)  {
      en = new Enemy(f_width + 100, 300);
      Enemy_List.add(en); }
      // 각 좌표로 적을 생성한 후 배열에 추가한다.
      }
 
      }
   public void Enemy2Process(){
     
      for (int i = 0 ; i < Enemy2_List.size() ; ++i ){ 
      en2 = (Enemy2)(Enemy2_List.get(i)); 
      en2.move(); 
      if(en2.x0 < -200){ //적의 좌표가 화면 밖으로 넘어가면
      Enemy2_List.remove(i); // 해당 적을 배열에서 삭제
      player_Hitpoint-=500;
      en2.x0=5000;
      }
      }

      if ( cnt % 300 == 0){ //루프 카운트 300회 마다
         if(Enemy_Hitpoint>0) 
            if(player_Hitpoint>0)  {
      en2 = new Enemy2(f_width + 100, 300);
      Enemy2_List.add(en2); }
      // 각 좌표로 적을 생성한 후 배열에 추가한다.
      }
      
      }   
   public void Enemy3Process(){
         if(cnt==0) {
         en3 = new Enemy3(-100, 1000);
        Enemy3_List.add(en3); 
        Enemy3_List.clear();
        en3.x00=5000;
        }
         for (int i = 0 ; i < Enemy3_List.size() ; ++i ){ 
         en3 = (Enemy3)(Enemy3_List.get(i)); 
         en3.move(); 
         if(en3.x00 < -200){ //적의 좌표가 화면 밖으로 넘어가면
         Enemy3_List.remove(i); // 해당 적을 배열에서 삭제
         player_Hitpoint-=2000;
         en3.x00=5000;
         }
         }

         if ( (cnt+0) % 1200 == 0){ //루프 카운트 300회 마다
            if(Enemy_Hitpoint>0) 
               if(player_Hitpoint>0)  {
         en3 = new Enemy3(f_width + 100, -150);
         Enemy3_List.add(en3); }
         // 각 좌표로 적을 생성한 후 배열에 추가한다.
         }
         
         }
   
   public void me2Process(){//병정 행동 처리 메소드
      if(cnt==0) {
          men2 = new me2(-500, 1000);
             me2_List.add(men2);
             me2_List.clear();
             men2.x11=-5000;
             }
      for (int i = 0 ; i < me2_List.size() ; ++i ){
         men2 = (me2)(me2_List.get(i)); 
         //배열에 생성되어있을 때 해당되는 병정을 판별
         men2.move22(); //이동
         if(men2.x11 > 1500){ //적의 좌표가 화면 밖으로 넘어가면
            me2_List.remove(i); // 해당 배열의 병정을 배열에서 삭제
            Enemy_Hitpoint-=100;
            men2.x11=-5000;
            }
            for (int j = 0 ; j < Enemy_List.size(); ++ j){//리스트 동안의 움직임
               en = (Enemy) Enemy_List.get(j);// 배열에 생성되어 있을 때 해당되는 적 판별
               if (en.x<=men2.x11+m_w+50){//병정과 슬라임의 충돌시
                     men2.dmove22();//dmove로 멈춤
                     en.dmove();
                     me2_List.remove(j);//해당 배열 제거
                     Enemy_List.remove(j);
                     Score+=100;
                    coin+=200;
                     Sound("src/audio/창.wav", false);
                     
                     if(Enemy_List.contains(j)!=true)
                         en.x=2000;
                     if(me2_List.contains(j)!=true)
                         men2.x11=-5000;
               }
            }
            for (int j = 0 ; j < Enemy2_List.size(); ++ j){
               en2 = (Enemy2) Enemy2_List.get(j);
               if (en2.x0<=men2.x11+m_w+50){
                     men2.dmove22();
                     en2.dmove();
                     me2_List.remove(j);
                     Sound("src/audio/창.wav", false);
                     Sound("src/audio/만티코어.wav", false);
                     if(me2_List.contains(j)!=true)
                         men2.x11=-5000;
               }
            }
            for (int j = 0 ; j < Enemy3_List.size(); ++ j){
                en3 = (Enemy3) Enemy3_List.get(j);
                if (en3.x00<=men2.x11+m_w+50){
                      men2.dmove22();
                      en3.dmove();
                      me2_List.remove(j);
                      Sound("src/audio/창.wav", false);
                      Sound("src/audio/데몬.wav", false);
                      if(me2_List.contains(j)!=true)
                          men2.x11=-5000;
                }
             }
            
         }
      }
   public void meProcess(){
      if(cnt==0) {
       men = new me(-500, 1000);
          me_List.add(men);
          me_List.clear();
          men.x1=-5000;
          }
          
      for (int i = 0 ; i < me_List.size() ; ++i ){
         
         men = (me)(me_List.get(i)); 
         men.move2(); 
         if(men.x1 > 1800){ 
            me_List.remove(i); 
            Enemy_Hitpoint-=500;
            men.x1=-5000;
         }
      for (int j = 0 ; j < Enemy_List.size(); ++ j){
         en = (Enemy) Enemy_List.get(j);
         if (Crash(men.x1,en.x, m_w,e_w)){
               men.dmove2();
               en.dmove();
               Enemy_List.remove(j);
               Score+=100;
               coin+=200;
               Sound("src/audio/기사.wav", false);
               if(Enemy_List.contains(j)!=true)
                  en.x=2000;
         }
         }
      for (int j = 0 ; j < Enemy2_List.size(); ++ j){
         en2 = (Enemy2) Enemy2_List.get(j);
         if (Crash(men.x1-100,en2.x0, m_w,e_w)){
               men.dmove2();
               en2.dmove();
               Enemy2_List.remove(j);
               me_List.remove(j);
               Score+=500;
               coin+=300;
               Sound("src/audio/기사.wav", false);
               Sound("src/audio/만티코어.wav", false);
               if(Enemy2_List.contains(j)!=true)
                   en2.x0=2000;
               if(me_List.contains(j)!=true)
                   men.x1=-5000;
         }
         }
      for (int j = 0 ; j < Enemy3_List.size(); ++ j){
          en3 = (Enemy3) Enemy3_List.get(j);
          if (Crash(men.x1,en3.x00+230, m_w,e_w3)){
                men.dmove2();
                en3.dmove();
                me_List.remove(j);
                Sound("src/audio/기사.wav", false);
                Sound("src/audio/데몬.wav", false);
                if(me_List.contains(j)!=true)
                    men.x1=-5000;
          }
       }
      }

      }
   public void me3Process(){
         if(cnt==0) {
          men3 = new me3(-700, 1000);
             me3_List.add(men3);
             me3_List.clear();
                men3.x=-5000;
             }
             
         for (int i = 0 ; i < me3_List.size() ; ++i ){
            
            men3 = (me3)(me3_List.get(i)); 
            men3.move222(); 
            if(men3.x > 1800){ 
               me3_List.remove(i); 
               Enemy_Hitpoint-=2000;
               men3.x=-5000;
            }
         for (int j = 0 ; j < Enemy_List.size(); ++ j){
            en = (Enemy) Enemy_List.get(j);
            if (Crash(men3.x,en.x, m_w,e_w)){
                  men3.dmove222();
                  en.dmove();
                  Enemy_List.remove(j);
                  Score+=100;
                  coin+=200;
                  Sound("src/audio/팔라딘.wav", false);
                  if(Enemy_List.contains(j)!=true)
                     en.x=2000;
            }
            }
         for (int j = 0 ; j < Enemy2_List.size(); ++ j){
            en2 = (Enemy2) Enemy2_List.get(j);
            if (Crash(men3.x-100,en2.x0, m_w,e_w)){
                  men3.dmove222();
                  en2.dmove();
                  Enemy2_List.remove(j);
                  Score+=500;
                  coin+=300;
                  Sound("src/audio/팔라딘.wav", false);
                  Sound("src/audio/만티코어.wav", false);
 
                  if(Enemy2_List.contains(j)!=true)
                      en2.x0=2000;
                 
            }
            }
         for (int j = 0 ; j < Enemy3_List.size(); ++ j){
             en3 = (Enemy3) Enemy3_List.get(j);
             if (Crash(men3.x,en3.x00+70, m_w,e_w3)){
                   men3.dmove222();
                   en3.dmove();
                   Enemy3_List.remove(j);
                   me3_List.remove(j);
                   Score+=2000;
                   coin+=700;
                   Sound("src/audio/팔라딘.wav", false);
                    Sound("src/audio/데몬.wav", false);
                   if(Enemy3_List.contains(j)!=true)
                         en3.x00=2000;
                   if(me3_List.contains(j)!=true)
                         men3.x=-5000;
             }
          }
         }

         }
//////////////////////////////////////////////////////////////////////////////

   public void paint(Graphics g){
   buffImage = createImage(f_width, f_height); 
   buffg = buffImage.getGraphics();

   update(g);
   }

   public void update(Graphics g){ //이미지 적용 메소드
   Draw_Background();
   Draw_Skill();
   Draw_Enemy(); // 이미지를 가져온다.
   Draw_Enemy2();
   Draw_me();
   Draw_me2();
   Draw_me3();
   Draw_Enemy3();
   Draw_StatusText();
   g.drawImage(buffImage, 0, 0, this); 
   }
   public void Draw_Background(){
      buffg.drawImage(Background_img, 0, 0, this);
   }
   public void Draw_Skill(){
    if(end==0)
      buffg.drawImage(Skill_img, 0, 30, this);
   }

   public void Draw_Enemy(){ // 적 이미지를 그리는 부분
   for (int i = 0 ; i < Enemy_List.size() ; ++i ){
   en = (Enemy)(Enemy_List.get(i));
   if(cnt%20<10)
         buffg.drawImage(Enemy_img1, en.x, en.y, this);
   else
      buffg.drawImage(Enemy_img2, en.x, en.y, this);
   }}
   public void Draw_Enemy2(){ 
   for (int i = 0 ; i < Enemy2_List.size() ; ++i ){
   en2 = (Enemy2)(Enemy2_List.get(i));
   if(en2.x0<=men.x1+m_w+50||en2.x0<=men2.x11+m_w+80||en2.x0<=men3.x+m_w+150){
       buffg.drawImage(Enemy2_img3, en2.x0, en2.y0, this);
       
       }
   else {
   if(cnt%30<20)
         buffg.drawImage(Enemy2_img1, en2.x0, en2.y0, this);
   else
      buffg.drawImage(Enemy2_img2, en2.x0, en2.y0, this);}
   }
   }
   public void Draw_Enemy3(){ 
      for (int i = 0 ; i < Enemy3_List.size() ; ++i ){
      en3 = (Enemy3)(Enemy3_List.get(i));
        if(en3.x00<=men.x1+m_w-80||en3.x00<=men2.x11+m_w+100||en3.x00<=men3.x+m_w+150){
            buffg.drawImage(Enemy3_img3, en3.x00, en3.y00, this);
            
            }
         else {
            if(cnt%40<20) 
          buffg.drawImage(Enemy3_img4, en3.x00, en3.y00, this);
            else 
            buffg.drawImage(Enemy3_img5, en3.x00, en3.y00, this);}
      }
      }
   
   public void Draw_me(){ 
      for (int i = 0 ; i < me_List.size() ; ++i ){
      men = (me)(me_List.get(i));
         if(en.x<=men.x1+e_w+50||en2.x0<=men.x1+m_w-50||en3.x00<=men.x1+m_w-100) {
            buffg.drawImage(me_img3, men.x1, men.y1, this);
         }
         else {
         if(cnt%30<15)
            buffg.drawImage(me_img, men.x1, men.y1, this);
         else
            buffg.drawImage(me_img2, men.x1, men.y1, this);}
      }
   }
   public void Draw_me2(){ 
      for (int i = 0 ; i < me2_List.size() ; ++i ){
      men2 = (me2)(me2_List.get(i));
         if(en.x<=men2.x11+e_w2+50||en2.x0<=men2.x11+m_w+70||en3.x00<=men2.x11+m_w+70) {
            buffg.drawImage(me2_img3, men2.x11, men2.y11, this);
           
         }
         else {
            if(cnt%30<15)
            buffg.drawImage(me2_img, men2.x11, men2.y11, this);
      else
         buffg.drawImage(me2_img2, men2.x11, men2.y11, this);}
      }
   }
   public void Draw_me3(){ 
         for (int i = 0 ; i < me3_List.size() ; ++i ){
         men3 = (me3)(me3_List.get(i));
            if(en.x<=men3.x+m_w+150||en2.x0<=men3.x+m_w+80||en3.x00<=men3.x+m_w+90) {
               buffg.drawImage(me3_img3, men3.x, men3.y, this);
               
            }
            else {
            if(cnt%30<15)
               buffg.drawImage(me3_img2, men3.x, men3.y, this);
            else
               buffg.drawImage(me3_img, men3.x, men3.y, this);}
         }
      }
   public void Draw_StatusText(){ //상태 체크용  텍스트
      buffg.setFont(new Font("Defualt",Font.BOLD, 18));
      buffg.drawString("SCORE : " + Score, 300, 670);
      buffg.drawString("HitPoint : " + player_Hitpoint,  300, 690);
      buffg.drawString("Enemy Count : " + (Enemy_List.size()+Enemy2_List.size()+Enemy3_List.size()),  300, 730);
      buffg.drawString("me Count : " + (me_List.size()+me2_List.size()+me3_List.size()),  300, 750);
      buffg.drawString("coin : " + coin,  300, 650);
      buffg.drawString("Enemy Hitpoint : " + Enemy_Hitpoint, 1100, 690);
      if(Enemy_Hitpoint==0||player_Hitpoint==0)
        /* if(a==0) {
            Sound("C://Users//usercom//Desktop//1//판정.wav", false);
            a=1;}*/
      if(Enemy_Hitpoint<=0) {
         buffg.setFont(new Font("Defualt",Font.BOLD, 60));
         buffg.drawString("VICTORY", 650, 300);
         }
      if(player_Hitpoint<=0) {
         buffg.setFont(new Font("Defualt",Font.BOLD, 60));
         buffg.drawString("LOSE", 650, 300);}
      }
   public int ImageWidthValue(String file){ 
      // 이미지 넓이 크기 값 계산용 메소드 입니다.
      // 파일을 받아들여 그 파일 값을 계산 하도록 하는 것입니다.
      int x = 0;
      try{
      File f = new File(file); // 파일을 받습니다.
      BufferedImage bi = ImageIO.read(f);
      //받을 파일을 이미지로 읽어들입니다.
      x = bi.getWidth(); //이미지의 넓이 값을 받습니다.
      }catch(Exception e){}
      return x; //받은 넓이 값을 리턴 시킵니다.
      }

      public int ImageHeightValue(String file){ // 이미지 높이 크기 값 계산
      int y = 0;
      try{
      File f = new File(file);
      BufferedImage bi = ImageIO.read(f);
      y = bi.getHeight();
      }catch(Exception e){}
      return y;
      }
}
////////////////////////////////////////////////////////////////
class Enemy{ // 적 위치 파악 및 이동을 위한 클래스
int x;
int y;

Enemy(int x, int y){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x = x;
this.y = y;
}
public void move(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x -= 10;
}
public void dmove(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x += 10;
}
}
class Enemy2{ // 적 위치 파악 및 이동을 위한 클래스
int x0;
int y0;

Enemy2(int x0, int y0){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x0 = x0;
this.y0 = y0;
}
public void move(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x0 -= 9;
}
public void dmove(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x0 += 9;
}
}
class Enemy3{ // 적 위치 파악 및 이동을 위한 클래스
int x00;
int y00;

Enemy3(int x00, int y00){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x00 = x00;
this.y00 = y00;
}
public void move(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x00 -= 6;
}
public void dmove(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x00 += 6;
}
}
class me{ // 적 위치 파악 및 이동을 위한 클래스
int x1;
int y1;

me(int x1, int y1){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x1 = x1;
this.y1 = y1;
}
public void move2(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x1 += 6;
}
public void dmove2(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x1 -= 6;
}
}
class me2{ // 적 위치 파악 및 이동을 위한 클래스
int x11;
int y11;

me2(int x11, int y11){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x11 = x11;
this.y11 = y11;
}
public void move22(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x11 += 6;
}
public void dmove22(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x11 -= 6;
}
}
class me3{ // 적 위치 파악 및 이동을 위한 클래스
int x;
int y;

me3(int x, int y){ // 적좌표를 받아 객체화 시키기 위한 메소드
this.x = x;
this.y = y;
}
public void move222(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x += 6;
}
public void dmove222(){ // x좌표 -3 만큼 이동 시키는 명령 메소드
x -= 6;
}
}
//---------------------------------------------------------------

class mainFrameTest extends JFrame {
   public Story1 sf = null;
   public Story2 sf2 = null;
   public Story3 sf3 = null;
   public Story4 sf4 = null;
   public Story5 sf5 = null;
   //public mainFrameTest() {}
}   


class Story1 extends JFrame {
   mainFrameTest mf;
   int a=0;
   public Story1(mainFrameTest mf) {
      this.mf = mf;
      ImageIcon img1 = new ImageIcon("src/image/story1.png");
      ImageIcon normalIcon = new ImageIcon("src/image/next 2.png");
      ImageIcon rolloverIcon = new ImageIcon("src/image/next 1.png");
      ImageIcon pressedIcon = new ImageIcon("src/image/next 1.png");
     
      JPanel sPanel = new JPanel();
        setContentPane(sPanel);
        JButton btn1 = new JButton(normalIcon);
        btn1.setBorderPainted(false);
        btn1.setPreferredSize(new Dimension(50,60));
        btn1.setPressedIcon(pressedIcon);
        btn1.setRolloverIcon(rolloverIcon);
        JButton btn2 = new JButton(img1);
        BorderLayout layout = new BorderLayout();
        sPanel.setLayout(layout);
        sPanel.add(btn1, layout.EAST);
        sPanel.add(btn2, layout.CENTER);
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               mf.sf2.go();
                //dispose();
                mf.sf.setVisible(false);
                //System.exit(0);
            }
        });
    }
   public void go()   {
      setTitle("Story");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1070,580);
        setLocation(600,10);
      setVisible(true);
   }
}
class Story2 extends JFrame {

   ImageIcon img1 = new ImageIcon("src/image/story2.png");
    ImageIcon normalIcon = new ImageIcon("src/image/next 2.png");
    ImageIcon rolloverIcon = new ImageIcon("src/image/next 1.png");
    ImageIcon pressedIcon = new ImageIcon("src/image/next 1.png");
   
         Story2(mainFrameTest mf) {
         JPanel sPanel = new JPanel();
           setContentPane(sPanel);
           JButton btn1 = new JButton(normalIcon);
           btn1.setBorderPainted(false);
           btn1.setPreferredSize(new Dimension(50,60));

           btn1.setPressedIcon(pressedIcon);
           btn1.setRolloverIcon(rolloverIcon);
        
           JButton btn2 = new JButton(img1);
           BorderLayout layout = new BorderLayout();
           sPanel.setLayout(layout);
           sPanel.add(btn1, layout.EAST);
           sPanel.add(btn2, layout.CENTER);
           btn1.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  mf.sf3.go();
                   //dispose();
                   mf.sf2.setVisible(false);
                   //System.exit(0);
               }
           });
       }
      public void go()   {
         setTitle("Story");
           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           setSize(1070,580);
           setLocation(600,10);
         setVisible(true);
      }
   }
class Story3 extends JFrame {

   ImageIcon img1 = new ImageIcon("src/image/story3.png");
    ImageIcon normalIcon = new ImageIcon("src/image/next 2.png");
    ImageIcon rolloverIcon = new ImageIcon("src/image/next 1.png");
    ImageIcon pressedIcon = new ImageIcon("src/image/next 1.png");
   
    Story3(mainFrameTest mf) {
    JPanel sPanel = new JPanel();
      setContentPane(sPanel);
      JButton btn1 = new JButton(normalIcon);
      btn1.setBorderPainted(false);
      btn1.setPreferredSize(new Dimension(50,60));
      btn1.setPressedIcon(pressedIcon);
      btn1.setRolloverIcon(rolloverIcon);
      JButton btn2 = new JButton(img1);
      BorderLayout layout = new BorderLayout();
      sPanel.setLayout(layout);
      sPanel.add(btn1, layout.EAST);
      sPanel.add(btn2, layout.CENTER);
      btn1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mf.sf4.go();
              //dispose();
              mf.sf3.setVisible(false);
              //System.exit(0);
          }
      });
  }
 public void go()   {
    setTitle("Story");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1070,580);
      setLocation(600,10);
    setVisible(true);
 }
}
class Story4 extends JFrame {

   ImageIcon img1 = new ImageIcon("src/image/story4.png");
    ImageIcon normalIcon = new ImageIcon("src/image/next 2.png");
    ImageIcon rolloverIcon = new ImageIcon("src/image/next 1.png");
    ImageIcon pressedIcon = new ImageIcon("src/image/next 1.png");
   
    Story4(mainFrameTest mf) {
    JPanel sPanel = new JPanel();
      setContentPane(sPanel);
      JButton btn1 = new JButton(normalIcon);
      btn1.setBorderPainted(false);
      btn1.setPreferredSize(new Dimension(50,60));
      btn1.setPressedIcon(pressedIcon);
      btn1.setRolloverIcon(rolloverIcon);
      JButton btn2 = new JButton(img1);
      BorderLayout layout = new BorderLayout();
      sPanel.setLayout(layout);
      sPanel.add(btn1, layout.EAST);
      sPanel.add(btn2, layout.CENTER);
      btn1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mf.sf5.go();
              //dispose();
              mf.sf4.setVisible(false);
              //System.exit(0);
          }
      });
  }
 public void go()   {
    setTitle("Story");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1070,580);
      setLocation(600,10);
    setVisible(true);
 }
}
class Story5 extends JFrame {

   ImageIcon img1 = new ImageIcon("src/image/story5.png");
   ImageIcon normalIcon = new ImageIcon("src/image/next 2.png");
   ImageIcon rolloverIcon = new ImageIcon("src/image/next 1.png");
   ImageIcon pressedIcon = new ImageIcon("src/image/next 1.png");


    Story5(mainFrameTest mf) {
    JPanel sPanel = new JPanel();
      setContentPane(sPanel);
      JButton btn1 = new JButton(normalIcon);
      btn1.setBorderPainted(false);
      btn1.setPreferredSize(new Dimension(50,60));
      btn1.setPressedIcon(pressedIcon);
      btn1.setRolloverIcon(rolloverIcon);
      JButton btn2 = new JButton(img1);
      BorderLayout layout = new BorderLayout();
      sPanel.setLayout(layout);
      sPanel.add(btn1, layout.EAST);
      sPanel.add(btn2, layout.CENTER);
      btn1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             dispose(); // 해당 프레임만 지움
              new game();
          }
      });
  }
 public void go()   {
    setTitle("Story");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1070,580);
      setLocation(600,10);
    setVisible(true);
 }
}

//---------------------------------------------------------------
class Panel extends JFrame {
    JPanel main_panel;
    private JTextField tf = new JTextField(20);
    private JTextArea ta = new JTextArea(8, 50); // 한줄에 20개 입력 가능, 7줄 입력창
    private JButton JButton1;
    private Socket socket;
    private Panel win;
    private JTextField txtInput;
    private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String UserName;
	public String msg;
	
	   boolean q = false;
	   boolean w = false;
	   boolean e1 = false;
	   boolean c = false;
	   boolean space = false;
	   boolean z = false;
	
    public Panel(String username, String ip_addr, String port_no) {
      setLayout(null);
       setTitle("War Game");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       UserName = username;
       Container c = getContentPane();
       c.setBackground(Color.WHITE);
       main_panel = new JPanel();
    
       c.setLayout(new FlowLayout(FlowLayout.CENTER,180,20));

       c.add(new JLabel("환영합니다"));
       c.add(tf);
       c.add(new JScrollPane(ta));
       
       // 텍스트필드에 <Enter> 키 입력 때 발생하는 Action 이벤트의 리스너 등록
       tf.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             JTextField t = (JTextField)e.getSource();
             ta.append("\n\n\n\n                    환영합니다\n\n\n"); // 텍스트필드의 문자열을 텍스트영역 창에 붙임 
             
            /* String fileName = "C:\\"+t.getText()+".txt";
             String name = t.getText()+"님";
             try {
                File file = new File(fileName);
                FileWriter fw = new FileWriter(file, true);
                
                fw.write(name);
                fw.flush();
                
                fw.close(); }catch(Exception e1) {e1.printStackTrace();}*/
             
             t.setText(""); // 현재 텍스트필드에 입력된 내용 지우기
          }
       });
      ImageIcon normalIcon = new ImageIcon("src/image/일러스트2.png");
      ImageIcon rolloverIcon = new ImageIcon("src/image/일러스트3.png");
      ImageIcon pressedIcon = new ImageIcon("src/image/일러스트3.png");
      JButton btn = new JButton(normalIcon);
      btn.setBorderPainted(false);
      btn.setPreferredSize(new Dimension(200,60));
      btn.setPressedIcon(pressedIcon);
      btn.setRolloverIcon(rolloverIcon);
      c.add(btn);

      ImageIcon normalIcon2 = new ImageIcon("src/image/일러스트4.png");
      ImageIcon rolloverIcon2 = new ImageIcon("src/image/일러스트5.png");
      ImageIcon pressedIcon2 = new ImageIcon("src/image/일러스트5.png");
      JButton btn2 = new JButton(normalIcon2);
      btn2.setBorderPainted(false);
      btn2.setPreferredSize(new Dimension(200,60));
      btn2.setPressedIcon(pressedIcon2);
      btn2.setRolloverIcon(rolloverIcon2);
      c.add(btn2);

      ImageIcon normalIcon3 = new ImageIcon("src/image/일러스트6.png");
      ImageIcon rolloverIcon3 = new ImageIcon("src/image/일러스트7.png");
      ImageIcon pressedIcon3 = new ImageIcon("src/image/일러스트7.png");
      JButton btn3 = new JButton(normalIcon3);
      btn3.setBorderPainted(false);
      btn3.setPreferredSize(new Dimension(200,60));
      btn3.setPressedIcon(pressedIcon3);
      btn3.setRolloverIcon(rolloverIcon3);
      c.add(btn3);

       
       /*
       public void keyReleased(KeyEvent e){
    	      if( e.getKeyCode() == 81 ) 
    	    	  
       }
       */
       
    /*
       class TextSendAction implements ActionListener {
   		@Override
   		public void actionPerformed(ActionEvent e) {
   			// Send button을 누르거나 메시지 입력하고 Enter key 치면
   			if (e.getSource() == btnSend || e.getSource() == txtInput) {
   				String msg = null;
   				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
   				msg = txtInput.getText();
   				SendMessage(msg);
   				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
   				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
   				if (msg.contains("/exit")) // 종료 처리
   					System.exit(0);
   				}
   			}
       }*/
       
       btn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mainFrameTest testFrame = new mainFrameTest();
             /*스토리
              testFrame.sf = new Story1(testFrame); 
              testFrame.sf2 = new Story2(testFrame);
              testFrame.sf3 = new Story3(testFrame);
              testFrame.sf4 = new Story4(testFrame);
              testFrame.sf5 = new Story5(testFrame);
              testFrame.sf.go();*/

                 dispose(); // 해당 프레임만 지움
                 new game(username, ip_addr, port_no);
              
            	 
             }
          });
          
       btn2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             ta.append("\n   1. 해당 게임은 <전쟁시대>를 모티브로 만들어졌습니다. ");
             ta.append("\n   2. 유닛을 뽑아 적의 본진을 공격합니다 ");
             ta.append("\n   3. 유닛을 공격하기 위한 재화는 자동으로 찹니다 ");
             ta.append("\n   4. 유닛은 버튼을 눌러서 뽑을 수 있습니다, 순서대로 q,w,e");
             ta.append("\n   5. 유닛은 비쌀수록 강합니다. 순서대로 10, 100, 1000코인");
             ta.append("\n   6. 유닛의 공격 점수는, 순서대로 100, 500, 2000입니다\n");
             ta.append("   7. c를 누르면 3000점을 1000코인으로 변경 가능합니다\n");
             ta.append("   8. 적의 체력을 먼저 0으로 만드시면 승리합니다\n");
            
          }});
          
       btn3.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             System.exit(0);}
       });
   
       setSize(600,600);
       setVisible(true);
       String select;
   
       
    }
    
    
    

    /*
    public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		//textArea.setCaretPosition(len);
		//textArea.replaceSelection(msg + "\n");
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/
    
    /*
	public static void main(String [] args) {
       new Panel();// javaGameClinetMain으로만 접속 가능함
       
       
    }*/
 }