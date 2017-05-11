package org.jabref.gui.filelist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jabref.gui.BasePanel;
import org.jabref.gui.actions.BaseAction;
import org.jabref.gui.undo.UndoableFieldChange;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public class FinderAction extends Thread implements BaseAction {
	
	private final BasePanel panel;
	
	public FinderAction(BasePanel panel) {
		this.panel = panel;
	}
	
	public void run() {
		BibEntry entry = panel.getSelectedEntries().get(0);
		panel.output(Localization.lang("Searching for file in this computer") + '.');
    	Optional<String> o = entry.getTitle();
    	String s = o.get();
    	System.out.println(s);
    	Path startingDir = Paths.get(System.getProperty("user.home"));
    	String results = StringEscapeUtils.escapeJava(s);
    	String pattern = s + ".pdf"; // "(?i)" + ".(docx|pdf)"
    	Finder finder = new Finder(pattern);
    	try {
			Files.walkFileTree(startingDir, finder);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	if(!finder.getPaths().isEmpty()) {
    		JOptionPane.showMessageDialog(null, "The search has found a result for " + s + " in " + finder.getFirstPath());
        	FileListEntry flEntry = new FileListEntry("", "");
        	
        	flEntry.setLink(finder.getFirstPath());
        	FileListTableModel model = new FileListTableModel();
            entry.getField(FieldName.FILE).ifPresent(model::setContent);
            model.addEntry(model.getRowCount(), flEntry);
            String newVal = model.getStringRepresentation();
            System.out.println("MainTable" + newVal);

            UndoableFieldChange ce = new UndoableFieldChange(entry, FieldName.FILE, entry.getField(FieldName.FILE).orElse(null), newVal);
            entry.setField(FieldName.FILE, newVal);
    	} else {
    		JOptionPane.showMessageDialog(null, "We didn't find any results for \"" + s +"\"");
    	}
	}

	@Override
	public void action() throws Exception {
		run();
	}
	
}
