'''
Author: Nigel Siddeley
Student ID: 501186392

PROBLEM DESCRIPTION:
In my free time I enjoy playing video games among other things.
The game I play most often is Valorant, a tactical 5v5 FPS style game.
A common problem I see players have is deciding which agent to pick in game when
all 4 of their teammates have already selected. Each agent has different abilities,
and picking the right agent with the right abilities for the right team composition 
can sometimes be the difference between winning or losing a match. This can be a 
stressful decision to make, especially for newer players. This program will take
into account the abilities and roles of the other 4 agents on the users team, and decide
for the user which specific agent to choose, or create a list of optimal agents to select from.
'''


    
def agentsPicked(agentList):
    '''
    This function prompts the user to enter the 4 agents that were selected
    by their teammates and stores them in a list which is returned. 
    Invalid and duplicate inputs are accounted for.
    parameters: agentList (list): List of all the agents in Valorant
    returns: teammates (list): List of the user's 4 other teammates in Valorant
    '''
    
    # print statements instruct the user to enter their teammates agent selections, gives a list of valid inputs
    print("Enter the 4 agents that have already been selected by your teammates.")
    print("=====================================================================")
    print(f"Valid agents: \n{agentList}\n")
    
    teammates = [] # creates an empty list called 'teammates' which will be used to store 4 agents
    count = 0 # count variable to keep track of number of unique agents that have been entered
    
    # while loop prompts user to enter each agent one by one and stores valid agent inputs in the list 'teammates'  
    while count < 4: # once 4 unique agents are entered the while condition is no longer true 
        agent = "" # temporary variable to store user input
        while agent not in agentList: # nested while loop makes sure user enters a valid agent name
            agent = input(f" > Agent {count+1}: ")
            if agent.lower() not in agentList: # if the user enters an invalid name they are given an error msg 
                print(f"{agent} is not an agent!")
            agent = agent.lower() # converts user input to lowercase to keep consistency when comparing strings
        if agent not in teammates: # if statement checks if the agent entered is already in the team list
            teammates.append(agent) # if the agent is unique they are added to the teammates list
            count += 1 # increases the count of unique teammates by 1
        else: # if the user tries to enter a duplicate agent they are given an error message
            print("you cannot have duplicate agents on your team!")
           
    # returns the list of 4 unique agents to the function call
    return teammates 



def favAgent(agentList):
    '''
    This function prompts the user to enter their favourite agent.
    Selecting an agent as your facourite does not give it an advantage in the selection process.
    we're here to win, not have fun.
    In certain cases the favourite agent is used as a tie breaker (only if it is tied for most optimal)
    parameters: agentList (list): List of all the agents in Valorant
    returns: favAgent (string): User's favourite agent
    '''
    
    # print statement prompts user to enter thier favourite agent (or 'none' if they don't have one)
    print("\nEnter your favourite agent. If you don't have one, enter 'none'")
    favAgent = "" # variable is initialized here so the while loop can be entered
    
    # while loop will prompt user to enter an agent or 'none' until a valid input is entered
    while ((favAgent in agentList) or (favAgent == "none")) == False: # checks if a valid input has been entered, if not the while loop is entered
        favAgent = input(" > Favourite Agent: ") # propmts user to enter their favourite agent
        favAgent = favAgent.lower() # converts input to lowercase
        if ((favAgent in agentList) or (favAgent == "none")) == False: # if the user enters and invalid input they are given an error msg
            print("That is not a valid input!")
       
    # returns the user's favourite agent (with the first letter capitalized of course) to the function call   
    return favAgent[0].upper() + favAgent[1:]



