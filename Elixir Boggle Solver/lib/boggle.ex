defmodule Boggle do

  @moduledoc """
    Add your boggle function below. You may add additional helper functions if you desire. 
    Test your code by running 'mix test' from the tester_ex_simple directory.
  """

  def boggle(board, words) do

    # a set of the word list for O(1) access when searching for words
    wordSet = MapSet.new(words) 

    # a set of prefixes for checking when to prematurely end scanning for words
    # from a given prefix when a valid word can no longer be made
    prefixSet = MapSet.new(makePrefixList(words))

    # a map of the board with points as keys and the character at that point as the value
    boardMap = makeBoardMap(board)

    scanBoard(boardMap, wordSet, prefixSet)
    
  end


  # calls findWords on each cell of the boggle board, using depth first search
  # to find all valid words stemming from that point on the board.
  # eventually returns a map of all valid words according to the given wordlist
  def scanBoard(boardMap, wordSet, prefixSet) do
    points = Map.keys(boardMap)
    scanBoard(points, boardMap, wordSet, prefixSet, Map.new())
  end
  defp scanBoard([], _, _, _, foundWords), do: foundWords
  defp scanBoard([h|t], boardMap, wordSet, prefixSet, foundWords) do
    row = elem(h, 0)
    col = elem(h, 1)
    curWord = [{row,col}]
    used = MapSet.new(curWord)
    newWords = Map.merge(foundWords, findWords(boardMap, wordSet, prefixSet, curWord, used, row, col)) 
    scanBoard(t, boardMap, wordSet, prefixSet, newWords)
  end
    

  # scans the a certain point on the boggle board to see if it is a valid word, or can it
  # can make a valid word with its neighbours
  def findWords(boardMap, wordSet, prefixSet, curWord, used, row, col) do
    cond do
      # if the current word is not in the prefix set, it will not lead to a valid
      # word, so stop scanning and return an empty map
      not (MapSet.member?(prefixSet, pointsToWord(curWord,boardMap))) 
        -> Map.new()
      # if the current word is in the prefix set, continue scanning that word
      # and its neigbouring points for more valid words
      true
        -> findWordsHelper(boardMap, wordSet, prefixSet, curWord, used, row, col)
    end
  end


  # helper function for findWords, only entered when a word is a member of the prefix set
  def findWordsHelper(boardMap, wordSet, prefixSet, curWord, used, row, col) do
    # map for storing found words. If the word is a valid word it is added to the found words map
    found = addWord(curWord, wordSet, boardMap)

    # a list of valid neighbouring points to the current point
    neighbours = getValidNeighbours({row,col}, used, boardMap)

    # returns a map of the current word (if valid) and any words resulting from
    # scanning the neighbouring points
    Map.merge(found, scanNeighbours(neighbours, boardMap, wordSet, prefixSet, curWord, used, found))
  
  end


  # checks if a words is a valid word in the wordlist and returns a map with
  # the word string as a key and a list of points as its value
  def addWord(word, wordSet, boardMap) do
    wordStr = pointsToWord(word, boardMap)
    if MapSet.member?(wordSet, wordStr) do
      Map.put_new(Map.new(), wordStr, word)
    else
      Map.new()
    end
  end


  # iterates through a list of neighbours of a certain point, scanning for words
  # using the findWords function
  def scanNeighbours([], _, _, _, _, _, found), do: found
  def scanNeighbours([h|t], boardMap, wordSet, prefixSet, curWord, used, found) do
    row = elem(h,0)
    col = elem(h,1)
    newWord = curWord ++ [{row,col}]
    newUsed = MapSet.put(used, {row,col})
    newFound = Map.merge(found, findWords(boardMap, wordSet, prefixSet, newWord, newUsed, row, col))
    scanNeighbours(t, boardMap, wordSet, prefixSet, curWord, used, newFound)
  end
    

  # checks the eight potential neighbouring points of a given point.
  # returns a list of all the valid neighbouring points, 
  # meaning points that exist on the board and are not used in the word already
  def getValidNeighbours(curPoint, used, board) do
    row = elem(curPoint, 0)
    col = elem(curPoint, 1)

    # a list of two elem tuples, with the first elem being 
    # true or false if a point is a valid neighbour,
    # and the second elem being the point itself
    conditions = [
      # top left neighbour
      {Map.has_key?(board, {row-1, col-1}) && not MapSet.member?(used, {row-1, col-1}), {row-1, col-1}},
      # top neighbour
      {Map.has_key?(board, {row-1, col}) && not MapSet.member?(used, {row-1, col}), {row-1, col}},
      # top right neighbour
      {Map.has_key?(board, {row-1, col+1}) && not MapSet.member?(used, {row-1, col+1}), {row-1, col+1}},
      # left neighbour
      {Map.has_key?(board, {row, col-1}) && not MapSet.member?(used, {row, col-1}), {row, col-1}},
      # right neighbour
      {Map.has_key?(board, {row, col+1}) && not MapSet.member?(used, {row, col+1}), {row,col+1}},
      # bottom left neighbour
      {Map.has_key?(board, {row+1, col-1}) && not MapSet.member?(used, {row+1, col-1}), {row+1,col-1}},
      # bottom neighbour
      {Map.has_key?(board, {row+1, col}) && not MapSet.member?(used, {row+1, col}), {row+1,col}},
      # bottom right neighbour
      {Map.has_key?(board, {row+1, col+1}) && not MapSet.member?(used, {row+1, col+1}), {row+1,col+1}}
    ]
   
    # makes a list of the valid neighbouring points which is returned by the function
    Enum.reduce(conditions, [], fn
      {true, item}, list -> list ++ [item]
      _, list -> list
      end)
  end
    

  # Converts a list of tuples representing points on the board to a string made of the letters at the points
  def pointsToWord(points, board), do: pointsToWord(points, board, "")
  def pointsToWord([], _, word), do: word
  def pointsToWord([h|t], board, word) do
    row = elem(h,0)
    col = elem(h,1)
    ch = Map.fetch!(board, {row,col})
    pointsToWord(t, board, word <> ch)
  end


  # turns a list of words into a list of all the prefixes of the words 
  # (including the original words themselves)
  def makePrefixList(words), do: makePrefixList(words, [])
  def makePrefixList([], acc), do: acc
  def makePrefixList([h | t], acc), do: makePrefixList(t, getPrefixes(h) ++ acc)


  # returns all possible prefixes of a word
  def getPrefixes(str), do: getPrefixes(str, String.length(str), [])
  def getPrefixes(_, 0, acc), do: acc
  def getPrefixes(str, n, acc) do
    prefix = String.slice(str, 0, n)
    getPrefixes(str, n-1, [prefix | acc])
  end


  # Turns the board (tuple of tuples) into a Map 
  # with each key representing a point and 
  # the values representing the characters at that given point e.g. {0,0}=>'e'
  def makeBoardMap(board), do: makeBoardMap(Tuple.to_list(board), 0, Map.new())
  def makeBoardMap([], _, map), do: map
  def makeBoardMap([h | t], r, map), do: makeBoardMap(t, r+1, Map.merge(map, pointifyRow(h, r)))


  # Turns a tuple representing a row on the board to a Map 
  # with the points as keys and the character at that point as the values
  def pointifyRow(tup, r), do: pointifyRow(Tuple.to_list(tup), r, 0, Map.new())
  def pointifyRow([], _, _, map), do: map
  def pointifyRow([h | t], r, c, map), do: pointifyRow(t, r, c+1, Map.put_new(map, {r, c}, h))
  
end
