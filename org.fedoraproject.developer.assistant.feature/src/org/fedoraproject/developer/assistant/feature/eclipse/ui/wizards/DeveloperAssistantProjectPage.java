package org.fedoraproject.developer.assistant.feature.eclipse.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class DeveloperAssistantProjectPage extends WizardNewProjectCreationPage {

	public DeveloperAssistantProjectPage(String pageName) {
		super(pageName);
		setTitle("Developer Assistant project specification page");
		setDescription("Specify project name a workspace directory:");
		// TODO Auto-generated constructor stub
	}

	public DeveloperAssistantProjectPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createControl(parent);
		Composite container = (Composite) getControl();
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		
		setControl(container);
		

	}

}
