import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

 public class Walk {
	 
	   public static class Finder
       extends SimpleFileVisitor<Path> {

       private final PathMatcher matcher;
       private int numMatches = 0;

       Finder(String pattern) {
           matcher = FileSystems.getDefault()
                   .getPathMatcher("glob:" + pattern);
       }

       // Compares the glob pattern against
       // the file or directory name.
       void find(Path file) {
           Path name = file.getFileName();
           if (name != null && matcher.matches(name)) {
               numMatches++;
               System.out.println(file);
           }
       }

       // Prints the total number of
       // matches to standard out.
       void done() {
           System.out.println("Matched: "
               + numMatches);
       }

       // Invoke the pattern matching
       // method on each file.
       @Override
       public FileVisitResult visitFile(Path file,
               BasicFileAttributes attrs) {
           find(file);
           return CONTINUE;
       }

       // Invoke the pattern matching
       // method on each directory.
       @Override
       public FileVisitResult preVisitDirectory(Path dir,
               BasicFileAttributes attrs) {
           find(dir);
           return CONTINUE;
       }

       @Override
       public FileVisitResult visitFileFailed(Path file,
               IOException exc) {
           System.err.println(exc);
           return CONTINUE;
       }
	 
     public static void main(String args[]) throws IOException {
    	 
         Path startingDir = Paths.get(System.getProperty("user.home"));
        // System.out.println(System.getProperty("user.home"));
         String pattern = "resumo.md";
         Finder finder = new Finder(pattern);
         Files.walkFileTree(startingDir, finder);
         finder.done(); 
     } //main
	   }}
 
 
 