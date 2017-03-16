
package com.proj.forummatrix.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author amendrashrestha
 */
public class WriteIntoFile {
    
    final private String path;
    private boolean append_to_file = false;
    
    public WriteIntoFile(String file_path){
        path = file_path;
    }
    
    public WriteIntoFile(String file_path, boolean append_value){
        path = file_path;
        append_to_file = append_value;
    }
    
    public void writeToFile(String textLine) throws IOException{
        FileWriter write = new FileWriter(path, append_to_file);
        try (PrintWriter print_line = new PrintWriter(write)) {
            print_line.printf("%s", textLine + "\n");
        }
    }
}
