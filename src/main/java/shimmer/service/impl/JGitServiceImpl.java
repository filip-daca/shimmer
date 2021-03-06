package shimmer.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.omnifaces.util.Messages;

import shimmer.domain.Graph;
import shimmer.service.FileService;
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
	
	@Inject
	private FileService fileService;
	
	// ************************************************************************
	// IMPLEMENTATIONS

	@Override
	public void cloneAndAnalyse(Graph graph, String gitUrl) {
		String repositoryPath = cloneRepository(gitUrl);
		if (repositoryPath != null) {
			analyse(graph, repositoryPath);
		}
	}
	
	@Override
	public void analyse(Graph graph, String repositoryPath) {
		File workTree = new File(repositoryPath);
		Repository repository;
		try {
			repository = new RepositoryBuilder().setGitDir(workTree).readEnvironment()
			        .findGitDir().build();
		} catch (IOException e) {
			e.printStackTrace();
			Messages.addGlobalError("Could not build cloned remote repository");
			return;
		}
		
		List<File> filesToAnalyse = new ArrayList<File>();
		filesToAnalyse.addAll(fileService.listClassFiles(workTree.getParentFile()));

		for (File file : filesToAnalyse) {
			if (!graph.isFileRepresented(file.getPath())) {
				continue;
			}
			
			String filePath = file.getPath().substring(workTree.getParent().length() + 1).replace('\\', '/');
			
			Git git = new Git(repository);
			LogCommand logCommand = null;
			try {
				logCommand = git.log()
						.setMaxCount(500)
				        .add(git.getRepository().resolve(Constants.HEAD))
				        .addPath(filePath);
				
				for (RevCommit commit : logCommand.call()) {
					graph.noticeCommit(file.getPath(), new Date((long) commit.getCommitTime() * 1000L), 
							commit.getAuthorIdent().getName());
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				Messages.addGlobalError("Could not analyse history of file " + file.getName());
				continue;
			}
			
		}
		
		repository.close();
	}
	
	// ************************************************************************
	// HELPER METHORS	

	private String cloneRepository(String gitUrl) {
		String localRepositoryPath = fileProperties.getProperty("fileSystem.temp") + TEMPORARY_REPOSITORY;
		Git result = null;
		try {
			// prepare a new folder for the cloned repository
	        File localPath = fileService.prepareDirectory(localRepositoryPath);
	
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
	
}
