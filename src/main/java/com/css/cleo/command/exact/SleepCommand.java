package com.css.cleo.command.exact;

import com.css.cleo.command.Command;
import com.css.cleo.os.OsFeature;
import com.css.cleo.util.GuiUtil;
import edu.cmu.sphinx.api.SpeechResult;

public class SleepCommand implements Command {
    @Override
    public void execute(OsFeature osFeature, SpeechResult result) {
        if (GuiUtil.confirm("Are u sure about sleep OS?"))
            osFeature.hibernate();
    }
}
