package com.example.assistant;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.nio.file.Path;
import java.util.*;
import java.io.*;
import java.text.*;

public class KnowledgeBase
{
    public static TreeMap<String,String> quesResponseMap=null;
    public static SQLiteDatabase mydatabase = null;
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private static SQLiteDatabase getDatabaseInstance(String fileName){
        System.out.println("=========Data Base iniittalted "+fileName);
        if(mydatabase != null) return mydatabase;
        try {
            mydatabase = SQLiteDatabase.openOrCreateDatabase(new File(fileName), null);
            System.out.println("================mydatabase : "+mydatabase);
        }catch(SQLiteCantOpenDatabaseException ex){
            System.out.println("======= Error in opening DB========");
            mydatabase = SQLiteDatabase.create(null);
            createData(mydatabase);
        }
        System.out.println("============Data Base activated");
        return mydatabase;
    }

    private static void createData(SQLiteDatabase mydatabase){
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS KnowledgeBase(Question VARCHAR,Answer VARCHAR);");
        mydatabase.execSQL("INSERT INTO KnowledgeBase VALUES('How are You','I am Fine. What About you?');");
        mydatabase.execSQL("INSERT INTO KnowledgeBase VALUES('What is your name','My name is ChatBot 1.0');");
        mydatabase.execSQL("INSERT INTO KnowledgeBase VALUES('where do you live','I live in your phone');");
        mydatabase.execSQL("INSERT INTO KnowledgeBase VALUES('I am Fine','That is Good.');");
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public static TreeMap<String,String> getKnowledgeBase(String fileName) throws Exception {
        quesResponseMap = new TreeMap<String, String>();
        SQLiteDatabase mydatabase = getDatabaseInstance(fileName);
        Cursor resultSet = null;
        try{
        resultSet = mydatabase.rawQuery("Select * from " + ChatBot.DATABASE_NAME, null);
        }catch(Exception e){
            System.out.println("================= No Table found =======");
            createData(mydatabase);
            resultSet = mydatabase.rawQuery("Select * from " + ChatBot.DATABASE_NAME, null);
        }
        System.out.println(" resultSet : "+resultSet);
        while(resultSet.moveToNext() != resultSet.isLast()){
            quesResponseMap.put(resultSet.getString(0),resultSet.getString(1));
        }
        quesResponseMap.put(resultSet.getString(0),resultSet.getString(1));
        System.out.println(quesResponseMap);
        resultSet.close();
        return quesResponseMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public static void saveNewKnowledge(TreeMap<String,String> newKnowledge , String fileName) throws Exception
    {
        SQLiteDatabase mydatabase = getDatabaseInstance(fileName);
        for(String key:  newKnowledge.keySet()){
            ContentValues values = new ContentValues();
            values.put("Question",key);
            values.put("Answer",newKnowledge.get(key));
            mydatabase.insert(ChatBot.DATABASE_NAME,null,values);
        }
        System.out.println("============ saved ");
    }

    public static void saveLogs(String chat,String username) throws Exception
    {

    }

}