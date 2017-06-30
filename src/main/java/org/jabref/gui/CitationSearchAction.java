package org.jabref.gui;

import java.util.ArrayList;
import java.util.List;

import org.jabref.JabRefGUI;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public class CitationSearchAction extends AbstractWorker {
	private static int count = 0;
	public CitationSearchAction() {
	}

	@Override
	public void run() {
		BasePanel panel = JabRefGUI.getMainFrame().getCurrentBasePanel();
		List<BibEntry> listEntries = panel.getSelectedEntries();
		
		for(BibEntry e : listEntries) {
			// TODO Auto-generated constructor for Daniels
			String value = "Vitorio " + count;
			e.setField(FieldName.CITATIONS, value);
			count++;
			System.out.println(count);
		}
	}

}
