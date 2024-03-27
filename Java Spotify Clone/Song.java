// Name: Nigel Siddeley
//   ID: 501186392

/*
 * A Song is a type of AudioContent. A Song has extra fields such as Artist (person(s) singing the song) and composer 
 */
public class Song extends AudioContent implements Comparable<Song>// implement the Comparable interface
{
	public static final String TYPENAME =	"SONG";
	
	public static enum Genre {POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL}; 
	private String artist; 		// Can be multiple names separated by commas
	private String composer; 	// Can be multiple names separated by commas
	private Genre  genre; 
	private String lyrics;
	
	
	
	public Song(String title, int year, String id, String type, String audioFile, int length, String artist,
			String composer, Song.Genre genre, String lyrics)
	{
		// Make use of the constructor in the super class AudioContent. 
		// Initialize additional Song instance variables.
		super(title, year, id, type, audioFile, length);
		this.artist = artist;
		this.composer = composer;
		this.genre = genre;
		this.lyrics = lyrics; 
	}
	
	public String getType()
	{
		return TYPENAME;
	}
	
	// Print information about the song. First print the basic information of the AudioContent 
	// by making use of the printInfo() method in superclass AudioContent and then print artist, composer, genre 
	public void printInfo()
	{
		super.printInfo(); // uses the super method printInfo to print AudioContent info
		System.out.println (String.format("Artist: %s Composer: %s Genre: %s", artist, composer, genre)); // prints the info unique to songs
	}
	
	// Play the song by setting the audioFile to the lyrics string and then calling the play() method of the superclass
	public void play()
	{
		super.setAudioFile(lyrics); // sets the AudioFile variable inherited from the superclass to the song lyrics
		super.play(); // uses super method play to play the song
	}
	
	public String getComposer()
	{
		return composer;
	}
	public void setComposer(String composer)
	{
		this.composer = composer;
	}
	
	public String getArtist()
	{
		return artist;
	}
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	public String getLyrics()
	{
		return lyrics;
	}
	public void setLyrics(String lyrics)
	{
		this.lyrics = lyrics;
	}

	public Genre getGenre()
	{
		return genre;
	}

	public void setGenre(Genre genre)
	{
		this.genre = genre;
	}	
	
	// Two songs are equal if their AudioContent information is equal and both the composer and artists are the same
	// Make use of the superclass equals() method
	public boolean equals(Object other)
	{
		Song otherSong = (Song) other; // typecasts the other object to a Song object
		return super.equals(otherSong) // checks if the AudioContent of the other object is equal
		&& this.composer.equals(otherSong.getComposer()) && this.artist.equals(otherSong.getArtist()); // checks if the information unique to Songs is equal
	}
	
	// Implement the Comparable interface 
	// Compare two songs based on their title
	// This method will allow songs to be sorted alphabetically

	// This method will put shorter words first (e.g. final would come before finally)
	public int compareTo(Song other)
	{
		if(this.getTitle().equals(other.getTitle())){ return 0; } // returns 0 if the titles are the same

		int retValue = 0; // value that will be returned later
		int length = 0;

		if(this.getTitle().length()<other.getTitle().length()) // if word a is shorter length = length of a
		{
			length = this.getTitle().length();
			retValue = -1;
		}else{ // if word b is shorter length = length of b
			length = other.getTitle().length();
			retValue = 1;
		}

		for(int i=0; i<length; i++) // iterates through both words to the largest index of the shorter word
		{
			int ch1 = this.getTitle().toUpperCase().charAt(i); // gets the character at index i of word a
			int ch2 = other.getTitle().toUpperCase().charAt(i);// gets the character at index i of word b

			if(ch1==ch2){ // if the characters are the same continue to the next iteration to check the next letters
				continue;
			}else if(ch1<ch2){ // if word a's character has a lower ascii value return -1 (a should come first)
				return -1;
			}else{ // if word b's character has a lower ascii value return 1 (b should come first)
				return 1;
			}
		}

		return retValue; // if one word is contained in the other (e.g. final in finally) the smaller word comes first
	}
}
