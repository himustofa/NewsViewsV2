package com.apps.newsviews.utility;

public class ConstantKey {

    public final static String WEB_URL = "webUrl";
    public final static String NUMBER_API_RESULT_KEY = "resultKey";

    public final static String BASE_URL = "https://newsapi.org/v2/";
    public final static String COIN = "Apple";
    public final static String DATE = "2018-12-27";
    public final static String SORT = "popularity";
    public final static String API = "9688fe5fdd5148c4bcee81fcf94e1837"; //https://newsapi.org/
    public final static String SOURCE = "bbc-news";
    public final static String NUMBER_API = "http://numbersapi.com/number/type"; //http://numbersapi.com/42/trivia

    //@GET("everything")
    //Call<ResponseModel> getNews(@Query("q") String q, @Query("from") String from, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
    //private static final String BASE_URL = "https://newsapi.org/v2/everything?q=bitcoin&from=2018-11-26&sortBy=publishedAt&apiKey=9688fe5fdd5148c4bcee81fcf94e1837";
    //public final static String url = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=9688fe5fdd5148c4bcee81fcf94e1837";


    public final static String COLUMN_ID = "id";
    public final static String HISTORY_TABLE_NAME = "history_table";
    public final static String HISTORY_COLUMN1 = "history_item";
    public final static String HISTORY_COLUMN2 = "created_at";

    public final static String CREATE_HISTORY_TABLE = "CREATE TABLE " + HISTORY_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HISTORY_COLUMN1 + " TEXT, " + HISTORY_COLUMN2 + " TEXT ) ";
    public final static String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME + " ";
    public final static String SELECT_HISTORY_TABLE = "SELECT * FROM " + HISTORY_TABLE_NAME;
}
