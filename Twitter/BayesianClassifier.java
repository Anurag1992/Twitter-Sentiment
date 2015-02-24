
package Twitter;

import com.esotericsoftware.kryo.Kryo;
import com.opencsv.CSVReader;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import twitter4j.Status;
/**
 *
 * @author 007
 */
public class BayesianClassifier {

    /**
     * @param args the command line arguments
     */
     int poscount,negcount,neutralcount;
    HashMap<String,Integer> ngramdata;
    BayesianClassifier() throws FileNotFoundException, IOException, ClassNotFoundException
    {      RandomAccessFile raf = new RandomAccessFile("C:\\Users\\Anurag\\Documents\\NetBeansProjects\\Twitter007\\resources\\ngramdata.tmp", "rw");  
           FileInputStream fis = new FileInputStream(raf.getFD());
           BufferedInputStream bis = new BufferedInputStream(fis);
           ObjectInputStream d = new ObjectInputStream(bis);
           ngramdata =  (HashMap<String,Integer>) d.readObject();
         
    }
   
   
 
  public Integer classifier(String tweet) throws FileNotFoundException, IOException, ClassNotFoundException
  {   
    
      List<String> tweetwords = tokenizer(tweet);
     
      int negcount = ngramdata.get("negcount");
      int poscount = ngramdata.get("poscount");
      double posProb =  (poscount+0.0)/(poscount+negcount);
      double negProb =  (negcount+0.0)/(poscount+negcount);
      Double [] posarray = new Double [tweetwords.size()];
      Double [] negarray = new Double [tweetwords.size()];
      int p=0,n=0;
      /*filter stop words*/
      CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Anurag\\Documents\\NetBeansProjects\\Twitter007\\resources\\stopwords.csv"));
      String [] nextL;
      List<String> stopwords = new ArrayList<String>();
      while ((nextL = reader.readNext()) != null)
       {
             stopwords.add(nextL[0]);
       }
      for(int i=0;i<tweetwords.size();i++)
       {
           if(stopwords.contains(tweetwords.get(i)))
               tweetwords.remove(tweetwords.get(i));
       }
      
      /***Pos*/
      for(String word : tweetwords)
          if(ngramdata.containsKey(word+"pos"))
          {
              posarray[p]  =  (ngramdata.get(word+"pos")+1.00)/poscount;
             
              p++;
          }
          else
          {
              posarray[p] = 1.00/poscount;
           
              p++;
          }
       
      for(String word : tweetwords)
          if(ngramdata.containsKey(word+"neg"))
          {
              negarray[n]  =  (ngramdata.get(word+"neg")+1.00)/negcount;
              
              n++;
          }
          else
          {
              negarray[n] = 1.00/negcount;
            
              n++;
          }
      double pProb=1.00,nProb=1.00;
      for(int i=0;i<n;i++)
      {  pProb = pProb*posarray[i];
         
      }
      pProb = pProb*posProb;
      for(int i=0;i<n;i++)
          nProb = nProb*negarray[i];
      nProb = nProb*negProb;
      
      if(pProb>nProb)
          return 4;
      else if(pProb<nProb)
          return 0;
      else
          return 2;
      
  }
    
    /* Creates ngrams, for n iterate n times)*/
    public static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        String[] words = str.toLowerCase().split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append(i > start ? " " : "").append(words[i]);
        return sb.toString();
    }
    
    public static List<String> tokenizer(String inputTweet)
    {  
        inputTweet = inputTweet.replaceAll("((www\\.[\\s]+)|(https?://[^\\s]+))", "URL").replaceAll("@[^\\s]+", "").replaceAll("[\\s]+", " ").replaceAll("#","").replaceAll("[^0-9a-zA-Z\\s]+","");
        inputTweet = inputTweet.trim();
        final List<String> list1= ngrams(1,inputTweet);
        final List<String> list2= ngrams(2,inputTweet);
        List<String> finalList = new ArrayList<String>() { { addAll(list1); addAll(list2); } };
        return finalList;

    }
    
   
}
