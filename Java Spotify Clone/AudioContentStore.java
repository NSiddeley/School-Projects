// Name: Nigel Siddeley
//   ID: 501186392

import java.util.Map;
import java.util.HashMap;


import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

// Simulation of audio content in an online store
// The songs, podcasts, audiobooks listed here can be "downloaded" to your library

public class AudioContentStore
{
		private ArrayList<AudioContent> contents;

		// ASSIGNMENT 2: initializing instance variable maps that will be used for search and download functions
		private Map<String, Integer> titleMap;
		private Map<String, ArrayList<Integer>> artistMap;
		private Map<String, ArrayList<Integer>> genreMap;
		
		// ASSIGNMENT 2: AudioContentStore constructor now uses file IO instead and creates title, artist, and genre hashmaps
		public AudioContentStore()
		{

			// try catch block calls method which reads store from a txt file
			try
			{
				contents = readStore("store.txt"); // creates content list using method readStore
			}
			catch (IOException e) // catches any IO exceptions such as file not found
			{
				// if an exception is thrown when reading the txt file the exception message is printed and the program is exited
				System.out.println(e.getMessage()); 
				System.exit(1);
			}
			
			// generates title map
			titleMap = new HashMap<String, Integer>();
			String title;
			for(int i=0; i<contents.size(); i++) // iterates through content list
			{
				title = contents.get(i).getTitle().toUpperCase(); // gets the title of the current content. toUpperCase used to avoid case sensitivity later on
				titleMap.put(title, i+1); // stores the title of the content at index i as the key, with the index (1-indexing) of the content in the store as the key value
			}

			// generate artist map
			artistMap = new HashMap<String, ArrayList<Integer>>();
			String artist;
			for(int i=0; i<contents.size(); i++) // iterates through content list
			{
				
				if(contents.get(i).getType().equals(Song.TYPENAME)) // checks if the current audiocontent is a song
				{
					Song song = (Song) contents.get(i);
					artist = song.getArtist().toUpperCase(); // gets the artist if the content is a song. toUpperCase used to avoid case sensitivity later on
				}
				else // else means the content is an audiobook (since podcasts are not part A2)
				{
					AudioBook book = (AudioBook) contents.get(i);
					artist = book.getAuthor().toUpperCase(); // gets the author if the content is an audiobook. toUpperCase used to avoid case sensitivity later on
				}

				if(artistMap.containsKey(artist)) // if the artist key exists in the map already this if is entered
				{
					// updates the store indices associated with the given artist. adds new index
					ArrayList<Integer> indices = artistMap.get(artist);
					indices.add(i+1);
					artistMap.put(artist, indices);
				}
				else // else is entered if the artist/author is not already in the map
				{
					// creates a new indices arraylist and adds the artist key and arraylist of indices value pair to the map
					ArrayList<Integer> indices = new ArrayList<Integer>();
					indices.add(i+1);
					artistMap.put(artist, indices);
				}
			}

			// generates genre map
			genreMap = new HashMap<String, ArrayList<Integer>>();
			String genre;
			for(int i=0; i<contents.size(); i++) // iterates through content list
			{
				if(!contents.get(i).getType().equals(Song.TYPENAME)){continue;} // skips to next audio content if the current one is an audio book (only songs have genres)

				Song song = (Song) contents.get(i);
				genre = String.valueOf(song.getGenre()); // gets genre of current song
				if(genreMap.containsKey(genre)) // if the genre key exists in the map already this if is entered
				{
					// updates the store indices associated with the given genre. adds new index
					ArrayList<Integer> indices = genreMap.get(genre);
					indices.add(i+1);
					genreMap.put(genre, indices);
				}
				else // else is entered if the genre is not already in the map
				{
					// creates a new indices arraylist and adds the genre key and arraylist of indices value pair to the map
					ArrayList<Integer> indices = new ArrayList<Integer>();
					indices.add(i+1);
					genreMap.put(genre, indices);
				}
			}




		}

		public AudioContent getContent(int index)
		{
			if (index < 1 || index > contents.size())
			{
				return null;
			}
			return contents.get(index-1);
		}
		
