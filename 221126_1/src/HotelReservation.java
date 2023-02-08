import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class HotelReservation {
	private static Room[][] room;
	public static  Calendar cal;
	static int getToDay =0;

	public static void getDeteInteger() {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyMMdd"); // 오늘날짜를 "yyMMdd"형태로 만들어주기
			String now = df.format(cal.getTime());
			int today = Integer.parseInt(now); // 오늘날짜 정수 변환
			today =getToDay;
	}
	
	
	// 날짜 세팅하기(오늘날짜로 변경해주기 -> 예약 날짜 배열을 오늘날짜 기준으로 만들었기때문에 프로그램 시작할때마다 세팅해줘서 지난날짜의 배열의 원소값은 지워줌
	public static void setDate() {
		
		getDeteInteger();
		for (int i = 0; i < room.length; i++) {
			for (int j = 0; j < room[i].length; j++) {
				String[] reservationDate = room[i][j].getReservationDate(); // Room[i][j]의 예약날짜 배열 = reservationDate
				for (int d = 0; d < reservationDate.length; d++) {
					if (Integer.parseInt(reservationDate[d]) < getToDay) { // 날짜 지난경우 초기화 "000000"
						reservationDate[d] = "000000";
						room[i][j].setRoomStatus("빈방"); // 방 상태도 빈방으로 초기화
					} else if (Integer.parseInt(reservationDate[d]) != 0) { // 배열의 인덱스 = 예약날짜 - 오늘날짜
						int index = Integer.parseInt(reservationDate[d]) - getToDay;
						reservationDate[index] = reservationDate[d]; // 배열의 값을 인덱스에 맞게 옮기기 // 오류 없는지 확인해보기!!
					}
				}
			}
		}                         
	}
	
	// 예약하기
	public static void reservation(int roomNo, Guest guest, String roomDate) {
		// 방번호로  room 배열 인덱스 구하기
		int floor = roomNo / 100 - 2; // 2층 -> 0, 3층 -> 1, ...
		int roomNum = roomNo % 100 - 1; // 201호 -> 0, 202호 -> 1, ... 
		
		// 오늘날짜를 "yyMMdd"형태로 만들어주기 -> 오늘날짜 기준으로  reservationDate index 정해주기 / today -> reservationDate[0]
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
		String today = df.format(cal.getTime());
		int reservationDateIndex = Integer.parseInt(roomDate) - Integer.parseInt(today);
		
		if (reservationDateIndex < 0) { // 지난날짜 예약 불가능
			System.out.println("지난날짜는 예약불가능합니다."); // 확인용!!
		} else {
			// 예약날짜로 예약가능여부 확인하기 (예약날짜 배열에 포함되어 있지 않아야함)
			String[] reservationDate = room[floor][roomNum].getReservationDate(); // 선택한 방의 예약 내역 보기(캘린더 배열)
			if (reservationDate[reservationDateIndex].equals(roomDate)) { // 예약날짜 배열에 입력한 날짜와 같은 날짜가 있는 경우 예약안됨
				System.out.println("이미 예약된 방입니다."); // 확인용!!
			} else if (!reservationDate[reservationDateIndex].equals(roomDate)) {
				if (reservationDateIndex == 0) { // 예약가능 확인 후 입력날짜가 오늘일 경우 방상태를 투숙중으로 변경
					room[floor][roomNum].setRoomStatus("투숙중");
					room[floor][roomNum].setGuest(guest);
				} else {
					reservationDate[reservationDateIndex] = roomDate; // 예약날짜 배열에 값 저장하기
					room[floor][roomNum].setReservationDate(reservationDate); // 변경된 배열을 세팅하기(메서드안에서 새로 생성한 배열이므로 변경된 배열을 저장해줘야함)
				}
				System.out.println("예약완료되었습니다."); // 확인용!!
			}
		}
	}
	
	// 현재 객실 상태 보기
	public static void showRoomStatus(int roomNo) {
		int floor = roomNo / 100 - 2;
		int roomNum = roomNo % 100 - 1;
		System.out.println(room[floor][roomNum].getRoomStatus() + "입니다.");
	}
	
	// 예약 내역 보기(날짜 선택)
	public static void reservationInputDate(String date) {
		for (int i = 0; i < room.length; i++) {
			for (int j = 0; j < room[i].length; j++) { // 룸 이차원배열의 원소 접근
				String[] reservationDate = room[i][j].getReservationDate(); // 룸의 예약날짜 배열을 새로운 변수로 지정
				boolean possible = true;
				for (int d = 0; d < reservationDate.length; d++) {
					possible = true;
					if (reservationDate[d].equals(date)) { // 입력한 날짜와 예약날짜가 같은 경우
						possible = false;
						d = reservationDate.length; // for문 탈출
					} 
				}
				if (possible == false) {
					System.out.print((i + 2) * 100 + (j + 1) + "호:X  ");
				} else if (possible == true) {
					System.out.print((i + 2) * 100 + (j + 1) + "호:O  ");
				}
				if ((j+1) % 20 == 0) {
					System.out.println();
				}
			}
		}
	}
	
	// 예약 내역 보기(방 선택)
	public static void reservationInputRoom(int roomNo) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
		String today = df.format(cal.getTime());
		
		int floor = roomNo / 100 - 2;
		int roomNum = roomNo % 100 - 1;
		String[] reservationDate = room[floor][roomNum].getReservationDate();
		
		for (int i = 0; i < reservationDate.length; i++) {
			if (reservationDate[i].equals("000000")) { // 입력한 날짜의 방번호의 예약날짜 배열의 원소가 "000000"일 경우 예약가능 
				// 날짜의 년,월이 자동으로 변경되게 해주기
				int roomDate = Integer.parseInt(today) + i;
				int roomDateYear = roomDate / 10000;
				int roomDateMonth = (roomDate % 10000 / 100);
				int roomDateDay = roomDate % 100;
				Calendar reserveDate = Calendar.getInstance();
				SimpleDateFormat df2 = new SimpleDateFormat("yyMMdd");
				reserveDate.set(roomDateYear, roomDateMonth - 1, roomDateDay);
				System.out.print(df2.format(reserveDate.getTime()) + ":예약가능    ");
			} else { // 입력한 날짜의 방번호의 예약날짜 배열의 원소가 "000000"이 아닐 경우 예약완료
				System.out.print(reservationDate[i] + ":예약완료    ");
			}
			if ((i + 1) % 10 == 0) { // 10개씩 보여주기
				System.out.println();
			}
		}
	}
	
	// 예약상태변경(오늘만 가능)
	public static void changeReservationStatus(int roomNo, String date) {
		int floor = roomNo / 100 - 2;
		int roomNum = roomNo % 100 - 1;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
		String today = df.format(cal.getTime());
		if (today.equals(date)) {
			if (room[floor][roomNum].getRoomStatus().equals("빈방")) { // 빈방일 경우 투숙중으로 변경
				room[floor][roomNum].setRoomStatus("투숙중");
			} else if (room[floor][roomNum].getRoomStatus().equals("투숙중")) { // 투숙중일 경우 빈방으로 변경
				room[floor][roomNum].setRoomStatus("빈방");
			} 
			System.out.println("변경완료: " + room[floor][roomNum].getRoomStatus());
		} else { // 입력한 날짜가 오늘이 아닐 경우 
			System.out.println("당일 예약 상태 변경만 가능합니다.");
		}
	}
	
	// 객실 정보 보기(전체)
	public static void showInformation() {
		for (int i = 0; i < room.length; i++) {
			System.out.println((i + 2) + "층");
			for (int j = 0; j < room[i].length; j++) {
				System.out.println(room[i][j].toString());
			}
		}
	}
	
	// 객실 정보 보기(개별)
	public static void showInformationGuest(int roomNo) {
		int floor = roomNo / 100 - 2;
		int roomNum = roomNo % 100 - 1;
		System.out.println(room[floor][roomNum].toString());
	}
	
	public static void roomViewer() {	
		
		// 룸 객체 생성(생성자 파라미터: 룸번호, 룸타입) 
		// 룸 번호 설정 Room[0][1] = 202 -> (0+2)x100+(1+1)=202
		// 룸 타입 설정(짝수-더블, 홀수-싱글)
		for (int i = 0; i < room.length; i++) {
			for (int j = 0; j < room[i].length; j++) {
				int roomNo = (i + 2) * 100 + (j + 1);
				if (j % 2 == 0) {
					room[i][j] = new Room(roomNo, "single");
					//System.out.print(room[i][j].getRoomNo());
					//System.out.print("■  ");
				} else {
					room[i][j] = new Room(roomNo, "double");
					//System.out.print(room[i][j].getRoomNo());
					//System.out.print("■ ■  ");
				}
				if(j%20==0) {
					//System.out.println();
				}
			}
		}
	}
	
	// 메인
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		// 룸 배열 만들기 ([층][호실] 룸번호:201~420)
		room = new Room[4][20];  
		roomViewer();
	
		// 오늘날짜 설정(고정)
		setDate();

		// 시작(기능 선택)
		boolean go = true;
		while(go) {
			System.out.println("===============");
			System.out.printf("1.현재 객실 상태 확인 \n2.예약하기(체크인)\n3.예약 내역 보기\n4.예약 상태 변경\n5.객실 정보 보기\n0.프로그램 종료\n");
			System.out.println("===============");
			System.out.print("번호 입력: ");
			int user = scan.nextInt();
			switch(user) {
			case 1: // 현재 객실 상태 확인
				System.out.print("방번호 입력(201~520): ");
				int input1 = scan.nextInt();
				showRoomStatus(input1);
				break;
			case 2: // 예약하기
				System.out.print("방번호 입력(201~520): ");
				int input2 = scan.nextInt();
				System.out.print("날짜 입력(6자리 숫자): ");
				String inputDate = scan.next();
				System.out.print("이름 입력: ");
				String inputName = scan.next();
				System.out.print("폰번호 입력: ");
				String inputPhone = scan.next();
				reservation(input2, new Guest(inputName, inputPhone), inputDate);
				break;
			case 3: // 예약 내역 보기
				System.out.println("1.날짜선택  2.방선택");
				System.out.print("번호 입력: ");
				int input3 = scan.nextInt();
				if (input3 == 1) {
					System.out.print("날짜 입력(6자리 숫자): ");
					reservationInputDate(scan.next());
				} else if (input3 == 2) {
					System.out.print("방번호 입력(201~520): ");
					reservationInputRoom(scan.nextInt());
				}
				break;
			case 4: // 예약 상태 변경
				System.out.print("방번호 입력(201~520): ");
				int input4 = scan.nextInt();
				System.out.print("날짜 입력(6자리 숫자): ");
				String input5 = scan.next();
				changeReservationStatus(input4, input5);
				break;
			case 5: // 객실 정보 보기
				System.out.println("1.전체 객실 정보  2.개별 객실 정보");
				int input6 = scan.nextInt();
				if (input6 == 1) {
					showInformation();
				}
				else if (input6 == 2) {
					System.out.print("방번호 입력(201~520): ");
					int input7 = scan.nextInt();
					showInformationGuest(input7);
				}
				break;
			default:
				go = false;
			}		
		}
	}
}
