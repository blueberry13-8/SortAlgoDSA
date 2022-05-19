//import java.time.temporal.ChronoField;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//
//public class Sort {
//    public static<T extends Comparable<? super T>> void mergeSort(T[] array, Comparator<T> cmp){
//        if (array.length <= 1){
//            return;
//        }
//        if (cmp == null){
//            cmp = (T t1, T t2) -> {return t1.compareTo(t2);};
//        }
//        T[] left = (T[])new Comparable[array.length / 2];
//        T[] right = (T[])new Comparable[array.length / 2 + array.length % 2];
//        System.arraycopy(array, 0, left, 0, array.length / 2);
//        if (array.length - array.length / 2 >= 0)
//            System.arraycopy(array, array.length / 2, right, 0, array.length - array.length / 2);
//        mergeSort(left, cmp);
//        mergeSort(right, cmp);
//        merge(array, left, right, cmp);
//    }
//
//    private static<T extends Comparable<? super T>> void merge(T[] array, T[] left, T[] right, Comparator<T> cmp){
//        int I = 0, J = 0;
//        for (int i = 0; i < left.length + right.length; i++){
//            if (I < left.length && J < right.length && cmp.compare(left[I], right[J]) > 0 || I == left.length){
//                array[i] = right[J++];
//            } else{
//                array[i] = left[I++];
//            }
//        }
//    }
//
//    public static void countingSort(BankTrans[] array){
//        long max = -10000000, min = 10000000;
//        for (BankTrans x : array){
//            max = Math.max(max, x.getDate().toEpochDay());
//            min = Math.min(min, x.getDate().toEpochDay());
//        }
//        int[] counter = new int[(int)(max - min + 1)];
//        for (BankTrans x : array){
//            counter[(int)(x.getDate().toEpochDay() - min)]++;
//            //counter.get((int)(x.getDate().toEpochDay() - min)) + 1
//        }
//        int[] accum = new int[counter.length];
//        accum[0] = counter[0];
//        for (int i = 1; i < counter.length; i++){
//            accum[i] = accum[i - 1] + counter[i];
//        }
//        BankTrans[] sorted = new BankTrans[array.length];
//        for (int i = array.length - 1; i >= 0; i--){
//            int index = (int)(array[i].getDate().toEpochDay() - min);
//            if (accum[index] == 0){
//                continue;
//            }
//            sorted[--accum[index]] = array[i];
//        }
//        for (int i = 0; i < array.length; i++){
//            array[i] = new BankTrans(sorted[i]);
//        }
//    }
//}
