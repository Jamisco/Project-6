// Created by God Emperor James Mufuh
// CMSC 204

package project_6;

import java.util.*;

public class Road implements Comparable<Road>
{
	public String roadName;
	
	public Town townA;
	public Town townB;
	
	public int roadDistance;
	
	public Road(Town townA, Town townB, int distance, String roadName)
	{
		this.roadName = roadName;
		
		this.townA = townA;
		this.townB = townB;
		
		roadDistance =  distance;
		
		townA.ConnectTown(townB, this);
		townB.ConnectTown(townA, this);
				
	}
	
	public String RoadName()
	{
		return roadName;
	}
	
	
	public int RoadDistance()
	{
		return roadDistance;
	}
	
	@Override
	public int compareTo(Road o)
	{
		if(roadDistance == o.roadDistance)
		{
			return 0;
		}
		
		if(roadDistance > o.roadDistance)
		{
			return 1;
		}
		
		if(roadDistance < o.roadDistance)
		{
			return -1;
		}		
		
		return 1; // this should never run
	}

	public boolean equals(Road road)
	{
		if(compareTo(road) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public String getName()
	{
		return roadName;
	}

	public String toString()
	{
		return getName();
	}
}
