import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class InvertedIndex {
	
	//String is the word as key, the inside HashMap has the the fileID as key and a value for each of these key an integer number (in this case the integer is tf:term frequency).
	HashMap<String, HashMap<Integer, Integer>> dictionary = new HashMap<String, HashMap<Integer, Integer>>();
	//A HashMap that contains "startingPaths"'s subDirectories as keys and integers as values, one for each key.
	HashMap<File, Integer> listedFiles = new HashMap<File, Integer>();
	
	//Empty constructor, just for references from other classes.
	public InvertedIndex() {};
	
	//The bellow constructor gets called only in the case that .ser files already exist in the disk.
	public InvertedIndex(String serPathOfDictionary)
	{
		Load(serPathOfDictionary);
		Print();
		ParseQuery(serPathOfDictionary);
	}
	
	@SuppressWarnings("unchecked")
	public void Load(String serPathOfDictionary)
	{
		try
		{
			FileInputStream fInStream = new FileInputStream(serPathOfDictionary);
			ObjectInputStream oInStream = new ObjectInputStream(fInStream);
			
			dictionary = (HashMap<String, HashMap<Integer, Integer>>) oInStream.readObject();
			
			oInStream.close();
			fInStream.close();
		}catch(FileNotFoundException fnfExc)
		{
			fnfExc.printStackTrace();
		}catch(ClassNotFoundException cnfExc)
		{
			cnfExc.printStackTrace();
		}catch(IOException ioExc)
		{
			ioExc.printStackTrace();
		}
	}
	
	public void Print()
	{
//		System.out.println(dictionary);
		System.out.println("\nReady to accept queries...\n");
//		System.out.println();
	}
	
	//Handles the user's queries. This method is written here to avoid spend memory between unnecessary classes.
	private void ParseQuery(String serPathOfDictionary)
	{
		//Consider keyboard to be the input.
		Scanner input = new Scanner(System.in);
		
		//Print a helpful message.
		System.out.println("Type \"enter\" to exit.");
		System.out.print("Type the word that you want to search: ");
		//Read from keyboard.
		String question = input.next();
		
		//The input of enter button ("") will terminate the program.
		while(!question.contentEquals(""))
		{
			//Splits the query into tokens, splitting by white space.
			question = question.replaceAll("\\W", " ");
			String[] splitedQuery = question.split("\\s");
			
			FindResults(splitedQuery, question);
			
			question = input.next();
		}
		input.close();
	}
	
	private void FindResults(String[] splitedQuery, String question)
	{
		HashMap<String, Set<Integer>> validTokens = new HashMap<String, Set<Integer>>();
		HashMap<String, Set<Integer>> toCompare = new HashMap<String, Set<Integer>>();
		ArrayList<Integer> listOfResults = new ArrayList<Integer>();
		
		toCompare.put(splitedQuery[0], dictionary.get(splitedQuery[0]).keySet());
		for(int i=0; i<splitedQuery.length; i++)
		{
			if(dictionary.containsKey(splitedQuery[i]) && !splitedQuery[i].contentEquals(" "))
			{
				validTokens.put(splitedQuery[i], dictionary.get(splitedQuery[i]).keySet());
			}
		}
		
		for(String token : validTokens.keySet())
		{
			Iterator<Integer> it = toCompare.get(token).iterator();
			while(it.hasNext())
			{
				Integer file = it.next();
				if(validTokens.get(token).contains(file))
				{
					listOfResults.add(file);
				}
			}
		}
		
		System.out.println("Results found in files: " + listOfResults);
		System.out.println();
	}
	
	//The bellow method updates the dictionary for every new word that the Parser finds.
	public void UpdateDictionary(String word, File file)
	{
		//Check if word already exists.
		if(dictionary.containsKey(word))
		{
			//Make a temporary HashMap to control word's (key of "dictionary") values.
			HashMap<Integer, Integer> tempHash = dictionary.get(word);
			//Grab the ID (key of "listedFiles") of the current file.
			int fileID = listedFiles.get(file);
			
			//Check if this file already exists in the record for the current word.
			if(tempHash.containsKey(fileID))
			{
				//If yes, then increase the word's termFrec by 1 (because it already exists in previously recorded file).
				int termFreq = tempHash.get(fileID) + 1;
				tempHash.put(fileID, termFreq);
				dictionary.put(word, tempHash);
			}else
			{
				//If not, then add this file as new record for the word and its termFreq (because the word existed in the dictionary, but for first time for this file).
				tempHash.put(fileID, 1);
				dictionary.put(word, tempHash);
			}
		}else
		{
			//The word does'nt exist at all in the dictionary, simply add it.
			HashMap<Integer, Integer> tempHash = new HashMap<Integer, Integer>();
			int fileID = listedFiles.get(file);
			
			tempHash.put(fileID, 1);
			dictionary.put(word, tempHash);
		}
	}
	
	public void WriteDictOnFile()
	{
		//Finally write the .ser dictionary, once and for all on the disk.
		try
		{
			//Creates output streams to write on a .ser file.
			FileOutputStream fOutStr = new FileOutputStream("C:\\Users\\xenid\\OneDrive\\DataMining\\Browser\\src\\DataOutput\\dictionary.ser");
			ObjectOutputStream oOutStr = new ObjectOutputStream(fOutStr);
			
			//Bellow command does the writing.
			oOutStr.writeObject(dictionary);
			//Close the streams to avoid errors.
			oOutStr.flush();
			oOutStr.close();
		}catch(IOException ioExc)
		{
			ioExc.printStackTrace();
		}
	}
	
}
