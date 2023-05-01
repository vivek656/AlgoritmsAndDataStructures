package common;

public class DSUtil {

    @SuppressWarnings("unchecked")
     public static <T> T[]getGenericArray(int length){
      var tempArray = (T[]) (new Object[length]);
      return tempArray;
    }
}
