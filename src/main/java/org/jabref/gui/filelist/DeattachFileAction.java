package org.jabref.gui.filelist;

import org.jabref.gui.BasePanel;
import org.jabref.gui.actions.BaseAction;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public class DeattachFileAction implements BaseAction {

    private final BasePanel panel;


    public DeattachFileAction(BasePanel panel) {
        this.panel = panel;
    }

    @Override
    public void action() {
        if (panel.getSelectedEntries().size() != 1) {
            panel.output(Localization.lang("This operation requires exactly one item to be selected."));
            return;
        }
        BibEntry entry = panel.getSelectedEntries().get(0);
        entry.clearField(FieldName.FILE);
    }

}
