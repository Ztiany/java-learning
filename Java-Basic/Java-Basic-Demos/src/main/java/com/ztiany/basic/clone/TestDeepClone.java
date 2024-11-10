package com.ztiany.basic.clone;

import java.util.HashMap;

public class TestDeepClone {

    public static void main(String[] args) {
        System.out.println(cache);
        HashMap<String, SearchWord> clonedCache = (HashMap<String, SearchWord>) cache.clone();
        clonedCache.remove("a");
        clonedCache.get("b").searchTime = 0;
        System.out.println(clonedCache);
        System.out.println(cache);
    }

    private static HashMap<String, Object> cache = new HashMap<>();

    private static class SearchWord implements Cloneable {
        public String word;
        public long searchTime;

        public SearchWord(String word, long searchTime) {
            this.word = word;
            this.searchTime = searchTime;
        }

        @Override
        public String toString() {
            return "SearchWord{" + "word='" + word + '\'' + ", searchTime=" + searchTime + '}';
        }

        @Override
        protected SearchWord clone() throws CloneNotSupportedException {
            return (SearchWord) super.clone();
        }
    }

    static {
        cache.put("a", new SearchWord("a", System.currentTimeMillis()));
        cache.put("b", new SearchWord("b", System.currentTimeMillis()));
        cache.put("c", new SearchWord("c", System.currentTimeMillis()));
        cache.put("d", new SearchWord("d", System.currentTimeMillis()));
        cache.put("e", new SearchWord("e", System.currentTimeMillis()));
    }

}