		public void listAll()
		{
			for (int i = 0; i < contents.size(); i++)
			{
				int index = i + 1;
				System.out.print("" + index + ". ");
				contents.get(i).printInfo();
				System.out.println();
			}
		}


		// ASSIGNMENT 2


		// creats a contents list from a store txt file
		private ArrayList<AudioContent> readStore(String fileName) throws IOException
		{

			Scanner in = new Scanner(new File(fileName)); // creates scanner object for reading the store txt file
			ArrayList<AudioContent> contents = new ArrayList<AudioContent>(); // creates content arraylist

			// initializing variables that will be frequently reused while scanning file
			String title;			
			int year; 				
			String id;				
			int length;
			String artist; 	
			String composer;	
			String genre;	
			String lyrics;
			int numLines;
			String author;
			String narrator;

			while(in.hasNext()) // while loop exited when the store txt file has no more text left to read
			{
				String type = in.nextLine(); // takes in the type of whichever audiocontent is next in the store text file (SONG or AUDIOBOOK)

				if(type.equals("SONG")) // if entered if the content is a song
				{

					// stores all the song info based on the file format
					id = in.nextLine(); 
					title = in.nextLine();
					year = in.nextInt();
					in.nextLine();
					length = in.nextInt();
					in.nextLine();
					artist = in.nextLine();
					composer = in.nextLine();
					genre = in.nextLine();
					lyrics = "";

					numLines = in.nextInt();
					in.nextLine();

					// for loop reads and stores the song lyrics since lyrics take up multiple lines
					for(int i=0; i<numLines; i++)
					{
						lyrics += in.nextLine() + "\n";
					}

					// creates a new song object with the info given from the file and adds it to content list
					contents.add(new Song(title, year, id, "SONG", "", length, artist, composer, Song.Genre.valueOf(genre), lyrics));

				}
				else if(type.equals("AUDIOBOOK")) // else if entered if the content is an audiobook
				{

					// stores all the audio book info based on the file format
					id = in.nextLine(); 
					title = in.nextLine();
					year = in.nextInt(); 
					in.nextLine();
					length = in.nextInt(); 
					in.nextLine();
					author = in.nextLine();
					narrator = in.nextLine();

					int numChapters = in.nextInt();
					in.nextLine();

					// for loop reads and stores chapter titles since chapter titles take up multiple lines
					ArrayList<String> chapTitles = new ArrayList<String>();
					for(int i=0; i<numChapters; i++)
					{
						chapTitles.add(in.nextLine());
					}

					ArrayList<String> chapters = new ArrayList<String>();
					String chapter;
					// for loop reads and stores chapter since chapter take up multiple lines
					for(int i=0; i<numChapters; i++)
					{
						numLines = in.nextInt();
						in.nextLine();
						chapter = "";
						for(int j=0; j<numLines; j++)
						{
							chapter += in.nextLine() + "\n";
						}
						chapters.add(chapter);
					}

					// creates a new audio book object with the info given from the file and adds it to content list
					contents.add(new AudioBook(title, year, id, "AUDIOBOOK", "", length, author, narrator, chapTitles, chapters));

				}
				else // else is entered if there is an error in the store txt file formatting
				{
					// throws an exception
					throw new IOException("File Formated Incorrectly");
				}
			}

			return contents; // returns the list of contents generated from the file
		}

		// searches for content by a certain title, returns the store index of any matches
		public int searchTitle(String title)
		{
			title = title.toUpperCase(); // converts the title entered by the user to upper case to avoid case sensitivity
			if(titleMap.containsKey(title)){ 
				return titleMap.get(title); // if the title exists in the map the store index of the song with that title is returned
			}else{
				return -1; // if the title does not exist in the map -1 is returned (this is further dealt with in MyAudioUI)
			}
		}

