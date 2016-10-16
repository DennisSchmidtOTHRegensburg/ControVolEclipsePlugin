package accessgit;

import java.io.File;

/**
 * Home for GitAccess-Project
 */
public interface GTHome {

    /**
     * Determins the information by using the path to find the GIT-Home
     * @param f, the current version
     * @return Retrieve all previous versions of a file
     */
    public GTHistory getGitHistoryOfFile(File f) throws Exception;

}
