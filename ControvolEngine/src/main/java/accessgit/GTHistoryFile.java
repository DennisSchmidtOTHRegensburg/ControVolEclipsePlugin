package accessgit;

import java.io.File;
import java.util.Calendar;

/**
 * Represents one version in the history of a file
 */
public interface GTHistoryFile {

    /**
     * @return Get the file. The file is stored in the os-temp-directory
     */
    public File getFile() throws Exception;

    /**
     * @return name of author
     */
    public String getAuthor();

    /**
     * @return comment entered by commit
     */
    public String getComment();

    /**
     * @return date of commit
     */
    public Calendar getCommitDate();


}
