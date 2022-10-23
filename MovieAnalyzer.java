package Assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

    public class MovieAnalyzer {
        private String URL;//0
        private String movieName;//1
        private String releaseYear;//2
        private String certificate;//3
        private String runtime;//4
        private String genre;//5
        private String rate;//6
        private String overview;//7
        private String meta_score;//8
        private String director;//9
        private String star1;//10
        private String star2;//11
        private String star3;//12
        private String star4;//13
        private String votes;//14
        private String money;//15
        private List<String[]> list;


        public MovieAnalyzer(String dataset_path) {
            List<String[]> list = new ArrayList<>();
            FileReader f = null;
            BufferedReader bf = null;
            try {
                f = new FileReader(dataset_path);
                bf = new BufferedReader(f);
                String str;
                while ((str = bf.readLine()) != null) {
                    String[] s = new String[16];
                    for (int i = 0; i < str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1).length; i++) {
                        s[i] = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)[i];
                    }
                    list.add(s);
                }
                bf.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != bf) {
                        bf.close();
                    }
                    if (null != f) {
                        f.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.list = list;
        }//构造器

        public Map<Integer, Integer> getMovieCountByYear() {
            HashMap<Integer, Integer> m = new HashMap();
            for (int i = 1; i < this.list.size(); i++) {
                int year = Integer.parseInt(this.list.get(i)[2]);
                if (m.containsKey(year)) {
                    int value = m.get(year);
                    m.put(year, value + 1);
                }
                while (!m.containsKey(year)) {
                    m.put(year, 1);
                }
            }

//        Map<Integer, Integer> sortedMap = m.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByKey())
//                .collect(Collectors
//                        .toMap(Map.Entry::getKey,
//                                Map.Entry::getValue,
//                                (e1, e2)->e1,
//                                LinkedHashMap::new));


            Map<Integer, Integer> ans = new LinkedHashMap<>();
            class tuple {
                final int a;
                final int b;

                public tuple(int a, int b) {
                    this.a = a;
                    this.b = b;
                }
            }

            ArrayList<tuple> temp = new ArrayList<>();
            m.forEach((key, value)->{
                temp.add(new tuple(key, value));
            });
            for (int i = temp.size() - 1; i >= 0; i--) {
                ans.put(temp.get(i).a, temp.get(i).b);
            }
            return ans;
        }

        public Map<String, Integer> getMovieCountByGenre() {
            HashMap<String, Integer> m = new HashMap();
            for (int i = 1; i < this.list.size(); i++) {
                String processed;
                String s = this.list.get(i)[5];
                processed = s.replaceAll("\"", "").replaceAll(" ", "");
                String[] genreType = new String[processed.split(",").length];//单独一个genre的类型个数
                for (int j = 0; j < processed.split(",").length; j++) {
                    genreType[j] = processed.split(",")[j];
                    if (m.containsKey(genreType[j])) {
                        int value = m.get(genreType[j]);
                        m.put(genreType[j], value + 1);
                    }
                    while (!m.containsKey(genreType[j])) {
                        m.put(genreType[j], 1);
                    }
                }
            }

            Map<String, Integer> ans = new LinkedHashMap<>();
            class tuple {
                String a;
                int b;

                public tuple(String a, int b) {
                    this.a = a;
                    this.b = b;
                }
            }

            ArrayList<tuple> temp = new ArrayList<>();
            m.forEach((key, value)->{
                temp.add(new tuple(key, value));
            });

            for (int x = 0; x < temp.size() - 1; x++) {
                for (int i = 0; i < temp.size() - 1 - x; i++) {
                    if (temp.get(i).b > temp.get(i + 1).b) {
                        String current1 = temp.get(i).a;
                        int current = temp.get(i).b;
                        temp.get(i).b = temp.get(i + 1).b;
                        temp.get(i).a = temp.get(i + 1).a;
                        temp.get(i + 1).b = current;
                        temp.get(i + 1).a = current1;
                    } else if (temp.get(i).b == temp.get(i + 1).b) {
                        if (temp.get(i).a.compareTo(temp.get(i + 1).a) < 0) {
                            String current1 = temp.get(i + 1).a;
                            int current = temp.get(i + 1).b;
                            temp.get(i + 1).b = temp.get(i).b;
                            temp.get(i + 1).a = temp.get(i).a;
                            temp.get(i).b = current;
                            temp.get(i).a = current1;
                        }
                    }
                }
            }

            for (int i = temp.size() - 1; i >= 0; i--) {
                ans.put(temp.get(i).a, temp.get(i).b);
            }

            return ans;
        }


        public Map<List<String>, Integer> getCoStarCount() {//人名 按字母序
            HashMap<List<String>, Integer> m = new HashMap();
            for (int i = 1; i < this.list.size(); i++) {
                String star1 = this.list.get(i)[10];
                String star2 = this.list.get(i)[11];
                String star3 = this.list.get(i)[12];
                String star4 = this.list.get(i)[13];
                String[] star = new String[4];
                star[0] = star1;
                star[1] = star2;
                star[2] = star3;
                star[3] = star4;
                for (int j = 0; j < star.length; j++) {
                    for (int k = 0; k < star.length - j - 1; k++) {
                        List<String> starList = new ArrayList();
                        starList.add(star[j]);
                        starList.add(star[j + k + 1]);
                        if (m.containsKey(starList)) {
                            int value = m.get(starList);
                            m.put(starList, value + 1);
                        }
                        while (!m.containsKey(starList)) {
                            m.put(starList, 1);
                        }
                    }
                }
            }
            return m;
        }


        public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
            List<String> l = new ArrayList();
            return l;
        }


        public List<String> getTopMovies(int top_k, String by) {
            List<String> o = new ArrayList();
            while (by.equals("runtime")) {
                List<String> s = new ArrayList();
                Map<String, Integer> m = new HashMap();
                for (int i = 1; i < this.list.size(); i++) {
                    String temp = this.list.get(i)[4];
                    int runtime = Integer.parseInt(temp.replace(" min", ""));
                    String movieName = this.list.get(i)[1];
                    while (!m.containsKey(movieName)) {
                        m.put(movieName, runtime);
                    }
                }

                Map<String, Integer> sortedMap = m.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())//重写比较器
                        .collect(Collectors
                                .toMap(Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2)->e1,
                                        LinkedHashMap::new));
                List<String> ans = new ArrayList<>();
                sortedMap.forEach((key, value)->{
                    ans.add(key);
                });
                List<String> tmp = new ArrayList<>();
                for (int i = ans.size() - 1; i >= ans.size() - top_k; i++) {
                    tmp.add(ans.get(i));
                }
                return tmp;
            }

            while (by.equals("overview")) {
                List<String> s = new ArrayList();
                Map<String, Integer> m = new HashMap();
                for (int i = 1; i < this.list.size(); i++) {
                    int overview = this.list.get(i)[7].length();
                    String movieName = this.list.get(i)[1];
                    while (!m.containsKey(movieName)) {
                        m.put(movieName, overview);
                    }
                }

                Map<String, Integer> sortedMap = m.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors
                                .toMap(Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2)->e1,
                                        LinkedHashMap::new));
                String l = sortedMap.toString();
                System.out.println(l);
                break;
            }
            //        List<String[]> m = new ArrayList<>();
