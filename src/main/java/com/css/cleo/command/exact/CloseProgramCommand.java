package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import com.css.cleo.util.GuiUtil;
import edu.cmu.sphinx.api.SpeechResult;

public class CloseProgramCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        if (GuiUtil.confirm("Are you sure close Cleo?"))
            System.exit(0);
    }
}
