// Name: Nigel Siddeley
//   ID: 501186392

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class manages, stores, and plays audio content such as songs, podcasts and audiobooks. 
 */
public class Library
{
	private ArrayList<Song> 			songs; 
	private ArrayList<AudioBook> 	audiobooks;
	private ArrayList<Playlist> 	playlists; 
	
	

	public Library()
	{
		songs 			= new ArrayList<Song>(); 
		audiobooks 	= new ArrayList<AudioBook>(); ;
		playlists   = new ArrayList<Playlist>();
	}
	/*
	 * Download audio content from the store. Since we have decided (design decision) to keep 3 separate lists in our library
	 * to store our songs, podcasts and audiobooks (we could have used one list) then we need to look at the type of
	 * audio content (hint: use the getType() method and compare to Song.TYPENAME or AudioBook.TYPENAME etc)
	 * to determine which list it belongs to above
	 * 
	 * Make sure you do not add song/podcast/audiobook to a list if it is already there. Hint: use the equals() method
	 * If it is already in a list, set the errorMsg string and return false. Otherwise add it to the list and return true
	 * See the video
	 */
	public void download(AudioContent content) throws RuntimeException
	{
		if(content.getType().equals(Song.TYPENAME))
		{
			Song song = (Song) content;

			if(!songs.contains(song))
			{
				songs.add(song);
				System.out.println("SONG " + song.getTitle() + " Added to Library");
			}
			else
			{
				throw new ContentAlreadyDownloadedException(song.getTitle() + " Has Already Been Downloaded");
			}
		}
		else
		{
			AudioBook book = (AudioBook) content;
			if(!audiobooks.contains(book))
			{
				audiobooks.add(book);
				System.out.println("BOOK " + book.getTitle() + " Added to Library");
			}
			else
			{
				throw new ContentAlreadyDownloadedException(book.getTitle() + " Has Already Been Downloaded");
			}
		}
	}
	
	// Print Information (printInfo()) about all songs in the array list
	public void listAllSongs()
	{
		for (int i = 0; i < songs.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			songs.get(i).printInfo();
			System.out.println();	
		}
	}
	
	// Print Information (printInfo()) about all audiobooks in the array list
	public void listAllAudioBooks()
	{
		for (int i = 0; i < audiobooks.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			audiobooks.get(i).printInfo();
			System.out.println();	
		}		
	}
	
  // Print Information (printInfo()) about all podcasts in the array list
	/* 
	public void listAllPodcasts()
	{
		for (int i = 0; i < podcasts.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			podcasts.get(i).printInfo();
			System.out.println();	
		}
	}
	*/

  // Print the name of all playlists in the playlists array list
	// First print the index number as in listAllSongs() above
	public void listAllPlaylists()
	{
		for (int i = 0; i < playlists.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			System.out.print(playlists.get(i).getTitle());
			System.out.println();	
		}
	}
	
  // Print the name of all artists. 
	public void listAllArtists()
	{
		// First create a new (empty) array list of string 
		// Go through the songs array list and add the artist name to the new arraylist only if it is
		// not already there. Once the artist arrayl ist is complete, print the artists names
		ArrayList<String> artists = new ArrayList<String>();

		for (int i=0; i<songs.size(); i++)
		{
			String artist = songs.get(i).getArtist();
			if(!artists.contains(artist))
			{
				artists.add(artist);
			}
		}
		
		for (int i=0; i<artists.size(); i++)
		{
			int index = i+1;
			System.out.print(String.format("%s. %s\n", index, artists.get(i)));

		}
	}

	// Delete a song from the library (i.e. the songs list) - 
	// also go through all playlists and remove it from any playlist as well if it is part of the playlist
	public void deleteSong(int index) throws RuntimeException
	{
		if(index >= 1 && index <= songs.size())
		{
			Song song = songs.get(index-1);
			songs.remove(song);

			for(int i=0; i<playlists.size(); i++)
			{
				Playlist pl = playlists.get(i);
				for(int j=0; j<pl.getContent().size(); j++)
				{
					if(pl.getContent().get(j).getType().equals("SONG"))
					{
						if(pl.getContent().get(j).equals(song))
						{
							playlists.get(i).deleteContent(j+1);
						}
					}
				}
			}		
		}
		throw new ContentNotFoundException("Song Not Found");
	}
  //Sort songs in library by year
	public void sortSongsByYear()
	{
		Collections.sort(songs, new SongYearComparator());
	}
  // Write a class SongYearComparator that implements
	// the Comparator interface and compare two songs based on year
	private class SongYearComparator implements Comparator<Song>
	{
		public int compare(Song a, Song b)
		{ return a.getYear()-b.getYear(); }
	}

