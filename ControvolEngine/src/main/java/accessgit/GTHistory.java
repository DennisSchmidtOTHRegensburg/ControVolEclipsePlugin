package accessgit;

import java.io.File;
import java.util.List;

/**
 * Represent the history of one specific file
 */
public interface GTHistory {


    /**
     * @return Get the most up2date file
     */
    public File getCurrentFile() throws Exception;


    /**
     * @return Get the full history.
     */
    public List<GTHistoryFile> getHistoryFiles() throws Exception;

}
