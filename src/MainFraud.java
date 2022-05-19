import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class MainFraud {
    public static void main(String[] args) {
        /*
         * Viktor Kovalev
         * vi.kovalev@innopolis.university
         */
        // Time complexity of whole algorithm is O(n*d*log(d))

        Scanner input = new Scanner(System.in);

        // Read numbers of transaction and trailing days
        int N = input.nextInt(), d = input.nextInt();

        // Create array for all transactions and read them into array
        BankTransaction[] bankTransactions = new BankTransaction[N];
        for (int i = 0; i < N; i++) {
            String date = input.next();
            double money = Double.parseDouble(input.next().replace("$", ""));
            bankTransactions[i] = new BankTransaction(LocalDate.parse(date), money);
        }

        // Sort all transaction by using counting sort. Increasing order by dates.
        Sort.countingSort(bankTransactions);

        int numberOfAlerts = 0;

        // Linked List for trailing days which store the transaction of whole days in trailing.
        LinkedList<BankTransaction> trailingDays = new LinkedList<>();

        // Add first transaction. Variable 'I' is counter for array 'days'.
        trailingDays.add(bankTransactions[0]);
        int I = 1;

        // Put first d days in Linked List.
        while (I < N && (Duration.between(trailingDays.peek().getDate().atStartOfDay(), bankTransactions[I].getDate().atStartOfDay()).toDays() < d
                || bankTransactions[I].getDate().equals(trailingDays.peekLast().getDate()))) {
            // If the last added transaction has the same date with current transaction => merge these two in one.
            if (bankTransactions[I].getDate().equals(trailingDays.peekLast().getDate())) {
                bankTransactions[I] = new BankTransaction(bankTransactions[I].getDate(), bankTransactions[I].getMoney() + trailingDays.peekLast().getMoney());
                trailingDays.pollLast();
            }
            // Add to trailing days.
            trailingDays.add(bankTransactions[I++]);
        }

        // Variable for the next transaction after trailing days.
        BankTransaction currentTransaction = bankTransactions[I];
        // Loop for all transactions.
        for (int i = I + 1; i <= N; i++) {
            // Delete all days from Linked List which have more than d days difference.
            while (trailingDays.size() > 0 && Math.abs(Duration.between(trailingDays.peek().getDate().atStartOfDay(), currentTransaction.getDate().atStartOfDay()).toDays()) > d) {
                trailingDays.poll();
            }
            // Calculate the zero's days which we have in the trailing days. Also initialize the variable for median.
            int zeros = d - trailingDays.size();
            double median = 0;

            // Calculate the median. If zeros less than half of days in Linked List than median is not zero.
            // Otherwise, median is zero.
            if (zeros <= d / 2) {

                // Put all days from Linked list in the array.
                Double[] doubleWindow = new Double[trailingDays.size()];
                int j = 0;
                for (BankTransaction x : trailingDays) {
                    doubleWindow[j++] = x.getMoney();
                }

                // Sort the trailing days by money by using MergeSort.
                Sort.mergeSort(doubleWindow, null);

                // We should calculate median in different ways for odd and even values of d.
                if (d % 2 == 0) {
                    if (zeros == d / 2) {
                        // Current median in the first index, because the second number is zero.
                        median = doubleWindow[0];
                    } else {
                        // Otherwise, just calculate.
                        median = doubleWindow[d / 2 - zeros - 1] + doubleWindow[d / 2 - zeros];
                    }
                } else {
                    // Get the middle value and double it.
                    median = 2 * doubleWindow[d / 2 - zeros];
                }
            }

            // Check conditions for alert.
            if (currentTransaction.getMoney() > 0 && currentTransaction.getMoney() >= median) {
                numberOfAlerts++; // Increase the number of alerts.
            }

            // If it is the last transaction, then break the loop.
            if (i == N) break;

            // Check is next transaction has the same date.
            if (currentTransaction.getDate().equals(bankTransactions[i].getDate())) {

                // Add to current transaction money from next transaction if the same date.
                currentTransaction = new BankTransaction(currentTransaction.getDate(), currentTransaction.getMoney() + bankTransactions[i].getMoney());
            } else {
                // Update the current transaction to the new with greater date and put the previous to the trailing.
                BankTransaction toQueue = new BankTransaction(currentTransaction);
                trailingDays.add(toQueue);
                currentTransaction = new BankTransaction(bankTransactions[i]);
            }
        }

        System.out.println(numberOfAlerts); // Print the alerts' number.
    }

    /**
     * Class with two static sorting algorithms. (1) MergeSort. (2) CountingSort.
     */
    public static class Sort {
        /**
         * Sorting algorithm of the canonical MergeSort.
         * <p>
         * Time complexity - O(n * log(n))
         *
         * @param array - Link to array which will be sorted.
         * @param cmp   - Custom comparator for elements if it needed. Write 'null' if not.
         * @param <T>   - Comparable type.
         */
        public static <T extends Comparable<? super T>> void mergeSort(T[] array, Comparator<T> cmp) {
            // Base case.
            if (array.length <= 1) {
                return;
            }
            // Initialise the comparator if it needed.
            if (cmp == null) {
                cmp = (T t1, T t2) -> {
                    return t1.compareTo(t2);
                };
            }

            // Arrays for left and right halfs.
            T[] left = (T[]) new Comparable[array.length / 2];
            T[] right = (T[]) new Comparable[array.length / 2 + array.length % 2];

            // Coping the both halfs of array to the left and right.
            System.arraycopy(array, 0, left, 0, array.length / 2);
            if (array.length - array.length / 2 >= 0)
                System.arraycopy(array, array.length / 2, right, 0, array.length - array.length / 2);

            // Call the MergeSort for both arrays.
            mergeSort(left, cmp);
            mergeSort(right, cmp);

            // Merge it in the original array.
            merge(array, left, right, cmp);
        }

        /**
         * Private method for merging two parts into one.
         * <br/>Time complexity - O(n)
         * @param array - Link to array which will be merged.
         * @param left - Link to the left part.
         * @param right - Link to the right part.
         * @param cmp - Comparator.
         * @param <T> - Comparable type.
         */
        private static <T extends Comparable<? super T>> void merge(T[] array, T[] left, T[] right, Comparator<T> cmp) {
            // Indexes for left and right arrays.
            int I = 0, J = 0;
            for (int i = 0; i < left.length + right.length; i++) {
                if (I < left.length && J < right.length && cmp.compare(left[I], right[J]) > 0 || I == left.length) {
                    // Add value from right if it is smaller than value from left, and we have values in right array.
                    array[i] = right[J++];
                } else {
                    // Otherwise, add value from left.
                    array[i] = left[I++];
                }
            }
        }

        /**
         * Sorting algorithm of the canonical CountingSort for LocalDates class.
         * <br/>Time complexity - O(n).
         * @param array - Link to the array which will be sorted.
         */
        public static void countingSort(BankTransaction[] array) {
            // Min and Max values.
            long max = -10000000, min = 10000000;

            // Determining the current min and max values.
            for (BankTransaction x : array) {
                max = Math.max(max, x.getDate().toEpochDay());
                min = Math.min(min, x.getDate().toEpochDay());
            }

            // Counting all values.
            int[] counter = new int[(int) (max - min + 1)];
            for (BankTransaction x : array) {
                counter[(int) (x.getDate().toEpochDay() - min)]++;
            }

            // Array for saving the original order for the same value.
            int[] accum = new int[counter.length];
            accum[0] = counter[0];
            // Calculate indexes for the last elements with the same value.
            for (int i = 1; i < counter.length; i++) {
                accum[i] = accum[i - 1] + counter[i];
            }
            // Array for sorted values.
            BankTransaction[] sorted = new BankTransaction[array.length];

            // Put elements from last to the needed indexes.
            for (int i = array.length - 1; i >= 0; i--) {
                int index = (int) (array[i].getDate().toEpochDay() - min);
                if (accum[index] == 0) {
                    continue;
                }
                sorted[--accum[index]] = array[i];
            }

            // Copy all elements from sorted array to the original array.
            for (int i = 0; i < array.length; i++) {
                array[i] = new BankTransaction(sorted[i]);
            }
        }
    }

    /**
     * Class for bank's transactions with comparable by dates method.
     * double money.
     * LocalDate date.
     */
    public static class BankTransaction implements Comparable<BankTransaction> {
        private final double money;
        private final LocalDate date;

        /**
         * @param date  - Date of transaction.
         * @param money - Amount of transaction.
         */
        public BankTransaction(LocalDate date, double money) {
            this.date = date;
            this.money = money;
        }

        /**
         * @param bankTransaction - Other transaction which will be copied to the new one.
         */
        public BankTransaction(BankTransaction bankTransaction) {
            this(bankTransaction.getDate(), bankTransaction.getMoney());
        }

        /**
         * @return - Amount of money.
         */
        public double getMoney() {
            return money;
        }

        /**
         * @return - Date of transaction.
         */
        public LocalDate getDate() {
            return date;
        }

        /**
         * @param b - Transaction for evaluating.
         * @return - -1 if first less than second. 0 if equal. 1 if first greater than second.
         */
        @Override
        public int compareTo(BankTransaction b) {
            return this.date.compareTo(b.getDate());
        }

        /**
         * @return - String with date+" "+money format.
         */
        public String toString() {
            return date.toString() + " " + money;
        }
    }
}