import java.util.Arrays;

public class Room {
	private int roomNo;
	private String roomType;
	private String roomStatus;
	private String[] reservationDate;
	private Guest guest;
	
	public Room(int roomNo, String roomType) {
		super();
		this.roomNo = roomNo;
		this.roomType = roomType;
		reservationDate = new String[30]; // reservationDate 객체 생성(예약 100번 받기)
		Arrays.fill(reservationDate, "000000"); // reservationDate 초기화
		guest = new Guest("", ""); // 초기화한 후 예약받을 때 게스트 만들기
		roomStatus = "빈방"; // 초기화한 후 예약받을 때 상태 변경
	}
	
	public int getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(int roomNo) {
		this.roomNo = roomNo;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(String reservationStatus) {
		this.roomStatus = reservationStatus;
	}
	public String[] getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(String[] reservationDate) {
		this.reservationDate = reservationDate;
	}
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	@Override
	public String toString() {
		return roomNo + "호 [방타입: " + roomType + ", 현재 객실 상태: " + roomStatus
				+ ", 고객정보" + guest + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(reservationDate);
		result = prime * result + roomNo;
		return result;
	}
	
}