	// Sort songs by length
	public void sortSongsByLength()
	{
		Collections.sort(songs, new SongLengthComparator());
	}
  // Write a class SongLengthComparator that implements
	// the Comparator interface and compare two songs based on length
	private class SongLengthComparator implements Comparator<Song>
	{
		public int compare(Song a, Song b)
		{ return a.getLength()-b.getLength(); }	
	}

	// Sort songs by title 
	public void sortSongsByName()
	{
		Collections.sort(songs, new SongNameComparator());
	}
	private class SongNameComparator implements Comparator<Song>
	{
		public int compare(Song a, Song b)
		{ return a.compareTo(b);}
	}
	
	
	/*
	 * Play Content
	 */
	
	// Play song from songs list
	public void playSong(int index) throws RuntimeException
	{
		if (index < 1 || index > songs.size())
		{
			throw new ContentNotFoundException("Song Not Found");
		}
		songs.get(index-1).play();
	}
	
	// Play podcast from list (specify season and episode)
	// Bonus
	/* 
	public void playPodcast(int index, int season, int episode)
	{
		if (index < 1 || index > podcasts.size())
		{
			throw new ContentNotFoundException("Podcast Not Found");
		}

		if (season < 1 || season > podcasts.get(index-1).getSeasons().size())
		{
			throw new ContentNotFoundException("Season Not Found");
		}

		if (episode < 1 || episode > podcasts.get(index-1).getSeasons().get(season-1).episodeFiles.size())
		{
			throw new ContentNotFoundException("Episode Not Found");
		}

		podcasts.get(index-1).selectSeason(season);
		podcasts.get(index-1).getSeasons().get(season-1).selectEpisode(episode);
		podcasts.get(index-1).play();
	}
	*/

	// Print the episode titles of a specified season
	// Bonus 
	/* 
	public void printPodcastEpisodes(int index, int season)
	{
		if (index < 1 || index > podcasts.size())
		{
			throw new ContentNotFoundException("Podcast Not Found");
		}

		if (season < 1 || season > podcasts.get(index-1).getSeasons().size())
		{
			throw new ContentNotFoundException("Season Not Found");
		}

		podcasts.get(index-1).printTOC(season);
	}
	*/
	
	// Play a chapter of an audio book from list of audiobooks
	public void playAudioBook(int index, int chapter) throws RuntimeException
	{
		if (index < 1 || index > audiobooks.size())
		{
			throw new ContentNotFoundException("Audio Book Not Found");
		}

		if (chapter < 1 || chapter > audiobooks.get(index-1).getChapters().size())
		{
			throw new ContentNotFoundException("Chapter Not Found");
		}

		audiobooks.get(index-1).selectChapter(chapter);
		audiobooks.get(index-1).play();

	}
	
	// Print the chapter titles (Table Of Contents) of an audiobook
	// see class AudioBook
	public void printAudioBookTOC(int index) throws RuntimeException
	{
		if (index < 1 || index > audiobooks.size())
		{
			throw new ContentNotFoundException("Audio Book Not Found");
		}
		audiobooks.get(index-1).printTOC();
	}
	
  /*
   * Playlist Related Methods
   */
	
	// Make a new playlist and add to playlists array list
	// Make sure a playlist with the same title doesn't already exist
	public void makePlaylist(String title) throws RuntimeException
	{
		Playlist p = new Playlist(title);

		if(playlists.contains(p))
		{
			throw new ContentAlreadyDownloadedException("A Playlist With This Title Already Exists");
		}
		else
		{
			playlists.add(p);
		}
	}
	
	// Print list of content information (songs, audiobooks etc) in playlist named title from list of playlists
	public void printPlaylist(String title) throws RuntimeException
	{
		for(int i=0; i<playlists.size(); i++)
		{
			if(playlists.get(i).getTitle().equals(title))
			{
				playlists.get(i).printContents();
			}
		}
		throw new ContentNotFoundException("Playlist Not Found");
	}
	
