import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	
	private InvertedIndex invIndObj = new InvertedIndex();
	
	private File[] arrayOfFiles;
	
	public Parser(String startingPath) throws IOException
	{
		ExaminePaths(startingPath);
		
		invIndObj.WriteDictOnFile();
	}
	
	private void ExaminePaths(String startingPath) throws IOException
	{
		File fileObj = new File(startingPath);
		if(fileObj.exists())
		{
			arrayOfFiles = fileObj.listFiles();
			int index=0;
			
			for(File file : arrayOfFiles)
			{
				if(file.isFile())
				{
					/*Calls a method from InvertedIndex class to write into a new serializable file a pair of (key, value).
					  This pair is the key that we sign on the file that is currently under process.*/
					invIndObj.listedFiles.put(file, index);
					index++;
					
					ExamineFile(file);
//					System.out.println(file);
				}else
				{
					try
					{//Calls again the method "ExaminePaths(String) with the new directory as parameter. This loop will stop when all directories and files are configured.
						ExaminePaths(file.getPath());
					} catch (IOException ioExc)
					{
						ioExc.printStackTrace();
					}
				}
			}
		}
		else
		{//Message in case in case the input data isn't at the expected position.
			System.out.println("Caused by the bellows: ");
			throw new java.io.FileNotFoundException("The directory you are trying to read is not existing");

		}
	}
	
	private void ExamineFile(File file) throws IOException
	{/*This method splits the document in lines and these lines into words. Then checks if there is any record existing for the word that is currently under process. 
	   If there is such a record as said, the HashMap "dictionary" gets updated. If not, a new record is being created for that word (including the file spotted in 
	   as a pair).*/
		Scanner reader = new Scanner(file);
		
		String[] arrayOfLines;
		
		while(reader.hasNextLine())
		{
			String line = reader.nextLine();
			line = line.replaceAll("[^a-zA-Z0-9.\\s]", "");
			line = line.toLowerCase();
			arrayOfLines = line.split(" ");
			
//			System.out.println(line);
//			System.out.println(arrayOfLines);
			for(String word : arrayOfLines)
			{//Stores the word into a HashMap named "dictionary".
				invIndObj.UpdateDictionary(word, file);
			}
//			System.out.println();
		}
		reader.close();
	}
	
}
