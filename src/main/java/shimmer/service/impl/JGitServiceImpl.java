package shimmer.service.impl;

import java.io.File;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jgit.api.Git;
import org.omnifaces.util.Messages;

import shimmer.domain.Graph;
import shimmer.service.JGitService;

@Named
public class JGitServiceImpl implements JGitService {

	
	// ************************************************************************
	// STATIC FIELDS
		
	private static final String TEMPORARY_REPOSITORY = "cloned";

	// ************************************************************************
	// TOOLS
	
	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	@Named("fileProperties")
	private Properties fileProperties;
	
	// ************************************************************************
	// IMPLEMENTATIONS

	@Override
	public void applyHistoricalAnalysis(Graph graph, String gitUrl) {
		
	}
	
	@Override
	public String cloneRepository(String gitUrl) {
		String localRepositoryPath = fileProperties.getProperty("fileSystem.temp") + File.separator + TEMPORARY_REPOSITORY;
		Git result = null;
		try {
			// prepare a new folder for the cloned repository
	        File localPath = File.createTempFile(localRepositoryPath, "");
	        localPath.delete();
	
	        // then clone
	        result = Git.cloneRepository()
	                .setURI(gitUrl)
	                .setDirectory(localPath)
	                .call();

	        return result.getRepository().getDirectory().getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Could not clone remote repository");
			return null;
        } finally {
        	result.close();
        }
	}
	
	// ************************************************************************
	// HELPER METHORS	
	
	
}