def optimalAgent(teammates, agentList):
    '''
    This function analyzes the user's teammates roles and abilities and uses a
    scoring system to determine which of the available agents would best cover the weaknesses
    of the user's team
    parameters: teammates (list): List of the user's 4 other teammates in Valorant
                agentList (list): List of all the agents in Valorant
    returns: optimalAgents (list): List of optimal agents for the user to pick
    '''
    
    # creates sets of agents based on their in-game roles. All roles are about equally as valuable to the team and is treated as such 
    # roles have higher priority over abilities, and are a good general indicator of an agents offensive or defensive capabilities
    # roles are an in game concept created by Riot Games, the creators of Valorant
    # it is important to note that agents can have multiple abilities, but only ONE role, this helps differentiate agents who have the same role
    duelists = set(agentList[0:6])
    initiators = set(agentList[6:11])
    controllers = set(agentList[11:16])
    sentinels = set(agentList[16:])
    
    # creates sets of agents based on their abilites
    # all ability categories are about equally as useful and are treated as such 
    flashes = ("phoenix", "yoru", "breach", "skye", "omen", "reyna", "kay/o")  
    smokes = ("brimstone", "omen", "viper", "astra", "harbor")
    recon = ("sova", "fade", "cypher")
    healers = ("sage", "skye")
    lineups = ("sova", "viper", "killjoy", "brimstone", "kay/o", "phoenix", "raze")
    stuns = ("breach", "astra", "cypher", "fade", "neon")
    
    neededAgents = set() # creates an empty set which will be used to store potential most optimal agent candidates
    optimalAgents = [] # empty list that will be used to store the most optimal agents
    
    # creates boolean variables which will be used to check if a certain role or ability is needed
    # roles
    hasDuelist = False
    hasInitiator = False
    hasController = False
    hasSentinel = False
    # abilities
    hasFlashes = False
    hasSmokes = False
    hasRecon = False
    hasHeals = False
    hasLineups = False
    hasStuns = False
    
    # for loop checks the roles and abilites of the agents on the user's team
    # and sets the respective boolean variable 'has___' to True if the role or ability
    # is had by one of the agents on the team
    for agent in teammates:
        if agent in duelists:
            hasDuelist = True
        elif agent in initiators:
            hasInitiator = True
        elif agent in controllers:
            hasController = True
        elif agent in sentinels:
            hasSentinel = True 
            
        # agents can have more than one ability so elifs cannot be used here
        if agent in flashes:
            hasFlashes = True
        if agent in smokes:
            hasSmokes = True
        if agent in recon:
            hasRecon = True
        if agent in healers:
            hasHeals = True
        if agent in lineups:
            hasLineups = True
        if agent in stuns:
            hasStuns = True
            
    
    # this boolean variable is True when ALL roles are fulfilled by agents on the team.
    # Since roles have a higher priority, when this variable is True the optimal agents are selected
    # solely by their abilities. If this variable is False, only agents from a missing role can be selected
    hasAllRoles = (hasDuelist and hasInitiator and hasController and hasSentinel)
    
    # this boolean variable is true when ALL abilities are fulfilled by agents on the team.
    # this variable is only used in the case that all Roles AND Abilites are fulfilled
    hasAllAbilities = (hasFlashes and hasSmokes and hasRecon and hasHeals and hasLineups and hasStuns)
    
    # if all roles and abilities are covered all the agents will have the same score so it doesn't matter who the user picks       
    if (hasAllRoles==True) and (hasAllAbilities==True):
        optimalAgents.append("everyone")
        return optimalAgents
    
    
    # This if / nested ifs & elifs checks for all the missing roles and abilities on the user's team.
    # All the agents with a role or ability that is needed are added to the set 'neededAgents'
    if hasAllRoles == False:
        if hasDuelist == False: 
            for agent in duelists: neededAgents.add(agent)
        elif hasInitiator == False: 
            for agent in initiators: neededAgents.add(agent)
        elif hasController == False: 
            for agent in controllers: neededAgents.add(agent) 
        elif hasSentinel == False: 
            for agent in sentinels: neededAgents.add(agent)              
    else: # if all roles are fulfilled, THEN needed abilities are considered.
          # This makes it so that roles are given higher priority
        if hasFlashes == False:
            for agent in flashes: neededAgents.add(agent)
        if hasSmokes == False:
            for agent in smokes: neededAgents.add(agent)    
        if hasRecon == False:
            for agent in recon: neededAgents.add(agent)        
        if hasHeals == False:
            for agent in healers: neededAgents.add(agent)
        if hasLineups == False:
            for agent in lineups: neededAgents.add(agent)
        if hasStuns == False:
            for agent in stuns: neededAgents.add(agent)
       
            
    agentScoreList = [] # creates an empty list that will be used to store agent names and scores in tuples  
    # for loop checks the abilites of each agent in neededAgents and gives them +1
    # point if they have an ability that is missing on the team.
    # This scoring process differntiates agents whose roles are both needed.
    for agent in neededAgents:
        agentScore = 0
        if (agent in flashes) and (hasFlashes==False): agentScore += 1
        if (agent in smokes) and (hasSmokes==False): agentScore += 1
        if (agent in recon) and (hasRecon==False): agentScore += 1
        if (agent in healers) and (hasHeals==False): agentScore += 1
        if (agent in lineups) and (hasLineups==False): agentScore += 1
        if (agent in stuns) and (hasStuns==False): agentScore += 1
        agentProfile = (agent, agentScore) # creates a tuple with the agent's name and score
        agentScoreList.append(agentProfile) # adds the agent profile to the list of tuples           
    
    
    highest = 0 # variable that will be used to keep track of the highest agent score
    # for loop checks each relevant agents score and compares them to other relevant agents
    for i in range(len(agentScoreList)):
        agent = agentScoreList[i][0][0].upper() + agentScoreList[i][0][1:] # stores agent name at index i of the list of tuples 'agentScores' in a variable
        score = agentScoreList[i][1] # stores agent score at index i of 'agentScores' in a variable
        if score > highest: # if a valid agent has the highest score, it is stored alone in the list optimalAgents
            optimalAgents.clear()
            optimalAgents.append(agent) 
            highest = score
        elif score == highest: # if a valid agent matches the highest score it is added to the list optimalAgents
            optimalAgents.append(agent)

    # returns a list of the best possible agents for the user to select to the function call
    return optimalAgents 



