// Created by God Emperor James Mufuh
// CMSC 204

package project_6;

import java.io.*;
import java.util.*;

public class TownGraphManager implements TownGraphManagerInterface
{
	TownGraph theTownGraph = new TownGraph();

	@Override
	public boolean addRoad(String town1, String town2, int weight, String roadName)
	{
		Town townA = new Town(town1);
		Town townB = new Town(town2);

		Road tempRoad = theTownGraph.addEdge(townA, townB, weight, roadName);

		if(tempRoad != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getRoad(String town1, String town2)
	{
		Town townA = null;
		Town townB = null;

		for (Town aTown : theTownGraph.Towns)
		{
			if(aTown.getTownName().equals(town1))
			{
				townA = aTown;
				break;
			}
			else if(aTown.getTownName().equals(town2))
			{
				townB = aTown;
				break;
			}
		}

		Road aRoad = theTownGraph.getEdge(townA, townB);

		if(aRoad != null)
		{
			return aRoad.roadName;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean addTown(String v)
	{
		Town newTown = new Town(v);

		return theTownGraph.addVertex(newTown);
	}

	@Override
	public Town getTown(String name)
	{
		Town town2Get = null;

		for (Town aTown : theTownGraph.Towns)
		{
			if(aTown.getTownName().equals(name))
			{
				town2Get = aTown;
				break;
			}
		}

		if(town2Get != null)
		{
			return town2Get;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean containsTown(String v)
	{
		for (Town aTown : theTownGraph.Towns)
		{
			if(aTown.getTownName().equals(v))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsRoadConnection(String town1, String town2)
	{
		Town townA = this.getTown(town1);
		Town townB = this.getTown(town2);

		return theTownGraph.containsEdge(townA, townB);
	}

	@Override
	public ArrayList<String> allRoads()
	{
		ArrayList<String> roadNames = new ArrayList<String>();

		for (Road aRoad : theTownGraph.Roads)
		{
			roadNames.add(aRoad.roadName);
		}

		Collections.sort(roadNames);

		return roadNames;
	}

	@Override
	public boolean deleteRoadConnection(String town1, String town2, String road)
	{
		Town townA = this.getTown(town1);
		Town townB = this.getTown(town2);

		Road road2Delete = theTownGraph.getEdge(townA, townB);

		if(road2Delete.roadName.equals(road))
		{
			Road deletedRoad = theTownGraph.removeEdge(townA, townB, road2Delete.roadDistance, road);

			if(deletedRoad == road2Delete)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean deleteTown(String v)
	{
		Town town2Delete = this.getTown(v);

		return theTownGraph.removeVertex(town2Delete);
	}

	@Override
	public ArrayList<String> allTowns()
	{
		ArrayList<String> allTowns = new ArrayList<String>();

		for (Town aRoad : theTownGraph.Towns)
		{
			allTowns.add(aRoad.getTownName());
		}

		Collections.sort(allTowns);

		return allTowns;
	}

	@Override
	public ArrayList<String> getPath(String town1, String town2)
	{
		Town townA = this.getTown(town1);
		Town townB = this.getTown(town2);
		
		 return theTownGraph.shortestPath(townA, townB);
	}

	public void populateTownGraph(File selectedFile) throws FileNotFoundException
	{
		Scanner file2Read = null;
		String line = "";
		String data[] = null;
		String data2[] = null;
		String data3[] = null;

		try
		{
			file2Read = new Scanner(selectedFile);
		}
		catch (FileNotFoundException e)
		{
			System.out.print(e.getMessage());
			throw new FileNotFoundException();
		}
		
		ArrayList<String> names = new ArrayList<String>();
		
		try
		{
			while (file2Read.hasNextLine())
			{
				line = file2Read.nextLine();	
				
				data =  line.split(";");
				data2 = data[0].split(",");
				
				int distance = Integer.parseInt(data2[1]);
				
				names.add(data[1]);
				names.add(data[2]);
				
				String a = data[1];
				String b = data[2];
				
				Town townA = theTownGraph.contains(a);
				Town townB = theTownGraph.contains(b);
				
				if(townA == null)
				{
					townA = new Town(a);
					theTownGraph.addVertex(townA);
				}
				
				if(townB == null)
				{
					townB = new Town(b);
					theTownGraph.addVertex(townB);
				}
				
				String h = data[0].split(",")[0];
				
				theTownGraph.addEdge(townA, townB, distance, h);			
			}
			
			file2Read.close();
		}
		catch (Exception e)
		{
			System.out.println("Something Went terribly wrong when reading the file");
			System.out.println(e.getMessage());
		}
		
		

	}

}
