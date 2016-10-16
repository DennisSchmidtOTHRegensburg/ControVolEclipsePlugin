package accessgit;

import java.io.File;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Manager for implementing AutoCloseable
 */
public class MRepositoryManager implements AutoCloseable{

    Repository rep = null;

    public Repository getRepository() throws Exception {
        return rep;
    }

    /**
     * Retrieve repository by file
     */
    public MRepositoryManager(File f) throws Exception {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        rep = builder.findGitDir(f).build();
    }

    public static MRepositoryManager getManager(File f) throws Exception {
        return new MRepositoryManager(f);
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        rep.close();
    }
}
