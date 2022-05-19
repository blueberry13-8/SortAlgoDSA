//import java.time.LocalDate;
//
//public class BankTrans implements Comparable<BankTrans> {
//    private final double money;
//    private final LocalDate date;
//    public BankTrans(LocalDate date, double money){
//        this.date = date;
//        this.money = money;
//    }
//
//    public BankTrans(BankTrans trans){
//        this(trans.getDate(), trans.getMoney());
//    }
//
//    public double getMoney() {
//        return money;
//    }
//
//    public LocalDate getDate() {
//        return date;
//    }
//
//    @Override
//    public int compareTo(BankTrans b) {
//        return this.date.compareTo(b.getDate());
//    }
//
//    public String toString(){
//        return date.toString() + " " + money;
//    }
//}