package com.example.assistivetechfrontend;



import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


public class FileIO {
    static private Context context;

    public FileIO(Context context)
    {
        this.context = context;
    }

    //write content to the file(append content to the file)
    public boolean write(String dataname,String content)
    {

        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(dataname, Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //write content to the file(clear the previous content in the file)
    public boolean privatewrite(String dataname,String content)
    {

        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(dataname, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //load content in the file
    public String load(String dataname)
    {
        FileInputStream in =null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            in = context.openFileInput(dataname);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine())!=null){
                content.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(reader!=null){
                    reader.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

}
