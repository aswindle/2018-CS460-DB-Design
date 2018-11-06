import java.sql.SQLOutput;
import java.util.Scanner;

public class TestMethods {
    private final static String menu =
            "Enter 1-5 to make a query selection:\n" +
                    "1) Count of schools with different names in 2015 and 2018\n" +
                    "2) Info about schools with decreasing achievement percentages\n" +
                    "3) Top combined Level 4 and 5 percentages\n" +
                    "4) MY QUERY HERE\n" +
                    "5) Exit\n";

    /**
     * Loop until the user enters an integer from 1 to 5; return that number
     * @param kb: Scanner initialized to System.in
     * @return 1, 2, 3, 4, or 5
     */
    public static int getMenuChoice(Scanner kb){
        while(true){
            System.out.print(menu);
            String result = kb.next();
            switch(result){
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    return Integer.parseInt(result);
            }
        }
    }

    public static String getYear(Scanner kb){
        while(true) {
            System.out.print("Enter a year (2015-2018): ");
            String attempt = kb.next();
            switch (attempt) {
                case "2015":
                case "2016":
                case "2017":
                case "2018":
                    return attempt;
                default:
                    continue;
            }
        }
    }
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int menu = getMenuChoice(kb);
        System.out.println(menu);
    }
}