def output(theBest, favAgent):
    '''
    This function prints the output for the program
    parameters: theBest (list): List of optimal agents 
                favAgent (string): User's favourite agent
    returns: nothing
    '''
    # if prints output for if all roles and abilites are covered by the user's team
    if theBest[0] == "everyone": print("\nYour team's got everything covered, it doesn't matter who you play. Pick whoever seems fun!")
    # elif prints output for if the favAgent is an optimal agent 
    elif favAgent in theBest: print(f"\nCongratulations! Your favourite agent is optimal, you should play {favAgent[0].upper() + favAgent[1:]}.")
    # elif prints a list of optimal agents if there is more than one 
    elif len(theBest) > 1: 
        print("\nThese are the agents that would benefit your team the most. Pick whichever one seems most fun to you!")
        for agent in theBest:
            print(" > " + f"{agent}")
    # else prints the output for all other cases (if there is only one optimal agent)
    else: print(f"\nThe most beneficial agent for your team is {theBest[0]}!")
        
        
        
if __name__ == "__main__":
    
    # welcome message
    print("\nWelcome to the Valorant Agent Picker.") 
    
    # creates a list of all the agents in valorant organized based off in-game roles
    agentList = ["jett", "raze", "reyna", "yoru", "neon", "phoenix", "breach", "kay/o", "skye", "sova", "fade", "astra", "omen", "viper", "harbor", "brimstone", "cypher", "killjoy", "chamber", "sage"]
    
    # calls function that collects user's teammates and stores them in a list
    teammates = agentsPicked(agentList) 
    
    # determines the user's favourite agent. This variable is used to help decide which agent should be picked
    favAgent = favAgent(agentList) 
    
    # takes into account the abilities of the user's teammates and determines which agent(s) would be best to play
    theBest = optimalAgent(teammates, agentList)
    
    # prints the output (most optimal agent(s))
    output(theBest, favAgent)

        
        
    
    
   


    

