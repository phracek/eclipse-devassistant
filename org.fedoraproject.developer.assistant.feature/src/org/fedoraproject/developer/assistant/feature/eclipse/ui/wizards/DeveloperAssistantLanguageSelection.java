package org.fedoraproject.developer.assistant.feature.eclipse.ui.wizards;

import java.io.*;
import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.yaml.snakeyaml.Yaml;

public class DeveloperAssistantLanguageSelection extends WizardPage implements Listener {
	
	private String devassistant="/home/phracek/work/devassistant/devassistant.py";
	private ArrayList<String> mainAss = new ArrayList<String>();
	private ArrayList<String> subAss = new ArrayList<String>();
	private String selAssistant="";
	private List mainList;
	private List subList;
	private Label labelSub;

	public DeveloperAssistantLanguageSelection(String pageName) {
		super(pageName);
		setTitle("Developer Assistant select Languaguge");
		// TODO Auto-generated constructor stub
	}

	public DeveloperAssistantLanguageSelection(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}
	
	private String getAssistants(String assistant) throws FileNotFoundException
	{
		String str="";
		File folder = new File("/home/phracek/work/devassistant/devassistant/assistants/yaml");
		File[] listOfFiles = folder.listFiles();
		for (int i=0; i< listOfFiles.length; i++)
		{
			if(listOfFiles[i].isFile())
			{
				String fileName = listOfFiles[i].getName();
				if(fileName.endsWith(".yaml"))
				{
					System.out.println("File name is:"+fileName);
					InputStream input = new FileInputStream(listOfFiles[i]);
					Yaml yaml = new Yaml();
					for(Object data : yaml.loadAll(input))
					{
						System.out.println(data);
					}
				}
			}
		}
		
		try
		{
			Process p = Runtime.getRuntime().exec(devassistant+" "+assistant+" --help | tail -n 1");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = stdInput.readLine())!= null)
			{
				System.out.println("Line is:"+s);
				StringTokenizer st = new StringTokenizer(s,"{");
				if (!st.hasMoreTokens())
				{
					break;
				}
				st.nextToken();
				if(!st.hasMoreTokens())
				{
					break;
				}
				String list = st.nextToken();
				//System.out.println("Second:"+list);
				StringTokenizer stlast = new StringTokenizer(list,"}");
				if (!stlast.hasMoreTokens())
					break;
				list = stlast.nextToken();
				//System.out.println("Third:"+list);
				StringTokenizer comp = new StringTokenizer(list, ",");
				boolean bSubAssistant = false;
				while(comp.hasMoreTokens())
				{
					bSubAssistant = true;
					String assist = comp.nextToken();
					//System.out.println("subassistants:"+assist);
					//System.out.println("Main assista:"+assistant);
					if(assistant.compareTo("")!=0)
					{
						//System.out.println("Subassistant: "+assist);
						subAss.add(assistant+"="+assist);
						getAssistants(assistant+ "" + assist);
					}
					else
					{
						mainAss.add(assist);
						getAssistants(assistant+ "" + assist);
					}
				}
				if(!bSubAssistant)
					mainAss.add(list);
			}
			
			//System.out.println("Loop where finished");
		}
		catch (IOException ie)
		{
			return "";
		}
		return str;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		System.out.println("Creating dialog");
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		label.setText("Select language which you would like to setup");
		//System.out.println("Calling devassistant binary");
		try
		{
			getAssistants("");
		}
		catch (FileNotFoundException fnf)
		{
			System.out.println("Exception:"+fnf.getMessage());
		}
		mainList = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.LEFT | SWT.READ_ONLY);
		mainList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainList.addListener(SWT.Selection, this);
		for(int i=0; i<mainAss.size(); i++)
		{
			mainList.add(mainAss.get(i));
		}
	
		labelSub = new Label(composite, SWT.NONE);
		labelSub.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		labelSub.setText("Select subassistant which you would like to setup");
		//mainList.addListener(SWT.Selection, this);
		subList = new List(composite, SWT.NONE | SWT.BORDER | SWT.READ_ONLY);
		subList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 20));
		subList.addListener(SWT.Selection, this);
		for(int i=0; i<subAss.size(); i++)		{
			System.out.println(i+" "+subAss.get(i));
			StringTokenizer st = new StringTokenizer(subAss.get(i),"=");
			if(selAssistant.compareTo(st.nextToken())==0)
				subList.add(st.nextToken());
		}
		//setPageComplete(false);
		setControl(composite);
	}
	public void handleEvent(Event e)
	{
		if(e.widget == mainList)
		{
			if(mainList.getSelectionCount() > 0)
			{
				int index = mainList.getSelectionIndex();
				//System.out.println("Index: " +index+ " "+mainAss.get(index));
				selAssistant = mainAss.get(index);
				subList.removeAll();
				boolean bFound=false;
				for(int i=0; i<subAss.size(); i++)
				{
					//System.out.println(subAss.get(i));
					StringTokenizer st = new StringTokenizer(subAss.get(i),"=");
					String main = st.nextToken();
					//System.out.println(main + selAssistant);
					if(selAssistant.compareTo(main)==0)
					{
						bFound = true;
						subList.add(st.nextToken());
					}
				}
				subList.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
				labelSub.setVisible(bFound);
				subList.setVisible(bFound);
			}
		}
		else if(e.widget == subList)
		{
			if(mainList.getSelectionCount() > 0)
			{
				int index = mainList.getSelectionIndex();
				String main = mainAss.get(index);
				System.out.println(main);
				int indexSub = subList.getSelectionIndex();
				int count=0;
				for(int i=0; i<subAss.size();i++)
				{
					StringTokenizer st = new StringTokenizer(subAss.get(i),"=");
					String mainSub = st.nextToken();
					System.out.println(i+" "+subAss.get(i));
					if(mainSub.compareTo(main)==0)
					{
						if(indexSub == count)
							System.out.println("Correct subassistant is:"+subAss.get(i));
						count++;
					}
				}
			}
		}
		
	}
	
	public String getMainAssistant()
	{
		System.out.println(subList.getSelectionCount());
		return selAssistant;
	}

	public String getSubAssistant()
	{
		
		String subassistant="";
		if(mainList.getSelectionCount() > 0)
		{
			int index = mainList.getSelectionIndex();
			String main = mainAss.get(index);
			//System.out.println(main);
			int indexSub = subList.getSelectionIndex();
			int count=0;
			for(int i=0; i<subAss.size();i++)
			{
				StringTokenizer st = new StringTokenizer(subAss.get(i),"=");
				String mainSub = st.nextToken();
				//System.out.println(i+" "+subAss.get(i));
				if(mainSub.compareTo(main)==0)
				{
					if(indexSub == count)
						subassistant = st.nextToken();
					count++;
				}
			}
		}
		return subassistant;
	}
	
	public String getDevAssistantPath()
	{
		return devassistant;
	}
	
	public boolean getSubListState()
	{
		return subList.getVisible();
	}
}
