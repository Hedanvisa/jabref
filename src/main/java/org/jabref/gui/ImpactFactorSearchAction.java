package org.jabref.gui;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jabref.JabRefGUI;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public class ImpactFactorSearchAction extends AbstractWorker {

	public ImpactFactorSearchAction() {
	}

	@Override
	public void run() {
		BasePanel panel = JabRefGUI.getMainFrame().getCurrentBasePanel();
		List<BibEntry> listEntries = panel.getSelectedEntries();
		
		FinderManager finder = null;
		
		for(BibEntry e : listEntries) {
			System.out.println(e.getTitle().get());
			if(!e.getField(FieldName.IMPACTFACTOR).isPresent()) {
				
				Optional<String> crossrefOptional = e.getField(FieldName.CROSSREF);
				Optional<String> booktitleOptional = e.getField(FieldName.BOOKTITLE);
				
				if(crossrefOptional.isPresent()) { 
					String crossref = crossrefOptional.get();
				
					System.out.println(crossref);
					List<BibEntry> listBooks = panel.getDatabase().getEntries();
					
					
					try {
						finder = new FinderManager(crossref);
						if(booktitleOptional.isPresent())
							finder.setTitle(booktitleOptional.get());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					String value = finder.searchForImpactFactor();
					
					if(!value.isEmpty()) {
						e.setField(FieldName.IMPACTFACTOR, value);
						
						for(BibEntry e2 : listBooks) {
							
							if(e2.hasCiteKey() && e2.getCiteKeyOptional().get().equals(crossref)) {
								e2.setField(FieldName.IMPACTFACTOR, value);
							}
						}
						return;
					}
				}
				
				if(booktitleOptional.isPresent()) {
					String bookTitle = booktitleOptional.get();
					bookTitle.replaceAll("\\}", "");
					System.out.println(bookTitle);
					
					finder = null;
					try {
						finder = new FinderManager(bookTitle);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String value = finder.searchForImpactFactor();
					
					if(!value.isEmpty()) {
						e.setField(FieldName.IMPACTFACTOR, value);
						return;
					}
				}
				String value = "Unavailable";
				e.setField(FieldName.IMPACTFACTOR, value);
				
			}
		}
	}
	
}
