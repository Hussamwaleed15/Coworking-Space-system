public class Instructor extends User {
    // New fields for Projector and Board types
    private String Projector_type;
    private String Board_type;

    // Constructor for Instructor class
    public Instructor(String firstname, String lastname, int userid, String email, String password, String type, String reserve_id,
                      double totalHours, String date, String time, String timeStart, String timeEnd, double value,
                      String Projector_type, String Board_type , String roomId) {
        // Call the constructor of the User class
        super(firstname, lastname, userid, email, password, type, reserve_id, totalHours, date,time, timeStart, timeEnd,value, roomId);

        // Initialize the additional fields for Projector and Board types
        this.Projector_type = Projector_type;
        this.Board_type = Board_type;
    }

    // Getter for Projector_type
    public String getProjector_type() {
        return Projector_type;
    }

    // Setter for Projector_type
    public void setProjector_type(String Projector_type) {
        this.Projector_type = Projector_type;
    }

    // Getter for Board_type
    public String getBoard_type() {
        return Board_type;
    }

    // Setter for Board_type
    public void setBoard_type(String Board_type) {
        this.Board_type = Board_type;
    }

    // Optionally, override the toString() method to include Projector and Board info
    @Override
    public String toString() {
        return super.toString() + ", Projector Type: " + Projector_type + ", Board Type: " + Board_type;
    }
}
