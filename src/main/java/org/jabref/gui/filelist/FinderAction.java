package org.jabref.gui.filelist;

import org.jabref.JabRefGUI;
import org.jabref.gui.BasePanel;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FinderAction extends AbstractWorker {

    private BasePanel panel;

    public FinderAction() {
    }

    @Override
    public void run() {
        panel = JabRefGUI.getMainFrame().getCurrentBasePanel();
        List<BibEntry> entradas = panel.getSelectedEntries();

        for( BibEntry entry : entradas ) {
            panel.output(Localization.lang("Searching for file in this computer") + '.');
            Optional<String> o = entry.getTitle();
            String s = o.get();
            Path startingDir = Paths.get(System.getProperty("user.home"));
            String pattern = s + ".{pdf,docx,doc,ppt,pptx,odt,epub,mobi}"; // "(?i)" + ".(docx|pdf)"
            Finder finder = new Finder(pattern);
            try {
                Files.walkFileTree(startingDir, finder);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (!finder.getPaths().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The search has found a result for " + s + " in " + finder.getFirstPath());
                FileListEntry flEntry = new FileListEntry("", "");

                flEntry.setLink(finder.getFirstPath());
                FileListTableModel model = new FileListTableModel();
                entry.getField(FieldName.FILE).ifPresent(model::setContent);
                model.addEntry(model.getRowCount(), flEntry);
                String newVal = model.getStringRepresentation();

                entry.setField(FieldName.FILE, newVal);
            } else {
                JOptionPane.showMessageDialog(null, "We didn't find any results for \"" + s + "\"");
            }
        }
    }
}

