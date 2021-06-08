import java.text.NumberFormat;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cinema {

    final static byte FRONT_TICKET_PRICE = 10;
    final static byte BACK_TICKET_PRICE = 8;
    final static byte SCREEN_ROOM_SIZE = 60;
    final static String EMPTY_SEAT = "S";
    final static String TAKEN_SEAT = "B";
    static String[][] theatre;
    static int seats;
    static int rows;
    static int currentIncome;
    static int purchasedTickets;

    public static void main(String[] args) {
        createTheatre();
        printTheatre(theatre, seats);
        chooseAction();
    }

    public static void chooseAction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Show the seats\n" + "2. Buy a ticket\n" + "3. Statistics\n" + "0. Exit");
        try {
            int number = scanner.nextInt();
            switch (number) {
                case 1:
                    printTheatre(theatre, seats);
                    chooseAction();
                    break;
                case 2:
                    bookSeat(theatre);
                    chooseAction();
                    break;
                case 0:
                    return;
                case 3:
                    showStatistics();
                    chooseAction();
                    break;
                default:
                    System.out.println("Option doesn't exist!");
                    chooseAction();
            }
        } catch (InputMismatchException exception) {
            System.out.println("Wrong input");
            chooseAction();
        }
    }

    public static void createTheatre(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of rows: ");
            rows = scanner.nextInt();
            System.out.print("Enter the number of seats in each row: ");
            seats = scanner.nextInt();
            theatre = new String[rows][seats];
            for (String[] row: theatre) {
                Arrays.fill(row, EMPTY_SEAT);
            }
        } catch (InputMismatchException exception) {
            System.out.println("Wrong input!");
            createTheatre();
        }
    }

    public static String buildSeatsString(int seats) {
        StringBuilder amountSeats = new StringBuilder();
        for (int i = 0; i < seats; i++) {
            amountSeats.append(i + 1 + " ");
        }
        return "  " + amountSeats;
    }

    public static void printTheatre(String[][] matrix, int seats) {
        int row = 1;
        System.out.println("Cinema:");
        System.out.println(buildSeatsString(seats));
        for (String[] elements : matrix) {
            System.out.print(row + " ");
            for (String element : elements) {
                System.out.print(element + " ");
            }
            System.out.println();
            row++;
        }
        System.out.println();
    }

    public static void bookSeat(String[][] theatreMatrix) {
        Scanner scanner = new Scanner(System.in);
        int rowNumber, seatNumber;
        try {
            do {
                System.out.print("Enter a row number: ");
                rowNumber = scanner.nextInt();
            } while (rowNumber < 1 && rowNumber > rows);
            do {
                System.out.print("Enter a seat number in that row: ");
                seatNumber = scanner.nextInt();
            } while (seatNumber < 1 && seatNumber > seats);

            if (theatreMatrix[rowNumber - 1][seatNumber - 1].equals(TAKEN_SEAT)) {
                System.out.println("That ticket has already been purchased!");
                System.out.println();
                bookSeat(theatre);
            } else {
                System.out.println("Ticket price: " + calcTicketPrice(rows, seats, rowNumber));
                System.out.println();
                purchasedTickets++;
                theatreMatrix[rowNumber - 1][seatNumber - 1] = TAKEN_SEAT;
            }
        } catch (ArrayIndexOutOfBoundsException | InputMismatchException exception) {
            System.out.println("Wrong input!");
            System.out.println();
            bookSeat(theatre);
        }
    }

    public static void showStatistics() {
        System.out.println("Number of purchased tickets: " + purchasedTickets);
        System.out.println("Percentage: " + percentagePurchasedTickets() + "%");
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + calcTotalIncome(rows, seats));
        System.out.println();
    }

    public static String percentagePurchasedTickets() {
        double totalTickets = rows * seats;
        double percentage = (double)purchasedTickets * 100 / totalTickets;
        return String.format("%.2f", percentage);
    }

    public static int calcTotalIncome(double amountOfRows, double amountOfSeats) {
        if ((amountOfSeats * amountOfRows) <= 60) {
            double income = (amountOfRows * amountOfSeats) * FRONT_TICKET_PRICE;
            return (int)income;
        } else {
            double frontHalfRows = Math.floor(amountOfRows / 2);
            double backHalfRows = Math.ceil(amountOfRows / 2);
            double income = ((frontHalfRows * amountOfSeats) * FRONT_TICKET_PRICE) + ((backHalfRows * amountOfSeats) * BACK_TICKET_PRICE);
            return (int)income;
        }
    }

    public static String calcTicketPrice(double amountOfRows, double amountOfSeats, double rowNumber) {
        if ((amountOfSeats * amountOfRows) <= SCREEN_ROOM_SIZE) {
            currentIncome += FRONT_TICKET_PRICE;
            return NumberFormat.getCurrencyInstance().format(FRONT_TICKET_PRICE);
        } else {
            if (rowNumber <= Math.floor(amountOfRows / 2)) {
                currentIncome += FRONT_TICKET_PRICE;
                return NumberFormat.getCurrencyInstance().format(FRONT_TICKET_PRICE);
            } else {
                currentIncome += BACK_TICKET_PRICE;
                return NumberFormat.getCurrencyInstance().format(BACK_TICKET_PRICE);
            }
        }
    }
}