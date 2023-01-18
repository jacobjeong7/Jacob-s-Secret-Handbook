package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 *
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
        studentsInLine      = l;
        musicalChairs       = m;
        seatingAvailability = a;
        studentsSitting     = s;
    }
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     *
     * Reads students from input file and inserts these students in alphabetical
     * order to studentsInLine singly linked list.
     *
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student
     * information separated by spaces: FirstName LastName Height
     *
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        int numOfStudents = StdIn.readInt();
        Student[] students = new Student[numOfStudents];

        {
            int i = 0;
            while (i < students.length) {
                String firstName = StdIn.readString();
                String lastName = StdIn.readString();
                int height = StdIn.readInt();
                students[i] = new Student(firstName, lastName, height);
                i++;
            }
        }

        int i = 0;
        while (i < students.length) {
            for(int k = i + 1; k < students.length; k++){
                if(students[i].compareNameTo(students[k]) > 0){
                    Student ptr = students[i];
                    students[i] = students[k];
                    students[k] = ptr;
                }
            }
            i++;
        }

        int p = students.length - 1;
        if (p >= 0) {
            do {
                SNode nodu = new SNode(students[p], studentsInLine);
                studentsInLine = nodu;
                p--;
            } while (p >= 0);
        }
    }

    /**
     *
     * This method creates and initializes the seatingAvailability (2D array) of
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     *
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     *
     * This method does not seat students on the seats.
     *
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();

        seatingAvailability = new boolean[r][c];
        studentsSitting = new Student[r][c];

        for(int i = 0; i < r; i++){
            for(int k = 0; k < c; k++){
                seatingAvailability[i][k] = StdIn.readBoolean();
            }
        }
    }

    /**
     *
     * This method simulates students taking their seats in the classroom.
     *
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     *
     * studentsInLine will then be empty
     */
    public void seatStudents () {

        // WRITE YOUR CODE HERE
        for(int i = 0; i < seatingAvailability.length; i++){
            for(int k = 0; k < seatingAvailability[0].length; k++){
                if(!seatingAvailability[i][k])
                    continue;
                if(musicalChairs != null){
                    studentsSitting[i][k] = musicalChairs.getStudent();
                    musicalChairs = musicalChairs.getNext();
                }else if(studentsInLine != null){
                    studentsSitting[i][k] = studentsInLine.getStudent();
                    studentsInLine= studentsInLine.getNext();
                }
            }

        }

    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     *
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {

        // WRITE YOUR CODE HERE
        SNode head = new SNode();

        for(int i = 0; i < studentsSitting.length; i++){
            for(int k = 0; k < studentsSitting[0].length; k++){
                if (studentsSitting[i][k] == null) {
                    continue;
                }
                SNode s = new SNode();
                s.setStudent(studentsSitting[i][k]);
                studentsSitting[i][k] = null;
                if (musicalChairs != null) {
                    s.setNext(musicalChairs.getNext());
                    musicalChairs.setNext(s);
                    musicalChairs = s;
                } else {
                    s.setNext(s);
                    head = s;
                    musicalChairs = s;
                }
            }
        }
        musicalChairs.setNext(head);
    }

    /**
     *
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     *
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the
     * list, b-1 is the last.
     *
     * Removes eliminated student from the list and inserts students back in studentsInLine
     * in ascending height order (shortest to tallest).
     *
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    private SNode countLinkedList(SNode head, int i){
        SNode head4 = head;
        int count = 0;
        while(count < i){
            head4 = head4.getNext();
            count += 1;
        }
        return head4;
    }
    private int lengthCirclyLinkedList(SNode head){
        SNode head3 = head.getNext();
        int count = 1;
        while(head3 != head){
            head3 = head3.getNext();
            count += 1;
        }
        return count;
    }



    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        int numOfStudents = lengthCirclyLinkedList(musicalChairs);
        int num = numOfStudents;

        for(int i = 0; i < numOfStudents; i++){
            int jElim = StdRandom.uniform(num);


            if(num != 1){
                SNode theNode = countLinkedList(musicalChairs, jElim);
                SNode elimination = theNode.getNext();
                theNode.setNext(elimination.getNext());
                if(jElim == num -1)
                    musicalChairs = theNode;
                elimination.setNext(null);
                num--;

                if(studentsInLine == null){
                    studentsInLine = elimination;
                }else if(studentsInLine.getStudent().getHeight() >= elimination.getStudent().getHeight()){
                    elimination.setNext(studentsInLine);
                    studentsInLine = elimination;
                }
                else{
                    SNode count = studentsInLine;
                    while (count != null && count != elimination){
                        if(count.getNext() == null){
                            count.setNext(elimination);
                        }else if(count.getStudent().getHeight() < elimination.getStudent().getHeight() && count.getNext().getStudent().getHeight() == elimination.getStudent().getHeight() || count.getNext().getStudent().getHeight() > elimination.getStudent().getHeight()){
                            elimination.setNext(count.getNext());
                            count.setNext(elimination);
                        }
                        count = count.getNext();
                    }
                }
            }
        }
        musicalChairs.setNext(null);
        seatStudents();
    }

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    private boolean ifStudentisTrue(){
        for(int i = 0; i < studentsSitting.length; i++){
            for(int j = 0; j < studentsSitting[0].length; j++){
                if(studentsSitting[i][j] != null){
                    return false;
                }
            }
        }
        return true;
    }

    public void addLateStudent ( String firstName, String lastName, int height ) {

        // WRITE YOUR CODE HERE
        Student jacobStudent = new Student(firstName, lastName, height);
        SNode jacobNode = new SNode();
        jacobNode.setStudent(jacobStudent);

        if(studentsInLine == null){
            if(ifStudentisTrue()){
                jacobNode.setNext(musicalChairs.getNext());
                musicalChairs.setNext(jacobNode);
                musicalChairs = jacobNode;
            }else{
                poolLoop:
                for(int i = 0; i < studentsSitting.length; i++){
                    for(int j = 0; j < studentsSitting[0].length; j++){
                        if(studentsSitting[i][j] == null && seatingAvailability[i][j]){
                            studentsSitting[i][j] = jacobStudent;
                            break poolLoop;
                        }
                    }
                }
            }
        }
        else{
            SNode count = studentsInLine;
            while(count.getNext() != null) count = count.getNext();
            count.setNext(jacobNode);
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students
     * are at (ie. whatever activity is not empty)
     *
     * Assume the student's name is unique
     *
     * @param firstName the student's first name
     * @param lastName the student's last name
     */

    private SNode postElim(SNode head, String name){
        if(head.getNext().getStudent().getFullName().equalsIgnoreCase(name)){
            return head;
        }
        SNode count = head.getNext();
        while(count != head && count.getNext() != null){
            if(count.getNext().getStudent().getFullName().equalsIgnoreCase(name)){
                return count;
            }
            count = count.getNext();
        }
        return null;
    }

    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // WRITE YOUR CODE HERE
        String name = (firstName + " " + lastName).trim();
        if(studentsInLine == null){
            if(ifStudentisTrue()){
                SNode preElime = postElim(musicalChairs, name);
                if(preElime == null) return;
                if (!musicalChairs.getStudent().getFullName().equalsIgnoreCase(name)) {
                    SNode elim = preElime.getNext();
                    preElime.setNext(elim.getNext());
                } else {
                    preElime.setNext(musicalChairs.getNext());
                    musicalChairs = preElime;
                }
            } else{
                loop:
                for(int i = 0; i < studentsSitting.length; i++){
                    for(int k = 0; k < studentsSitting[0].length; k++){
                        if(studentsSitting[i][k] != null && studentsSitting[i][k].getFullName().equalsIgnoreCase(name)){
                            studentsSitting[i][k] = null;
                            break loop;
                        }
                    }
                }
            }
        } else{
            if(studentsInLine.getStudent().getFullName().equalsIgnoreCase(name))
                studentsInLine = studentsInLine.getNext();
            else{
                SNode pElim = postElim(studentsInLine, name);
                if(pElim == null)
                    return;
                SNode elime = pElim.getNext();
                pElim.setNext(elime.getNext());
            }
        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {

            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );

                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
