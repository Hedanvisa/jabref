package org.jabref.gui;

import java.util.List;

import org.jabref.JabRefGUI;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

import ca.odell.glazedlists.matchers.SearchEngineTextMatcherEditor.Field;

public class ImpactFactorSearchAction extends AbstractWorker {

	public ImpactFactorSearchAction() {
	}

	@Override
	public void run() {
		BasePanel panel = JabRefGUI.getMainFrame().getCurrentBasePanel();
		List<BibEntry> listEntries = panel.getSelectedEntries();
		
		for(BibEntry e : listEntries) {
			// TODO Auto-generated constructor for Daniels
			String value = "Savio";
			e.setField(FieldName.IMPACTFACTOR, value);
		}
	}
	
}
