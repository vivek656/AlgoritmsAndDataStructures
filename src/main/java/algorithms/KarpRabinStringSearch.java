package algorithms;

import datastructures.HashTableSet;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class KarpRabinStringSearch {
    public static final Integer BASE = 7;
    public static KarpRabinCell search(String s , String text) throws IOException {
        return searchStream(s , new CharArrayReader(text.toCharArray()));

    }

    private KarpRabinStringSearch() {
    }

    public static KarpRabinCell searchStream(String s , CharArrayReader is) throws IOException {
        var algo = new KarpRabinStringSearch();
        var hashTableSet = new HashTableSet<KarpRabinCell>(cell -> cell.key);
        var builder = new StringBuilder();
        var cellToSearch = algo.getCellForString(s);
        char[] readLen = new char[s.length()];

        var res =is.read(readLen , 0 , s.length());
        if(res == -1) return null;
        var rollingBuilder = new StringBuilder(String.copyValueOf(readLen));
        if(rollingBuilder.length() < s.length()) return null;
        builder.append(rollingBuilder);
        var rollingCell = algo.getCellForString(rollingBuilder.toString());
        rollingCell.offsets.add(0);
        hashTableSet.insert(rollingCell);
        var offset = 0;
        var currentChar = is.read();
        while (currentChar != -1){
            builder.append((char) currentChar);
            offset++;
            rollingCell = algo.skipFirstAndAppendAndGet(rollingCell, (char) currentChar);
            rollingCell.text = builder;
            if(hashTableSet.find(rollingCell)==null){
                hashTableSet.insert(rollingCell);
            }
            hashTableSet.find(rollingCell).offsets.add(offset);
            currentChar = is.read();
        }
        return hashTableSet.find(cellToSearch);
    }

    private  KarpRabinCell append(KarpRabinCell cell , Character s){
        var newKey = cell.key * BASE + Integer.valueOf(s);
        return new KarpRabinCell(cell.s+s , newKey , cell.text , new ArrayList<>());
    }

    private KarpRabinCell skipFirstAndAppendAndGet(KarpRabinCell cell , Character s){
        if(cell.s.isEmpty()) return append(cell, s);
        int newKey = cell.key * BASE + Integer.valueOf(s);
        int charAtFirst = cell.s.charAt(0);
        newKey = (int) (newKey - (charAtFirst * Math.pow(BASE , cell.s.length())));
        return new KarpRabinCell(cell.s.substring(1) + s , (int) newKey, cell.text , new ArrayList<>());
    }

    private KarpRabinCell getCellForString(String s){
        var newKey = 0;
        for(int i = 0 ; i < s.length() ; i++ ){
            newKey = (newKey * BASE) + s.charAt(i);
        }
        return new KarpRabinCell(s , newKey , null , new ArrayList<>());
    }
/*
ell = 5813
hell = 41485
ello = 40802
 */
    private KarpRabinCell getEmptyCell(){
        return new KarpRabinCell("" , 0 , new StringBuilder(), Collections.emptyList());
    }

    public static class KarpRabinCell implements  Comparable<KarpRabinCell>{
        final String s ;
        final Integer key;
        StringBuilder text;
        public final List<Integer> offsets;

        public KarpRabinCell(String s, int key, StringBuilder text, List<Integer> offsets) {
            this.s = s;
            this.key = key;
            this.text = text;
            this.offsets = offsets;
        }

        @Override
        public String toString() {
            return "KarpRabinCell{" +
                    "s='" + s + '\'' +
                    ", key=" + key +
                    '}';
        }

        @Override
        public int compareTo(@NotNull KarpRabinCell o) {
            return this.s.compareTo(o.s);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof KarpRabinCell)) return false;
            return this.s.equals(((KarpRabinCell) obj).s);
        }

    }
}
