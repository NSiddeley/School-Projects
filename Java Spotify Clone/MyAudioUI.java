// Name: Nigel Siddeley
//   ID: 501186392

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple Text-based Music App (like Apple Music)

public class MyAudioUI
{
	public static void main(String[] args)
	{
		// Simulation of audio content in an online store
		// The songs, podcasts, audiobooks in the store can be downloaded to your mylibrary
		AudioContentStore store = new AudioContentStore();
		
		// Create my music mylibrary
		Library mylibrary = new Library();

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		// Process keyboard actions
		
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();

			try
			{
				if (action == null || action.equals("")) 
				{
					System.out.print("\n>");
					continue;
				}
				else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
					return;
				
				else if (action.equalsIgnoreCase("STORE"))	// List all
				{
					store.listAll(); 
				}
				else if (action.equalsIgnoreCase("SONGS"))	// List all songs
				{
					mylibrary.listAllSongs(); 
				}
				else if (action.equalsIgnoreCase("BOOKS"))	// List all audio books
				{
					mylibrary.listAllAudioBooks(); 
				}
				else if (action.equalsIgnoreCase("PODCASTS"))	// List all podcasts
				{					
					//mylibrary.listAllPodcasts(); 
				}
				else if (action.equalsIgnoreCase("ARTISTS"))	// List all artists
				{
					mylibrary.listAllArtists(); 
				}
				else if (action.equalsIgnoreCase("PLAYLISTS"))	// List all play lists
				{
					mylibrary.listAllPlaylists(); 
				}


				// Download audiocontent (song/audiobook/podcast) from the store 
				// Specify the index of the content
				else if (action.equalsIgnoreCase("DOWNLOAD")) 
				{
					int index1 = 0;
					int index2 = 0;
					
					System.out.print("From Store Content #: ");
					if (scanner.hasNextInt())
					{
						index1 = scanner.nextInt();
						scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())

						System.out.print("To Store Content #: ");
						if (scanner.hasNextInt())
						{
							index2 = scanner.nextInt();
							scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
							System.out.print("\n");
						
						}else{System.out.println("Please Enter a Number");}
					}
					else{System.out.println("Please Enter a Number");}
						
					// swaps the index values if the first index is bigger
					if(index1>index2)
					{
						int temp = index1;
						index1 = index2;
						index2 = temp;
					}

					// downloads all songs in the range entered by the user
					AudioContent content;
					for(int i=index1; i<=index2; i++)
					{
						// try catch block ensures that the loop continues if there is an exception thrown
						// for one of the song download attempts
						try 
						{
							content = store.getContent(i);
							if (content == null)
							{
								System.out.println("Content Not Found in Store");
							}
							else
							{	
								mylibrary.download(content);
							}

						}catch(RuntimeException e){System.out.println(e.getMessage());}	
					}				
				}


				// Get the *library* index (index of a song based on the songs list)
				// of a song from the keyboard and play the song 
				else if (action.equalsIgnoreCase("PLAYSONG")) 
				{
					System.out.print("Song Number: "); // prompts user to enter a song index
					int index = 0; // initializes index variable which will store the user's input later

					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()
						
						// calls the PlaySong method in the Library class. The method will throw an exception if there is an error
						mylibrary.playSong(index);
						
					}else{ System.out.println("Please Enter a Number"); }	// if the user does not enter a number for the index they are prompted to do so

				}


				// Print the table of contents (TOC) of an audiobook that
				// has been downloaded to the library. Get the desired book index
				// from the keyboard - the index is based on the list of books in the library
				else if (action.equalsIgnoreCase("BOOKTOC")) 
				{
					System.out.print("Audio Book Number: "); // prompts user to enter an audiobook index
					int index = 0; // initializes index variable which will store the user's input later

					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()
						
						// calls the printAudioBookTOC method in the Library class. The method will throw an exception if there is an error
						mylibrary.printAudioBookTOC(index);
						
					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the index they are prompted to do so

				}


