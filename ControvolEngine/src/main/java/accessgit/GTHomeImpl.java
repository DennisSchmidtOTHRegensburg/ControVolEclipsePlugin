package accessgit;

import java.io.File;

/**
 * Implementation of home-interface
 */
public class GTHomeImpl implements GTHome{

    @Override
    public GTHistory getGitHistoryOfFile(File f) throws Exception{
        return new MGitHistory().getHistory(f);
    }
}