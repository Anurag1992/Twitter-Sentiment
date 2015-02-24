/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author 007
 */
public class TweetHandler {
    public  List<Status> getTweets(String topic) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("");
        cb.setOAuthConsumerSecret("");
        cb.setOAuthAccessToken("");
        cb.setOAuthAccessTokenSecret("");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try 
        {
            Query query = new Query(topic);
            query.setCount(100);
            query.setLang("en");
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            
            return tweets;
        } 
        catch (TwitterException te) 
        {
            te.printStackTrace();
            return null;
        }
}
}
