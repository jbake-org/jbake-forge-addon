package org.jbake.forge.addon.utils;

import org.jbake.forge.addon.types.ContentType;
import org.jbake.forge.addon.types.PublishType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Mani Manasa Mylavarapu on 4/24/15.
 * A utility class for creating page and post
 */
public class ContentUtil {
    static File file;
     public static void createFile(String filePath,ContentType contentType,String pageTitle) {

        file = new File(filePath+File.separator+pageTitle+contentType.extn());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean writeFile(ContentType contentType, String pageTitle, String creationOrModificationDate, String pageOrPost, String tags, PublishType pageStatusType) {
        FileWriter fWriter = null;
        BufferedWriter writer = null;
        try {
            fWriter = new FileWriter(file);
            /*title=First blog
                    date=2014-12-02
            type=post
            tags=blog
            status=published
            ~~~~~~*/
            writer = new BufferedWriter(fWriter);
            writer.write("title="+pageTitle);
            writer.newLine(); //this is not actually needed for html files - can make your code more readable though
            writer.write("date=" + creationOrModificationDate);
            writer.newLine();
            writer.write("type=" + pageOrPost);
            writer.newLine();
            writer.write("tags=" + tags);
            writer.newLine();
            writer.write("status=" + pageStatusType);
            writer.newLine();
            writer.write("~~~~~~");
            writer.newLine();
            writer.close(); //make sure you close the writer object
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if(file.exists())
                return true;
            else
                return false;
        }
    }
}
