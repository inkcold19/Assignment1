package Assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class MovieAnalyzer {

  private List<String[]> list;
  private String dataset_path;


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
    m.forEach((key, value) -> {
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
      String[] genreType = new String[processed.split(",").length];
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
    m.forEach((key, value) -> {
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


  public List<String> getTopMovies(int top_k, String by) {
    List<String> o = new ArrayList();
    class tuple {

      String a;
      int b;

      public tuple(String a, int b) {
        this.a = a;
        this.b = b;
      }
    }

    if (by.equals("runtime")) {

      List<tuple> s = new ArrayList();
      for (int i = 1; i < this.list.size(); i++) {
        String temp = this.list.get(i)[4];
        String movieName = this.list.get(i)[1].replaceAll("\"", "");
        int runtime = Integer.parseInt(temp.replace(" min", ""));
        tuple a = new tuple(movieName, runtime);
        s.add(a);
      }

      Map<String, Integer> m = new HashMap();
      for (int i = 1; i < this.list.size(); i++) {
        String temp = this.list.get(i)[4];
        int runtime = Integer.parseInt(temp.replace(" min", ""));
        String movieName = this.list.get(i)[1].replaceAll("\"", "");
        while (!m.containsKey(movieName)) {
          m.put(movieName, runtime);
        }
      }

      ArrayList<tuple> temp = new ArrayList<>();
      m.forEach((key, value) -> {
        temp.add(new tuple(key, value));
      });

      for (int x = 0; x < s.size() - 1; x++) {
        for (int i = 0; i < s.size() - 1 - x; i++) {
          if (s.get(i).b > s.get(i + 1).b) {
            String current1 = s.get(i).a;
            int current = s.get(i).b;
            s.get(i).b = s.get(i + 1).b;
            s.get(i).a = s.get(i + 1).a;
            s.get(i + 1).b = current;
            s.get(i + 1).a = current1;
          } else if (s.get(i).b == s.get(i + 1).b) {
            if (s.get(i).a.compareTo(s.get(i + 1).a) < 0) {
              String current1 = s.get(i).a;
              int current = s.get(i).b;
              s.get(i).b = s.get(i + 1).b;
              s.get(i).a = s.get(i + 1).a;
              s.get(i + 1).b = current;
              s.get(i + 1).a = current1;
            }
          }
        }
      }

      List<String> tmp = new ArrayList<>();
      for (int i = s.size() - 1; i >= s.size() - top_k; i--) {
        tmp.add(s.get(i).a);
      }

      return tmp;
    }

    if (by.equals("overview")) {
      List<tuple> s = new ArrayList<>();
      for (int i = 1; i < this.list.size(); i++) {
        int overview;
        if (this.list.get(i)[7].startsWith("\"") && this.list.get(i)[7].endsWith("\"")) {
          overview = this.list.get(i)[7].length() - 2;
        } else {
          overview = this.list.get(i)[7].length();
        }

        String movieName = this.list.get(i)[1].replaceAll("\"", "");
        tuple a = new tuple(movieName, overview);
        s.add(a);
      }

      Comparator<tuple> comparator = new Comparator<tuple>() {
        @Override
        public int compare(tuple o1, tuple o2) {
          return (o2.b - o1.b == 0) ? o1.a.compareTo(o2.a) : o2.b - o1.b;
        }
      };
      Collections.sort(s, comparator);

      List<String> tmp = new ArrayList<>();
      for (int i = 0; i < top_k; i++) {
        tmp.add(s.get(i).a);
      }
      tmp.forEach(System.out::println);
      return tmp;
    }
    return o;
  }


  public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
    List<String> l = new ArrayList<>();
    for (int i = 1; i < this.list.size(); i++) {
      String genre1 = this.list.get(i)[5];
      float rating1 = Float.parseFloat(this.list.get(i)[6]);
      int runtime1 = Integer.parseInt(this.list.get(i)[4].replace(" min", ""));
      if (genre1.contains(genre) && min_rating <= rating1 && max_runtime >= runtime1) {
        System.out.println(rating1);
        String title = this.list.get(i)[1];
        if (this.list.get(i)[1].startsWith("\"")) {
          title = title.substring(1);
        }
        if (this.list.get(i)[1].endsWith("\"")) {
          title = title.substring(0, title.length() - 1);
        }
        l.add(title);
      }

    }
    Collections.sort(l);
    return l;
  }

  public List<String> getTopStars(int top_k, String by) {
    ArrayList<String> o = new ArrayList<>();

    if (by.equals("rating")) {
      Map<String, List<String>> mapStarName = new HashMap<>();
      for (int i = 1; i < this.list.size(); i++) {
        for (int j = 10; j < 14; j++) {
          List<String> l = new ArrayList<>();
          String starCurrent = this.list.get(i)[j];
          while (!mapStarName.containsKey(starCurrent)) {
            mapStarName.put(starCurrent, l);
          }
        }
      }

      Map<String, Double> end = new HashMap<>();
      mapStarName.forEach((key1, value) -> {
        List<String> movieName = new ArrayList<>();
        double Average = 0;
        double totalScore = 0;
        for (int j = 1; j < this.list.size(); j++) {
          for (int k = 10; k < 14; k++) {
            if (key1.equals(this.list.get(j)[k])) {
              String movieCurrent = this.list.get(j)[1];
              movieName.add(movieCurrent);
              totalScore = totalScore + Double.parseDouble(this.list.get(j)[6]);
              break;
            }
            Average = totalScore / movieName.size();
          }
        }
        end.put(key1, Average);
      });

      class tuple {

        String starName;
        double averageRating;

        public tuple(String starName, double averageRating) {
          this.starName = starName;
          this.averageRating = averageRating;
        }
      }

      List<tuple> tupleList = new ArrayList<>();
      end.forEach((key, value) -> {
        tupleList.add(new tuple(key, value));
      });

      for (int x = 0; x < tupleList.size() - 1; x++) {
        for (int i = 0; i < tupleList.size() - 1 - x; i++) {
          if (tupleList.get(i).averageRating > tupleList.get(i + 1).averageRating) {
            String current1 = tupleList.get(i).starName;
            double current = tupleList.get(i).averageRating;
            tupleList.get(i).averageRating = tupleList.get(i + 1).averageRating;
            tupleList.get(i).starName = tupleList.get(i + 1).starName;
            tupleList.get(i + 1).averageRating = current;
            tupleList.get(i + 1).starName = current1;
          } else if (tupleList.get(i).averageRating == tupleList.get(i + 1).averageRating) {
            if (tupleList.get(i).starName.compareTo(tupleList.get(i + 1).starName) < 0) {
              String current1 = tupleList.get(i + 1).starName;
              double current = tupleList.get(i + 1).averageRating;
              tupleList.get(i + 1).averageRating = tupleList.get(i).averageRating;
              tupleList.get(i + 1).starName = tupleList.get(i).starName;
              tupleList.get(i).averageRating = current;
              tupleList.get(i).starName = current1;
            }
          }
        }
      }

      List<String> tmp = new ArrayList<>();
      for (int i = tupleList.size() - 1; i >= tupleList.size() - top_k; i--) {
        tmp.add(tupleList.get(i).starName);
      }
      return tmp;
    }

    if (by.equals("gross")) {
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
        } else {
          m.put(movieName, -1);
        }
      }

      Map<String, Integer> sortedMap = m.entrySet()
          .stream()
          .sorted(Map.Entry.comparingByValue())
          .collect(Collectors
              .toMap(Map.Entry::getKey,
                  Map.Entry::getValue,
                  (e1, e2) -> e1,
                  LinkedHashMap::new));
      String l = sortedMap.toString();
      System.out.println(l);
    }
    return o;
  }


  public static void main(String[] args) throws IOException, URISyntaxException {
    MovieAnalyzer m = new MovieAnalyzer(
        "/Users/lychee./Downloads/A1_Sample/resources/imdb_top_5001.csv");
    System.out.println(m.getMovieCountByYear());
    System.out.println(m.getTopStars(2, "gross"));
    System.out.println(m.getMovieCountByGenre());
    List<String> a = new ArrayList();
    a.add("sss");
    a.add("aaa");
    System.out.println(m.getCoStarCount().keySet().size());
    System.out.println(m.getTopStars(20, "rating"));
    System.out.println(m.getTopMovies(5, "overview"));

    for (int i = 1; i < m.list.size(); i++) {
      for (int j = 0; j < m.list.get(i).length; j++) {
        System.out.println(m.list.get(i)[j]);
      }
      System.out.println("-------------");
    } //读取数据样例
  }
}
