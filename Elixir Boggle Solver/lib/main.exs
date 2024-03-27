defmodule Main do


  def boggle(board, words) do

    wordSet = MapSet.new(words)
    prefixSet = MapSet.new(makePrefixList(words))
    boardMap = makeBoardMap(board)

    scanBoard(boardMap, wordSet, prefixSet)
    
  end



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
    


  def findWords(boardMap, wordSet, prefixSet, curWord, used, row, col) do
    cond do
      not (MapSet.member?(prefixSet, pointsToWord(curWord,boardMap))) 
        -> Map.new()
      true
        -> findWordsHelper(boardMap, wordSet, prefixSet, curWord, used, row, col)
    end
  end



  defp findWordsHelper(boardMap, wordSet, prefixSet, curWord, used, row, col) do
    # map for storing found words. If the word is a valid word it is added to the found words map
    found = addWord(curWord, wordSet, boardMap)

    neighbours = getValidNeighbours({row,col}, used, boardMap)

    Map.merge(found, scanNeighbours(neighbours, boardMap, wordSet, prefixSet, curWord, used, found))
  
  end



  def addWord(word, wordSet, boardMap) do
    wordStr = pointsToWord(word, boardMap)
    if MapSet.member?(wordSet, wordStr) do
      Map.put_new(Map.new(), wordStr, word)
    else
      Map.new()
    end
  end



  defp scanNeighbours([], _, _, _, _, _, found), do: found
  defp scanNeighbours([h|t], boardMap, wordSet, prefixSet, curWord, used, found) do
    row = elem(h,0)
    col = elem(h,1)
    newWord = curWord ++ [{row,col}]
    newUsed = MapSet.put(used, {row,col})
    newFound = Map.merge(found, findWords(boardMap, wordSet, prefixSet, newWord, newUsed, row, col))
    scanNeighbours(t, boardMap, wordSet, prefixSet, curWord, used, newFound)
  end
    


  def getValidNeighbours(curPoint, used, board) do
    row = elem(curPoint, 0)
    col = elem(curPoint, 1)

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
   
    Enum.reduce(conditions, [], fn
      {true, item}, list -> list ++ [item]
      _, list -> list
      end)
  end
    

  # Converts a list of tuples representing points on the board to a string made of the letters at the points
  def pointsToWord(points, board), do: pointsToWord(points, board, "")
  defp pointsToWord([], _, word), do: word
  defp pointsToWord([h|t], board, word) do
    row = elem(h,0)
    col = elem(h,1)
    ch = Map.fetch!(board, {row,col})
    pointsToWord(t, board, word <> ch)
  end


  # turns a list of words into a list of all the prefixes of the words (including the original words)
  def makePrefixList(words), do: makePrefixList(words, [])
  defp makePrefixList([], acc), do: acc
  defp makePrefixList([h | t], acc), do: makePrefixList(t, getPrefixes(h) ++ acc)


  # returns all possible prefixes of a word
  def getPrefixes(str), do: getPrefixes(str, String.length(str), [])
  defp getPrefixes(_, 0, acc), do: acc
  defp getPrefixes(str, n, acc) do
    prefix = String.slice(str, 0, n)
    getPrefixes(str, n-1, [prefix | acc])
  end


  # Turns the board (tuple of tuples) into a Map with each key representing a point and the values representing the characters at that given point e.g. {0,0}=>'e'
  def makeBoardMap(board), do: makeBoardMap(Tuple.to_list(board), 0, Map.new())
  defp makeBoardMap([], _, map), do: map
  defp makeBoardMap([h | t], r, map), do: makeBoardMap(t, r+1, Map.merge(map, pointifyRow(h, r)))


  # Turns a tuple representing a row on the board to a Map with the point as a key and the   character at that point as the value
  def pointifyRow(tup, r), do: pointifyRow(Tuple.to_list(tup), r, 0, Map.new())
  defp pointifyRow([], _, _, map), do: map
  defp pointifyRow([h | t], r, c, map), do: pointifyRow(t, r, c+1, Map.put_new(map, {r, c}, h))
  

end


Main.boggle({{"e", "a"}, {"s", "t"}}, ["a", "at", "sat", "eat", "tea", "ate", "east", "seat"])