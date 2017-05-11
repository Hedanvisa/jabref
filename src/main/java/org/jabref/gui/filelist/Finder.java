package org.jabref.gui.filelist;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Finder extends SimpleFileVisitor<Path> {

	private final PathMatcher matcher;
    private int numMatches = 0;
    private List<Path> paths;
    private String pat;

	public Finder(String pattern) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		paths = new ArrayList<>();
		this.pat = pattern.toLowerCase();
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
       //if (name != null && (FuzzySearch.partialRatio(name.toString().toLowerCase(),this.pat) >= 85) && (FuzzySearch.ratio(name.toString().toLowerCase(),this.pat) >= 76)) {
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
