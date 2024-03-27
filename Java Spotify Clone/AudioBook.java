// Name: Nigel Siddeley
//   ID: 501186392

import java.util.ArrayList;

/*
 * An AudioBook is a type of AudioContent.
 * It is a recording made available on the internet of a book being read aloud by a narrator
 * 
 */
public class AudioBook extends AudioContent
{
	// Instance variables
	public static final String TYPENAME =	"AUDIOBOOK";
	
	private String author; 
	private String narrator;
	private ArrayList<String> chapterTitles;
	private ArrayList<String> chapters;
	private int currentChapter = 0;

	// Constructor method. Takes in AudioContent info and author (String), narrator (String), chapterTitles (String list) and chapters (String list)
	public AudioBook(String title, int year, String id, String type, String audioFile, int length,
									String author, String narrator, ArrayList<String> chapterTitles, ArrayList<String> chapters)
	{
		// Make use of the constructor in the super class AudioContent. 
		// Initialize additional AudioBook instance variables. 
		super(title, year, id, type, audioFile, length);
		this.author=author;
		this.narrator=narrator;
		this.chapterTitles = chapterTitles;
		this.chapters = chapters;
	}
	
	public String getType()
	{
		return TYPENAME;
	}

  // Print information about the audiobook. First print the basic information of the AudioContent 
	// by making use of the printInfo() method in superclass AudioContent and then print author and narrator
	// see the video
	public void printInfo()
	{
		super.printInfo(); // Uses the printInfo() method of the superclass to print all the information that AudioContent objects have
		System.out.println(String.format("Author: %s Narrated by: %s", author, narrator)); // Prints the information unique to the AudioBook subclass
	}
	
  // Play the audiobook by setting the audioFile to the current chapter title (from chapterTitles array list) 
	// followed by the current chapter (from chapters array list)
	// Then make use of the the play() method of the superclass
	public void play()
	{
		super.setAudioFile(String.format("%s\n%s", chapterTitles.get(currentChapter), chapters.get(currentChapter))); // Sets the audioFile of the audiobook to the current chapter title and current chapter content
		super.play(); // Uses the play() method from the superclass to play (print) the current chapter of the audiobook
	}
	
	// Print the table of contents of the book - i.e. the list of chapter titles
	// See the video
	public void printTOC()
	{
		for(int i=0; i<chapterTitles.size(); i++) // For loop iterates through chapterTitles arraylist
		{ 
			System.out.println(String.format("Chapter %s. %s\n", i+1, chapterTitles.get(i))); // prints each chapter title with "Chapter X." in front of it
		}		
	}

	// Select a specific chapter to play - nothing to do here
	public void selectChapter(int chapter)
	{
		if (chapter >= 1 && chapter <= chapters.size())
		{
			currentChapter = chapter - 1;
		}
	}
	
	//Two AudioBooks are equal if their AudioContent information is equal and both the author and narrators are equal
	public boolean equals(Object other)
	{
		AudioBook otherBook = (AudioBook) other; // typecasts the object to be compared to an AudioBook object
		return super.equals(otherBook) // uses the super.equals() method to check if AudioContent info is equal
		&& author.equals(otherBook.getAuthor()) && narrator.equals(otherBook.getNarrator()); // checks if author and narrator are also equal. Returns true if all three conditions are true.
	}
	
	public int getNumberOfChapters()
	{
		return chapters.size();
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getNarrator()
	{
		return narrator;
	}

	public void setNarrator(String narrator)
	{
		this.narrator = narrator;
	}

	public ArrayList<String> getChapterTitles()
	{
		return chapterTitles;
	}

	public void setChapterTitles(ArrayList<String> chapterTitles)
	{
		this.chapterTitles = chapterTitles;
	}

	public ArrayList<String> getChapters()
	{
		return chapters;
	}

	public void setChapters(ArrayList<String> chapters)
	{
		this.chapters = chapters;
	}

}
