package org.jabref.gui.filelist;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;

public class Finder extends SimpleFileVisitor<Path> {
	
	private final PathMatcher matcher;
    private int numMatches = 0;
    private List<Path> paths;

	public Finder(String pattern) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		paths = new ArrayList<>();
	}
	
	public List<Path> getPaths() {
		return paths;
	}
	
	public String getFirstPath() {
		return paths.get(0).toString();
	}
	
	// Compares the glob pattern against
    // the file or directory name.
    void find(Path file) {
        Path name = file.getFileName();
        if (name != null && matcher.matches(name)) {
            numMatches++;
            System.out.println(file);
            paths.add(file);
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
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }

}
