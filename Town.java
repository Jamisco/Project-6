// Created by God Emperor James Mufuh
// CMSC 204

package project_6;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Set;

public class Town implements Comparable<Town>
{
	private final String townName;

	// ArrayList<String> adjacentTown = new ArrayList<String>();

	private HashMap<Town, Road> AdjacentTowns = new HashMap<Town, Road>();

	@Override
	public int compareTo(Town o)
	{
		return this.getName().compareTo(o.getName());
	}

	public boolean equals(Town t)
	{
		if(getName().equals(t.getName()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public Town(String townName)
	{
		this.townName = townName;
	}

	public String getTownName()
	{
		return townName;
	}

	public Road GetConnectingRoad(Town destination)
	{
		return AdjacentTowns.get(destination);

	}

	public Town GetConnectingTown(Road connectingRoad)
	{
		for (Town aTown : AdjacentTowns.keySet())
		{
			if(AdjacentTowns.get(aTown) == connectingRoad)
			{
				return aTown;
			}
		}

		return null; // pray to god this doesnt run
	}

	public boolean ConnectTown(Town adjacentTown, Road connectingRoad)
	{
		// this will check if a connecting road already exists in the hashmap

		if(AdjacentTowns.get(adjacentTown) != connectingRoad)
		{
			if(!adjacentTown.equals(this))
			{
				AdjacentTowns.put(adjacentTown, connectingRoad);				
			}
			
			adjacentTown.ConnectTown(this, connectingRoad);

			return true;
		}
		else
		{
			return false;
		}

	}

	public void DisconnectTown(Town adjacentTown)
	{
		if(AdjacentTowns.get(adjacentTown) != null)
		{
			AdjacentTowns.remove(adjacentTown);
			adjacentTown.DisconnectTown(this);

			// we disconnect both ways
			// there should be no reference to the road once both towns disconnect so
			// java should clean it up
		}
	}
	
	public void Nuke()
	{
		try
		{
			for(Town aTown: GetAdjacentTowns())
			{
				this.DisconnectTown(aTown);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public ArrayList<Road> GetRoads()
	{
		ArrayList<Road> roads = new ArrayList<Road>();

		try
		{
			for (Road aRoad : AdjacentTowns.values())
			{
				roads.add(aRoad);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	
		return roads;
	}

	public Set<Town> GetAdjacentTowns()
	{
		return AdjacentTowns.keySet();
	}
	
	public ArrayList<Town> GetAdjacentTowns2()
	{
		ArrayList<Town> towns = new ArrayList<Town>();
		try
		{
			for(Town aTown: GetAdjacentTowns())
			{
				if(aTown != this)
				{
					towns.add(aTown);					
				}
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		return towns;
	}

	public String getName()
	{
		return getTownName();
	}
	
	public String toString()
	{
		return getName();
	}

}
