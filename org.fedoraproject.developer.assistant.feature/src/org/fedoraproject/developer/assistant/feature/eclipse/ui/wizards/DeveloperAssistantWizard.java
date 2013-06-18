package org.fedoraproject.developer.assistant.feature.eclipse.ui.wizards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

public class DeveloperAssistantWizard extends Wizard implements INewWizard{
	private DeveloperAssistantLanguageSelection langPage;
	private DeveloperAssistantOptions optionsPage;
	private DeveloperAssistantProjectPage projectPage;
	
	public DeveloperAssistantWizard() {
		// TODO Auto-generated constructor stub
		setWindowTitle("Developer Assistant Wizard");
		projectPage = new DeveloperAssistantProjectPage("Developer Assistant project name and path specification");
		langPage = new DeveloperAssistantLanguageSelection("Developer Assistant Language Selection");
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		addPage(projectPage);
		addPage(langPage);
		//addPage(optionsSelection);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		System.out.println("Method for generating project");
		String projectName = projectPage.getProjectName();
		IPath projectPath = projectPage.getLocationPath();
		System.out.println("ProjectName: "+projectName);
		System.out.println("ProjectPath: "+projectPath);
		String mainAssist = (langPage).getMainAssistant();
		String subAssist = (langPage).getSubAssistant();
		System.out.println("Main Assistant:"+mainAssist);
		System.out.println("Main Assistant:"+subAssist);
		if (mainAssist.compareTo("")==0)
		{
			System.out.println("You have to select at least one main assistant");
			return false;
		}
		else if (langPage.getSubListState()&&subAssist.compareTo("")==0)
		{
			System.out.println("You have to select at least one sub assistant");
			return false;
		}
		
		try
		{
			String parameters = " -n "+projectName+" -e"+projectPath;
			Process p = Runtime.getRuntime().exec(langPage.getDevAssistantPath()+" "+mainAssist+" "+subAssist+parameters);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = stdInput.readLine())!= null)
			{
				System.out.println(s);
			}
		}
		catch (IOException ie)
		{
			return false;
		}
		return false;
	}

	/**
	 * @see {@link org.eclipse.ui.iWorkbenchWizard.init}
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// intentionally left blank
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page)
	{
		if(page instanceof DeveloperAssistantLanguageSelection)
		{
			String mainAssist = ((DeveloperAssistantLanguageSelection)page).getMainAssistant();
			String subAssist = ((DeveloperAssistantLanguageSelection)page).getSubAssistant();
			optionsPage = new DeveloperAssistantOptions("Select options which should be setup",mainAssist, subAssist,((DeveloperAssistantLanguageSelection)page).getDevAssistantPath());
			addPage(optionsPage);
			return optionsPage;
		}
		return super.getNextPage(page);
	}
}
