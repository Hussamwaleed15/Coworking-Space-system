public class Formal extends User{
    // Constructor for Instructor class
    public Formal(String firstname, String lastname, int userid, String email, String password, String type, String reserve_id,
                      double totalHours, String date, String time, String timeStart, String timeEnd, double value , String roomId) {
        // Call the constructor of the User class
        super(firstname, lastname, userid, email, password, type, reserve_id, totalHours, date,time,timeStart, timeEnd,value , roomId);
    }
}
