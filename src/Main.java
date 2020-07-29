import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String args[]) throws IOException
	{
		String startingPath = "C:\\Users\\xenid\\OneDrive\\Υπολογιστής\\Μαθήματα Σχολής\\Ανάκτηση Πληροφοριών\\Εργασία\\DataInput\\Reuters8Kdata\\data\\";
		String serPathOfDictionary = "C:\\Users\\xenid\\OneDrive\\DataMining\\Browser\\src\\DataOutput\\dictionary.ser";
		
		MakeSerFile(serPathOfDictionary, startingPath);
	}
	
	private static void MakeSerFile(String serPathOfDictionary, String startingPath)
	{
		File serFile = new File(serPathOfDictionary);
		
		if(serFile.exists())
		{
			//Opens already existing .ser files.
			InvertedIndex invIndObj = new InvertedIndex(serPathOfDictionary);
			
		}
		else
		{//Tries to make a new .ser file because it doesn't already exists.
			try {
				System.out.println("PLease wait until all documents are parsed...");
				Parser parserObj = new Parser(startingPath);
				System.out.println("\nProcess finished...");
			}catch (IOException ioExc)
			{
				ioExc.printStackTrace();
			}
		}
	}
	
}
