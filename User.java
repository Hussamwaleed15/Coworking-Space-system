public class User {
    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    public String type;
    private String reserve_id;
    private double totalHours;
    private String date;
    private String time;
    private String timeStart;
    private String timeEnd;
    private double value;
    private String roomId;
    // Constructor
    public User(String firstname, String lastname, int userid, String email, String password, String type,
                String reserve_id, double totalHours, String date, String time, String timeStart, String timeEnd, double value , String roomId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.type = type;
        this.reserve_id = reserve_id;
        this.totalHours = totalHours;
        this.date = date;
        this.time = time;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.value = value;
        this.roomId = roomId;
    }

    // Getter and setter methods
    public int getUserid() {
        return userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public String getReserve_id() {
        return reserve_id;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public double getValue() {
        return value;
    }

    public int getId() {
        return userid;
    }

    public String getRoomId() {
        return roomId;
    }

    // Setters for reservation details
    public void setReserve_id(String reserve_id) {
        this.reserve_id = reserve_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public void setId(int id) {
        this.userid = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

}