				// Similar to playsong above except for audio book
				// In addition to the book index, read the chapter 
				// number from the keyboard - see class Library
				else if (action.equalsIgnoreCase("PLAYBOOK")) 
				{
					System.out.print("Audio Book Number: "); // prompts user to enter an audiobook index
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()
						
						System.out.print("Chapter: "); // prompts user to enter a chapter number
						if(scanner.hasNextInt()) // checks if the user input is a number
						{
							int chapter = scanner.nextInt(); // if the user did input a number it is stored in chapter
							scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()
							
							// calls the playAudioBook method in the Library class. The method will throw an exception if there is an error
							mylibrary.playAudioBook(index, chapter);
							
						}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the chapter index they are prompted to do so
						
					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the audiobook index they are prompted to do so
						
				}


				// Print the episode titles for the given season of the given podcast
				// In addition to the podcast index from the list of podcasts, 
				// read the season number from the keyboard
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("PODTOC")) 
				{
					/* 
					System.out.print("Podcast Number: "); // prompts user to enter a podcast index
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

						System.out.print("Season: "); // prompts user to enter a season index
						if(scanner.hasNextInt()) // checks if the user input is a number
						{
							int season = scanner.nextInt(); // if the user did input a number it is stored in season
							scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()
							
							// calls the printPodcastEpisodes method in the Library class. The method will throw an exception if there is an error
							mylibrary.printPodcastEpisodes(index, season);
						

						}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the season index they are prompted to do so

					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the podcast index they are prompted to do so
					*/
					
				}


				// Similar to playsong above except for podcast
				// In addition to the podcast index from the list of podcasts, 
				// read the season number and the episode number from the keyboard
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("PLAYPOD")) 
				{
					/* 
					System.out.print("Podcast Number: "); // prompts user to enter a podcast index
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

						System.out.print("Season: "); // prompts user to enter a season index
						if(scanner.hasNextInt()) // checks if the user input is a number
						{
							int season = scanner.nextInt(); // if the user did input a number it is stored in season
							scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

							System.out.print("Episode: "); // prompts user to enter a episode index
							if(scanner.hasNextInt()) // checks if the user input is a number
							{
								int episode = scanner.nextInt(); // if the user did input a number it is stored in episode
								scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

								// calls the playPodcast method in the Library class. The method will throw an exception if there is an error
								mylibrary.playPodcast(index, season, episode);
								

							}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the episode index they are prompted to do so

						}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the season index they are prompted to do so

					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the podcast index they are prompted to do so
					*/
				}


				// Specify a playlist title (string) 
				// Play all the audio content (songs, audiobooks, podcasts) of the playlist 
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("PLAYALLPL")) 
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title

					// calls the playPlaylist method for the playlist with the entered title (plays all).
					// The method will throw an exception if there is an error
					mylibrary.playPlaylist(title);

				}


				// Specify a playlist title (string) 
				// Read the index of a song/audiobook/podcast in the playist from the keyboard 
				// Play all the audio content 
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("PLAYPL")) 
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title

					System.out.print("Playlist Content #: "); // prompts user to enter a content index
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

						// calls the playPlaylist method for the playlist with the entered title (plays specific index).
						// The method will throw an exception if there is an error
						mylibrary.playPlaylist(title, index);
						

					}else{ System.out.println("Please Enter a Number"); }	// if the user does not enter a number for the content index they are prompted to do so			
				}


				// Delete a song from the list of songs in mylibrary and any play lists it belongs to
				// Read a song index from the keyboard
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("DELSONG")) 
				{
					System.out.print("Index Of Song To Delete: "); // prompts user to enter the index of the song to be deleted
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

						// calls the deleteSong method of the Library class.
						// If there is an error the method throws an exception
						mylibrary.deleteSong(index);
						

					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the index they are prompted to do so			
				}
				// Read a title string from the keyboard and make a playlist
				// see class Library for the method to call
				else if (action.equalsIgnoreCase("MAKEPL")) 
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title
					
					// calls the makePlaylist method in the Library class. creates a new playlist with the given title if it doesn't already exist
					// if the playlists already exists method throws an exception
					mylibrary.makePlaylist(title);
					
				}
				// Print the content information (songs, audiobooks, podcasts) in the playlist
				// Read a playlist title string from the keyboard
			// see class Library for the method to call
				else if (action.equalsIgnoreCase("PRINTPL"))	// print playlist content
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title
					
					// calls printPlaylist method in the Library class. prints content info of the given playlist
					// if the playlist entered does not exist method throws an exception
					mylibrary.printPlaylist(title);
					
					
				}
				// Add content (song, audiobook, podcast) from mylibrary (via index) to a playlist
				// Read the playlist title, the type of content ("song" "audiobook" "podcast")
				// and the index of the content (based on song list, audiobook list etc) from the keyboard
			// see class Library for the method to call
				else if (action.equalsIgnoreCase("ADDTOPL")) 
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title

					System.out.print("Content Type [SONG, PODCAST, AUDIOBOOK]: "); // prompts user to enter an audiocontent type
					String type = scanner.nextLine(); // stores the user input in type

					System.out.print("Library Content #: "); // prompts user to enter a content index from the library of the specified type
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()


						// calls addContentToPlaylist method in the Library class. adds audiocontent to specified playlist
						// if there is an error method throws an exception
						mylibrary.addContentToPlaylist(type, index, title);
						

					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the index they are prompted to do so			
				}
				// Delete content from play list based on index from the playlist
				// Read the playlist title string and the playlist index
			// see class Library for the method to call
				else if (action.equalsIgnoreCase("DELFROMPL")) 
				{
					System.out.print("Playlist Title: "); // prompts user to enter a playlist title
					String title = scanner.nextLine(); // stores the user input in title

					System.out.print("Playlist Content #: "); // prompts user to enter a content index from the playlist
					if(scanner.hasNextInt()) // checks if the user input is a number
					{
						int index = scanner.nextInt(); // if the user did input a number it is stored in index
						scanner.nextLine(); // skips a line, necessary when mixing nextLine() and nextInt()

						// calls delContentFromPlaylist method in the Library class. deletes content of specific index from the playlist
						// if there is an error method will throw an exception
						mylibrary.delContentFromPlaylist(index, title);
						

					}else{ System.out.println("Please Enter a Number"); } // if the user does not enter a number for the index they are prompted to do so		
				}
				
				else if (action.equalsIgnoreCase("SORTBYYEAR")) // sort songs by year
				{
					mylibrary.sortSongsByYear();
				}
				else if (action.equalsIgnoreCase("SORTBYNAME")) // sort songs by name (alphabetic)
				{
					mylibrary.sortSongsByName();
				}
				else if (action.equalsIgnoreCase("SORTBYLENGTH")) // sort songs by length
				{
					mylibrary.sortSongsByLength();
				}


				// ASSIGNMENT 2


				// searches for a song in the store based on its title
				else if (action.equalsIgnoreCase("SEARCH"))
				{
					System.out.print("Title: "); // prompts user to enter a title
					String title = scanner.nextLine(); // stores the user's input

					int index = store.searchTitle(title); // uses method searchTitle of class AudioContentStore to find store index of content that matches the title

					if(index==-1) // if searchTitle returns -1 there is no match
					{
						System.out.println("No Matches For " + title); // message printed to user
					}
					else // else entered when searchTitle returns valid store index
					{
						// gets content matching the entered title and prints its info
						AudioContent content = store.getContent(index);
						System.out.print(index + ". ");
						content.printInfo();
					}
				}
				// searches for songs or books in the store based on the artist/author
				else if (action.equalsIgnoreCase("SEARCHA"))
				{
					System.out.print("Artist: "); // prompts user to enter an artist
					String artist = scanner.nextLine(); // stores user input

					ArrayList<Integer> indices = store.searchArtist(artist); // uses method searchArtist of class AudioContentStore to find store indices associated with the given artist

					if(indices.size()==0) // if searchArtist returns an empty arraylist there are no matches 
					{
						System.out.println("No Matches For " + artist); // message printed to user
					}
					else // else entered if when searchArtist returns valid store indices
					{
						for(int i : indices) // iterates through each store index that matches the artist
						{
							// gets the content and prints its info
							AudioContent content = store.getContent(i);
							System.out.print(i + ". ");
							content.printInfo();
							System.out.println();
						}
					}
				}
				// searches for songs in the store based on their genre
				else if (action.equalsIgnoreCase("SEARCHG"))
				{
					System.out.print("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]: "); // prompts user for a genre
					String genre = scanner.nextLine(); // stores user input

					ArrayList<Integer> indices = store.searchGenre(genre); // uses method searchGenre of class AudioContentStore to find store indices that match the given genre

					if(indices.size()==0) // if searchGenre returns an empty arraylist there are no matches  
					{
						System.out.println("No Matches For " + genre); // message printed to user
					}
					else // else entered if when searchGenre returns valid store indices
					{
						for(int i : indices) // iterates through each store index that matches the genre
						{
							// gets the content and prints its info
							AudioContent content = store.getContent(i);
							System.out.print(i + ". ");
							content.printInfo();
							System.out.println();
						}
					}
				}
				// downloads all content by a certain artist/author
				else if (action.equalsIgnoreCase("DOWNLOADA"))
				{
					System.out.print("Artist: "); // prompts user to enter an artist
					String artist = scanner.nextLine(); // stores user input

					ArrayList<Integer> indices = store.searchArtist(artist); // uses method searchArtist of class AudioContentStore to find store indices associated with the given artist

					if(indices.size()==0) // if searchArtist returns an empty arraylist there are no matches for the given artist
					{
						System.out.println("No Matches For " + artist); // message printed to user
					}
					else // else entered if when searchArtist returns valid store indices
					{
						for(int i : indices) // iterates through each store index that matches the artist
						{
							// try catch block handles any exceptions thrown by the download method
							try 
							{
								// gets content and downloads it
								AudioContent content = store.getContent(i);
								mylibrary.download(content);
							} 
							catch (RuntimeException e) { System.out.println(e.getMessage()); }
						}
					}
				}
				// downloads all songs of a certain genre
				else if (action.equalsIgnoreCase("DOWNLOADG"))
				{
					System.out.print("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]: "); // prompts user for a genre
					String genre = scanner.nextLine(); // stores user input

					ArrayList<Integer> indices = store.searchGenre(genre); // uses method searchGenre of class AudioContentStore to find store indices that match the given genre

					if(indices.size()==0) // if searchGenre returns an empty arraylist there are no matches for the given genre
					{
						System.out.println("No Matches For " + genre); // message printed to user
					}
					else // else entered if searchGenre returns valid store indices
					{
						for(int i : indices) // iterates through each store index that matches the genre
						{
							// try catch block handles any exceptions thrown by the download method
							try 
							{
								// gets content and downloads it
								AudioContent content = store.getContent(i);
								mylibrary.download(content);
							}
							catch (RuntimeException e) { System.out.println(e.getMessage()); }
						}
					}
				}
				else if (action.equalsIgnoreCase("SEARCHP"))
				{
					System.out.print("Search For: "); // prompts user for a string to search for
					String s = scanner.nextLine(); // stores user input

					ArrayList<Integer> indices = store.searchPartial(s); // uses method searchPartial of class AudioContentStore to search all store content for the appearance of the substring

					if(indices.size()==0) // if searchPartial returns an empty arraylist there are no matches
					{
						System.out.println("No Matches For " + s); // message pritned to user
					}
					else // else entered if searchPartial returns valid store indices
					{
						for(int i : indices) // iterates through each store index that matches the substring anywhere in the content
						{
							// gets the content and prints its info
							AudioContent content = store.getContent(i);
							System.out.print(i + ". ");
							content.printInfo();
							System.out.println();
						}
					}
				}
			
			}catch (RuntimeException e){ // catch block handles any runtime exceptions thrown. Specifically the custom ones used in the Library class
				System.out.println(e.getMessage());
			}

			System.out.print("\n>");
		}
	}
}
