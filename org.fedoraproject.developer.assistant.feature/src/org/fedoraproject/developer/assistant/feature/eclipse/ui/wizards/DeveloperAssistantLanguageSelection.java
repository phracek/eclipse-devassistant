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
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class DeveloperAssistantLanguageSelection extends WizardPage implements Listener {
	
	private String devassistant="/home/phracek/work/devassistant/devassistant.py";
	private ArrayList<String> mainAss = new ArrayList<String>();
	private ArrayList<String> subAss = new ArrayList<String>();
	private String selAssistant="";
	private List mainList;
	private List subList;
	private Label labelSub;
	private String name="";

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
	
	private void findNode(int i, Map map, ArrayList list, String rootAss)
	{
		ArrayList object = new ArrayList();
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext())
		{
			//System.out.println("Iterator:"+i);
			Map.Entry entry = (Map.Entry) entries.next();
			Object value = entry.getValue();
			Object key = entry.getKey();
			System.out.println("KEY:"+key);
			System.out.println("VALUE:"+value);
			if (i==0)
				name = (String)key;
			if(i==1)
			{
				System.out.println(name+":"+key);
				if(key.toString().compareTo("fullname")==0)
				{
					if(rootAss.compareTo("")==0)
						list.add(name+"="+value);
					else
						list.add(rootAss+"="+name+"="+value);				}
			}
			if(value instanceof Map)
			{
				i++;
				findNode(i,(Map)value, list, rootAss);
				
			}
		}
	}
	
	private void getAssistants(String assistant) throws FileNotFoundException
	{
		String str="";
		String dirName = "/home/phracek/work/devassistant/devassistant/assistants/yaml";
		File folder = new File(dirName);
		File[] listOfFiles = folder.listFiles();
		// This section is used for main Assistants
		for (int i=0; i< listOfFiles.length; i++)
		{
			if(listOfFiles[i].isFile())
			{
				String fileName = listOfFiles[i].getName();
				if(fileName.endsWith(".yaml"))
				{
					String file = null;
					try
					{
						BufferedReader reader = new BufferedReader(new FileReader(listOfFiles[i]));
						StringBuilder stringBuilder = new StringBuilder();
						String line = null;
						String ls = System.getProperty("line.separator");
						while ((line = reader.readLine())!=null)
						{
							stringBuilder.append(line);
							stringBuilder.append(ls);
						}
						file = stringBuilder.toString();
						
					} catch (IOException io)
					{
					}
					Yaml yaml = new Yaml();
					Map map = (Map) yaml.load(file);
					//System.out.println(map);
					findNode(0,map,mainAss,"");
				}
			}
		}
		// This section is used for subassistant part
		System.out.println(mainAss);
		for(int i=0; i<mainAss.size(); i++)
		{
			StringTokenizer st = new StringTokenizer(mainAss.get(i),"=");
			String main = st.nextToken();
			File dir = new File(dirName+"/"+main);
			if(dir.exists())
			{
				File[] listOfSubFiles = dir.listFiles();
				// This section is used for main Assistants
				for (int j=0; j< listOfSubFiles.length; j++)
				{
					if(listOfSubFiles[j].isFile())
					{
						String fileName = listOfSubFiles[j].getName();
						if(fileName.endsWith(".yaml"))
						{
							String file = null;
							try
							{
								BufferedReader reader = new BufferedReader(new FileReader(listOfSubFiles[j]));
								StringBuilder stringBuilder = new StringBuilder();
								String line = null;
								String ls = System.getProperty("line.separator");
								while ((line = reader.readLine())!=null)
								{
									stringBuilder.append(line);
									stringBuilder.append(ls);
								}
								file = stringBuilder.toString();
								
							} catch (IOException io)
							{
							}
							Yaml yaml = new Yaml();
							Map map = (Map) yaml.load(file);
							//System.out.println(map);
							findNode(0,map,subAss,main);
						}
					}
				}
			}
		}
		System.out.println(subAss);
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
			StringTokenizer st = new StringTokenizer(mainAss.get(i),"=");
			st.nextToken();
			mainList.add(st.nextToken());
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
			{
				st.nextToken();
				subList.add(st.nextToken());
			}
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
				String mainRow = mainAss.get(index);
				StringTokenizer mainSt = new StringTokenizer(mainRow, "=");
				selAssistant = mainSt.nextToken();
				subList.removeAll();
				boolean bFound=false;
				for(int i=0; i<subAss.size(); i++)
				{
					//System.out.println(subAss.get(i));
					StringTokenizer st = new StringTokenizer(subAss.get(i),"=");
					String main = st.nextToken();
					System.out.println(main + selAssistant);
					if(selAssistant.compareTo(main)==0)
					{
						bFound = true;
						st.nextToken();
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
						{
							st.nextToken();
							System.out.println("Correct subassistant is:"+st.nextToken());
						}
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
			String mainRow = mainAss.get(index);
			StringTokenizer mainSt = new StringTokenizer(mainRow,"=");
			String main = mainSt.nextToken();
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
