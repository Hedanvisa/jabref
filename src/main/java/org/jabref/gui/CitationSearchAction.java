package org.jabref.gui;

import java.io.IOException;
import java.util.List;

import org.jabref.JabRefGUI;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public class CitationSearchAction extends AbstractWorker {
	public CitationSearchAction() {
	}

	@Override
	public void run() {
		BasePanel panel = JabRefGUI.getMainFrame().getCurrentBasePanel();
		List<BibEntry> listEntries = panel.getSelectedEntries();
		
		for(BibEntry e : listEntries) {
			if(!e.getType().equals("proceedings")) {
				FinderManager finder = null;
				try {
					finder = new FinderManager(e.getTitle().get().replaceAll("[\\{\\}]", ""));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				String value = finder.searchForCitations();
				
				if(!value.isEmpty()) {
					e.setField(FieldName.CITATIONS, value);
				} else {
					value = "Unavailable";
					e.setField(FieldName.CITATIONS, value);
				}
			}
			else {
				e.setField(FieldName.CITATIONS, "Not applicable");
			}
		}
	}

}
