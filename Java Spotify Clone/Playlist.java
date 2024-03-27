// Name: Nigel Siddeley
//   ID: 501186392

import java.util.ArrayList;

/*
 * A Playlist contains an array list of AudioContent (i.e. Song, AudioBooks, Podcasts) from the library
 */
public class Playlist
{
	// Instance variables
	private String title;
	private ArrayList<AudioContent> contents; // songs, books, or podcasts or even mixture
	
	// Constructor method, takes in and sets title and initializes content list
	public Playlist(String title)
	{
		this.title = title;
		contents = new ArrayList<AudioContent>();
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void addContent(AudioContent content)
	{
		contents.add(content);
	}
	
	public ArrayList<AudioContent> getContent()
	{
		return contents;
	}

	public void setContent(ArrayList<AudioContent> contents)
	{
		this.contents = contents;
	}
	
	/*
	 * Print the information of each audio content object (song, audiobook, podcast)
	 * in the contents array list. Print the index of the audio content object first
	 * followed by ". " then make use of the printInfo() method of each audio content object
	 * Make sure the index starts at 1
	 */
	public void printContents()
	{
		for(int i=0; i<contents.size(); i++){ // Iterates through each index of the contents list
			System.out.print(String.format("%s. ",i+1)); // Prints index in list
			contents.get(i).printInfo(); // Prints info of the AudioContent at the given index
			System.out.println();
		}
	}

	// Play all the AudioContent in the contents list
	public void playAll()
	{
		for(int i=0; i<contents.size(); i++){ // Iterates through each index of the contents list
			contents.get(i).play(); // Plays the AudioContent at the given index
			System.out.println();
		}		
	}
	
	// Play the specific AudioContent from the contents array list.
	// First make sure the index is in the correct range. 
	public void play(int index)
	{
		if(!contains(index)) return; // Checks if the index is in the contents list, if not returns without doing anything
		contents.get(index-1).play(); // If index is in contents list uses the play method of the AudioContent at that index
	}
	
	public boolean contains(int index)
	{
		return index >= 1 && index <= contents.size(); // Returns true if the contents list contains the index, false otherwise
	}

	public boolean contains(AudioContent content) // iterates through contents list and returns true if an AudioContent object is contained in the list
	{
		for(int i=0; i<contents.size(); i++)
		{
			if(contents.get(i).getType().equals(content.getType())){
				if(contents.get(i).equals(content))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// Two Playlists are equal if their titles are equal
	public boolean equals(Object other)
	{
		Playlist otherList = (Playlist) other; // Typecasts the other object to a Playlist object
		return this.getTitle().equals(otherList.getTitle()); // Checks if the titles of the two lists are the same, returns the result (True or False)
	}
	
	// Given an index of an audio content object in contents array list,
	// remove the audio content object from the array list
	// Hint: use the contains() method above to check if the index is valid
	// The given index is 1-indexed so convert to 0-indexing before removing
	public void deleteContent(int index)
	{
		if (!contains(index)) return; // if the index does not exist in the contents list return and do nothing
		contents.remove(index-1); // if the index does exist remove the AudioContent at that index
	}
	
	
}
