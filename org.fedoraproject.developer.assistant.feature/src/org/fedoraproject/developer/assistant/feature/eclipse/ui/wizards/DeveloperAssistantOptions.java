package org.fedoraproject.developer.assistant.feature.eclipse.ui.wizards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DeveloperAssistantOptions extends WizardPage {

	private String mainAssistant;
	private String subAssistant;
	private String devassistant;
	
	
	public DeveloperAssistantOptions(String pageName, String mainAssistant, String subAssistant, String devassistantPath) {
		super(pageName);
		this.mainAssistant = mainAssistant;
		this.subAssistant = subAssistant;
		this.devassistant = devassistantPath;
		// TODO Auto-generated constructor stub
	}

	public DeveloperAssistantOptions(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		System.out.println("Creating dialog2"+mainAssistant+subAssistant);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
		label.setText("Specify what options should be used by Developer Assistant");
		try
		{
			Process p = Runtime.getRuntime().exec(devassistant+" "+mainAssistant+" "+subAssistant+ " --help | tail -n 1");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			boolean bOption = false;
			String text="";
			
			while ((s = stdInput.readLine())!= null)
			{
				System.out.println("Line:"+s);
				System.out.println("Line:"+bOption);
				if(s.trim().startsWith("--"))
				{
					if(bOption)
					{
						System.out.println("Print Check box");
						Button check = new Button(composite, SWT.CHECK);
						check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
						check.setSelection(false);
						check.setText(text);
						//Label labelCheck = new Label(composite, SWT.NONE);
						//labelCheck.setText(text);
						text = "";
						//bOption = false;
						//continue;
					}
					String line = s.trim();
					StringTokenizer stLong = new StringTokenizer(line," ");
					stLong.nextToken();
					while (stLong.hasMoreTokens())
					{
						//System.out.println(stLong.nextToken());
						text = text + " " + stLong.nextToken();
					}
					bOption = true;
					System.out.println(text);
				}
				else if(s.trim().startsWith("-"))
				{
					if(s.trim().startsWith("-h"))
					{
						if(bOption)
						{
							System.out.println("Print Check box");
							Button check = new Button(composite, SWT.CHECK);
							check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
							check.setSelection(false);
							check.setText(text);
							//Label labelCheck = new Label(composite, SWT.NONE);
							//labelCheck.setText(text);
						}
						bOption =false;
						continue;
					}
					else if(s.trim().startsWith("-n"))
					{
						if(bOption)
						{
							System.out.println("Print Check box");
							Button check = new Button(composite, SWT.CHECK);
							check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
							check.setSelection(false);
							check.setText(text);
							//Label labelCheck = new Label(composite, SWT.NONE);
							//labelCheck.setText(text);
						}
						text = "";
						bOption =false;
						continue;
					}
					else if(s.trim().startsWith("-e"))
					{
						if(bOption)
						{
							System.out.println("Print Check box");
							Button check = new Button(composite, SWT.CHECK);
							check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
							check.setSelection(false);
							check.setText(text);
							//Label labelCheck = new Label(composite, SWT.NONE);
							//labelCheck.setText(text);
						}
						text = "";
						bOption = false;
						continue;
					}
					else if(bOption)
					{
						System.out.println("Print Check box");
						Button check = new Button(composite, SWT.CHECK);
						check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
						check.setSelection(false);
						check.setText(text);
						//Label labelCheck = new Label(composite, SWT.NONE);
						//labelCheck.setText(text);
						text = "";
						bOption = true;
						continue;
					}
					bOption = true;
				}
				else
				{
					if(bOption)
					{
						text = text + " " + s.trim();
						System.out.println("Gather text"+text);
					}
				}
			}
			//System.out.println("Loop where finished");
			if(bOption)
			{
				Button check = new Button(composite, SWT.CHECK);
				check.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
				check.setSelection(false);
				check.setText(text);
				//Label labelCheck = new Label(composite, SWT.NONE);
				//labelCheck.setText(text);
				text = "";
			}
		}
		catch (IOException ie)
		{
			System.out.println("");
		}
		setControl(composite);
	}

}