	// Play all content in a playlist
	public void playPlaylist(String playlistTitle) throws RuntimeException
	{
		for(int i=0; i<playlists.size(); i++)
		{
			if(playlists.get(i).getTitle().equals(playlistTitle))
			{
				playlists.get(i).playAll();
			}
		}
		throw new ContentNotFoundException("Playlist Not Found");
	}
	
	// Play a specific song/audiobook in a playlist
	public void playPlaylist(String playlistTitle, int indexInPL) throws RuntimeException
	{
		for(int i=0; i<playlists.size(); i++)
		{
			if(playlists.get(i).getTitle().equals(playlistTitle))
			{
				if(indexInPL < 1 || indexInPL > playlists.get(i).getContent().size())
				{
					throw new InvalidIndexException("Index Out Of Range");
				}
				else
				{
					System.out.println(playlists.get(i).getTitle());
					playlists.get(i).play(indexInPL);
				}
			}
		}
		throw new ContentNotFoundException("Playlist Not Found");
	}
	
	// Add a song/audiobook/podcast from library lists at top to a playlist
	// Use the type parameter and compare to Song.TYPENAME etc
	// to determine which array list it comes from then use the given index
	// for that list
	public void addContentToPlaylist(String type, int index, String playlistTitle) throws RuntimeException
	{
		boolean exists = false;
		int plIndex = 0;

		for(int i=0; i<playlists.size(); i++){
			if(playlistTitle.equals(playlists.get(i).getTitle()))
			{exists = true; plIndex = i;}	
		}

		if(!exists)
		{
			throw new ContentNotFoundException("Playlist Not Found");
		}

		if(type.equalsIgnoreCase(Song.TYPENAME))
		{
			if(index < 1 || index > songs.size())
			{
				throw new InvalidIndexException("Invalid Index. Please Enter An Index From The Songs List");
			}

			Song content = songs.get(index-1);
			if(playlists.get(plIndex).contains(content))
			{
				throw new ContentAlreadyDownloadedException("That Song Is Already In This Playlist");
			}
			else
			{
				playlists.get(plIndex).addContent(content);
			}

		}
		else if(type.equalsIgnoreCase(AudioBook.TYPENAME))
		{
			if(index < 1 || index > audiobooks.size())
			{
				throw new InvalidIndexException("Invalid Index. Please Enter An Index From The Audio Books List");
			}

			AudioBook content = audiobooks.get(index-1);
			if(playlists.get(plIndex).contains(content))
			{
				throw new ContentAlreadyDownloadedException("That Audiobook Is Already In This Playlist");
			}
			else
			{
				playlists.get(plIndex).addContent(content);
			}

		}
		else
		{
			throw new ContentNotFoundException("That Audio Content Type Does Not Exist");
		}

	}

  // Delete a song/audiobook/podcast from a playlist with the given title
	// Make sure the given index of the song/audiobook/podcast in the playlist is valid 
	public void delContentFromPlaylist(int index, String title) throws RuntimeException
	{
		for(int i=0; i<playlists.size(); i++)
		{
			if(playlists.get(i).getTitle().equals(title))
			{
				if(index < 1 || index > playlists.get(i).getContent().size())
				{
					throw new InvalidIndexException("Index Out Of Range");
				}
				else
				{
					playlists.get(i).deleteContent(index);
				}
			}
		}
	
		throw new ContentNotFoundException("Playlist Not Found");
	}

}

// ASSIGNMENT 2

// custom exception for if audiocontent can not be found
class ContentNotFoundException extends RuntimeException
{
	public ContentNotFoundException() {}

	public ContentNotFoundException(String msg) { super(msg); }
}

// custom exception for if user enters an invalid index
class InvalidIndexException extends RuntimeException
{
	public InvalidIndexException() {}

	public InvalidIndexException(String msg) { super(msg); }
}

// custom exception for when user tries to download content for a second time
class ContentAlreadyDownloadedException extends RuntimeException
{
	public ContentAlreadyDownloadedException() {}

	public ContentAlreadyDownloadedException(String msg) { super(msg); }
}
