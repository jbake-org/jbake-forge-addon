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
            writer = new BufferedWriter(fWriter);
            if (contentType == ContentType.AsciiDoc) {
                writer.write("= " + pageTitle);
                writer.newLine();
                writer.write(creationOrModificationDate);
                writer.newLine();
                writer.write(":jbake-type: " + pageOrPost);
                writer.newLine();
                writer.write(":jbake-tags: " + tags);
                writer.newLine();
                writer.write("jbake-status: " + pageStatusType);
                writer.newLine();
                writer.newLine();
                writer.newLine();
            } else {
                writer.write("title=" + pageTitle);
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
                writer.newLine();
            }
            writer.close();
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