//        while (by.equals("runtime")){
//            for (int i = 1; i < this.list.size(); i++) {
//                String[] k = new String[2];
//                String name = this.list.get(i)[1];
//                String runtime = this.list.get(i)[4];
//                k[0] = name;
//                k[1] = runtime;
//                m.add(k);
//            }

//            for (int i = 0; i < m.size(); i++) {
//                for (int j = 0; j < m.size()-i-1; j++) {
//                  int pivot = Integer.parseInt(m.get(i)[1]);
//                  int compare = Integer.parseInt(m.get(i+j+1)[1]);
//                  if (pivot >compare){
//
//                  }
//
//                }
//            }
            return o;
        }

        public List<String> getTopStars(int top_k, String by) {//怎么把map转成list
            List<String> o = new ArrayList();
            while (by.equals("rating")) {
                List<String> s = new ArrayList();
                Map<String, Float> m = new HashMap();
                for (int i = 1; i < this.list.size(); i++) {
                    Float rating = Float.parseFloat(this.list.get(i)[6]);
                    String movieName = this.list.get(i)[1];
                    while (!m.containsKey(movieName)) {
                        m.put(movieName, rating);
                    }
                }

                Map<String, Float> sortedMap = m.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors
                                .toMap(Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2)->e1,
                                        LinkedHashMap::new));
                String l = sortedMap.toString();
                System.out.println(l);
                break;
            }

            while (by.equals("gross")) {
                List<String> s = new ArrayList();
                Map<String, Integer> m = new HashMap();
                for (int i = 1; i < this.list.size(); i++) {
                    String temp = this.list.get(i)[15];
                    String movieName = this.list.get(i)[1];
                    if (!temp.equals("")) {
                        temp = temp.replaceAll(",", "").replaceAll("\"", "");
                        int gross = Integer.parseInt(temp);
                        while (!m.containsKey(movieName)) {
                            m.put(movieName, gross);
                        }
                    } else
                        m.put(movieName, -1);
                }

                Map<String, Integer> sortedMap = m.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors
                                .toMap(Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2)->e1,
                                        LinkedHashMap::new));
                String l = sortedMap.toString();
                System.out.println(l);
                break;
            }
            return o;
        }


        public static void main(String[] args) throws IOException, URISyntaxException {
            MovieAnalyzer m = new MovieAnalyzer("/Users/lychee./Downloads/A1_Sample/resources/imdb_top_5001.csv");
            System.out.println(m.getMovieCountByYear());
            System.out.println(m.getTopStars(2,"gross"));
            System.out.println(m.getMovieCountByGenre());
          System.out.println(m.getCoStarCount());
          System.out.println(m.getMovieCountByYear());
       System.out.println(m.getTopMovies(20,"runtime"));
       for (int i = 1; i < 5; i++) {
           for (int j = 0; j < m.list.get(i).length; j++) {
               System.out.println(m.list.get(i)[j]);
           }
           System.out.println("-------------");
       } //读取数据样例

        }
    }