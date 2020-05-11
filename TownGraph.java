// Created by God Emperor James Mufuh
// CMSC 204
package project_6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TownGraph implements GraphInterface<Town, Road>
{
	ArrayList<Town> Towns = new ArrayList<Town>();
	Set<Road> Roads = new HashSet<Road>();

	ArrayList<ArrayList<Road>> townMatrix = new ArrayList<ArrayList<Road>>();

	public TownGraph()
	{
		// ....
	}

	public void UpdateMatrix(Town sourceTown, Town connectingTown, Road connectingRoad)
	{
		int rowIndex = GetTownIndex(sourceTown);
		int columnIndex = GetTownIndex(connectingTown);

		// this should not crash given roads/towns are being updated properly in this
		// object
		townMatrix.get(rowIndex).set(columnIndex, connectingRoad);
		// make sure both cities connect to each other
		townMatrix.get(columnIndex).set(rowIndex, connectingRoad);

	}

	public Road AccessRoad(Town sourceTown, Town connectingTown)
	{
		try
		{
			int rowIndex = Towns.indexOf(sourceTown);
			int columnIndex = Towns.indexOf(connectingTown);

			// this should not crash given roads/towns are being updated properly in this
			// object
			return townMatrix.get(rowIndex).get(columnIndex);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	public boolean NukeTown(Town sourceTown)
	{
		ArrayList<ArrayList<Road>> newTownMatrix = new ArrayList<ArrayList<Road>>();
		ArrayList<Road> roads2Add = new ArrayList<Road>();

		int rowIndex = Towns.indexOf(sourceTown);
		int columnIndex = Towns.indexOf(sourceTown);

		for (int r = 0; r < Towns.size(); r++)
		{
			if(r != rowIndex)
			{
				for (int c = 0; c < Towns.size(); c++)
				{
					if(c != columnIndex)
					{
						roads2Add.add(townMatrix.get(r).get(c));
					}
				}

				newTownMatrix.add(roads2Add);
				roads2Add.clear();
			}
		}

		Towns.remove(sourceTown);
		sourceTown.Nuke();

		townMatrix = newTownMatrix;

		return true;
	}

	public ArrayList<Road> GetRoads(Town sourceTown)
	{
		ArrayList<Road> roads = new ArrayList<Road>();

		try
		{
			int rowIndex = Towns.indexOf(sourceTown);

			for (int c = 0; c < townMatrix.get(rowIndex).size(); c++)
			{
				if(townMatrix.get(rowIndex).get(c) != null)
				{
					roads.add(townMatrix.get(rowIndex).get(c));
				}
			}

			// this should not crash given roads/towns are being updated properly in this
			// object
			return roads;
		}
		catch (Exception e)
		{
			return null;
		}

	}

	@Override
	public Road getEdge(Town sourceVertex, Town destinationVertex)
	{
		if(contains(sourceVertex) && contains(destinationVertex))
		{
			int rowIndex = Towns.indexOf(sourceVertex);
			int columnIndex = Towns.indexOf(destinationVertex);

			return townMatrix.get(rowIndex).get(columnIndex);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Road addEdge(Town sourceVertex, Town destinationVertex, int weight, String description)
	{
		if(sourceVertex == null || destinationVertex == null)
		{
			throw new NullPointerException();
		}

		if(contains(sourceVertex) && contains(destinationVertex))
		{
			int sourceIndex = GetTownIndex(sourceVertex);
			int destinIndex = GetTownIndex(destinationVertex);
			
			sourceVertex = Towns.get(sourceIndex);
			destinationVertex = Towns.get(destinIndex);
			
			Road tempRoad = new Road(sourceVertex, destinationVertex, weight, description);

			UpdateMatrix(sourceVertex, destinationVertex, tempRoad);

			Roads.add(tempRoad);

			return tempRoad;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean addVertex(Town v)
	{
		if(v == null)
		{
			throw new NullPointerException();
		}

		if(!contains(v))
		{
			Towns.add(v);

			ArrayList<Road> newRoads = new ArrayList<Road>();

			for (Town aTown : Towns)
			{
				newRoads.add(null);
			}

			townMatrix.add(newRoads);

			// makes sure the previous arrays have thesame sizes as the Towns List

			for (int r = 0; r < townMatrix.size(); r++)
			{
				if(townMatrix.get(r).size() < Towns.size())
				{
					for (int c = townMatrix.get(r).size(); c < Towns.size(); c++)
					{
						townMatrix.get(r).add(null);
					}
				}

			}

			//UpdateMatrix(v, v, null);

			return true;
		}

		return false;
	}

	public Town getVertex(Town sourceTown, Road connectingRoad)
	{
		if(contains(sourceTown))
		{
			int rowIndex = Towns.indexOf(sourceTown);

			int columnIndex = townMatrix.get(rowIndex).indexOf(connectingRoad);

			return Towns.get(columnIndex);
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean containsEdge(Town sourceVertex, Town destinationVertex)
	{
		if(!contains(sourceVertex) || !contains(destinationVertex))
		{
			return false;
		}

		if(sourceVertex == null || destinationVertex == null)
		{
			return false;
		}

		if(AccessRoad(sourceVertex, destinationVertex) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean containsVertex(Town v)
	{
		if(v == null)
		{
			return false;
		}

		if(contains(v.getName()) != null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	public Set<Road> edgeSet()
	{
		return Roads;
	}

	@Override
	public Set<Road> edgesOf(Town vertex)
	{
		if(vertex == null)
		{
			throw new NullPointerException();
		}

		if(!contains(vertex))
		{
			throw new IllegalArgumentException();
		}

		Set<Road> tempSet = new HashSet<Road>(this.GetRoads(vertex));

		return tempSet;
	}

	@Override
	public Road removeEdge(Town sourceVertex, Town destinationVertex, int weight, String description)
	{
		if(sourceVertex == null || destinationVertex == null)
		{
			throw new NullPointerException();
		}

		if(!contains(sourceVertex) || !contains(destinationVertex))
		{
			throw new IllegalArgumentException();
		}

		Road connectedRoad = AccessRoad(sourceVertex, destinationVertex);

		if(connectedRoad != null)
		{
			if(connectedRoad.roadDistance > 0) // this might need to be -1 according to interface
			{
				if(connectedRoad.roadName != null)
				{
					if(connectedRoad.roadDistance == weight)
					{
						if(connectedRoad.roadName.equals(description))
						{
							Roads.remove(connectedRoad);
							UpdateMatrix(sourceVertex, destinationVertex, null);
							sourceVertex.DisconnectTown(destinationVertex);

							return connectedRoad;
						}
					}
				}
			}
		}

		return null; // this is will only run if it fails any of our if blocks
	}

	@Override
	public boolean removeVertex(Town v)
	{
		if(!contains(v))
		{
			return false;
		}

		if(v != null)
		{
			Towns.remove(v);

			NukeTown(v);

			v = null;

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Set<Town> vertexSet()
	{
		return new HashSet<Town>(Towns);
	}

	@Override
	public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex)
	{
		HashMap<Town, Town> shortestPath = DoTheThing(sourceVertex);

		ArrayList<Town> townPath = new ArrayList<Town>();
		ArrayList<String> directions = new ArrayList<String>();

		int distance = 0;

		Town currentTown = destinationVertex;
		Town tempTown = null;

		while (true)
		{
			tempTown = currentTown;

			currentTown = shortestPath.get(currentTown);

			if(tempTown == currentTown)
			{
				break;
			}

			if(currentTown != null)
			{
				townPath.add(currentTown);
			}
		}

		Collections.reverse(townPath);

		townPath.add(destinationVertex);

		for (int i = 0; i < townPath.size() - 1; i++)
		{
			String direction = "";

			Town townA = townPath.get(i);
			Town townB = townPath.get(i + 1);

			Road connectingRoad = null;

			try
			{
				connectingRoad = getEdge(townA, townB);
				distance = connectingRoad.roadDistance;
			}
			catch (Exception e)
			{
				continue;
			}

			String town1 = townA.getName();
			String town2 = townB.getName();

			// * Vertex_1 via Edge_2 to Vertex_3 4 (first string in ArrayList)

			direction = town1 + " via " + connectingRoad.roadName + " to " + town2 + " " + distance + " mi";
			directions.add(direction);
		}

		return directions;

	}

	private HashMap<Town, Town> DoTheThing(Town sourceVertex)
	{
		HashMap<Town, Integer> shortestPath = new HashMap<Town, Integer>();

		HashMap<Town, Town> prevPath = new HashMap<Town, Town>();

		ArrayList<Road> connectingRoads;

		Town currentTown = sourceVertex;

		Road minRoad = null;
		int prevMinRoadDistance = 0;
		int distanceFromSource = 0;

		shortestPath.put(sourceVertex, 0);

		for (Town aTown : Towns)
		{
			if(!shortestPath.containsKey(aTown) && aTown != sourceVertex)
			{
				shortestPath.put(aTown, Integer.MAX_VALUE);
			}
		}

		ArrayList<Town> VisitedTowns = new ArrayList<Town>();
		ArrayList<Town> mainTowns = sourceVertex.GetAdjacentTowns2();

		Road tempRoad = null;

		Town tempTown = null;
		Town nextTown = null;
		Town prevTown = null;

		while (true)
		{
			if(currentTown == prevTown || currentTown == null)
			{
				break;
			}

			connectingRoads = currentTown.GetRoads();

			prevMinRoadDistance = Integer.MAX_VALUE; // this is merely used for comparison if below if statement

			for (int i = 0; i < connectingRoads.size(); i++)
			{
				tempRoad = connectingRoads.get(i);
				tempTown = getVertex(currentTown, tempRoad);

				if(VisitedTowns.contains(tempTown))
				{
					continue;
				}

				if(tempRoad.roadDistance < prevMinRoadDistance)
				{
					minRoad = tempRoad;
					prevMinRoadDistance = tempRoad.roadDistance;
				}

				int tempInt = distanceFromSource + tempRoad.roadDistance;

				if(shortestPath.get(tempTown) > tempInt) // so if current town has smaller road
				{
					shortestPath.put(tempTown, tempInt);
					prevPath.put(tempTown, currentTown);
				}

			}

			VisitedTowns.add(currentTown);
			
			if(VisitedTowns.containsAll(Towns))
			{
				System.out.println("\n----------------------------\n");
				
				return prevPath;
			}
			
			nextTown = GetNextTown(shortestPath, VisitedTowns);

			if(nextTown == null)
			{
				return prevPath;
			}
			
			distanceFromSource = shortestPath.get(nextTown);
			//System.out.println("Visited " + currentTown + " Next Is " + nextTown);

			prevTown = currentTown;
			currentTown = nextTown;

		}
		
		//System.out.println("\n----------------------------\n");

		return prevPath;
	}

	public Town GetNextTown(HashMap<Town, Integer> prevPath, ArrayList<Town> VisitedTowns)
	{
		Town minTown = null;
		int prevDistance = Integer.MAX_VALUE;

		for (Town aTown : prevPath.keySet())
		{
			if(!VisitedTowns.contains(aTown))
			{
				int distance = prevPath.get(aTown);

				if(distance < prevDistance)
				{
					minTown = aTown;
					prevDistance =  distance;
				}
			}

		}

		return minTown;
	}

	@Override
	public void dijkstraShortestPath(Town sourceVertex)
	{
		HashMap<Town, Integer> shortestPath = new HashMap<Town, Integer>();

		HashMap<Town, Town> prevPath = new HashMap<Town, Town>();

		ArrayList<Road> connectingRoads;

		Town currentTown = sourceVertex;

		Road minRoad = null;
		int prevMinRoadDistance = 0;
		int distanceFromSource = 0;

		shortestPath.put(sourceVertex, 0);

		for (Town aTown : Towns)
		{
			if(!shortestPath.containsKey(aTown) && aTown != sourceVertex)
			{
				shortestPath.put(aTown, Integer.MAX_VALUE);
			}
		}

		ArrayList<Town> VisitedTowns = new ArrayList<Town>();
		ArrayList<Town> mainTowns = sourceVertex.GetAdjacentTowns2();

		Road tempRoad = null;

		Town tempTown = null;
		Town nextTown = null;
		Town prevTown = null;
		Town mainTown = currentTown;

		while (true)
		{
			if(currentTown == prevTown)
			{
				break;
			}

			connectingRoads = currentTown.GetRoads();

			prevMinRoadDistance = Integer.MAX_VALUE; // this is merely used for comparison if below if statement

			for (int i = 0; i < connectingRoads.size(); i++)
			{
				tempRoad = connectingRoads.get(i);
				tempTown = getVertex(currentTown, tempRoad);

				if(VisitedTowns.contains(tempTown))
				{
					VisitedTowns.add(currentTown);
					continue;
				}

				if(tempRoad.roadDistance < prevMinRoadDistance)
				{
					minRoad = tempRoad;
					prevMinRoadDistance = tempRoad.roadDistance;
				}

				int tempInt = distanceFromSource + tempRoad.roadDistance;

				if(shortestPath.get(tempTown) > tempInt) // so if current town has smaller road
				{
					shortestPath.put(tempTown, tempInt);
					prevPath.put(tempTown, currentTown);
				}

				nextTown = getVertex(currentTown, minRoad);

				if(VisitedTowns.contains(nextTown) || nextTown == currentTown)
				{
					continue; // restart loop
				}

			}

			distanceFromSource += prevMinRoadDistance;
			VisitedTowns.add(currentTown);

			prevTown = currentTown;
			currentTown = nextTown;

		}
	}

	public Town contains(String townName)
	{
		for (Town aTown : Towns)
		{
			if(aTown.getName().equals(townName))
			{
				return aTown;
			}
		}

		return null;
	}
	

	public boolean contains(Town townName)
	{
		for (Town aTown : Towns)
		{
			if(aTown.getName().equals(townName.getName()))
			{
				return true;
			}
		}

		return false;
	}

	public int GetTownIndex(Town theTown)
	{
		int index = 0;
		
		for(Town aTown: Towns)
		{
			if(aTown.equals(theTown))
			{
				return index;
			}
			
			index++;
		}
		
		return -1; // this line should never run
	}
}