		// searches for content by a certain artist/author, returns the store index/indices of any matches
		public ArrayList<Integer> searchArtist(String artist)
		{
			artist = artist.toUpperCase(); // converts the title entered by the user to upper case to avoid case sensitivity
			if(artistMap.containsKey(artist)){
				return artistMap.get(artist); // if the artist/author exists in the map an arraylist of store indices associated with that artist/author is returned
			}else{
				return new ArrayList<Integer>(); // if the artist/author does not exist in the map an empty arraylist is returned
			}
		}

		public ArrayList<Integer> searchGenre(String genre)
		{
			genre = genre.toUpperCase(); // converts the genre entered by the user to upper case to avoid case sensitivity
			if(genreMap.containsKey(genre)){ 
				return genreMap.get(genre); // if the genre exists in the map an arraylist of store indices associated with that genre is returned
			}else{
				return new ArrayList<Integer>(); // if the genre does not exist in the map an empty arraylist is returned
			}
		}

		// bonus
		// searches for content with a partial match (anywhere) to a given string
		public ArrayList<Integer> searchPartial(String s)
		{
			ArrayList<Integer> indices = new ArrayList<Integer>(); // arraylist of store indices of content that matches the string
			boolean contains; // boolean used to keep track of if an audio content matches the string somewhere in its info/content
			ArrayList<String> allTheDamnInfo; // arraylist used to store all the damn info of an audiocontent
			AudioContent content; // used to temporarily store audio content 

			for(int i=0; i<contents.size(); i++) // iterates through all store contents
			{
				content = contents.get(i); // gets current content
				contains = false; // contains set to false by default

				if(content.getType().equals(Song.TYPENAME)) // if entered if the content is a song
				{
					Song song = (Song) content; // typecasts current content to a song
					allTheDamnInfo = new ArrayList<String>(); // sets all the damn info to an empty arraylist

					// adds all the damn info of a song to the arraylist
					allTheDamnInfo.add(song.getArtist());
					allTheDamnInfo.add(song.getComposer());
					allTheDamnInfo.add(song.getId());
					allTheDamnInfo.add(String.valueOf(song.getLength()));
					allTheDamnInfo.add(song.getLyrics());
					allTheDamnInfo.add(song.getTitle());
					allTheDamnInfo.add(song.getType());
					allTheDamnInfo.add(String.valueOf(song.getYear()));
					allTheDamnInfo.add(String.valueOf(song.getGenre()));
					
					// for loop iterates through all the damn info one by one
					for(String info : allTheDamnInfo) 
					{ if(info.contains(s)) {contains = true;} } // if checks if the substring is contained in any of the info. If yes then contains is set to true
					
					
				}
				else
				{
					AudioBook book = (AudioBook) content; // typecasts current content to an audiobook
					allTheDamnInfo = new ArrayList<String>(); // sets all the damn info to an empty arraylist

					// adds all the damn info of an audiobook to the arraylist (not including chapters and chapter titles they are checked separately)
					allTheDamnInfo.add(book.getAuthor());
					allTheDamnInfo.add(book.getId());
					allTheDamnInfo.add(String.valueOf(book.getLength()));
					allTheDamnInfo.add(book.getNarrator());
					allTheDamnInfo.add(book.getTitle());
					allTheDamnInfo.add(book.getType());
					allTheDamnInfo.add(String.valueOf(book.getYear()));
					
					// for loop iterates through all the damn info one by one
					for(String info : allTheDamnInfo) 
					{ if(info.contains(s)) {contains = true;} } // if checks if the substring is contained in any of the info. If yes then contains is set to true

					// for loop iterates through each chapter title
					for(String title : book.getChapterTitles()) 
					{ if(title.contains(s)) {contains = true;} } // if checks if the substring is contained anywhere in the chapter titles. If yes then contains is set to true

					// for loop iterates through each chapter 
					for(String chap : book.getChapters()) 
					{if(chap.contains(s)) {contains = true;} } // if checks if the substring is contained anywhere in the chapters. If yes then contains is set to true
				}

				if(contains) { indices.add(i+1); } // if the substring is contained anywhere in the audiocontent info/content its store index is added to indices arraylist

			}
			return indices; // returns store indices of content that matched the substring
		}

		
		

		
		
		
		
		
		
}
